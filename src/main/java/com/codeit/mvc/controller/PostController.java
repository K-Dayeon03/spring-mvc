package com.codeit.mvc.controller;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import com.codeit.mvc.dto.reponse.CommentResponse;
import com.codeit.mvc.dto.reponse.PostResponse;
import com.codeit.mvc.dto.request.PostRequest;
import com.codeit.mvc.service.CommentService;
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
    private final CommentService commentService;

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
    public String create(PostRequest postRequest,
                         @RequestParam(value = "thumbnail", required = false) MultipartFile file,
                         Model model) {
        log.info("/posts: Post, 전달된 값: {}", postRequest);

        // 파일 업로드 처리
        if (file != null && !file.isEmpty()) {
            String fileName = fileService.saveFile(file);
            postRequest = postRequest.withThumbnailPath(fileName);
        }


        Post post = postService.createPost(postRequest);

        // redirect: 재요청
        // redirect:/posts: 응답을 클라이언트로 내보낸 후 자동 재요청으로 /posts 요청이 들어오도록 유도해 달라.
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PostResponse resDto = postService.getPostById(id);
        List<CommentResponse> dtoList = commentService.getCommentsByPostId(id);

        model.addAttribute("post", resDto);
        model.addAttribute("pageTitle", resDto.title());
        model.addAttribute("comments", dtoList);

        return "posts/detail";
    }

    //검색 기능
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Category category,
                         @RequestParam(required = false, defaultValue = "latest") String sort,
                         Model model) {

        List<PostResponse> dtoList = postService.searchPost(keyword, category, sort);

        model.addAttribute("posts", dtoList);
        model.addAttribute("pageTitle", "🔎 검색 결과");

        // 화면단에 사용자가 선택한 여러가지 조건들을 렌더링 과정에 표시하기 위해 추가 정보를 model에 담아서 내려줍니다.
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("sort", sort);

        return "posts/list";
    }


    /// /////////////////////////////

    //댓글 관련 앤드포인트


    //댓글 작성
    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id,
                             @RequestParam String author,
                             @RequestParam String content){
        log.info("POST /posts/{}/comments  - author:{}", author, content);
        CommentResponse resDto = commentService.createComment(id, content, author);
        return "redirect:/posts/" + id;
    }
    //댓글 삭제
    @PostMapping("{postId}/comments/{id}/delete")
    public String deleteComment(@PathVariable Long postId, @RequestParam Long id){
       commentService.deleteComment(id);
        return "redirect:/posts/" + postId;
    }
}
