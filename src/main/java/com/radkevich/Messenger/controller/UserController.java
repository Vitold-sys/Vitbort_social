package com.radkevich.Messenger.controller;

import com.radkevich.Messenger.model.User;
import com.radkevich.Messenger.model.dto.UserDto;
import com.radkevich.Messenger.service.UserService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;


@RestController
@CrossOrigin(origins = "http://localhost:8090")
@PreAuthorize("hasAuthority('USER')")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<UserDto> getUserById(@ApiParam(value = "ID of user to return", required = true)
                                               @PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("subscribe/{id}")
    public ResponseEntity<String> subscribe(@ApiParam(value = "ID of user to return", required = true)
                                            @PathVariable long id
    ) {
        userService.subscribe(id);
        return new ResponseEntity<String>("You have subscribe", HttpStatus.OK);
    }

    @GetMapping("unsubscribe/{id}")
    public ResponseEntity<String> unsubscribe(@ApiParam(value = "ID of user to return", required = true)
                                              @PathVariable long id
    ) {
        userService.unsubscribe(id);
        return new ResponseEntity<String>("You have unsubscribe", HttpStatus.OK);
    }

    @PutMapping("settings")
    public ResponseEntity<?> updateProfile(@RequestBody User userUpdate) throws IOException {
        if (userUpdate.getGender() == null || userUpdate.getPassword() == null || userUpdate.getFirstName() == null
                || userUpdate.getLastName() == null || userUpdate.getEmail() == null) {
            return new ResponseEntity<>("Please insert all fields", HttpStatus.BAD_REQUEST);
        }
        userService.updateProfile(userUpdate);
        return new ResponseEntity<>("You have been changed your user information", HttpStatus.OK);
    }

    @GetMapping("/downloadFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) throws MalformedURLException {
        Resource resource = userService.loadFileAsResource(id);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
