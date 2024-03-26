package vncafe.news.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vncafe.news.entities.News;
import vncafe.news.models.paramsRequest.NewsParamsReq;
import vncafe.news.models.request.NewsReq;
import vncafe.news.models.response.MainResponse;
import vncafe.news.repositories.repositoryUtil.PageCustom;
import vncafe.news.services.NewsService;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
  final NewsService newsService;

  @PostMapping("")
  public ResponseEntity<MainResponse<News>> addNews(@RequestBody @Valid NewsReq newsRes) {
    return ResponseEntity.ok().body(MainResponse.oke(newsService.addNews(newsRes)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<MainResponse<News>> getOne(@PathVariable String id) {
    return ResponseEntity.ok().body(MainResponse.oke(newsService.getOne(id)));
  }

  @GetMapping("")
  public ResponseEntity<MainResponse<PageCustom<News>>> find(
      @PageableDefault(size = 10, page = 0, sort = "createAt,desc") Pageable pageable,
      @ModelAttribute @Valid NewsParamsReq newsParams) {
    return ResponseEntity.ok().body(MainResponse.oke(newsService.find(pageable, newsParams)));
  }

  @PostMapping("/{id}")
  public ResponseEntity<MainResponse<News>> update(@PathVariable String id,
      @RequestBody @Valid NewsReq newsReq) {
    return ResponseEntity.ok().body(MainResponse.oke(newsService.update(id, newsReq)));
  }

}
