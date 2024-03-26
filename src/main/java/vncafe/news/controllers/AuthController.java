package vncafe.news.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vncafe.news.models.request.LoginReq;
import vncafe.news.models.request.RegisterReq;
import vncafe.news.models.response.LoginRes;
import vncafe.news.models.response.MainResponse;
import vncafe.news.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<MainResponse<LoginRes>> register(@RequestBody @Valid RegisterReq registerReq) {
    return ResponseEntity.ok().body(MainResponse.oke(userService.registerUserLocal(registerReq, false)));
  }

  @PostMapping("/register-owner")
  public ResponseEntity<MainResponse<LoginRes>> registerOwner(@RequestBody @Valid RegisterReq registerReq) {
    return ResponseEntity.ok().body(MainResponse.oke(userService.registerUserLocal(registerReq, true)));
  }

  @PostMapping("/login")
  public ResponseEntity<MainResponse<LoginRes>> login(@RequestBody @Valid LoginReq loginReq) {
    return ResponseEntity.ok().body(MainResponse.oke(userService.loginLocal(loginReq)));
  }

}
