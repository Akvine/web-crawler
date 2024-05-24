package ru.akvine.web_crawler.rest.validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.web_crawler.exceptions.ValidationException;
import ru.akvine.web_crawler.rest.dto.CrawlRequest;

@Component
public class WebCrawlerValidator {
    @Value("${max.level}")
    private int maxLevel;

    public void verifyCrawlRequest(CrawlRequest request) {
        int maxLevel = request.getMaxLevel();

        if (maxLevel > this.maxLevel) {
            String errorMessage = String.format(
                    "Crawling level %s is greater than max available level = [%s]",
                    maxLevel, this.maxLevel);
            throw new ValidationException(errorMessage);
        }
    }
}
