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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class WebCrawlerService {

    public Map<Integer, Set<String>> startCrawling(CrawlRequest request) {
        String startUrl = request.getStartUrl();
        int level = request.getMaxLevel();
        Integer maxUrls = request.getMaxUrlsInLevel();

        log.info("Start crawling, level = {}, start url = {}", level, startUrl);

        String baseUrl;
        try {
            URL url = new URL(startUrl);
            baseUrl = url.getProtocol() + "://" + url.getHost();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Can't parse base url from start url = " + startUrl);
        }

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
                    String extractedUrl = baseUrl + element.attr("href");
                    if (tree.containsValue(extractedUrl)) {
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
}
