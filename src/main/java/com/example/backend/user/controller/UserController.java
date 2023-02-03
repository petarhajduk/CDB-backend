package com.example.backend.user.controller;

import com.example.backend.user.controller.dto.UserDTO;
import com.example.backend.user.model.AUTHORITY;
import com.example.backend.user.model.AppUser;
import com.example.backend.user.service.UserService;
import com.example.backend.util.JwtVerifier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @ResponseBody
    public ResponseEntity addUser(@RequestHeader String googleTokenEncoded, @RequestBody UserDTO userDTO) {
        if (userService.existsAny()) {
            List<AUTHORITY> a = List.of(AUTHORITY.ADMIN);
            String email = JwtVerifier.verifyAndReturnEmail(googleTokenEncoded);
            if (email == null)
                return new ResponseEntity("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
            if (userService.findByEmail(email) == null)
                return new ResponseEntity("You don't have access to CDB", HttpStatus.UNAUTHORIZED);
            if (!a.contains(userService.findByEmail(email).getAuthority()))
                return new ResponseEntity("You don't have premission to this resource", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity(userService.addUser(userDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity findUser(@RequestHeader String googleTokenEncoded, @PathVariable Long id){
        String email = JwtVerifier.verifyAndReturnEmail(googleTokenEncoded);
        if (email == null)
            return new ResponseEntity("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        if (userService.findByEmail(email) == null)
            return new ResponseEntity("You don't have access to CDB", HttpStatus.UNAUTHORIZED);

        if (!userService.existsById(id)) return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity(userService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity deleteUser(@RequestHeader String googleTokenEncoded, @PathVariable Long id){
        List<AUTHORITY> a = List.of(AUTHORITY.ADMIN);
        String email = JwtVerifier.verifyAndReturnEmail(googleTokenEncoded);
        if (email == null)
            return new ResponseEntity("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        if (userService.findByEmail(email) == null)
            return new ResponseEntity("You don't have access to CDB", HttpStatus.UNAUTHORIZED);
        if (!a.contains(userService.findByEmail(email).getAuthority()))
            return new ResponseEntity("You don't have premission to this resource", HttpStatus.UNAUTHORIZED);

        if (!userService.existsById(id)) return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        userService.deleteUser(id);
        return new ResponseEntity("User under id: " + id + " successfully deleted.", HttpStatus.OK);
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<AppUser> findAll(@RequestHeader String googleTokenEncoded){
        String email = JwtVerifier.verifyAndReturnEmail(googleTokenEncoded);
        if (email == null)
            return new ResponseEntity("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        if (userService.findByEmail(email) == null)
            return new ResponseEntity("You don't have access to CDB", HttpStatus.UNAUTHORIZED);

        return new ResponseEntity(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/exists-any")
    public boolean existsAny(){
        return userService.existsAny();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@RequestHeader String googleTokenEncoded, @RequestBody UserDTO userDTO, @PathVariable Long id){
        List<AUTHORITY> a = new LinkedList<>(List.of(AUTHORITY.ADMIN));
        String email = JwtVerifier.verifyAndReturnEmail(googleTokenEncoded);
        if (email == null)
            return new ResponseEntity("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        if (userService.findByEmail(email) == null)
            return new ResponseEntity("You don't have access to CDB", HttpStatus.UNAUTHORIZED);
        if (!a.contains(userService.findByEmail(email).getAuthority()))
            return new ResponseEntity("You don't have premission to this resource", HttpStatus.UNAUTHORIZED);

        if (!userService.existsById(id)) return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity(userService.updateUser(userDTO, id), HttpStatus.OK);
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity getByEmail(@RequestHeader String googleTokenEncoded){
        String email = JwtVerifier.verifyAndReturnEmail(googleTokenEncoded);
        if (!userService.existsByEmail(email)) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(userService.findByEmail(email), HttpStatus.OK);
    }
}
