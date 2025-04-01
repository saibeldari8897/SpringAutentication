package org.example.authentication.ServiceImpl;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.authentication.Dtos.SignInRequestDto;
import org.example.authentication.Dtos.SignUpRequestDto;
import org.example.authentication.Exceptions.EmailAlreadyExistsException;
import org.example.authentication.Models.User;
import org.example.authentication.Repository.UserRepository;
import org.example.authentication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    private PasswordEncoder passwordEncoder;
    @Qualifier("userService")
    @Autowired
    private UserService userService;

    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public String registerUser(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .firstName(signUpRequestDto.getFirstName())
                .lastName(signUpRequestDto.getLastName())
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .build();

        // Validate entity before saving
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new RuntimeException("Validation failed: " + errorMessage);
        }
        userRepository.save(user);
        return "User created Successfully";
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean signInUser(SignInRequestDto signInRequestDto) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(signInRequestDto.getEmail()));
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public User getUserByName(String name){
        User user = userRepository.getUserByName(String name);
        if(user.isPresent()){
            return user;
        }
        return null;
    }
}
