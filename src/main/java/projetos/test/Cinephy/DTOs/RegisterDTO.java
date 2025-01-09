package projetos.test.Cinephy.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class RegisterDTO {
    @Email
    @NotBlank(message = "O email não pode ser nulo")
    private String email;

    @Size(min = 8,message = "A senha deve conter oito caracteres")
    private String password;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotBlank(message = "A data de nascimento é obrigatória")
    private String birthDate;
    @NotBlank(message = "O nome de usuario é obrigatório")
    private String nickName;

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public @NotBlank(message = "A data de nascimento é obrigatória") String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@NotBlank(message = "A data de nascimento é obrigatória") String birthDate) {
        this.birthDate = birthDate;
    }

    public String getNickName() {return nickName;}

    public void setNickName(String nickName) {this.nickName = nickName;}


}
