package project_management__api.exceptions;

public class UserMembershipNotFound extends RuntimeException {
    public UserMembershipNotFound(Long userId,Long projectId) {
        super("The user with id: " +userId+ " isnot a member of the project with id: " +projectId);
    }
}
