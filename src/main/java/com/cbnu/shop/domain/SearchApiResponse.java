package com.cbnu.shop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SearchApiResponse {
    @JsonProperty("lastBuildDate")
    private String lastBuildDate;

    private int total;
    private int start;
    private int display;

    private List<ItemDto> items;

}
