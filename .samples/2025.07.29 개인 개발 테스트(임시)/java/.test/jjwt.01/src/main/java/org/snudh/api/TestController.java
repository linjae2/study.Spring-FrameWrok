package org.snudh.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/alimtalk")
@RequiredArgsConstructor
public class TestController {
  @GetMapping("/send")
  public ResponseEntity<Void> getSned() {
    return ResponseEntity.ok().build();
  }
}
