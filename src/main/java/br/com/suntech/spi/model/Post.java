package br.com.suntech.spi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter @Setter
public class Post implements Serializable {

    private Long id_post;
    private Long original_id;
    private String content;
    private String link;
    private String author_username;
    private String author_name;
    private String author_avatar;
    private String author_link;
    private Timestamp created_original_date;

    private String interaction_type;
    private String source;
    private String language;
    private String sentiment;
    private String geo_lat;
    private String geo_long;
    private String address;
    private String keywords;

}
