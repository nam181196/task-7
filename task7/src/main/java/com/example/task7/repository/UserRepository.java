package com.example.task7.repository;

import com.example.task7.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM user WHERE first_name LIKE :prefix%", nativeQuery = true)
    Page<User> findByFirstNameStartingWith(@Param("prefix") String prefix, Pageable pageable);
    @Query(value = "SELECT * FROM user WHERE first_name LIKE %:keyword%", nativeQuery = true)
    Page<User> findByFirstNameContaining(@Param("keyword") String keyword, Pageable pageable);
    Page<User> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName, Pageable pageable);
    Page<User> findByIdIn(List<Long> ids, Pageable pageable);
}
