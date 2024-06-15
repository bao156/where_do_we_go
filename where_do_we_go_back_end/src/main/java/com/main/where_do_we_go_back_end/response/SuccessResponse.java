package com.main.where_do_we_go_back_end.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SuccessResponse {

  private String successCode;

}