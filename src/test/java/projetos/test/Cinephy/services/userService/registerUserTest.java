package projetos.test.Cinephy.services.userService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.UserRepository;
import projetos.test.Cinephy.security.JwtUtils;
import projetos.test.Cinephy.services.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class registerUserTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private JwtUtils jwtUtil;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void set_up(){
        MockitoAnnotations.openMocks(this);
        userService = new UserService(jwtUtil,encoder,userRepository);
    }



    @Test
    void registerUser_success() {
        String email = "test@example.com";
        String password = "password123";
        LocalDate birthDate = LocalDate.of(1995, 5, 15);
        String nickname = "testUser";

        UserEntity mockUser = new UserEntity();
        mockUser.setEmail(email);
        mockUser.setPassword("encodedPassword");
        mockUser.setBirthDate(birthDate);
        mockUser.setNickName(nickname);

        when(encoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

        UserEntity result = userService.registerUser(password, email, birthDate, nickname);

        assertNotNull(result, "The registered user should not be null.");
        assertEquals(email, result.getEmail(), "Email should match.");
        assertEquals("encodedPassword", result.getPassword(), "Password should be encoded.");
        assertEquals(birthDate, result.getBirthDate(), "Birth date should match.");
        assertEquals(nickname, result.getNickName(), "Nickname should match.");
    }

}
