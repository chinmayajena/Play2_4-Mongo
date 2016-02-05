package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;


@Entity(value = "user", noClassnameStored = true)
public class User {
  
  @Id
  protected ObjectId _id;
  
  private String userName;
  private String userId;
  private String password;
  private String email;
  
  
  public ObjectId get_id() {
    return _id;
  }
  public void set_id(ObjectId _id) {
    this._id = _id;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  
}
