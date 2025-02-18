package org.example.authentication.Controller;


import jakarta.validation.Valid;
import org.example.authentication.Dtos.SignInRequestDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody @Valid SignUpRequestDto signUpRequestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(errorResponse); // Return error as JSON
        }

        try {
            String response = userService.registerUser(signUpRequestDto);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", response); // wrap response in JSON
            return ResponseEntity.ok(successResponse); // Return success as JSON
        } catch (EmailAlreadyExistsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse); // Return error as JSON
        }
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @PostMapping("/signIn")
    public String signIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        if(userService.signInUser(signInRequestDto)){
            return "success";
        }
        return "fail";
    }

}
