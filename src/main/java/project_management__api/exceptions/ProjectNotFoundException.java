package project_management__api.exceptions;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long projectId) {
        super("Project with id: "+projectId+ " is not found");
    }
}
