package org.example.greeting;

import java.util.List;

import org.example.global.IdResponse;
import org.springframework.stereotype.Component;

@Component
public class GreetingMapper {
  public List<GreetingResponse> toGreetingResponses(final List<Greeting> greetings) {
    return greetings.stream().map(this::toGreetingResponse).toList();
  }

  private GreetingResponse toGreetingResponse(final Greeting greeting) {
    return new GreetingResponse(
      greeting.getId(),
      greeting.isEveryoneGreetingTheOther(),
      greeting.isEveryoneHavingGoodDay());
  }

  public List<IdResponse<Long>> toIdResponses(final List<Greeting> greetings) {
    return greetings.stream().map(Greeting::getId).map(IdResponse::new).toList();
  }
}
