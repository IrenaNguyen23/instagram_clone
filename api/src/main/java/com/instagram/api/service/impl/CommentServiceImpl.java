package com.instagram.api.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instagram.api.dto.UserDto;
import com.instagram.api.exceptions.CommentException;
import com.instagram.api.exceptions.PostException;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.Comment;
import com.instagram.api.modal.Post;
import com.instagram.api.modal.User;
import com.instagram.api.repository.CommentRepository;
import com.instagram.api.repository.PostRepository;
import com.instagram.api.service.CommentService;
import com.instagram.api.service.PostService;
import com.instagram.api.service.UserService;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Override
    public Comment createComment(Comment comment, Integer postId, Integer userId) throws UserException, PostException {
        User user = userService.findUserById(userId);

        Post post = postService.findPostById(postId);

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUserImage(user.getImage());
        userDto.setUsername(user.getUsername());

        comment.setUser(userDto);
        comment.setCreateAt(LocalDateTime.now());
        Comment createdComment = commentRepository.save(comment);
        post.getComments().add(createdComment);

        postRepository.save(post);

        return createdComment;
    }

    @Override
    public Comment findCommentById(Integer commentId) throws CommentException {
        Optional<Comment> opt = commentRepository.findById(commentId);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new CommentException("Comment is not found with id" + commentId);
    }

    @Override
    public Comment likedComment(Integer commentId, Integer userId) throws CommentException, UserException {
        User user = userService.findUserById(userId);
        Comment comment = findCommentById(commentId);
        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUserImage(user.getImage());
        userDto.setUsername(user.getUsername());

        comment.getLikedByUsers().add(userDto);

        return commentRepository.save(comment);
    }

    @Override
    public Comment unLikedComment(Integer commentId, Integer userId) throws CommentException, UserException {
        User user = userService.findUserById(userId);
        Comment comment = findCommentById(commentId);
        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUserImage(user.getImage());
        userDto.setUsername(user.getUsername());

        comment.getLikedByUsers().remove(userDto);

        return commentRepository.save(comment);
    }

}
