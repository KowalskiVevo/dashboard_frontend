package ru.urfu.dashboard_frontend.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.urfu.dashboard_frontend.config.OAuthFeignConfig;
import ru.urfu.dashboard_frontend.dto.MessageDto;
import ru.urfu.dashboard_frontend.dto.NotificationDto;
import ru.urfu.dashboard_frontend.dto.UserDto;

import java.util.List;

@FeignClient(name = "${app.backend.name:dashboardBackend}", url = "${app.backend.url}",
    configuration = OAuthFeignConfig.class)
public interface BackendClient {
  @GetMapping("/api/me")
  ResponseEntity<UserDto> getMe();

  @GetMapping("/api/all")
  ResponseEntity<List<UserDto>> getAllUser();

  @PutMapping(value = "/api/user")
  ResponseEntity<UserDto> putUser(@RequestBody UserDto userDto);

  @PostMapping("/api/message")
  ResponseEntity createMessage(@RequestBody MessageDto messageDto);

  @GetMapping("/api/notification")
  ResponseEntity<List<NotificationDto>> getNotificationsByUserTag(@RequestParam String userTag);
}
