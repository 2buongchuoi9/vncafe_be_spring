package vncafe.news.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class MainController {
  @GetMapping("")
  public String getMethodName() {
    return "redirect:/swagger-ui/index.html";
  }

}
