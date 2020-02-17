package com.radkevich.Messenger.controller;

import com.radkevich.Messenger.model.User;
import com.radkevich.Messenger.model.dto.AdminUserDto;
import com.radkevich.Messenger.service.UserService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users/")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> userList() {
        return userService.getAll();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<AdminUserDto> getUserById(@ApiParam(value = "ID of user to return", required = true)
                                                    @PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        AdminUserDto result = AdminUserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> userEditForm(@ApiParam(value = "ID of user to return", required = true)
                                               @PathVariable(name = "id") User user) {
        return new ResponseEntity<>("User roles has been changed", HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@ApiParam(value = "ID of user to return", required = true)
                                         @PathVariable("id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>("User has been deleted", HttpStatus.OK);
    }
}
