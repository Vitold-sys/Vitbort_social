package com.radkevich.Messenger.controller;

import com.radkevich.Messenger.model.User;
import com.radkevich.Messenger.model.dto.UserDto;
import com.radkevich.Messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@PreAuthorize("hasAuthority('USER')")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id){
        User user = userService.findById(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("settings")
    public ResponseEntity<?> updateProfile(@RequestBody User userUpdate) throws IOException {
        if(userUpdate.getGender() == null || userUpdate.getPassword() == null || userUpdate.getFirstName() == null
        || userUpdate.getLastName() == null || userUpdate.getEmail() == null){
            return new ResponseEntity<>("Please insert all fields", HttpStatus.BAD_REQUEST);
        }
        userService.updateProfile(userUpdate);
        return new ResponseEntity<>("You have been changed your user information", HttpStatus.OK);
    }
}
