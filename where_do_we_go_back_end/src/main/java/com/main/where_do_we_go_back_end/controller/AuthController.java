package com.main.where_do_we_go_back_end.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.main.where_do_we_go_back_end.request.SigninRequest;
import com.main.where_do_we_go_back_end.request.SignupRequest;
import com.main.where_do_we_go_back_end.response.JwtResponse;
import com.main.where_do_we_go_back_end.response.SuccessResponse;
import com.main.where_do_we_go_back_end.service.AuthService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<JwtResponse> authenticateUser(
      @Valid @RequestBody SigninRequest signinRequest) {
    var jwtResponse = authService.authenticateUser(signinRequest);
    return ResponseEntity.ok(jwtResponse);
  }

  @PostMapping("/signup")
  public ResponseEntity<SuccessResponse> registerUser(
      @Valid @RequestBody SignupRequest signUpRequest) {
    var successResponse = authService.registerUser(signUpRequest);
    return ResponseEntity.ok(successResponse);
  }

}
