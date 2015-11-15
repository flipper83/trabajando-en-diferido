package com.karumi.trabajandoendiferido.api.response;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
  @SerializedName("value") private String value;

  public String getValue() {
    return value;
  }
}
