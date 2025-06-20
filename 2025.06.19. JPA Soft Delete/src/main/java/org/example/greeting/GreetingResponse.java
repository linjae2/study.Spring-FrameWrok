package org.example.greeting;

public record GreetingResponse(
  Long id, Boolean isEveryoneGreetingTheOther, Boolean isEveryoneHavingGoodDay) {}