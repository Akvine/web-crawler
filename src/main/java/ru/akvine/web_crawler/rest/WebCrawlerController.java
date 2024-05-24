package ru.akvine.web_crawler.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.web_crawler.rest.dto.CrawlRequest;
import ru.akvine.web_crawler.rest.validators.WebCrawlerValidator;
import ru.akvine.web_crawler.service.WebCrawlerService;

import java.io.IOException;

@RestController
@RequestMapping(value = "/web-crawler")
@RequiredArgsConstructor
public class WebCrawlerController {
    private final WebCrawlerService webCrawlerService;
    private final WebCrawlerValidator webCrawlerValidator;

    @GetMapping(value = "/start/")
    public String start(@RequestBody @Valid CrawlRequest request) throws IOException {
        webCrawlerValidator.verifyCrawlRequest(request);
        return webCrawlerService.startCrawling(request).toString();
    }
}
