package org.chorem.ecd.task;

import org.chorem.ecd.model.news.AggregatedNews;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class AggregatedNewsBuilderPeriodicTask extends EcdPeriodicTask {

    private static final Logger logger = LoggerFactory.getLogger(AggregatedNewsBuilderPeriodicTask.class);

    private final List<URL> newsFeedUrls;

    public static final String NAME = "AggregatedNewsBuilder";

    public AggregatedNewsBuilderPeriodicTask(List<URL> newsFeedUrls) {
        this.newsFeedUrls = newsFeedUrls;
    }

    @Override
    public void run() {
        AggregatedNews aggregatedNews = new AggregatedNews();

        for (URL newsFeedUrl : newsFeedUrls) {
            try {
                fetchNewsFromFeed(newsFeedUrl, aggregatedNews);
            } catch(JDOMException |IOException e) {
                logger.error("Echec de la récupération du flux " + newsFeedUrl, e);
            }
        }

        try {
            jsonService.saveToJson(aggregatedNews, config.getAggregatedNewsPath());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Integer getPeriodicity() {
        return 20;
    }

    @Override
    public TimeUnit getPeriodicityUnit() {
        return TimeUnit.MINUTES;
    }

    private void fetchNewsFromFeed(URL newsFeedUrl, AggregatedNews aggregatedNews) throws JDOMException, IOException {
        SAXBuilder jdomBuilder = new SAXBuilder();
        Document jdomDocument = jdomBuilder.build(newsFeedUrl);
        Element rss = jdomDocument.getRootElement();
        Element channel = rss.getChild("channel");
        String newsFeedTitle = channel.getChildText("title");
        List<String> titles = channel.getChildren("item").stream()
                .map(elt -> elt.getChildText("title"))
                .collect(Collectors.toList());
        aggregatedNews.addNews(newsFeedTitle, titles);
    }
}
