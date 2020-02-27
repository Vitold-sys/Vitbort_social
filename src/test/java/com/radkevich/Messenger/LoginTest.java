package com.radkevich.Messenger;

import com.radkevich.Messenger.controller.AuthenticationController;
import com.radkevich.Messenger.controller.UserController;
import com.radkevich.Messenger.filter.CustomRsqlVisitor;
import com.radkevich.Messenger.model.Message;
import com.radkevich.Messenger.model.Role;
import com.radkevich.Messenger.model.User;
import com.radkevich.Messenger.repository.MessageRepo;
import com.radkevich.Messenger.repository.UserRepository;
import com.radkevich.Messenger.security.jwt.JwtTokenProvider;
import com.radkevich.Messenger.service.MessageService;
import com.radkevich.Messenger.service.UserService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.radkevich.Messenger.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class LoginTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageRepo messageRepo;

    @Test
    public void getListUsers() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login")).andExpect(status().isForbidden());
    }

    @Test
    public void shouldGenerateAuthToken() throws Exception {
            String username = "ad";
        User user = userService.findByUsername(username);
        String token = jwtTokenProvider.createToken("ad", user.getRoles());
        assertNotNull(token);

    }

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/messages"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void AddMessage() throws Exception {
        this.mockMvc.perform(post("/messages").param("message", "Loliz")
                .param("tag", "Spring"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void filterWithMultiparam() {
        Page<Message> messages;
        Node rootNode = new RSQLParser().parse("tag==authumn");
        Specification<Message> spec = rootNode.accept(new CustomRsqlVisitor<Message>());
        messages =  messageRepo.findAll(spec, Pageable.unpaged());
        System.out.println(messages);
    }
}