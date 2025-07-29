package org.snudh.json.mapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json/mapping")
public class DtoTestController {

    @GetMapping("")
    String default_method() {
        return "OK";
    }

    @PostMapping("/dto-test")
    public Object requestPost(@RequestBody RequestDto request) {
      System.out.println(request);
      return request;
    }

    @PostMapping("/dto-test1")
    public Object requestPost1(RequestDto request) {
      System.out.println(request);
      return request;
    }
}
