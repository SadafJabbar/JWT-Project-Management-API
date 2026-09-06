package project_management__api.exceptions;

public class RefreshTokenNotFound extends RuntimeException {
    public RefreshTokenNotFound(String message) {
        super("the refresh token is not found");
    }
}
