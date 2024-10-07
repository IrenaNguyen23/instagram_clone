package com.instagram.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.User;
import com.instagram.api.repository.UserRepository;
import com.instagram.api.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<User> registerUserHandler(@RequestBody User user) throws UserException {
        User createUser = userService.registerUser(user);
        
        return new ResponseEntity<User>(createUser,HttpStatus.OK);
    }

    @GetMapping("/signin")
    public ResponseEntity<User> signinHandler (Authentication auth) throws BadCredentialsException{

        Optional<User> opt = userRepository.findByEmail(auth.getName());

        if(opt.isPresent()){
            return new ResponseEntity<User>(opt.get(), HttpStatus.OK);
        }
        throw new BadCredentialsException("invalid user or password");
    }
    
}
