package com.example.backend.user.repo;

import com.example.backend.user.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("ALL")
@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findByLoginEmail(String email);


    List<AppUser> findAll();
}
