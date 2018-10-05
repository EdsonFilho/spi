package br.com.suntech.spi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

@Getter @Setter
public class Post implements Serializable {

    private Integer id_post;
    private String original_id;
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

    private Double vl_similarity;

    private String similarity;
    private String postDate;

    public String getPostDate(){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format( created_original_date );
    }

    public String getSimilarity(){
        if (vl_similarity != null) {
            NumberFormat fmt = NumberFormat.getPercentInstance();
            fmt.setMaximumFractionDigits(2);
            return fmt.format(vl_similarity);
        } else {
            return "";
        }
    }
}
