package com.chiops.vehicle.libs.dtos;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Introspected
@Serdeable
public class InvitationCodeDTO {
  @NotBlank(message = "Code cannot be blank")
  private String code;

  @NotNull(message = "Used status cannot be null")
  private Boolean used;

  public InvitationCodeDTO(boolean used, String code) {
    this.used = used;
    this.code = code;
  }

  public InvitationCodeDTO() {}

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public boolean getIsUsed() {
    return used;
  }

  public void setUsed(boolean used) {
    this.used = used;
  }
}
