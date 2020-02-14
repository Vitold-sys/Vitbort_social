package com.radkevich.Messenger;

import com.radkevich.Messenger.filter.CustomRsqlVisitor;
import com.radkevich.Messenger.model.Message;
import com.radkevich.Messenger.model.User;
import com.radkevich.Messenger.repository.MessageRepo;
import com.radkevich.Messenger.repository.UserRepository;
import com.radkevich.Messenger.service.MessageService;
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
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private MessageService messageService;

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/messages"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }


    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/login").param("username", "jonh"))
                .andDo(print())
                .andExpect(status().isForbidden());
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