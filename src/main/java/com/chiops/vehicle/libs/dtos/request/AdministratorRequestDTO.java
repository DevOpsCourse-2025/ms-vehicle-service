package com.chiops.vehicle.libs.dtos.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Serdeable
@Introspected
public class AdministratorRequestDTO {
  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Email must be a valid email address")
  private String email;

  @NotBlank(message = "Password cannot be blank")
  private String invitationCode;

  @NotBlank(message = "Password cannot be blank")
  private String password;

  public AdministratorRequestDTO(String email, String invitationCode, String password) {
    this.email = email;
    this.invitationCode = invitationCode;
    this.password = password;
  }

  public AdministratorRequestDTO() {}

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getInvitationCode() {
    return invitationCode;
  }

  public void setInvitationCode(String invitationCode) {
    this.invitationCode = invitationCode;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
