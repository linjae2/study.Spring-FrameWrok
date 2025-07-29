package org.snudh.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class LoginController {

  //local storage
  Map<String, String> localSession = new HashMap<>();

  @GetMapping("/login")
  public String login(
    HttpSession session,
    @RequestParam String name
  ) {
    localSession.put(session.getId(), name);
    return "OK";
  }

  @GetMapping("/myName")
  public String myName(HttpSession session) {
    return localSession.get(session.getId());
  }
}
