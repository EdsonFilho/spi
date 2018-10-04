package br.com.suntech.spi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class PostRequest implements Serializable {

    private Integer postId;
    private String content;
    private Integer similarityLevel;
    private Integer pageSize;

}
