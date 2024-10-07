package com.instagram.api.modal;


import java.util.*;

import com.instagram.api.dto.UserDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;

    private String name;
    
    private String email;

    private String mobile;

    private String website;

    private String bio;

    private String gender;

    private String image;

    private String password;

    @Embedded
    @ElementCollection
    private Set<UserDto> follower = new HashSet<UserDto>();

    @Embedded
    @ElementCollection
    private Set<UserDto> following = new HashSet<UserDto>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Story> stories = new ArrayList<>();
    
    @ManyToMany
    private List<Post> savedPost = new ArrayList<>();

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", name=" + name + ", email=" + email + ", mobile="
                + mobile + ", website=" + website + ", bio=" + bio + ", gender=" + gender + ", image=" + image
                + ", password=" + password + ", follower=" + follower + ", following=" + following + ", stories="
                + stories + ", savedPost=" + savedPost + "]";
    }

}
