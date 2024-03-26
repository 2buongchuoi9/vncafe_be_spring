package vncafe.news.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

import vncafe.news.entities.Category;
import vncafe.news.models.response.MainResponse;
import vncafe.news.services.CategoryService;
import vncafe.news.utils.Constants.HASROLE;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
  final CategoryService cateService;

  @PostMapping("")
  @PreAuthorize(HASROLE.ADMIN + " or " + HASROLE.OWNER)
  public ResponseEntity<MainResponse<Category>> addCate(@RequestBody @Valid Category cate) {
    return ResponseEntity.ok().body(MainResponse.oke(cateService.addCate(cate)));
  }

  @GetMapping("")
  public ResponseEntity<MainResponse<List<Category>>> getAllCate() {
    return ResponseEntity.ok().body(MainResponse.oke(cateService.getAllCate()));
  }

}
