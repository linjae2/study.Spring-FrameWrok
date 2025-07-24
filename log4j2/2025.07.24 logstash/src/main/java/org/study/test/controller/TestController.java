package org.study.test.controller;

import org.apache.logging.log4j.message.StringMapMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
  private static final Logger log = LoggerFactory.getLogger(TestController.class);
  @GetMapping()
  public String getIndex() {
    log.info("==> getIndex()");

    // log.info(new StringMapMessage()
    //   .with("message", "Hello World!")
    //   .with("foo", "bar"));


    return "Hello Test!";
  }
}
