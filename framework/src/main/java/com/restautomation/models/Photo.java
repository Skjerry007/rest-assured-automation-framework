package com.restautomation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Photo - POJO for photo data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {
    private Integer id;
    
    @JsonProperty("albumId")
    private Integer albumId;
    
    private String title;
    private String url;
    
    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;
} 