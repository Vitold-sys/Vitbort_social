package com.radkevich.Messenger.controller;

import com.radkevich.Messenger.model.User;
import com.radkevich.Messenger.model.dto.AdminUserDto;
import com.radkevich.Messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
        private final UserService userService;

        @Autowired
        public AdminController(UserService userService) {
            this.userService = userService;
        }

        @GetMapping(value = "users/{id}")
        public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "id") Long id) {
            User user = userService.findById(id);

            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            AdminUserDto result = AdminUserDto.fromUser(user);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
}
