package projetos.test.Cinephy.services.userService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.UserRepository;
import projetos.test.Cinephy.security.JwtUtils;
import projetos.test.Cinephy.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class getUserFromTokenTest {
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
    void getUserFromToken_success() {
        String token = "Bearer jwtToken";
        String email = "test@example.com";

        UserEntity mockUser = new UserEntity();
        mockUser.setEmail(email);

        when(jwtUtil.getUserFromToken("jwtToken")).thenReturn(email);
        when(userRepository.findUserByEmail(email)).thenReturn(mockUser);

        UserEntity result = userService.getUserFromToken(token);

        assertNotNull(result, "The authenticated user should not be null.");
        assertEquals(email, result.getEmail(), "Email should match.");
    }

    @Test
    void getUserFromToken_invalidToken() {
        String token = "Bearer invalidJwtToken";

        when(jwtUtil.getUserFromToken("invalidJwtToken")).thenThrow(new RuntimeException("Invalid token"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getUserFromToken(token)
        );

        assertEquals("Invalid token", exception.getMessage(), "Exception message should match.");
    }

    @Test
    void getUserFromToken_userNotFound() {
        String token = "Bearer jwtToken";
        String email = "test@example.com";

        when(jwtUtil.getUserFromToken("jwtToken")).thenReturn(email);
        when(userRepository.findUserByEmail(email)).thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getUserFromToken(token)
        );

        assertEquals("Ocorreu um erro na autenticação", exception.getMessage(), "Exception message should match.");
    }
}
