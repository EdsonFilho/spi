package br.com.suntech.spi.service;

import br.com.suntech.spi.model.Post;
import br.com.suntech.spi.model.PostRequest;
import br.com.suntech.spi.model.PostResult;
import info.debatty.java.stringsimilarity.Cosine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PostService {

    private static final List<Post> DADOS = new ArrayList<>(Arrays.asList(
        new Post(1L, "Smartphone Samsung Galaxy On 7 leage eath pass nive coming sort top car", "@leochucre", "Leonard Moraes", 100000L),
        new Post(2L, "Geladeira Electrolux Frost Free", "@leochucre", "Leonard Moraes", 100000L),
        new Post(3L, "Purificador de Água Electrolux", "@derlisnelson", "Dérlis Resquin", 100000L),
        new Post(4L, "Smart Watch Relogio Bluetooth", "@derlisnelson", "Dérlis Resquin", 100000L),
        new Post(5L, "Smart TV LED 32\" Samsung", "@xndr", "Philipe Schneider", 100000L),
        new Post(6L, "Kit Pneu Aro 14 Dunlop 175/65r14", "@xndr", "Philipe Schneider", 100000L)
    ));

    public List<Post> list(Integer offset) {
        List<Post> list = new ArrayList<>();
        for (int i = 0; i < offset; i++) {
            list.add(DADOS.get(i));
        }
        return list;
    }

    public PostResult process(PostRequest request){
        Cosine instance = new Cosine();

        PostResult result = new PostResult();
        result.setRequest(request);

        List<Post> posts = DADOS;

        posts.forEach(post -> {
            if (instance.similarity(request.getContent(), post.getContent()) >= request.getSimilarityLevel()){
                result.getPost().add(post);
            }
        });

        return result;
    }


}
