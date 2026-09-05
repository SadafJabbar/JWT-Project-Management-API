package project_management__api.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super("Task with id: " +taskId+ "is not found");
    }
}
