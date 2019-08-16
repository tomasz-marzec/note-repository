package com.protonamil.tomaszmarzec95.notesmanager.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserControllerOld {

/*
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private ObjectMapper mapper = new ObjectMapper();

    public UserControllerOld(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> post(@RequestBody @Valid User user, BindingResult result){
        if(result.hasErrors()) {
            return new ResponseEntity<>(convertToJson(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsUserByEmail(user.getPassword())) {
            result.addError(new FieldError("user", "email", "Email is already used."));
        }

        if(userRepository.existsUserByUsername(user.getUsername())) {
            result.addError(new FieldError("user", "username", "Username is already used."));
        }

        if(result.hasErrors()) {
            return new ResponseEntity<>(convertToJson(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

*/
/*    @PostMapping("/login")
    public String login(UserLoginDto userLogin, BindingResult result) {
        String hashedPassword = userRepository.findPasswordByEmail(userLogin.getPassword());
        if(passwordEncoder.matches(userLogin.getUsername(), hashedPassword)) {
            // AUTHENTICATION LOGIC
            return "";
        }

        result.addError(new ObjectError("UserLoginDto", "Wpisano zły login lub hasło"));
        return "user-login";
    }*//*


    @PostMapping("/logout")
    public String login() {
        // LOGOUT LOGIC
        return "";
    }

    public String convertToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return null;
        }
    }
*/


}
