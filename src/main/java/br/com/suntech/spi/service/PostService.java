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

        PostResult result = new PostResult();
        result.setRequest(request);

        try {
            database.listNotProcessed(parameters);
            List<Post> posts = (List<Post>) parameters.get("listResultNotProcessed");
            doProcessSimilarityByCosine(result, posts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void saveProcessPostData(Long postId, Long relatedPostId, Double similarity){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("postId", postId);
        parameters.put("relatedPostId",relatedPostId);
        parameters.put("similarity",similarity);

        try {
            database.saveProcessed(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doProcessSimilarityByCosine(PostResult result, List<Post> posts){
        Cosine instance = new Cosine();
        PostRequest request = result.getRequest();
        posts.forEach(post -> {
            double similarity = instance.similarity(request.getContent(), post.getContent());
            Integer similarityLevel = request.getSimilarityLevel();

            if (similarityRule(similarityLevel, similarity)){
                result.add(post);
                saveProcessPostData(post.getId_post(), request.getPostId(), similarity);
                System.out.println(similarity + "---" + post.getId_post() + "-->" + post.getContent());
            }
        });
    }

    private Boolean similarityRule(double similarityLevel, double similarity){
        double similarityMin = similarityLevel > 1 ? similarityLevel / 100.0 : similarityLevel;
        double similarityMax = 1;
        return similarity >= similarityMin && similarity < similarityMax;
    }

}
