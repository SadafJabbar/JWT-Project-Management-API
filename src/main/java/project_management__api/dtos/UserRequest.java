package project_management__api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @Size(min = 3,max = 30,message = "username must be between 3 to 30 characters ")
        String username,
        String password,
        @Email(message = "Email must be valid")
        String email,
        Role systemRole
) {
}
