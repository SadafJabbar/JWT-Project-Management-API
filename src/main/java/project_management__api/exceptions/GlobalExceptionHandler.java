package project_management__api.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ExceptionResponse exceptionResponse;
    @Autowired
    public GlobalExceptionHandler(ExceptionResponse exceptionResponse){
        this.exceptionResponse=exceptionResponse;
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Map<String,Object>> projectNotFound(ProjectNotFoundException ex, WebRequest webRequest){
        return exceptionResponse.exceptionResponse(ex,webRequest,HttpStatus.NOT_FOUND,ex.getMessage());
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String,Object>> usertNotFound(UserNotFoundException ex, WebRequest webRequest){
        return exceptionResponse.exceptionResponse(ex,webRequest,HttpStatus.NOT_FOUND,ex.getMessage());
    }


    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String,Object>> tasktNotFound(TaskNotFoundException ex, WebRequest webRequest){
        return exceptionResponse.exceptionResponse(ex,webRequest,HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(MemberShipNotFoundException.class)
    public ResponseEntity<Map<String,Object>> memberNotFound(MemberShipNotFoundException ex, WebRequest webRequest){
        return exceptionResponse.exceptionResponse(ex,webRequest,HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(UserMembershipNotFound.class)
    public ResponseEntity<Map<String,Object>> userMembershipNotFound(UserMembershipNotFound ex, WebRequest webRequest){
        return exceptionResponse.exceptionResponse(ex,webRequest,HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFound.class)
    public ResponseEntity<Map<String,Object>> usernameNotFound(UsernameNotFound ex, WebRequest webRequest){
        return exceptionResponse.exceptionResponse(ex,webRequest,HttpStatus.NOT_FOUND,ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> globalException(Exception ex, WebRequest webRequest){
        return exceptionResponse.exceptionResponse(ex,webRequest,HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
    }

    @ExceptionHandler(RefreshTokenNotFound.class)
    public ResponseEntity<Map<String,Object>> refreshTokenException(RefreshTokenNotFound ex, WebRequest webRequest){
        return exceptionResponse.exceptionResponse(ex,webRequest,HttpStatus.NOT_FOUND,ex.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> methodArgumenentNotValid(MethodArgumentNotValidException ex,WebRequest webRequest){
        Map<String,Object> map=new HashMap<>();
        List<String> errors=new ArrayList<>();
        for (FieldError error:ex.getBindingResult().getFieldErrors()){
            errors.add(error.getField()+":"+error.getDefaultMessage());
        }
        map.put("timestamp",LocalDateTime.now());
        map.put("message","validation failed" );
        map.put("status",HttpStatus.BAD_REQUEST.value());
        map.put("errors",errors);
        map.put("path",webRequest.getDescription(false).replace("uri=",""));
        return  new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);

    }
}
