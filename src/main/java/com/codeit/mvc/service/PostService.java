package com.codeit.mvc.service;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import com.codeit.mvc.dto.request.PostRequest;
import com.codeit.mvc.dto.response.PostResponse;
import com.codeit.mvc.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getAllposts() {
        return postRepository.findAll().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed()) // 생성일자 내림차순
                .collect(Collectors.toList());
    }

    public Post createPost(PostRequest postRequest) {
        Post post = Post.builder()
                .author(postRequest.getAuthor())
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .category(postRequest.getCategory())
                .thumbnailPath(postRequest.getThumbnailPath())
                .build();

        return postRepository.save(post);
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        post.setViewCount();
        return PostResponse.from(post);
    }
    public List<PostResponse> searchPost(String keyword, Category category, String sort) {

        List<Post> posts;

        if (category != null) {
            posts = postRepository.findByCategory(category);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            posts = postRepository.findByTitleOrContentContaining(keyword);
        } else {
            posts = postRepository.findAll();
        }

        if ("viewCount".equals(sort)) {
            posts = posts.stream()
                    .sorted(Comparator.comparing(Post::getViewCount).reversed())
                    .collect(Collectors.toList());
        } else {
            posts = posts.stream()
                    .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                    .collect(Collectors.toList());
        }

        return posts.stream()
                .map(PostResponse::from)
                .toList();
    }
}










