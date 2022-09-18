package com.ubt.auth.repository;

import com.ubt.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    User findByEmail(String email);

    User findByUsername(String username);

    User findByUsernameOrEmail(String username, String email);

    User findByResetToken(String token);

    User findByConfirmToken(String token);

    // Page<User> findAlByFirstNameContainingOrLastNameContaining(String name, String lastName, Pageable pageable);

    void deleteAllByIdIn(List<Long> ids);
}
