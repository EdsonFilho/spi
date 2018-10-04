package br.com.suntech.spi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class Post implements Serializable {

    private Long original_id;
    private String content;
    private String link;
    private String author_username;
    private String author_name;
    private String author_avatar;
    private String author_link;
    private Long created_original_date;

    private String interaction_type;
    private String source;
    private String language;
    private String sentiment;
    private String geo_lat;
    private String geo_long;
    private String address;
    private String keywords;

    public Post(){}

    public Post(Long original_id, String content, String author_username, String author_name, Long created_original_date) {
        this.original_id = original_id;
        this.content = content;
        this.author_username = author_username;
        this.author_name = author_name;
        this.created_original_date = created_original_date;
    }
}
