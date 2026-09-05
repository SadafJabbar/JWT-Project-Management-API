package project_management__api.exceptions;

public class MemberShipNotFoundException extends RuntimeException {
    public MemberShipNotFoundException(Long id) {
        super(" membership with id: " +id+ " is not found");
    }
}
