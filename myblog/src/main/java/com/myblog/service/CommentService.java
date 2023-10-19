package com.myblog.service;

import com.myblog.payload.CommentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(Long postId, Long commentId);

    CommentDto updateComment(long postId, long id, CommentDto commentDto);

    ResponseEntity<String> deleteComment(long postId, long id);
}
