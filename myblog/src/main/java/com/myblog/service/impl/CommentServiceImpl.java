package com.myblog.service.impl;


import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.BolgApiException;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.CommentDto;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import com.myblog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper=mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
// retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));
// set post to comment entity
        comment.setPost(post);
// comment entity to DB
        Comment newComment = commentRepository.save(comment);

        CommentDto dto = mapToDTO(newComment);
        return dto;
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {

        postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId));


        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId)
        );

        if (comment.getPost().getId() != post.getId()) {
            throw new BolgApiException(HttpStatus.BAD_REQUEST, "Comment Doest not belong to post");

        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );
        Comment comment = commentRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", id)
        );
        if (comment.getPost().getId() != post.getId()) {
            throw new BolgApiException(HttpStatus.BAD_REQUEST, "post not matching with comment");
        }
        comment.setId(comment.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment newComment = commentRepository.save(comment);
        return mapToDTO(newComment);
    }

    @Override
    public ResponseEntity<String> deleteComment(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );
        Comment comment = commentRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", id)
        );
        if (comment.getPost().getId() != post.getId()) {
            throw new BolgApiException(HttpStatus.BAD_REQUEST, "post not matching with comment");
        }
        commentRepository.deleteById(id);
        return null;

    }


    private CommentDto mapToDTO(Comment newComment) {
        CommentDto commentDto = mapper.map(newComment, CommentDto.class);
        //CommentDto commentDto = new CommentDto();
       // commentDto.setId(newComment.getId());
        //commentDto.setName(newComment.getName());
        //commentDto.setEmail(newComment.getEmail());
        //commentDto.setBody(newComment.getBody());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
        //Comment comment = new Comment();
        //comment.setId(commentDto.getId());
        //comment.setName(commentDto.getName());
        //comment.setEmail(commentDto.getEmail());
        //comment.setBody(commentDto.getBody());
        return comment;
    }

}