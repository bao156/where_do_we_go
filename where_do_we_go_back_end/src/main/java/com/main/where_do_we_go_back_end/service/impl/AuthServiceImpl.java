package com.main.where_do_we_go_back_end.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import com.main.where_do_we_go_back_end.entity.Role;
import com.main.where_do_we_go_back_end.entity.RoleName;
import com.main.where_do_we_go_back_end.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.main.where_do_we_go_back_end.exception.ConflictException;
import com.main.where_do_we_go_back_end.exception.NotFoundException;
import com.main.where_do_we_go_back_end.repository.RoleRepository;
import com.main.where_do_we_go_back_end.repository.UserRepository;
import com.main.where_do_we_go_back_end.request.SigninRequest;
import com.main.where_do_we_go_back_end.request.SignupRequest;
import com.main.where_do_we_go_back_end.response.JwtResponse;
import com.main.where_do_we_go_back_end.response.SuccessResponse;
import com.main.where_do_we_go_back_end.security.jwt.JwtProvider;
import com.main.where_do_we_go_back_end.security.services.UserPrinciple;
import com.main.where_do_we_go_back_end.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  @Override
  public JwtResponse authenticateUser(SigninRequest signinRequest) {
    var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(signinRequest.getUsername(),
            signinRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    var jwt = jwtProvider.generateJwtToken(authentication);

    var userDetails = (UserPrinciple) authentication.getPrincipal();
    var roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return new JwtResponse(jwt, userDetails.getId(), userDetails.getName(),
        userDetails.getUsername(), userDetails.getEmail(), roles);
  }

  @Override
  public SuccessResponse registerUser(SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      throw new ConflictException("username-already-exists");
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new ConflictException("email-already-exists");
    }

    // Create new user's account
    var user = new User(signUpRequest.getName(),signUpRequest.getUsername(), signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()));

    Set<Role> roles = new HashSet<>();

    if (CollectionUtils.isEmpty(signUpRequest.getRoles())) {
      var userRole = roleRepository.findByName(RoleName.ROLE_USER)
          .orElseThrow(() -> new NotFoundException("role-not-found"));
      roles.add(userRole);
    } else {
      signUpRequest.getRoles().forEach(role -> {
        if (role.equals(RoleName.ROLE_ADMIN)) {
          var adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
              .orElseThrow(() -> new NotFoundException("role-not-found"));
          roles.add(adminRole);
        } else {
          var userRole = roleRepository.findByName(RoleName.ROLE_USER)
              .orElseThrow(() -> new NotFoundException("role-not-found"));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return SuccessResponse.builder().successCode("user-registered-successfully").build();
  }

}
