package eby.py.visitasrrpp.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleException(MaxUploadSizeExceededException exception){
        Map<String, Object> response = new HashMap<String, Object>();

        response.put("mensaje", "Maximum length  of documento excedido, max: 2MB");
        response.put("Error", exception.getMostSpecificCause().getMessage());

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
