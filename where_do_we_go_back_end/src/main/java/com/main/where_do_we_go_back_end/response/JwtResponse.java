package com.main.where_do_we_go_back_end.response;

import java.util.List;
import lombok.Data;

@Data
public class JwtResponse {

  private String token;
  private String type = "Bearer";
  private String id;
  private String name;
  private String username;
  private String email;
  private List<String> roles;

  public JwtResponse(String token, String id, String name, String username,
      String email, List<String> roles) {
    this.token = token;
    this.id = id;
    this.name = name;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }

}