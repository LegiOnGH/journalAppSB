package com.legion.journal.controller;

import com.legion.journal.dto.UserDTO;
import com.legion.journal.entity.User;
import com.legion.journal.service.UserService;
import com.legion.journal.utils.JWTUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/public")
@Tag(name="Public APIs")
public class PublicController {

    @Autowired
    private JWTUtil jwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody com.legion.journal.dto.UserDTO user){
        try {
            User newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setUserName(user.getUserName());
            newUser.setPassword(user.getPassword());
            newUser.setSentimentAnalysis(user.isSentimentAnalysis());
            userService.saveNewUser(newUser);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user){
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setUserName(user.getUserName());
        newUser.setPassword(user.getPassword());
        newUser.setSentimentAnalysis(user.isSentimentAnalysis());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(newUser.getUserName(), newUser.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getUserName());
            String token= jwt.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(token, HttpStatus.OK);

        }catch(Exception e){
            log.error("Exception occurred while Authentication: ",e);
            return new ResponseEntity<>("Incorrect username or password.", HttpStatus.BAD_REQUEST);
        }
    }
}
