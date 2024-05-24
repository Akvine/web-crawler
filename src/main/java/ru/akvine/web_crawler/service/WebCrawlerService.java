package ru.akvine.web_crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.akvine.web_crawler.rest.dto.CrawlRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class WebCrawlerService {

    private final static String HTTPS_PATTERN = "(?i)^https://";

    public Map<Integer, Set<String>> startCrawling(CrawlRequest request) {
        String startUrl = request.getStartUrl();
        int level = request.getMaxLevel();
        Integer maxUrls = request.getMaxUrlsInLevel();

        log.info("Start crawling, level = {}, start url = {}", level, startUrl);
        String baseUrl = extractBaseUrl(startUrl);

        Map<Integer, Set<String>> tree = new HashMap<>();
        tree.put(0, Set.of(startUrl));

        for (int i = 0; i < level; ++i) {
            tree.put(i + 1, new HashSet<>());
            for (String url : tree.get(i)) {
                Document doc;
                try {
                    doc = Jsoup.connect(url).get();
                } catch (Exception ex) {
                    continue;
                }
                Elements urls = doc.select("a[href]");
                for (Element element : urls) {
                    String extractedUrl = element.attr("href");
                    if (!isFullUrl(extractedUrl)) {
                        extractedUrl = baseUrl + extractedUrl;
                    }

                    if (contains(tree.values(), extractedUrl)) {
                        continue;
                    }
                    tree.get(i + 1).add(extractedUrl);

                    if (maxUrls != null && tree.get(i + 1).size() >= maxUrls) {
                        break;
                    }
                }
            }
        }

        return tree;
    }

    private boolean isFullUrl(String value) {
        return Pattern.matches(HTTPS_PATTERN, value);
    }

    private boolean contains(Collection<Set<String>> lists, String value) {
        List<String> allValues = new ArrayList<>();
        for (Set<String> list : lists) {
            allValues.addAll(list);
        }

        return allValues.contains(value);
    }

    private String extractBaseUrl(String startUrl) {
        try {
            URL url = new URL(startUrl);
            return url.getProtocol() + "://" + url.getHost();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Can't parse base url from start url = " + startUrl);
        }
    }
}
