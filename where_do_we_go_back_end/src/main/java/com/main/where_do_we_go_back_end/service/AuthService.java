package com.main.where_do_we_go_back_end.service;

import com.main.where_do_we_go_back_end.request.SigninRequest;
import com.main.where_do_we_go_back_end.request.SignupRequest;
import com.main.where_do_we_go_back_end.response.JwtResponse;
import com.main.where_do_we_go_back_end.response.SuccessResponse;

public interface AuthService {

  JwtResponse authenticateUser(SigninRequest signinRequest);

  SuccessResponse registerUser(SignupRequest signUpRequest);

}
