package org.chorem.ecd.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import jdk.internal.util.xml.impl.Input;
import org.chorem.ecd.dao.NewsFeedDao;
import org.chorem.ecd.dao.SignageStreamDao;
import org.chorem.ecd.dao.TransactionRequired;
import org.chorem.ecd.exception.InvalidArgumentException;
import org.chorem.ecd.model.news.AggregatedNews;
import org.chorem.ecd.model.news.NewsFeed;
import org.chorem.ecd.model.stream.SignageStream;
import org.chorem.ecd.model.stream.SignageStreamFrame;
import org.chorem.ecd.task.AggregatedNewsBuilderPeriodicTask;
import org.chorem.ecd.EcdConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class CmsService {

    private static final Logger logger = LoggerFactory.getLogger(CmsService.class);

    @Inject
    protected EcdConfig config;

    @Inject
    protected SignageStreamDao signageStreamDao;

    @Inject
    protected NewsFeedDao newsFeedDao;

    @Inject
    protected JsonService jsonService;

    @Inject
    protected MediaConverterService mediaConverterService;

    @Inject
    protected PeriodicTasksExecutorService periodicTasksExecutorService;

    public List<SignageStream> getSignageStreams() {
        return signageStreamDao.findAll();
    }

    public SignageStream getSignageStream(UUID id) {
        return signageStreamDao.get(id);
    }

    @PostConstruct
    public void init() {
        periodicTasksExecutorService.schedule(getAggregatedNewsBuilderTask());
    }

    @TransactionRequired
    public UUID addSignageStream(String streamName) throws IOException {
        validateStreamCreationParams(streamName);

        UUID streamId = UUID.randomUUID();

        Path streamPath = getStreamPath(streamId);
        Files.createDirectories(streamPath);

        SignageStream signageStream = new SignageStream();
        signageStream.setId(streamId);
        signageStream.setName(streamName);
        signageStream.setTiming(config.getDefaultStreamTiming());
        signageStream.setFrames(Lists.newArrayList());
        signageStreamDao.addSignageStream(signageStream);

        return streamId;
    }

    @TransactionRequired
    public void deleteSignageStream(UUID id) {
        signageStreamDao.delete(id);

        Path streamPath = getStreamPath(id);
        deleteDirectory(streamPath);
    }

    @TransactionRequired
    public UUID importSignageStreamFromPresentation(InputStream inputStream, String fileName, String streamName) throws IOException {
        validateStreamImportParams(inputStream, fileName, streamName);

        Path streamPath = null;
        try {
            UUID streamId = UUID.randomUUID();

            streamPath = getStreamPath(streamId);
            Files.createDirectories(streamPath);

            Path presentationPath = streamPath.resolve(fileName.replace(" ", "_"));

            Files.copy(inputStream, presentationPath);

            List<SignageStreamFrame> frames = mediaConverterService.extractFrames(presentationPath, streamPath);

            SignageStream signageStream = new SignageStream();
            signageStream.setId(streamId);
            signageStream.setName(streamName);
            signageStream.setTiming(config.getDefaultStreamTiming());
            signageStream.setFrames(frames);
            signageStreamDao.addSignageStream(signageStream);

            Files.delete(presentationPath);

            return streamId;
        } catch (IOException e) {
            deleteDirectory(streamPath);
            throw e;
        }
    }

    public File getSignageStreamResourceFile(UUID id, String resource) {
        return getStreamPath(id).resolve(resource).toFile();
    }

    public List<NewsFeed> getNewsFeeds() {
        return newsFeedDao.findAll();
    }

    @TransactionRequired
    public void deleteNewsFeed(UUID id) {
        newsFeedDao.delete(id);

        periodicTasksExecutorService.schedule(getAggregatedNewsBuilderTask());
    }

    @TransactionRequired
    public UUID addNewsFeed(String name, String url) {
        UUID newsFeedId = UUID.randomUUID();

        NewsFeed newsFeed = new NewsFeed();
        newsFeed.setId(newsFeedId);
        newsFeed.setName(name);
        newsFeed.setUrl(url);
        newsFeedDao.addNewsFeed(newsFeed);

        periodicTasksExecutorService.schedule(getAggregatedNewsBuilderTask());

        return newsFeedId;
    }

    @TransactionRequired
    public void setStreamTiming(UUID streamId, Integer timing) {
        validateStreamTimingParams(streamId, timing);
        signageStreamDao.setSignageStreamTiming(streamId, timing);
    }

    @TransactionRequired
    public void addStreamFrame(UUID streamId, InputStream inputStream, Integer newFrameIndex) throws IOException {
        validateStreamFrameAdditionParams(inputStream, newFrameIndex);

        Path streamPath = getStreamPath(streamId);
        Path imagePath = streamPath.resolve(UUID.randomUUID().toString());

        Files.copy(inputStream, imagePath);

        Path thumbnailPath = mediaConverterService.createThumbnail(imagePath);

        SignageStreamFrame frame = new SignageStreamFrame(imagePath.getFileName(), thumbnailPath.getFileName());

        SignageStream signageStream = signageStreamDao.get(streamId);
        List<SignageStreamFrame> frames = signageStream.getFrames();
        frames.add(newFrameIndex, frame);
        signageStreamDao.setSignageStreamFrames(streamId, frames);
    }

    public AggregatedNews getAggregatedNews() {
        try {
            AggregatedNews aggregatedNews = jsonService.loadFromJson(AggregatedNews.class, config.getAggregatedNewsPath());
            return Optional.ofNullable(aggregatedNews).orElse(new AggregatedNews());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private void validateStreamCreationParams(String streamName) throws InvalidArgumentException {
        if (Strings.isNullOrEmpty(streamName)) {
            throw new InvalidArgumentException("Le flux n'a pas de nom");
        }
    }

    private void validateStreamImportParams(InputStream inputStream, String fileName, String streamName) throws InvalidArgumentException {
        if (inputStream == null) {
            throw new InvalidArgumentException("Aucun fichier à importer");
        }
        if (Strings.isNullOrEmpty(fileName)) {
            throw new InvalidArgumentException("Le fichier n'a pas de nom");
        }
        validateStreamCreationParams(streamName);
    }

    private void validateStreamTimingParams(UUID streamId, Integer timing) throws InvalidArgumentException {
        if (streamId == null) {
            throw new InvalidArgumentException("Aucun flux spécifié");
        }
        if (timing == null) {
            throw new InvalidArgumentException("Aucun minutage spécifié");
        }
    }

    private void validateStreamFrameAdditionParams(InputStream inputStream, Integer index) throws InvalidArgumentException {
        if (inputStream == null) {
            throw new InvalidArgumentException("Aucun fichier à insérer");
        }
        if (index == null) {
            throw new InvalidArgumentException("Aucun index spécifié");
        }
    }

    private Path getStreamPath(UUID streamId) {
        return config.getStreamsPath().resolve(streamId.toString());
    }

    private AggregatedNewsBuilderPeriodicTask getAggregatedNewsBuilderTask() {
        List<URL> newsFeedUrls = newsFeedDao.findAll().stream()
                .map(NewsFeed::getParsedUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        AggregatedNewsBuilderPeriodicTask task = new AggregatedNewsBuilderPeriodicTask(newsFeedUrls);
        task.setConfig(config);
        task.setJsonService(jsonService);
        return task;
    }

    private void deleteDirectory(Path dirPath) {
        try {
            Set<Path> pathsToRemove = Files.list(dirPath).collect(Collectors.toSet());
            for (Path pathToRemove : pathsToRemove) {
                Files.delete(pathToRemove);
            }
            Files.delete(dirPath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
