package com.instagram.api.service;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.User;
import java.util.List;

public interface UserService{
    public User registerUser(User user) throws UserException;
    public User findUserById(Integer userId) throws UserException;
    public User findUserProfile(String token) throws UserException;
    public User findUserByUsername(String username) throws UserException;
    public String followUser(Integer reqUserId, Integer followUserId) throws UserException;
    public String unFollowUser(Integer reqUserId, Integer followUserId) throws UserException;
    public List<User> findUserByIds (List<Integer> userIds) throws UserException;
    public List<User> searchUser (String query) throws UserException;
    public User updateUserDetails(User updateUser, User existingUser) throws UserException;
    public List<User> getPopularUsers();

    
}
