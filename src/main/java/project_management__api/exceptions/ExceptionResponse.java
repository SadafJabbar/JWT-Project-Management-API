package project_management__api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionResponse {

    public ResponseEntity<Map<String,Object>> exceptionResponse(Exception ex,WebRequest webRequest,HttpStatus httpStatus, String message){
        Map<String,Object> map=new HashMap<>();
        map.put("timestamp", LocalDateTime.now());
        map.put("status", httpStatus.value());
        map.put("message",message);
        map.put("path",webRequest.getDescription(false).replace("uri=",""));
        return new ResponseEntity<>(map,httpStatus);}
}
