package vncafe.news.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import vncafe.news.entities.Comment;
import vncafe.news.models.paramsRequest.CommentParamsReq;
import vncafe.news.models.request.CommentReq;
import vncafe.news.models.response.MainResponse;
import vncafe.news.repositories.CommentRepo;
import vncafe.news.security.UserRoot;
import vncafe.news.services.CommentService;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
  final CommentService commentService;

  @PostMapping("")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<MainResponse<Comment>> add(@AuthenticationPrincipal UserRoot userRoot,
      @RequestBody @Valid CommentReq commentReq) {
    return ResponseEntity.ok().body(MainResponse.oke(commentService.add(userRoot.getUser(), commentReq)));
  }

  @GetMapping("")
  public ResponseEntity<MainResponse<List<Comment>>> getComments(
      @PageableDefault(size = 100, page = 0, sort = "createAt,desc") Pageable pageable,
      @ModelAttribute CommentParamsReq params) {
    return ResponseEntity.ok().body(MainResponse.oke(commentService.getComments(params, pageable)));
  }

  @PostMapping("/like/{commentId}")
  public ResponseEntity<MainResponse<Comment>> like(@AuthenticationPrincipal UserRoot userRoot,
      @PathVariable String commentId) {
    return ResponseEntity.ok().body(MainResponse.oke(commentService.likeComment(userRoot.getUser(), commentId)));
  }

  @DeleteMapping("/{commentId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<MainResponse<Boolean>> delete(@AuthenticationPrincipal UserRoot userRoot,
      @PathVariable String commentId) {
    return ResponseEntity.ok().body(MainResponse.oke(commentService.deleteComment(userRoot.getUser(), commentId)));
  }

}
