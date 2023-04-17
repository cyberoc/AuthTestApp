package org.ocm.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ocm.dto.UserProfileDTO;
import org.ocm.rest.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepo;

    private final PasswordEncoder pwdEnc;

    private final UserDetailsManager usersManager;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepo, PasswordEncoder pwdEnc, UserDetailsManager usersManager) {
        this.userProfileRepo = userProfileRepo;
        this.pwdEnc = pwdEnc;
        this.usersManager = usersManager;
    }

    public String register(UserProfileDTO newUser){
        if (usersManager.userExists(newUser.getUsername())){
            log.info("User is already registered: {}", newUser.getUsername());
            return "User is already registered";
        } else {
            try {
                userProfileRepo.save(newUser);
                log.info("Profile saved for: {}", newUser.getUsername());
                UserDetails user = User.builder()
                        .username(newUser.getUsername().toLowerCase())
                        .password(pwdEnc.encode(newUser.getPassword()))
                        .roles("USER")
                        .build();

                usersManager.createUser(user);
                log.info("User created: {}", newUser.getUsername());
                return String.format("User %s registered succesfully", newUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
                log.info("Error while persisting user/profile data for: {}", newUser.getUsername());
                return String.format("Error while persisting user/profile data for %s", newUser.getUsername());
            }
        }
    }

//    public String login(String username, String password){
//        Optional<UserProfileDTO> user = userProfileRepo.findById(username);
//        if (user.isPresent() && pwdEnc.matches(password, user.get().getPassword())){
//            return "Logged !";
//        } else {
//            return "Wrong credentials !";
//        }
//    }

    public String profile(String username){
        ObjectMapper mapper = new ObjectMapper();
        Optional<UserProfileDTO> user = userProfileRepo.findById(username);
        try {
            if (user.isPresent()){
                user.get().setPassword(null);
                return mapper.writeValueAsString(user.get());
            } else {
                log.info("Profile doesn't exist for {}", username);
                return "User not found !";
            }
        } catch (JsonProcessingException jpe) {
            return "Cannot format user profile: " + jpe.getMessage();
        }
    }

    public String update(UserProfileDTO updUser){
        Optional<UserProfileDTO> user = userProfileRepo.findById(updUser.getUsername());
        if (user.isPresent()){
            UserProfileDTO currUser = user.get();
            currUser.setFirstName(updUser.getFirstName());
            currUser.setLastName(updUser.getLastName());
            currUser.setEmail(updUser.getEmail());
            userProfileRepo.save(currUser);
            log.info("User {} updated", updUser.getUsername());
            return "User succesfully updated: " + updUser.getUsername();
        } else {
            log.info("Cannot update inexistent user {}", updUser.getUsername());
            return "User not found !";
        }
    }
}
