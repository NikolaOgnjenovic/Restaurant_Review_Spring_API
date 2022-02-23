package com.apostoli.recenzije.app.controller;

import com.apostoli.recenzije.app.model.*;
import com.apostoli.recenzije.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class UserController {
    @RequestMapping(method = RequestMethod.OPTIONS)
    ResponseEntity<?> options() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE)
                .build();
    }

    private final UserService userService;

    @GetMapping
    List<ReturnUserDto> getAllUsers() {
        log.info("Getting all reviews");
        return userService.getAllUsers();
    }

    @GetMapping("{id}/getUser")
    ReturnUserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("{username}/getUser")
    ReturnUserDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/validateLogin/{username}/{password}")
    boolean validateLogin(@PathVariable String username, @PathVariable String password) {
        return userService.validateLogin(username, password);
    }

    @GetMapping("/getLikeCount/{email}")
    int getUserLikeCount(@PathVariable String email) {
        return userService.getUserLikeCount(email);
    }

    @GetMapping("/getDislikeCount/{email}")
    int getUserDislikeCount(@PathVariable String email) {
        return userService.getUserDislikeCount(email);
    }

    @GetMapping("/getReviews/{email}")
    List<ReturnReviewDto> getReviewsByUserEmail(@PathVariable String email) {
        return userService.getReviewsByUserEmail(email);
    }

    @PostMapping("/forgotPassword")
    void forgotPassword(@RequestBody String email) {
        userService.forgotPassword(email);
    }

    @DeleteMapping("{id}/deleteUser")
    void deleteById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PostMapping
    ReturnUserDto createUser(@RequestBody @Valid CreateUserDto user) {
        User createUser = User.builder()
                .username(user.username())
                .password(user.password())
                .email(user.email())
                .build();
        return userService.createUser(createUser);
    }
}
