package br.com.suntech.spi.service;

import br.com.suntech.spi.mapper.PostMapper;
import br.com.suntech.spi.model.Post;
import br.com.suntech.spi.model.PostRequest;
import br.com.suntech.spi.model.PostResult;
import info.debatty.java.stringsimilarity.Cosine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    @Autowired
    PostMapper database;

    public List<Post> list(Integer offset) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("offset",offset);
        List<Post> list = null;
        try {
            list = database.list(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public PostResult process(PostRequest request) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("originalId",request.getOriginalId());
        parameters.put("pageSize",request.getPageSize());

        Cosine instance = new Cosine();

        PostResult result = new PostResult();
        result.setRequest(request);

        List<Post> posts = null;
        try {
            posts = database.list(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        posts.forEach(post -> {
            if (instance.similarity(request.getContent(), post.getContent()) >= request.getSimilarityLevel()){
                result.getPost().add(post);
            }
        });

        return result;
    }
}
