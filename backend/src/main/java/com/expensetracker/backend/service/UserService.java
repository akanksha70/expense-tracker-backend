package com.expensetracker.backend.service;

import com.expensetracker.backend.entity.User;
import com.expensetracker.backend.exception.DuplicateResourceException;
import com.expensetracker.backend.exception.InvalidOperationException;
import com.expensetracker.backend.exception.ResourceNotFoundException;
import com.expensetracker.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
private PasswordEncoder passwordEncoder;

    // Register a new user
   public User registerUser(String email, String password) {
    if (userRepository.existsByEmail(email)) {
        throw new DuplicateResourceException("User", "email", email);
    }
    
    // Hash the password
    String hashedPassword = passwordEncoder.encode(password);
    
    User user = new User(email, hashedPassword);
    return userRepository.save(user);
}

// Login user
public User login(String email, String password) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    
    // Verify password
    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new InvalidOperationException("Invalid password");
    }
    
    return user;
}

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Find user by ID
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    // Get all users (for testing only, remove in production)
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}