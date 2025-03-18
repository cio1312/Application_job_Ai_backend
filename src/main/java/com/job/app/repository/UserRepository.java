package com.job.app.repository;

import com.job.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByResetToken(String resetToken); // New method for finding user by token

	User findByUsernameAndEmail(String username, String email);
}


