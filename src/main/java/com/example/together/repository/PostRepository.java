package com.example.together.repository;

import com.example.together.domain.Category;
import com.example.together.domain.Post;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> findAllByOrderByModifiedAtDesc();
//  List<Post> findAllByCategory(Integer category);
//  List<Post> findAllByCategoryAndOrderByModifiedAtDesc(Category category);
  List<Post> findAllByCategory(Category category);

}
