package org.snudh.json;

import org.junit.jupiter.api.Test;
import org.snudh.json.dto.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ObjectMappingTest {

  final String expected = "{\"userId\":\"userId\",\"userPwd\":null,\"name\":\"tester\",\"authType\":\"web\",\"timestamp\":null,\"update\":false}";

  @Test
  public void objectToJson() throws JsonProcessingException {
    User user = new User("userId", "tester", "web");
    ObjectMapper objectMapper = new ObjectMapper();
    String userAsString = objectMapper.writeValueAsString(user);

    System.out.println(userAsString);
    System.out.println("---------------------");
    //assertThat(userAsString, equalTo(expected));
  }

  @Test
  public void jsonToObject() throws JsonProcessingException, JsonMappingException {
      ObjectMapper objectMapper = new ObjectMapper();
      User user = objectMapper.readValue(expected, User.class);
      assertThat(user.getUserId(), equalTo("userId"));
  }
}
