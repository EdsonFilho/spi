package br.com.suntech.spi.service;

import br.com.suntech.spi.mapper.PostMapper;
import br.com.suntech.spi.model.Post;
import br.com.suntech.spi.model.PostRequest;
import br.com.suntech.spi.model.PostResult;
import info.debatty.java.stringsimilarity.Cosine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    public static final Integer COSINE = 1;

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

    public List<Post> listProcessed(Integer postId, Double similarity, Integer offset) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("postId",postId);
        parameters.put("similarity",similarity);
        parameters.put("offset",offset);
        List<Post> list = null;
        try {
            database.listProcessed(parameters);
            list = (List<Post>) parameters.get("result");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public PostResult process(PostRequest request) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("postId", request.getPostId().intValue());
        parameters.put("maxReg",Integer.MAX_VALUE);

        PostResult result = new PostResult();
        result.setRequest(request);

        try {
            database.listNotProcessed(parameters);
            List<Post> posts = (List<Post>) parameters.get("listResultNotProcessed");

            // Pre parserData();

            switch (request.getStrategy()){
                case 1:
                    doProcessSimilarityByCosine(result, posts); break;
//                case LEVENSHTEIN:
//                    doProcessSimilarityByLevenshtein(result, posts); break;
//                case JARO_WINKLER:
//                    doProcessSimilarityByJaro-Winkler(result, posts); break;
                default: break;
            }

            //PosParserData


        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Post> listProcessedSorted = getListProcessedSorted(request);
        result.setPost(listProcessedSorted);

        return result;
    }

    private List<Post> getListProcessedSorted(PostRequest request) {
        Integer postId = request.getPostId();
        Integer similarityLevel = request.getSimilarityLevel();
        double similarityMin = similarityLevel > 1 ? similarityLevel / 100.0 : similarityLevel;
        int maxValue = Integer.MAX_VALUE;
        List<Post> posts = listProcessed(postId, similarityMin, maxValue);
        return posts;
    }

    private void saveProcessPostData(Integer postId, Integer relatedPostId, Double similarity){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("postId", postId);
        parameters.put("relatedPostId",relatedPostId);
        parameters.put("similarity",similarity);

        try {
//            System.out.println(postId + " + " + relatedPostId + " = " + similarity);
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
            saveProcessPostData(request.getPostId(), post.getId_post(), similarity);
        });

    }
}
