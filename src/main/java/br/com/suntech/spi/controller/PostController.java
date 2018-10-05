package br.com.suntech.spi.controller;

import br.com.suntech.spi.model.Post;
import br.com.suntech.spi.model.PostRequest;
import br.com.suntech.spi.model.PostResult;
import br.com.suntech.spi.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

    @PostMapping("/list/{size}")
    public List<Post> list(@PathVariable("size") Integer size) {
        List<Post> list = postService.list(size);
        return list;
    }

    @PostMapping("/process")
    public PostResult process(@RequestBody PostRequest request){
        PostResult process = postService.process(request);
        return process;
    }



}
