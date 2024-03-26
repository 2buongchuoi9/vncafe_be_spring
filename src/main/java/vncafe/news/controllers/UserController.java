package vncafe.news.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vncafe.news.entities.User;
import vncafe.news.models.request.UpdateUserReq;
import vncafe.news.models.response.MainResponse;
import vncafe.news.security.UserRoot;
import vncafe.news.services.UserService;
import vncafe.news.utils.Constants.HASROLE;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  final UserService userService;

  @PostMapping("/profile")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<MainResponse<User>> getInfo(@AuthenticationPrincipal UserRoot userRoot) {
    return ResponseEntity.ok().body(MainResponse.oke(userRoot.getUser()));
  }

  @PostMapping("/{id}")
  @PreAuthorize(HASROLE.ADMIN)
  public ResponseEntity<MainResponse<User>> updateUserByAdmin(@PathVariable String id,
      @RequestBody @Valid UpdateUserReq user) {
    return ResponseEntity.ok().body(MainResponse.oke(userService.updateUser(id, user)));
  }

  @PostMapping("")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<MainResponse<User>> updateCurrentUser(
      @RequestBody @Valid UpdateUserReq user, @AuthenticationPrincipal UserRoot userRoot) {
    return ResponseEntity.ok().body(MainResponse.oke(userService.updateUser(userRoot.getUser().getId(), user)));
  }
}
