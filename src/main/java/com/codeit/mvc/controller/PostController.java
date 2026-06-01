package com.codeit.mvc.controller;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.dto.request.PostRequest;
import com.codeit.mvc.dto.response.PostResponse;
import com.codeit.mvc.service.FileService;
import com.codeit.mvc.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final FileService fileService;

    /**
     * <b>Model</b>: <br>자바 데이터를 뷰(사용자에게 응답할 페이지)로 전달하는 용도로 사용하는 객체
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        log.info("/posts: GET, 목록 요청!");
        model.addAttribute("posts", postService.getAllposts());
        model.addAttribute("pageTitle", "블로그에 오신 것을 환영합니다!");

        return "posts/list";
    }

    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("pageTitle", "새 글 작성");
        model.addAttribute("categories", Category.values());

        return "posts/form";
    }

    @PostMapping
    public String create(PostRequest postRequest,
                         @RequestParam(value = "thumbnail", required = false) MultipartFile file,
                         Model model) {
        log.info("/posts: POST, POST: {}", postRequest);

        if (file != null && !file.isEmpty()) {
            String fileName = fileService.saveFile(file);
        }

        postService.createPost(postRequest);

        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PostResponse response = postService.getPostById(id);

        model.addAttribute("post", response);
        model.addAttribute("pageTitle", response.getTitle());
        model.addAttribute("comments", new ArrayList<>());

        return "posts/detail";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Category category,
                         @RequestParam(required = false, defaultValue = "latest") String sort,
                         Model model) {

        List<PostResponse> list = postService.searchPost(keyword, category, sort);

        model.addAttribute("posts", list);
        model.addAttribute("pageTitle", "검색 결과");

        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("sort", sort);

        return "posts/list";
    }
}
