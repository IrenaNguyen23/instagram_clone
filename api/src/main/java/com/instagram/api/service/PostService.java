package com.instagram.api.service;

import com.instagram.api.exceptions.PostException;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.Post;

import java.util.*;

public interface PostService {
    public Post createPost(Post post, Integer userId) throws UserException;
    
    public String deletePost(Integer postId, Integer userId) throws UserException,PostException;
    
    public List<Post> findPostByUserId(Integer userId) throws UserException;

    public Post findPostById(Integer postId) throws PostException;

    public List<Post> findAllPostByUserId(List<Integer> userId) throws PostException,UserException;

    public String savedPost(Integer postId, Integer userId) throws PostException,UserException;

    public String unSavedPost(Integer postId, Integer userId) throws PostException,UserException;

    public Post likePost(Integer postId, Integer userId) throws PostException,UserException;

    public Post unlikePost(Integer postId, Integer userId) throws PostException,UserException;
}
