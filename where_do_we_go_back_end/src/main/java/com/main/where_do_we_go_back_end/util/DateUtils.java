package com.main.where_do_we_go_back_end.util;

import java.util.Date;

public class DateUtils {

  public static Long now() {
    return currentDate().getTime();
  }

  public static Date currentDate() {
    return new Date();
  }

}
