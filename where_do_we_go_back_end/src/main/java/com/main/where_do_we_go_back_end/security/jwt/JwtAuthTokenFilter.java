package com.main.where_do_we_go_back_end.security.jwt;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.main.where_do_we_go_back_end.security.services.UserDetailsServiceImpl;

// filter value of Authorization header:
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

  @Autowired
  private JwtProvider tokenProvider;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain) throws ServletException, IOException {
    try {
      var jwt = getJwt(request);
      if (StringUtils.hasText(jwt) && tokenProvider.validateJwtToken(jwt)) {
        var username = tokenProvider.getUserNameFromJwtToken(jwt);

        var userDetails = userDetailsService.loadUserByUsername(username);
        var authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      log.error("Cannot set user authentication: {0}", e);
    }

    filterChain.doFilter(request, response);
  }

  private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
    return authorizationHeader == null || !authorizationHeader.startsWith("Bearer ");
  }

  private String getJwt(HttpServletRequest request) {
    var bearerToken = request.getHeader("Authorization");
    if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
      return null;
    }
    return bearerToken.substring(7);
  }

}
