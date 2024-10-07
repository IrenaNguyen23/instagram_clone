package com.instagram.api.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.api.exceptions.PostException;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.Post;
import com.instagram.api.modal.User;
import com.instagram.api.response.MessageResponse;
import com.instagram.api.service.PostService;
import com.instagram.api.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Post> createPostHandler(@RequestBody Post post, @RequestHeader("Authorization") String token)
            throws UserException {
        try {
            User user = userService.findUserProfile(token);
            Post createdPost = postService.createPost(post, user.getId());
            return new ResponseEntity<Post>(createdPost, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<Post>> findPostByUserIdHandler(@PathVariable("id") Integer userId) throws UserException {

        System.out.println("Received userId: " + userId); // Logging userId for debugging

        List<Post> posts = postService.findPostByUserId(userId);

        if (posts.isEmpty()) {
            System.out.println("No posts found for userId: " + userId);
        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/following/{ids}")
    public ResponseEntity<List<Post>> findAllPostByUserIdHandler(@PathVariable("ids") List<Integer> userId)
            throws UserException, PostException {

        List<Post> posts = postService.findAllPostByUserId(userId);

        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> findPostByIdHandler(@PathVariable Integer postId) throws PostException {

        Post posts = postService.findPostById(postId);

        return new ResponseEntity<Post>(posts, HttpStatus.OK);
    }

    @PutMapping("/like/{postId}")
    public ResponseEntity<Post> likePostHandler(@PathVariable Integer postId,
            @RequestHeader("Authorization") String token) throws UserException, PostException {
        User user = userService.findUserProfile(token);
        Post likedPost = postService.likePost(postId, user.getId());

        return new ResponseEntity<Post>(likedPost, HttpStatus.OK);
    }

    @PutMapping("/unlike/{postId}")
    public ResponseEntity<Post> unLikePostHandler(@PathVariable Integer postId,
            @RequestHeader("Authorization") String token) throws UserException, PostException {
        User user = userService.findUserProfile(token);
        Post likedPost = postService.unlikePost(postId, user.getId());

        return new ResponseEntity<Post>(likedPost, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<MessageResponse> deletePostHanlder(@PathVariable Integer postId,
            @RequestHeader("Authorization") String token) throws UserException, PostException {
        User user = userService.findUserProfile(token);
        String message = postService.deletePost(postId, user.getId());
        MessageResponse res = new MessageResponse(message);
        return new ResponseEntity<MessageResponse>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/save_post/{postId}")
    public ResponseEntity<MessageResponse> savedPostHandler(@PathVariable Integer postId,
            @RequestHeader("Authorization") String token) throws UserException, PostException {

        User user = userService.findUserProfile(token);

        String message = postService.savedPost(postId, user.getId());

        MessageResponse res = new MessageResponse(message);

        return new ResponseEntity<MessageResponse>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/unsave_post/{postId}")
    public ResponseEntity<MessageResponse> unSavedPostHandler(@PathVariable Integer postId,
            @RequestHeader("Authorization") String token) throws UserException, PostException {

        User user = userService.findUserProfile(token);

        String message = postService.unSavedPost(postId, user.getId());

        MessageResponse res = new MessageResponse(message);

        return new ResponseEntity<MessageResponse>(res, HttpStatus.ACCEPTED);
    }

}
