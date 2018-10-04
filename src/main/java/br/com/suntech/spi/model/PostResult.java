package br.com.suntech.spi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PostResult implements Serializable {

    private PostRequest request;
    private List<Post> post;

    public void add(Post item){
        if (post == null) post = new ArrayList<Post>();
        post.add(item);
    }
}
