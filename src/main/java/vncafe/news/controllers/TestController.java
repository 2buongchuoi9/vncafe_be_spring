package vncafe.news.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vncafe.news.entities.User;
import vncafe.news.exceptions.NotFoundError;
import vncafe.news.models.response.MainResponse;
import vncafe.news.repositories.UserRepo;
import vncafe.news.services.ApiNewsDataService;
import vncafe.news.services.UserService;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
  final UserService userService;
  final UserRepo userRepo;
  final ApiNewsDataService apiService;

  @GetMapping("")
  public ResponseEntity<?> test() {
    // return ResponseEntity.ok().body(Map.of("project", "shopipi", "hello",
    // "oke"));
    return ResponseEntity.ok().body(apiService.callApiAddNews(Optional.empty(), Optional.empty()));
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<MainResponse<User>> getUser(@PathVariable String id) {
    return ResponseEntity.ok()
        .body(MainResponse.oke(userRepo.findById(id).orElseThrow(() -> new NotFoundError("id", id))));
  }
}
