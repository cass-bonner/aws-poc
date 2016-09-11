package poc.sa.ms.service.user;

public class UserServiceImpl implements UserService {
  
  private String emailAddress;

  @Override
  public String getEmailAddress(String userName) {
    
    // for poc returning email address from spring config but 
    // would potentially be part of User and User is an attribute of 
    // Order or this would perform real lookup to user microservice.
    return emailAddress;
  }

}
