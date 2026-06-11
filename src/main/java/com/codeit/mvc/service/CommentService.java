package com.codeit.mvc.service;

import com.codeit.mvc.domain.Comment;
import com.codeit.mvc.dto.reponse.CommentResponse;
import com.codeit.mvc.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentResponse createComment(Long postId, String content, String author) {
        if(author == null || author.isBlank()) {
            throw new IllegalArgumentException("작성자는 비워둘 수 없습니다.");
        }
        if(content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용은 비워둘 수 없습니다.");
        }
        Comment comment = Comment.builder()
                .postId(postId)
                .content(content)
                .author(author)
                .build();
        Comment saved = commentRepository.save(comment);
        return CommentResponse.from(saved);
    }

    public List<CommentResponse> getCommentsByPostId(Long id) {
        List<Comment> list = commentRepository.findByPostId(id);

        return list.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long id) {
        commentRepository.findById(id)
                        .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 댓글입니다. id= "+id));
        commentRepository.deleteById(id);
    }
}
