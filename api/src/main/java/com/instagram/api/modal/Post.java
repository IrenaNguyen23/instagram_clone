package com.instagram.api.modal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.instagram.api.dto.UserDto;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String caption;

    private String image;

    private String location;

    private LocalDateTime createAt;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name="user_id")),
        @AttributeOverride(name = "email", column = @Column(name="user_email")),
    })
    private UserDto user;

    @OneToMany
    private List<Comment> comments = new ArrayList<Comment>();

    @Embedded
    @ElementCollection
    @JoinTable(name = "likedByUsers", joinColumns = @JoinColumn(name = "user id"))
    private Set<UserDto> likedByUsers = new HashSet<UserDto>();

    // Phương thức này sẽ tự động chạy trước khi lưu Post vào cơ sở dữ liệu
    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<UserDto> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(Set<UserDto> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }

    
}
