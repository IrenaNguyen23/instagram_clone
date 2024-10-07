package com.instagram.api.service;

import com.instagram.api.exceptions.CommentException;
import com.instagram.api.exceptions.PostException;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.Comment;

public interface CommentService {

    public Comment createComment(Comment comment, Integer postId, Integer userId) throws UserException, PostException;

    public Comment findCommentById(Integer commentId) throws CommentException;

    public Comment likedComment(Integer commentId, Integer userId) throws CommentException, UserException;

    public Comment unLikedComment(Integer commentId, Integer userId) throws CommentException, UserException;
} 
