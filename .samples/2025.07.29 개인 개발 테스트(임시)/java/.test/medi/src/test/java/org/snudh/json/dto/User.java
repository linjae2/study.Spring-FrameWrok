package org.snudh.json.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
  private String userId;
  private String userPwd;
  private String name;
  private String authType;
  private Date timestamp;
  private boolean isUpdate = false;
  
  public User() {
      super();
  }
  
  public User(String userId, String name, String authType) {
      super();
      this.userId = userId;
      this.name = name;
      this.authType = authType;
  }

}
