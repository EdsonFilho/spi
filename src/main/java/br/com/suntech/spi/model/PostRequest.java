package br.com.suntech.spi.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostRequest {

    private Long originalId;
    private String content;
    private Integer similarityLevel;
    private Integer pageSize;

}
