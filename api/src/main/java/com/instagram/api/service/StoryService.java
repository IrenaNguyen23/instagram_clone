package com.instagram.api.service;

import com.instagram.api.exceptions.StoryException;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.Story;
import java.util.*;

public interface StoryService {
    public Story createStory(Story story, Integer userId) throws UserException;

    public List<Story> findStoryByUserId(Integer userId) throws UserException, StoryException;
}
