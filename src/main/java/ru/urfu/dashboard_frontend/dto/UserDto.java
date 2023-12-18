package ru.urfu.dashboard_frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
  private String tag;
  private String lastname;
  private String firstname;
  private String middlename;
  private LocalDate birthdate;
  private String photo;
  private String about;
  private String roleAlias;
  private String roleName;
}