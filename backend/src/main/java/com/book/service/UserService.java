package com.book.service;

import com.book.entity.User;
import com.book.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public Optional<User> login(String email, String password) {
        return repo.findByEmail(email).filter(u -> u.getPassword().equals(password));
    }

    public User register(String name, String email, String password) {
        if (repo.findByEmail(email).isPresent()) throw new RuntimeException("邮箱已存在");
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(password);
        return repo.save(u);
    }

    public User findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public void promoteToAdmin(String email) {
        User u = repo.findByEmail(email).orElseThrow(() -> new RuntimeException("用户不存在"));
        u.setRole("admin");
        repo.save(u);
    }
}