package org.chorem.ecd.service;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.chorem.ecd.EcdConfig;
import org.chorem.ecd.model.stream.SignageStreamFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class MediaConverterService {

    private static final Logger logger = LoggerFactory.getLogger(MediaConverterService.class);

    @Inject
    private EcdConfig config;

    public String getUnoconvVersion() {
        return getVersion(config.getUnoconvCmd());
    }

    public String getConvertVersion() {
        return getVersion(config.getResizeCmd());
    }

    public List<SignageStreamFrame> extractFrames(Path source, Path destDir) throws IOException {
        List<String> cmdLineArgs = Arrays.asList(config.getUnoconvCmd(),
                "-vvv",
                "--doctype=presentation",
                "--format=html",
                "-e PublishMode=0",
                "-e Format=2",
                "-e Width=1024",
                "-e Compression=\"50%\"",
                "-e IsExportContentsPage=false",
                "--output=" + Paths.get(destDir.toString(), File.separator, ".").toString(),
                source.toString());

        executeCmd(cmdLineArgs);

        Comparator<Path> pathsWithDigitsComparator = (p1, p2) -> {
            int p1Sequence = Integer.parseInt(CharMatcher.digit().retainFrom(p1.getFileName().toString()));
            int p2Sequence = Integer.parseInt(CharMatcher.digit().retainFrom(p2.getFileName().toString()));
            return p1Sequence - p2Sequence;
        };

        Set<Path> toRemove = Files.list(destDir)
                .filter(p -> getFileExtension(p).equalsIgnoreCase("html"))
                .collect(Collectors.toSet());
        for (Path path : toRemove) {
            Files.delete(path);
        }

        SortedSet<Path> imagePaths = Files.list(destDir)
                .filter(p -> getFileExtension(p).equalsIgnoreCase("png"))
                .collect(ImmutableSortedSet.toImmutableSortedSet(pathsWithDigitsComparator));

        List<SignageStreamFrame> frames = Lists.newArrayList();

        for (Path imagePath : imagePaths) {
            Path thumbnailPath = createThumbnail(imagePath);
            SignageStreamFrame frame = new SignageStreamFrame(imagePath.getFileName(), thumbnailPath.getFileName());
            frames.add(frame);
        }

        return frames;
    }

    private Path createThumbnail(Path imagePath) throws IOException {
        Path resizedImagePath = getResizedImagePath(imagePath);
        List<String> cmdLineArgs = Arrays.asList(config.getResizeCmd(),
                "-resize",
                "140x",
                imagePath.toString(),
                resizedImagePath.toString());

        executeCmd(cmdLineArgs);

        return resizedImagePath;
    }

    private static String executeCmd(List<String> cmdLineArgs) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor exec = new DefaultExecutor();
        exec.setStreamHandler(streamHandler);

        CommandLine cmdLine = CommandLine.parse(cmdLineArgs.stream().collect(Collectors.joining(" ")));

        exec.execute(cmdLine);

        return outputStream.toString();
    }

    private String getVersion(String cmd) {
        String version = "";
        List<String> cmdLineArgs = Arrays.asList(cmd, "--version");
        try {
            version = executeCmd(cmdLineArgs);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return version;
    }

    private String getFileExtension(Path path) {
        return com.google.common.io.Files.getFileExtension(path.toString());
    }

    private String getNameWithoutExtension(Path path) {
        return com.google.common.io.Files.getNameWithoutExtension(path.toString());
    }

    private Path getResizedImagePath(Path imagePath) {
        String imageFileNameWithouExtension = getNameWithoutExtension(imagePath);
        String imageFileExtenstion = getFileExtension(imagePath);

        return imagePath.resolveSibling(imageFileNameWithouExtension + "-small." + imageFileExtenstion);
    }
}
