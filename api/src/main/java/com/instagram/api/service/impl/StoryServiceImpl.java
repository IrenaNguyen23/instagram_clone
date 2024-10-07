package com.instagram.api.service.impl;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instagram.api.dto.UserDto;
import com.instagram.api.exceptions.StoryException;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.Story;
import com.instagram.api.modal.User;
import com.instagram.api.repository.StoryRepository;
import com.instagram.api.repository.UserRepository;
import com.instagram.api.service.StoryService;
import com.instagram.api.service.UserService;

@Service
public class StoryServiceImpl implements StoryService{

    @Autowired
    private UserService userService;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Story createStory(Story story, Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUserImage(user.getImage());
        userDto.setUsername(user.getUsername());

        story.setUser(userDto);
        story.setTimestamp(LocalDateTime.now());
        user.getStories().add(story);

        return storyRepository.save(story);
    }

    @Override
    public List<Story> findStoryByUserId(Integer userId) throws UserException, StoryException {
        User user = userService.findUserById(userId);
        List<Story> stories= user.getStories();
        if(stories.size()==0){
            throw new StoryException("This user doesn't have any story");
        }
        return stories;
    }
    
}
