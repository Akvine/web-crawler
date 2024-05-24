package ru.akvine.web_crawler.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.web_crawler.rest.converters.WebCrawlerConverter;
import ru.akvine.web_crawler.rest.dto.CrawlRequest;
import ru.akvine.web_crawler.rest.validators.WebCrawlerValidator;
import ru.akvine.web_crawler.service.WebCrawlerService;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/web-crawler")
@RequiredArgsConstructor
public class WebCrawlerController {
    private final WebCrawlerService webCrawlerService;
    private final WebCrawlerValidator webCrawlerValidator;
    private final WebCrawlerConverter webCrawlerConverter;

    @GetMapping(value = "/start/")
    public ResponseEntity start(@RequestBody @Valid CrawlRequest request) {
        webCrawlerValidator.verifyCrawlRequest(request);
        Map<Integer, Set<String>> tree = webCrawlerService.startCrawling(request);
        return webCrawlerConverter.convertToExportFileResponse(tree);
    }
}
