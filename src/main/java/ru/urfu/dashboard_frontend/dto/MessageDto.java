package ru.urfu.dashboard_frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
  private UserDto userFrom;
  private UserDto userTo;
  private String theme;
  private String content;
}