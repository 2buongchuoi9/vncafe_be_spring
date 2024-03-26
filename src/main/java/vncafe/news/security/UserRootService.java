package vncafe.news.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vncafe.news.entities.User;
import vncafe.news.repositories.UserRepo;

@Service
@RequiredArgsConstructor
public class UserRootService implements UserDetailsService {

  private final UserRepo userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepo.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username));
    return UserRoot.create(user);
  }

}
