package com.codeit.mvc.controller;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import com.codeit.mvc.dto.request.PostRequest;
import com.codeit.mvc.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // 컨트롤러가 요청에 맞는 뷰 페이지를 결정하게 하겠다. (Server Side Rendering)
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        log.info("/posts: GET, 목록 요청!");
        List<Post> allposts = postService.getAllposts();
        // Model: 자바 데이터를 뷰(사용자에게 응답할 페이지)로 전달하는 용도로 사용하는 객체.
        model.addAttribute("posts", allposts);
        model.addAttribute("pageTitle", "블로그에 오신 것을 환영합니다!");
        return "posts/list";
    }

    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("pageTitle", "✏️ 새 글 작성");
        model.addAttribute("categories", Category.values());
        return "posts/form";
    }

    @PostMapping
    public String create(PostRequest postRequest, Model model) {
        log.info("/posts: Post, 전달된 값: {}", postRequest);
        Post post = postService.createPost(postRequest);

        // redirect: 재요청
        // redirect:/posts: 응답을 클라이언트로 내보낸 후 자동 재요청으로 /posts 요청이 들어오도록 유도해 달라.
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id) {
        postService.getPostById(id);
    }


}







