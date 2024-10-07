package com.instagram.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.api.exceptions.StoryException;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.Story;
import com.instagram.api.modal.User;
import com.instagram.api.service.StoryService;
import com.instagram.api.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private StoryService storyService;

    @PostMapping("/create")
    public ResponseEntity<Story> createStoryHandler(@RequestBody Story story, @RequestHeader("Authorization") String token) throws UserException{
        User user = userService.findUserProfile(token);

        Story createStory = storyService.createStory(story, user.getId());

        return new ResponseEntity<Story>(createStory, HttpStatus.OK);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<Story>> findAllStoryByUserIdHandler(@PathVariable Integer userId) throws UserException, StoryException{
        User user = userService.findUserById(userId);
        List<Story> stories = storyService.findStoryByUserId(user.getId());
        return new ResponseEntity<List<Story>>(stories,HttpStatus.OK);
    }
}
