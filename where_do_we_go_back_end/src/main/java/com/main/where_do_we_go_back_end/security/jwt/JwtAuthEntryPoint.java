package com.main.where_do_we_go_back_end.security.jwt;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// used to throw un authorize msg if user credentials is not correct
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException e) throws IOException {
    log.error("Unauthorized error: {}", e.getMessage());
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
  }

}