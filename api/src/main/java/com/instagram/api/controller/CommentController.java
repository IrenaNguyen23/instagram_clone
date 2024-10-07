package com.instagram.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.api.exceptions.CommentException;
import com.instagram.api.exceptions.PostException;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.Comment;
import com.instagram.api.modal.User;
import com.instagram.api.service.CommentService;
import com.instagram.api.service.UserService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping("/create/{postId}")
    public ResponseEntity<Comment> createCommentHandler(@RequestBody Comment comment, @PathVariable Integer postId, @RequestHeader("Authorization") String token) throws UserException, PostException{
        User user = userService.findUserProfile(token);
        Comment createComment = commentService.createComment(comment, postId, user.getId());
        return new ResponseEntity<Comment>(createComment,HttpStatus.OK);
    }

    @PutMapping("/like/{commentId}")
    public ResponseEntity<Comment> likedCommentHandler(@RequestHeader("Authorization") String token, @PathVariable Integer commentId) throws UserException, PostException, CommentException{
        User user = userService.findUserProfile(token);
        Comment comment = commentService.likedComment(commentId,user.getId());
        return new ResponseEntity<Comment>(comment,HttpStatus.OK);
    }

    @PutMapping("/unlike/{commentId}")
    public ResponseEntity<Comment> unLikedCommentHandler(@RequestHeader("Authorization") String token, @PathVariable Integer commentId) throws UserException, PostException, CommentException{
        User user = userService.findUserProfile(token);
        Comment comment = commentService.unLikedComment(commentId,user.getId());
        return new ResponseEntity<Comment>(comment,HttpStatus.OK);
    }
}
