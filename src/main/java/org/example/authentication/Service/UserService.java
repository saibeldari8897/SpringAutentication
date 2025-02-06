package org.example.authentication.Service;


import org.example.authentication.Dtos.SignUpRequestDto;
import org.example.authentication.Models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public String registerUser(SignUpRequestDto signUpRequestDto);


    List<User> getAllUsers();
}
