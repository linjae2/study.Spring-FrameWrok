package org.example.greeting;

import java.util.List;

import org.example.global.IdResponse;
import org.example.global.envelop.ApiResponse;
import org.example.global.envelop.ApiStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/greeting")
public class GreetingController {
  private final GreetingService greetingService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<List<IdResponse<Long>>> createGreeting() {
      return ApiResponse.of(greetingService.createGreeting(), ApiStatus.GREETING_CREATED);
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateGreeting() {
      greetingService.updateGreetingsToBeHappy();
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteGreeting() {
    greetingService.deleteGreeting();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<List<GreetingResponse>> findAllGreetings() {
    return ApiResponse.of(greetingService.findAllGreetings(), ApiStatus.GREETINGS_FOUND);
  }
}
