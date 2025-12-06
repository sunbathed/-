package com.book.controller;

import com.book.entity.User;
import com.book.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import com.book.utils.Maps;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService svc;
    public UserController(UserService svc) { this.svc = svc; }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody Map<String,String> body) {
        return svc.login(body.get("email"), body.get("password"))
                .map(u -> {
                    Map<String, Object> res = new java.util.HashMap<>();
                    res.put("id", u.getId());
                    res.put("name", u.getName());
                    res.put("email", u.getEmail());
                    res.put("role", u.getRole());
                    return res;
                })
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.UNAUTHORIZED, "账号或密码错误"));
    }

    @PostMapping("/register")
    public Map<String,Object> register(@RequestBody Map<String,String> body) {
        User u = svc.register(body.get("name"), body.get("email"), body.get("password"));
        Map<String, Object> res = new java.util.HashMap<>();
        res.put("id", u.getId());
        res.put("name", u.getName());
        res.put("email", u.getEmail());
        res.put("role", u.getRole());
        return res;
    }

    @PostMapping("/admin/promote")
    public Map<String,String> promote(
            @RequestParam(value = "email", required = false) String email,
            @RequestBody(required = false) Map<String,String> body
    ) {
        String targetEmail = email != null ? email : (body != null ? body.get("email") : null);
        if (targetEmail == null || targetEmail.trim().isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "email不能为空");
        }
        svc.promoteToAdmin(targetEmail);
        return Maps.of("status", "ok");
    }
}
