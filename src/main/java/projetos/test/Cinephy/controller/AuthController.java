package projetos.test.Cinephy.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetos.test.Cinephy.DTOs.LoginDTO;
import projetos.test.Cinephy.DTOs.RegisterDTO;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.services.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/auth")
public class AuthController{

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO register){
        LocalDate parsedDate = LocalDate.parse(register.getBirthDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        service.registerUser(register.getPassword(),register.getEmail(),parsedDate, register.getNickName());
        return ResponseEntity.ok("Usuario registrado com sucesso!!!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> token(@RequestBody @Valid LoginDTO login){
        String token = service.loginUser(login.getEmail(),login.getPassword());
        return ResponseEntity.ok(token);
    }

}
