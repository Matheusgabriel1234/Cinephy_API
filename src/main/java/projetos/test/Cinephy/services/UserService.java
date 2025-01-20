package projetos.test.Cinephy.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projetos.test.Cinephy.Exceptions.EmailAlreadyExistsException;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.UserRepository;
import projetos.test.Cinephy.security.JwtUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    private final JwtUtils jwtUtil;

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    public UserService(JwtUtils jwtUtil, PasswordEncoder encoder, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    public UserEntity registerUser(String password, String email, LocalDate birthDate,String nickname){
        UserEntity user = new UserEntity();
        user.setPassword(encoder.encode(password));
        user.setEmail(email);
        user.setBirthDate(birthDate);
        user.setNickName(nickname);

        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException("Esse email ja foi registrado");
        }

        return userRepository.save(user);

    }


    public String loginUser(String email,String password){
        UserEntity user = userRepository.findUserByEmail(email);

        if(user == null || !encoder.matches(password,user.getPassword())){
            throw new RuntimeException("Ocorreu um erro na autenticação");
        }

        return jwtUtil.generateToken(user);

    }

    public UserEntity getUserFromToken(String token){

        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        String email = jwtUtil.getUserFromToken(token);
        UserEntity authUser = userRepository.findUserByEmail(email);
        if(authUser == null){
            throw new RuntimeException("Ocorreu um erro na autenticação");
        }
        return authUser;
    }
}
