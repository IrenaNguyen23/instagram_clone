package com.instagram.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.User;
import com.instagram.api.response.MessageResponse;
import com.instagram.api.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findUserByIdHandler(@PathVariable Integer id) throws UserException {
        User user = userService.findUserById(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> findUserByUsernameHandler(@PathVariable String username) throws UserException {
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PutMapping("/follow/{followUserId}")
    public ResponseEntity<MessageResponse> followUserHandler(@PathVariable Integer followUserId,
            @RequestHeader("Authorization") String token) {
        try {
            // Tìm người dùng từ token
            User user = userService.findUserProfile(token);

            // Thực hiện theo dõi người dùng
            String message = userService.followUser(user.getId(), followUserId);

            // Tạo phản hồi
            MessageResponse messageResponse = new MessageResponse(message);

            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (UserException e) {
            // Xử lý lỗi tùy chỉnh và trả về phản hồi lỗi
            MessageResponse errorResponse = new MessageResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Xử lý các lỗi khác
            MessageResponse errorResponse = new MessageResponse("An error occurred while processing the request.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/unfollow/{userId}")
    public ResponseEntity<MessageResponse> unFollowUserHandler(@PathVariable Integer userId,
            @RequestHeader("Authorization") String token) {
        try {
            // Tìm người dùng từ token
            User user = userService.findUserProfile(token);

            // Thực hiện unfollow người dùng
            String message = userService.unFollowUser(user.getId(), userId);

            // Tạo phản hồi
            MessageResponse messageResponse = new MessageResponse(message);

            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (UserException e) {
            // Xử lý lỗi tùy chỉnh và trả về phản hồi lỗi
            MessageResponse errorResponse = new MessageResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Xử lý các lỗi khác
            MessageResponse errorResponse = new MessageResponse("An error occurred while processing the request.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/req")
    public ResponseEntity<User> findUserProfileHandler(@RequestHeader("Authorization") String token)
            throws UserException {
        User user = userService.findUserProfile(token);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/userid/{userIds}")
    public ResponseEntity<List<User>> findUserByUserIdsHandler(@PathVariable List<Integer> userIds)
            throws UserException {
        List<User> users = userService.findUserByIds(userIds);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUserHandler(@RequestParam("q") String query) throws UserException {
        List<User> users = userService.searchUser(query);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }    

    @PutMapping("/account/edit")
    public ResponseEntity<User> updateUserHandler(@RequestHeader("Authorization") String token,
            @RequestBody User user) throws UserException {
        User reqUser = userService.findUserProfile(token);
        User updateUser = userService.updateUserDetails(user, reqUser);
        return new ResponseEntity<User>(updateUser, HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<User>> getPopularUsers() {
        List<User> popularUsers = userService.getPopularUsers();
        return new ResponseEntity<>(popularUsers, HttpStatus.OK);
    }
}
