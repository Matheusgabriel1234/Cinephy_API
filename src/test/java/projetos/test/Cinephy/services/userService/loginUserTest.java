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
import projetos.test.Cinephy.services.ReviewService;
import projetos.test.Cinephy.services.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class loginUserTest {
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
    void loginUser_success() {
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        String token = "jwtToken";

        UserEntity mockUser = new UserEntity();
        mockUser.setEmail(email);
        mockUser.setPassword(encodedPassword);

        when(userRepository.findUserByEmail(email)).thenReturn(mockUser);
        when(encoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(mockUser)).thenReturn(token);

        String result = userService.loginUser(email, password);

        assertNotNull(result, "The token should not be null.");
        assertEquals(token, result, "Token should match.");
    }

    @Test
    void loginUser_invalidCredentials() {
        String email = "test@example.com";
        String password = "wrongPassword";

        UserEntity mockUser = new UserEntity();
        mockUser.setEmail(email);
        mockUser.setPassword("encodedPassword");

        when(userRepository.findUserByEmail(email)).thenReturn(mockUser);
        when(encoder.matches(password, "encodedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.loginUser(email, password)
        );

        assertEquals("Ocorreu um erro na autenticação", exception.getMessage(), "Exception message should match.");
    }
}
