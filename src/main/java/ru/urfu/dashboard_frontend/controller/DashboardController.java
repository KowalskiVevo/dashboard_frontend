package ru.urfu.dashboard_frontend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.urfu.dashboard_frontend.dto.MessageDto;
import ru.urfu.dashboard_frontend.dto.UserDto;
import ru.urfu.dashboard_frontend.feign.BackendClient;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
  private final BackendClient backendClient;

  @GetMapping("/")
  public String mainPage(Model model) {
    UserDto userDto = Optional.ofNullable(backendClient.getMe())
        .map(ResponseEntity::getBody)
        .orElse(UserDto.builder()
            .firstname("John")
            .lastname("Doe")
            .build());
    model.addAttribute("user", userDto);
    List<UserDto> users = backendClient.getAllUser().getBody();
    model.addAttribute("users", users);
    var notifics = backendClient.getNotificationsByUserTag(userDto.getTag()).getBody();
    Collections.reverse(notifics);
    model.addAttribute("notifics", notifics);
    return "main";
  }

  @GetMapping("/me")
  public String mePage(Model model) {
    log.info(SecurityContextHolder.getContext().getAuthentication().getName());
    UserDto userDto = Optional.ofNullable(backendClient.getMe())
        .map(ResponseEntity::getBody)
        .orElse(UserDto.builder()
            .firstname("John")
            .lastname("Doe")
            .build());
    if (!StringUtils.hasText(userDto.getAbout())) {
      userDto.setAbout("Описание отсутствует");
    }
    model.addAttribute("user", userDto);
    var notifics = backendClient.getNotificationsByUserTag(userDto.getTag()).getBody();
    Collections.reverse(notifics);
    model.addAttribute("notifics", notifics);
    return "me";
  }

  @GetMapping("/me/edit")
  public String meEditPage(Model model) {
    UserDto userDto = Optional.ofNullable(backendClient.getMe())
        .map(ResponseEntity::getBody)
        .orElse(UserDto.builder()
            .firstname("John")
            .lastname("Doe")
            .build());
    model.addAttribute("user", userDto);
    var notifics = backendClient.getNotificationsByUserTag(userDto.getTag()).getBody();
    Collections.reverse(notifics);
    model.addAttribute("notifics", notifics);
    return "redact_me";
  }

  @PostMapping("/me/edit")
  public String postMeEditPage(@RequestParam(required = false) MultipartFile avatar,
                               @RequestParam String lastname,
                               @RequestParam String firstname,
                               @RequestParam String about,
                               @RequestParam String birthdate,
                               Model model) {
    UserDto newUserData = UserDto.builder()
        .tag(SecurityContextHolder.getContext().getAuthentication().getName())
        .birthdate(LocalDate.parse(birthdate))
        .lastname(lastname)
        .firstname(firstname)
        .about(about)
        .build();
    if (avatar.getSize() <= 0) {
      avatar = null;
    }
    UserDto savedUser = Optional.ofNullable(backendClient.putUser(newUserData, avatar))
        .map(ResponseEntity::getBody)
        .orElse(UserDto.builder()
            .firstname("John")
            .lastname("Doe")
            .build());
    model.addAttribute("user", savedUser);
    var notifics = backendClient.getNotificationsByUserTag(
        SecurityContextHolder.getContext().getAuthentication().getName()).getBody();
    Collections.reverse(notifics);
    model.addAttribute("notifics", notifics);
    return "me";
  }

  @GetMapping("/message")
  public String getMessage(Model model) {
    UserDto userDto = Optional.ofNullable(backendClient.getMe())
        .map(ResponseEntity::getBody)
        .orElse(UserDto.builder()
            .firstname("John")
            .lastname("Doe")
            .build());
    model.addAttribute("user", userDto);
    model.addAttribute("options", backendClient.getAllUser().getBody().stream().map(UserDto::getTag).toList());
    var notifics = backendClient.getNotificationsByUserTag(userDto.getTag()).getBody();
    Collections.reverse(notifics);
    model.addAttribute("notifics", notifics);
    return "message";
  }

  @PostMapping("/message")
  public String postMessage(@RequestParam String[] users,
                            @RequestParam String theme,
                            @RequestParam String content,
                            Model model) {
    List<String> userTags = Arrays.asList(users);
    userTags.forEach(tag -> backendClient.createMessage(MessageDto.builder()
            .userTo(UserDto.builder().tag(SecurityContextHolder.getContext().getAuthentication().getName()).build())
            .userFrom(UserDto.builder().tag(tag).build())
            .theme(theme)
            .content(content)
        .build()));

    UserDto userDto = Optional.ofNullable(backendClient.getMe())
        .map(ResponseEntity::getBody)
        .orElse(UserDto.builder()
            .firstname("John")
            .lastname("Doe")
            .build());
    model.addAttribute("user", userDto);
    model.addAttribute("options", backendClient.getAllUser().getBody().stream().map(UserDto::getTag).toList());
    var notifics = backendClient.getNotificationsByUserTag(userDto.getTag()).getBody();
    Collections.reverse(notifics);
    model.addAttribute("notifics", notifics);
    return "/message";
  }
}
