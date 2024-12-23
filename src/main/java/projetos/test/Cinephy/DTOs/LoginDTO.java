package projetos.test.Cinephy.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class LoginDTO {
    @Email(message = "O campo deve ser um email valido")
    private String email;
    @Size(min = 8, message = "A senha deve ter no minimo 8 digitos")
    private String password;

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}
