package com.epam.shop.controller;


import com.epam.shop.dto.LoginDTO;
import com.epam.shop.dto.UserPostDTO;
import com.epam.shop.entity.Role;
import com.epam.shop.entity.User;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.repository.RoleRepository;
import com.epam.shop.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class UserControllerTest {
    private static final Integer USER_ID = 3;

    @MockBean
    UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DTOMapper<User, UserPostDTO> userPostMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;



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
        UserPostDTO userPostDTO = userPostDTO();

        String requestBody = objectMapper.writeValueAsString(userPostDTO);

        mockMvc.perform(post("/api/users/users")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        when(userService.findById(userPostDTO.getId())).thenReturn(userPostMapper.fromDTO(userPostDTO));
        User userCheck = userService.findById(userPostDTO.getId());
        assertEquals(userCheck, userPostMapper.fromDTO(userPostDTO));
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

    @Test
    void login() throws Exception {
        LoginDTO loginDTO = createUserPost();


        User user = new User();
        user.setUsername("admin");
        Optional<Role> optionalUserRole = roleRepository.findAll().stream().filter(roleType -> roleType.getRoleType().equals("USER")).findFirst();
        Role userRole = optionalUserRole.orElse(null);
        user.setRole(userRole);
        when(userService.findUserByUserName(loginDTO.getUsername())).thenReturn(user);
        String json = objectMapper.writeValueAsString(loginDTO);

        this.mockMvc.perform(post("/api/users/login")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

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

    private UserPostDTO userPostDTO() {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setId(3);
        userPostDTO.setUsername("test");
        return userPostDTO;
    }

    private User createUserObject() {
        User user = new User();
        user.setId(USER_ID);
        user.setOrders(new ArrayList<>());
        return user;
    }
}
