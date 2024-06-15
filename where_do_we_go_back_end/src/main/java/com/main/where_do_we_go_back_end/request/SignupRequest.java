package com.main.where_do_we_go_back_end.request;

import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import com.main.where_do_we_go_back_end.entity.RoleName;

@Data
public class SignupRequest {

  @NotEmpty(message = "cannot-be-empty")
  @Size(min = 3, max = 50, message = "length-must-be-greater-than-3-and-less-than-50")
  private String name;

  @NotEmpty(message = "cannot-be-empty")
  @Size(min = 3, max = 50, message = "length-must-be-greater-than-3-and-less-than-50")
  private String username;


  @NotEmpty(message = "cannot-be-empty")
  @Size(min = 6, max = 100, message = "length-must-be-greater-than-6-and-less-than-100")
  private String password;

  @NotEmpty(message = "cannot-be-empty")
  @Size(max = 50, message = "length-must-be-less-than-50")
  @Email
  private String email;

  @NotEmpty
  private Set<RoleName> roles;

}