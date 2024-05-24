package ru.akvine.web_crawler.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CrawlRequest {
    @Min(0)
    private int maxLevel;

    @NotBlank
    private String startUrl;

    private Integer maxUrlsInLevel;
}
