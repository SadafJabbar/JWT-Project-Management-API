package project_management__api.exceptions;

public class UsernameNotFound extends RuntimeException {
    public UsernameNotFound(String username) {
        super("User not found with username " + username);
    }
}
