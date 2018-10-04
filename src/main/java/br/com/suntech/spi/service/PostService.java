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
            database.list(parameters);
            list = (List<Post>) parameters.get("listResult");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public PostResult process(PostRequest request) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("postId",request.getPostId());
        parameters.put("pageSize",request.getPageSize());

        Cosine instance = new Cosine();

        PostResult result = new PostResult();
        result.setRequest(request);

        List<Post> posts = null;
        try {
            database.listNotProcessed(parameters);
            posts = (List<Post>) parameters.get("listResultNotProcessed");
        } catch (Exception e) {
            e.printStackTrace();
        }

        posts.forEach(post -> {
//            if (instance.similarity(request.getContent(), post.getContent()) >= (request.getSimilarityLevel() / 100)){
            double similarity = instance.similarity(request.getContent(), post.getContent());
            if (similarity >= 0.25 && similarity < 1){
                System.out.println(similarity);
                result.add(post);
            }
        });

        return result;
    }
}
