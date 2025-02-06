package org.example.authentication.Controller;


import jakarta.validation.Valid;
import org.example.authentication.Dtos.SignUpRequestDto;
import org.example.authentication.Exceptions.EmailAlreadyExistsException;
import org.example.authentication.Models.User;
import org.example.authentication.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid SignUpRequestDto signUpRequestDto,BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        try {
            String response = userService.registerUser(signUpRequestDto);
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


}
