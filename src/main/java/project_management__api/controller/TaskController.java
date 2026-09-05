package project_management__api.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project_management__api.dtos.ApiResponse;
import project_management__api.dtos.TaskRequest;
import project_management__api.dtos.TaskResponse;
import project_management__api.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService){
        this.taskService=taskService;
    }

    @Operation(summary="Get Task By Id")
    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> getById(@PathVariable Long id){
        return taskService.getById(id);
    }


    @Operation(summary = "Get All Tasks Records")
    @GetMapping
    public ApiResponse<List<TaskResponse>> getAllTasks(){
        return taskService.getAllTasks();
    }

    @Operation(summary = "Create a Task")
    @PostMapping
    public ApiResponse<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest){
        return taskService.createTask(taskRequest);
    }

    @Operation(summary = "Update a Task")
    @PutMapping("/{id}")
    public  ApiResponse<TaskResponse> updateTask(@PathVariable Long id
            ,@Valid @RequestBody TaskRequest taskRequest){
        return taskService.updateTask(id,taskRequest);
    }

    @Operation(summary = "Delete a Task")
    @DeleteMapping("/{id}")
    public ApiResponse<TaskResponse> deleteTask(@PathVariable Long id){
        return taskService.deleteTask(id);
    }
}
