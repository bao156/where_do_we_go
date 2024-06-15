package com.main.where_do_we_go_back_end.exception.handler;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import com.main.where_do_we_go_back_end.exception.ConflictException;
import com.main.where_do_we_go_back_end.exception.NotFoundException;
import com.main.where_do_we_go_back_end.response.ErrorResponse;
import com.main.where_do_we_go_back_end.util.DateUtils;

@Slf4j
@ControllerAdvice
@RestController
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(
      ConflictException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.now())
        .status(HttpStatus.CONFLICT.value())
        .errorCode(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleRoleNotFoundException(
      NotFoundException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.now())
        .status(HttpStatus.NOT_FOUND.value())
        .errorCode(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handle(MethodArgumentNotValidException ex) {
    var error = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);

    if (error == null) {
      var errorResponse = ErrorResponse.builder()
          .timestamp(DateUtils.now())
          .status(HttpStatus.BAD_REQUEST.value())
          .errorCode("unknown-error")
          .build();
      return ResponseEntity.badRequest().body(errorResponse);
    }

    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .errorCode(error.getField() + "-"
            + Objects.requireNonNull(error.getDefaultMessage()).replaceAll("\\s", "-"))
        .build();
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(
      UsernameNotFoundException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.now())
        .status(HttpStatus.UNAUTHORIZED.value())
        .errorCode(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
      BadCredentialsException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.now())
        .status(HttpStatus.UNAUTHORIZED.value())
        .errorCode("username-or-password-is-not-correct")
        .build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

}
