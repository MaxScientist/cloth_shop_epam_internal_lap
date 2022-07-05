package com.epam.shop.controller;

import com.epam.shop.dto.DTOMapper;
import com.epam.shop.dto.LoginDTO;
import com.epam.shop.dto.UserGetDTO;
import com.epam.shop.dto.UserPostDTO;
import com.epam.shop.entity.User;
import com.epam.shop.security.JwtTokenProvider;
import com.epam.shop.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowedHeaders = "*")
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final DTOMapper<User, UserGetDTO> userGetMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final DTOMapper<User, UserPostDTO> userPostMapper;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('READ') or hasAuthority('WRITE')")
    public ResponseEntity<List<UserGetDTO>> getAllUser() {
        try {
            List<User> users = userService.findAll();
            List<UserGetDTO> userGetDTOS = users.stream().map(userGetMapper::toDTO
            ).collect(Collectors.toList());
            return new ResponseEntity<>(userGetDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/users", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveUser(@RequestBody UserPostDTO userPostDTO) {
        try {
            userService.save(userPostMapper.fromDTO(userPostDTO));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('READ','WRITE')")
    public ResponseEntity findUserById(@PathVariable("id") int id) {
        try {
            User user = userService.findById(id);
            return new ResponseEntity<>(userGetMapper.toDTO(user), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity updateUserById(@PathVariable("id") int id, @RequestBody UserPostDTO userPostDTO) {
        try {
            userService.updateById(id, userPostMapper.fromDTO(userPostDTO));
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity deleteUserById(@PathVariable("id") int id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
            User user = userService.findUserByUserName(loginDto.getUserName());
            if (user == null) {
                throw new UsernameNotFoundException("User does not exist");
            }

            String token = jwtTokenProvider.createToken(loginDto.getUserName(), user.getRole().getRoleType());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", loginDto.getUserName());
            response.put("token", token);
            response.put("permission", user.getRole().getPermission());
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity("Unable to login, check email or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
