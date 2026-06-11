package com.codeit.mvc.repository;

import com.codeit.mvc.domain.Comment;
import com.codeit.mvc.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
@Repository
public class MemoryCommentRepository implements CommentRepository {

    private final Map<Long, Comment> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1L);


    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            comment.setId(sequence.getAndIncrement());
        }
        store.put(comment.getId(), comment);
        return comment;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return store.values().stream()
                .filter(comment -> comment.getPostId() == postId)
                .collect(Collectors.toList());
    }
}
