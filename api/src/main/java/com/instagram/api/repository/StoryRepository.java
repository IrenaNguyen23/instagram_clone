package com.instagram.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instagram.api.modal.Story;
import java.util.*;

public interface StoryRepository extends JpaRepository<Story,Integer>{
    @Query("SELECT s FROM Story s WHERE s.user.id= :userId")
    List<Story> findAllStoryByUserId(@Param("userId") Integer userId);
} 