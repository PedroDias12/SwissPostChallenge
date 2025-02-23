package com.example.challenge.controller;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.exception.UserNotFoundException;
import com.example.challenge.resource.UserResource;
import com.example.challenge.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResource> getUser(@PathVariable final Long id) throws UserNotFoundException {
        UserResource resource = userService.getUser(id);
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody UserResource userResource) throws ServiceException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userResource));
    }
}
