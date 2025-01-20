package projetos.test.Cinephy.Exceptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidReviewException.class)
    public ResponseEntity<ErrorResponse> handleInvalidReviewException(InvalidReviewException err, HttpServletRequest req){
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),"Invalid",err.getMessage(), req.getRequestURI(),null );
     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMovieNotFoundException(MovieNotFoundException err,HttpServletRequest req){
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(),HttpStatus.NOT_FOUND.value(),"Not found", err.getMessage(),req.getRequestURI(),null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidMovieInTop10Exception.class)
    public ResponseEntity<ErrorResponse> handleInvalidMovieInTop10Exception(InvalidMovieInTop10Exception err,HttpServletRequest req){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Movie in Top 10",err.getMessage(),req.getRequestURI(),null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotValidArgumentException(MethodArgumentNotValidException err,HttpServletRequest req){
        List<String> details = err.getBindingResult().getAllErrors().stream().map(error -> ((FieldError) error).getField() + ":" + error.getDefaultMessage()).toList();
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(),"Validation Error","Erro na validação",req.getRequestURI(),details
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReviewNotFoundException(ReviewNotFoundException err, HttpServletRequest req){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Review Not Found",err.getMessage(),req.getRequestURI(),null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrorResponse> handleReviewNotFoundException(UnauthorizedActionException err, HttpServletRequest req){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Action Not Authorized",err.getMessage(),req.getRequestURI(),null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
