package projetos.test.Cinephy.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class RegisterDTO {
    @Email
    @NotBlank(message = "O email não pode ser nulo")
    private String email;

    @Size(min = 8,message = "A senha deve conter oito caracteres")
    private String password;
    @NotBlank(message = "A data de nascimento é obrigatória")
    private LocalDate birthDate;
    @NotBlank(message = "O nome de usuario é obrigatório")
    private String nickName;

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public LocalDate getBirthDate() {return birthDate;}

    public void setBirthDate(LocalDate birthDate) {this.birthDate = birthDate;}

    public String getNickName() {return nickName;}

    public void setNickName(String nickName) {this.nickName = nickName;}


}
