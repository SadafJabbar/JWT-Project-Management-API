package project_management__api.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User with id " + userId + " is not found");
    }
}
