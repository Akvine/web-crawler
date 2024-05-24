package ru.akvine.web_crawler.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CrawlRequest {
    private int maxLevel;

    @NotBlank
    private String startUrl;

    private Integer maxUrlsInLevel;
}
