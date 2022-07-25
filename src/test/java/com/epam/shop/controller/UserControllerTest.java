package com.epam.shop.controller;


import com.epam.shop.dto.LoginDTO;
import com.epam.shop.dto.UserPostDTO;
import com.epam.shop.entity.Permission;
import com.epam.shop.entity.Role;
import com.epam.shop.entity.User;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.mapper.UserGetMapper;
import com.epam.shop.repository.UserRepository;
import com.epam.shop.security.JwtTokenProvider;
import com.epam.shop.security.UserDetailsImpl;
import com.epam.shop.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final int ORDER_ID = 1;
    private static Integer USER_ID = 3;

    @MockBean
    UserServiceImpl userService;

    @Autowired
    private UserGetMapper userGetMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private DTOMapper<User, UserPostDTO> userPostMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AuthenticationManager authenticationManager;

    @Test
    void findAllUsers() throws Exception {
        User user = createUserObject();
        List<User> userList = List.of(user);

        Mockito.when(userService.findAll()).thenReturn(userList);

        this.mockMvc.perform(get("/api/users/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(USER_ID)));

        verify(userService, times(1)).findAll();
    }


    @Test
    void createUser() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(userPostDTO);

        this.mockMvc.perform(post("/api/users/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(userService, times(1)).save(userPostMapper.fromDTO(userPostDTO));
    }


    @Test
    void findUserById() throws Exception {
        User user = createUserObject();

        Mockito.when(userService.findById(USER_ID)).thenReturn(user);

        this.mockMvc.perform(get("/api/users/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(USER_ID)));

        verify(userService, times(1)).findById(USER_ID);
    }

    @Test
    void updateUser() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(userPostDTO);

        this.mockMvc.perform(put("/api/users/3")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateById(USER_ID, userPostMapper.fromDTO(userPostDTO));
    }

    @Test
    void deleteUserById() throws Exception {
        User user = createUserObject();

        Mockito.when(userService.findById(USER_ID)).thenReturn(user);

        this.mockMvc.perform(delete("/api/users/3"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteById(USER_ID);
    }


    @Autowired
    private UserRepository userRepository;

    @Test
    void login() throws Exception {
        LoginDTO loginDTO = createUserPost();

        User user = new User();
        user.setUsername("test");
//        Role role = new Role();
//        role.setRoleType("USER");
//        Permission permission = new Permission();
//        permission.setName("WRITE");
//        Set<Permission> permissions = new HashSet<>();
//        permissions.add(permission);
//        role.setPermission(permissions);
        user.setFirstname("firstName");
        user.setLastname("lastName");
        user.setPhoneNumber("87051256932");
        user.setPassword("test");
//        user.setRole(role);
        userService.save(user);

        String json = objectMapper.writeValueAsString(loginDTO);
        User userCheck = loginCheckUser(loginDTO);
        String token = jwtTokenProvider.createToken(loginDTO.getUsername(), userCheck.getRole().getRoleType());

//        Map<Object, Object> response = new HashMap<>();
//        response.put("username", loginDTO.getUsername());
//        response.put("token", token);
//        response.put("permission", userCheck.getRole().getPermission());

        Mockito.when(userCheck).thenReturn(user);

        this.mockMvc.perform(post("/api/users/login")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

//        verify(userService, times(1)).findUserByUserName(loginDTO.getUsername());
    }

    private User loginCheckUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        User userCheck = userRepository.findByUsername(loginDTO.getUsername());
        if (userCheck == null) {
            throw new UsernameNotFoundException("User does not exist");
        }
        return userCheck;
    }

    @Test
    void logout() throws Exception {
        this.mockMvc.perform(get("/api/users/logout"))
                .andExpect(status().isOk());
    }

    private LoginDTO createUserPost() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("test");
        loginDTO.setPassword("test");
        return loginDTO;
    }

    private User createUserObject() {
        User user = new User();
        user.setId(USER_ID);
        user.setOrders(new ArrayList<>());
        return user;
    }
}
