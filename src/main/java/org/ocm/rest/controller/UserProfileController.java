package org.ocm.rest.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ocm.dto.UserProfileDTO;
import org.ocm.rest.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/rest-api/user")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /*@PostMapping("/login")
    public ResponseEntity login(@RequestBody String body, HttpServletRequest request) {
        try {
            JSONObject jobj = new JSONObject(body);
            request.login(jobj.getString("username"), jobj.getString("password"));
        } catch (ServletException e) {
            return new ResponseEntity("Wrong username or password", HttpStatus.UNAUTHORIZED);
        }

        Authentication auth = (Authentication) request.getUserPrincipal();
        User user = (User) auth.getPrincipal();
        log.info("User {} logged in", user.getUsername());
        return ok(String.format("User logged in %s", user.getUsername()));
    }*/

    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@Valid @RequestBody UserProfileDTO newUser, BindingResult bindingResult) {
        try {
            if (newUser.getPassword() == null || newUser.getPassword().isBlank()){
                throw new Exception("PASSWORD is required");
            }
            return ok(userProfileService.register(newUser));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Cannot register user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity("User data is not valid due to constrains: " + bindingResult.getAllErrors().stream().map(err-> err.getDefaultMessage()).collect(Collectors.toList()).toString(), HttpStatus.BAD_REQUEST);
            }
        }
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/profile")
    public ResponseEntity profile(Principal principal){
        log.info("Retrieving profile for logged in user {}", principal.getName());
        return ok(userProfileService.profile(principal.getName()));
    }

    @SecurityRequirement(name = "JWT")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@Valid @RequestBody UserProfileDTO user, BindingResult bindingResult, Principal principal) {
        try {
            user.setUsername(principal.getName());
            return ok(userProfileService.update(user));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Cannot update user data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity("User data is not valid due to constrains: " + bindingResult.getAllErrors().stream().map(err-> err.getDefaultMessage()).collect(Collectors.toList()).toString(), HttpStatus.BAD_REQUEST);
            }
        }
    }
}
