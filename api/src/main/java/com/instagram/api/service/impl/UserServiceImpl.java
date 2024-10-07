package com.instagram.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.instagram.api.dto.UserDto;
import com.instagram.api.exceptions.UserException;
import com.instagram.api.modal.User;
import com.instagram.api.repository.UserRepository;
import com.instagram.api.security.JwtTokenClaims;
import com.instagram.api.security.JwtTokenProvider;
import com.instagram.api.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public User registerUser(User user) throws UserException {
        Optional<User> isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist.isPresent()) {
            throw new UserException("Email is adready exist");
        }
        Optional<User> isUsernameExist = userRepository.findByUsername(user.getUsername());
        {
            if (isUsernameExist.isPresent()) {
                throw new UserException("Username is adready taken");
            }
        }
        if (user.getEmail() == null || user.getPassword() == null || user.getUsername() == null
                || user.getName() == null) {
            throw new UserException("All filds are required");
        }
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUsername(user.getUsername());
        newUser.setName(user.getName());
        return userRepository.save(newUser);
    }

    @Override
    public User findUserById(Integer userId) throws UserException {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new UserException("user not exist with id:" + userId);
    }

    @Override
    public User findUserProfile(String token) throws UserException {
        token = token.substring(7);
        JwtTokenClaims jwtTokenClaims = jwtTokenProvider.getClaimsFromToken(token);
        String email = jwtTokenClaims.getUsername();
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new UserException("Invalid Token...");
    }

    @Override
    public User findUserByUsername(String username) throws UserException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserException("user not exist with username:" + username);
    }

    @Override
    public String followUser(Integer reqUserId, Integer followUserId) throws UserException {
        
        User reqUser = findUserById(reqUserId);
        User followUser = findUserById(followUserId);

        // Kiểm tra nếu reqUser đã theo dõi followUser hay chưa
        boolean alreadyFollowing = reqUser.getFollowing().stream()
                .anyMatch(user -> user.getId().equals(followUserId));

        if (alreadyFollowing) {
            return "You are already following " + followUser.getUsername();
        }

        // Chuyển đổi User sang UserDto
        UserDto reqUserDto = new UserDto(reqUser);
        UserDto followUserDto = new UserDto(followUser);

        // Thêm vào tập hợp
        reqUser.getFollowing().add(followUserDto);
        followUser.getFollower().add(reqUserDto);

        // Lưu cả hai đối tượng vào database
        userRepository.save(reqUser);
        userRepository.save(followUser);

        return "You are now following " + followUser.getUsername();
    }

    @Override
    public String unFollowUser(Integer reqUserId, Integer followUserId) throws UserException {
        User reqUser = findUserById(reqUserId);
        User followUser = findUserById(followUserId);

        // Kiểm tra nếu reqUser đang theo dõi followUser hay không
        boolean isFollowing = reqUser.getFollowing().stream()
                .anyMatch(user -> user.getId().equals(followUserId));

        if (!isFollowing) {
            return "You are not following " + followUser.getUsername();
        }

        UserDto reqUserDto = new UserDto(reqUser);
        UserDto followUserDto = new UserDto(followUser);

        // Xóa followUserDto khỏi danh sách đang theo dõi của reqUser
        reqUser.getFollowing().remove(followUserDto);
        // Xóa reqUserDto khỏi danh sách người theo dõi của followUser
        followUser.getFollower().remove(reqUserDto);

        // Lưu cả hai đối tượng vào database
        userRepository.save(reqUser);
        userRepository.save(followUser);

        return "You have unfollowed " + followUser.getUsername();
    }

    @Override
    public List<User> findUserByIds(List<Integer> userIds) throws UserException {
        List<User> users = userRepository.findAllUsersByUserIds(userIds);
        return users;
    }

    @Override
    public List<User> searchUser(String query) throws UserException {
        List<User> users = userRepository.findByQuery(query);
        if (users.size() == 0) {
            throw new UserException("user not found");
        }
        return users;
    }

    @Override
    public User updateUserDetails(User updateUser, User existingUser) throws UserException {
        if (updateUser.getEmail() != null) {
            existingUser.setEmail(updateUser.getEmail());
        }
        if (updateUser.getBio() != null) {
            existingUser.setBio(updateUser.getBio());
        }
        if (updateUser.getName() != null) {
            existingUser.setName(updateUser.getName());
        }
        if (updateUser.getUsername() != null) {
            existingUser.setUsername(updateUser.getUsername());
        }
        if (updateUser.getMobile() != null) {
            existingUser.setMobile(updateUser.getMobile());
        }
        if (updateUser.getGender() != null) {
            existingUser.setGender(updateUser.getGender());
        }
        if (updateUser.getWebsite() != null) {
            existingUser.setWebsite(updateUser.getWebsite());
        }
        if (updateUser.getImage() != null) {
            existingUser.setImage(updateUser.getImage());
        }
        if (updateUser.getId().equals(existingUser.getId())) {
            return userRepository.save(existingUser);
        }

        throw new UserException("You can't update this user");

    }

    @Override
    public List<User> getPopularUsers() {
        Pageable topFive = PageRequest.of(0, 5);
        return userRepository.findPopularUsers(topFive);
    }

}
