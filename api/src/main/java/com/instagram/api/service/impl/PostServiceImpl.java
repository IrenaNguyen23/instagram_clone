package com.instagram.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instagram.api.dto.UserDto;
import com.instagram.api.exceptions.PostException;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.Post;
import com.instagram.api.modal.User;
import com.instagram.api.repository.PostRepository;
import com.instagram.api.repository.UserRepository;
import com.instagram.api.service.PostService;
import com.instagram.api.service.UserService;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Post createPost(Post post, Integer userId) throws UserException {
        User user = userService.findUserById(userId);

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUserImage(user.getImage());
        userDto.setUsername(user.getUsername());

        post.setUser(userDto);

        Post createdPost = postRepository.save(post);
        
        return createdPost;
        
    }

    @Override
    public String deletePost(Integer postId, Integer userId) throws UserException, PostException {
        Post post = findPostById(postId);
        User user = userService.findUserById(userId);

        if(post.getUser().getId().equals(user.getId())){
            postRepository.deleteById(post.getId());
            return "Post Deleted Successfully";
        };
        throw new PostException("You can't delete other user's post");
    }

    @Override
    public List<Post> findPostByUserId(Integer userId) throws UserException {
        List<Post> posts = postRepository.findByUserId(userId);
        if(posts.size()==0){
            throw new UserException("this user does not have any post");
        }
        return posts;
    }

    @Override
    public Post findPostById(Integer postId) throws PostException {
        Optional<Post> opt = postRepository.findById(postId);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new PostException("Post not found with id:" + postId);
    }

    @Override
    public List<Post> findAllPostByUserId(List<Integer> userId) throws PostException, UserException {
        List<Post> posts = postRepository.findAllPostByUserIds(userId);
        if(posts.size() == 0){
            throw new UserException("No post available");
        }
        return posts;
    }

    @Override
    public String savedPost(Integer postId, Integer userId) throws PostException, UserException {
        Post post = findPostById(postId);
        User user = userService.findUserById(userId);
        
        if(!user.getSavedPost().contains(post)){
            user.getSavedPost().add(post);
            userRepository.save(user);
        }
        return "Post save Successfully";
    }

    @Override
    public String unSavedPost(Integer postId, Integer userId) throws PostException, UserException {
        Post post = findPostById(postId);
        User user = userService.findUserById(userId);
        
        if(user.getSavedPost().contains(post)){
            user.getSavedPost().remove(post);
            userRepository.save(user);
        }
        return "Post Remove Successfully";
    }

    @Override
    public Post likePost(Integer postId, Integer userId) throws PostException, UserException {
        Post post = findPostById(postId);
        User user = userService.findUserById(userId);
        
        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUserImage(user.getImage());
        userDto.setUsername(user.getUsername());

        post.getLikedByUsers().add(userDto);

        return postRepository.save(post);
    }

    @Override
    public Post unlikePost(Integer postId, Integer userId) throws PostException, UserException {
        Post post = findPostById(postId);
        User user = userService.findUserById(userId);
        
        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUserImage(user.getImage());
        userDto.setUsername(user.getUsername());

        post.getLikedByUsers().remove(userDto);

        return postRepository.save(post);
    }
    
}
