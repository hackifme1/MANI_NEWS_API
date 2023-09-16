package com.maniproject.newswave.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Headline {

    private NewsSource source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;
    private String source_id;
    private String image_url;
    private String pubDate;
    private String link;


    public Headline (String title, String description, String publishedAt, String urlToImage)
    {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.urlToImage = urlToImage;
    }
    public Headline() {}


}
