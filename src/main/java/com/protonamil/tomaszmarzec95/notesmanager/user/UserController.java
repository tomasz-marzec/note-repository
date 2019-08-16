package com.protonamil.tomaszmarzec95.notesmanager.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.protonamil.tomaszmarzec95.notesmanager.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private AuthenticationManager authenticationManager;
    private  UserService userService;
    private ObjectMapper mapper = new ObjectMapper();
    private UserRepository userRepo;
    private JwtTokenProvider tokenProvider;

    public UserController(UserService userService, AuthenticationManager authenticationManager,
                          UserRepository userRepo, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid User user, BindingResult result) {
      if(result.hasErrors()) {
          return new ResponseEntity<>(convertToJson(result.getAllErrors()), HttpStatus.BAD_REQUEST);
      }

        if(userRepo.existsUserByEmail(user.getEmail())) {
            result.addError(new FieldError("user", "email", "Email is already used."));
        }

        if(userRepo.existsUserByUsername(user.getUsername())) {
            result.addError(new FieldError("user", "username", "Username is already used."));
        }

        if(result.hasErrors()) {
            return new ResponseEntity<>(convertToJson(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(user);

        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody UserLoginDto loginDto) {
        try{
            String username = loginDto.getUsername();
            String password = loginDto.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username, password));
            String token = tokenProvider.createToken(username,
                    this.userRepo.findByUsername(username).getRoleSet());
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new BadCredentialsException("Invalid email/password supplied");
        }
    }

    public String convertToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return null;
        }
    }

}
