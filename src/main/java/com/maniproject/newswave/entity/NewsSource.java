package com.maniproject.newswave.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Entity
public class NewsSource {

    @Id
    private String id;
    private String name;
    private String description;
    private String url;
    private String category;
    private String language;
    private String country;


    public NewsSource(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public NewsSource() {
    }
    public NewsSource(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
