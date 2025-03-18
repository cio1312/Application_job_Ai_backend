package com.job.app.service;

import com.job.app.model.User;
import com.job.app.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        // Directly save the user, assuming password is already encoded in the controller
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Add this method to find the user by ID
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);  // Return null if user not found
    }
    
    
    public User findByUsernameAndEmail(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }
    
    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }
    
    public void markUserAsPaid(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setHasPaidForCourse(true);
            userRepository.save(user);
        }
    }
}
