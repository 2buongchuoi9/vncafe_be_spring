package vncafe.news.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import vncafe.news.entities.Comment;
import vncafe.news.entities.News;
import vncafe.news.entities.User;
import vncafe.news.exceptions.NotFoundError;
import vncafe.news.models.paramsRequest.CommentParamsReq;
import vncafe.news.models.request.CommentReq;
import vncafe.news.repositories.CommentRepo;
import vncafe.news.security.UserRoot;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class CommentService {
  final private CommentRepo commentRepo;
  final private MongoTemplate mongoTemplate;

  public Comment add(User user, CommentReq commentReq) {
    int rightValue;
    if (commentReq.getParentId() != null) {
      Comment parent = commentRepo.findById(commentReq.getParentId())
          .orElseThrow(() -> new NotFoundError("parentId", commentReq.getParentId()));
      rightValue = parent.getRight();
      // update right của các comment có right >= rightValue lên 2 đơn vị
      mongoTemplate.updateMulti(
          Query.query(Criteria.where("right").gte(rightValue)),
          new Update().inc("right", 2),
          Comment.class);
      // update left của các comment có left > rightValue lên 2 đơn vị
      mongoTemplate.updateMulti(
          Query.query(Criteria.where("left").gt(rightValue)),
          new Update().inc("left", 2),
          Comment.class);

    } else {
      // tìm comment cuối cùng của bài viết
      Comment lastComment = mongoTemplate.findOne(Query.query(Criteria.where("newsId").is(commentReq.getNewsId()))
          .with(Sort.by(Sort.Direction.DESC, "right")).limit(1), Comment.class);
      rightValue = lastComment == null ? 1 : lastComment.getRight() + 2;
    }
    return commentRepo.save(Comment.builder()
        .newsId(commentReq.getNewsId())
        .user(user)
        .content(commentReq.getContent())
        .left(rightValue)
        .right(rightValue + 1)
        .parentId(commentReq.getParentId())
        .build());
  }

  public List<Comment> getComments(CommentParamsReq params, Pageable pageable) {
    String newsId = params.getNewsId();
    String parentId = params.getParentId();
    String userId = params.getUserId();

    Query query = new Query();

    // newsId
    if (newsId != null) {
      query.addCriteria(Criteria.where("newsId").is(newsId));
    }

    // parentId
    if (parentId != null) {
      query.addCriteria(Criteria.where("parentId").is(parentId));
    }

    // userId
    if (userId != null) {
      query.addCriteria(Criteria.where("user.id").is(userId));
    }

    query.with(pageable.getSort());

    // if (parentId == null) {
    // return commentRepo.findByNewsIdOrderByLeftDesc(newsId);
    // } else {
    // Comment parent = commentRepo.findById(parentId)
    // .orElseThrow(() -> new NotFoundError("parentId", parentId));
    // return commentRepo.findByNewsIdAndParentIdOrderByLeftDesc(newsId,
    // parent.getId());
    // }

    return mongoTemplate.find(query, Comment.class);

  }

  public Comment updateComment(User user, String commentId, CommentReq commentReq) {
    Comment comment = commentRepo.findById(commentId)
        .orElseThrow(() -> new NotFoundError("commentId", commentId));
    if (!comment.getUser().getId().equals(user.getId())) {
      throw new NotFoundError("commentId", commentId);
    }
    comment.setContent(commentReq.getContent());
    return commentRepo.save(comment);
  }

  public boolean deleteComment(User user, String commentId) {
    try {
      Comment comment = commentRepo.findById(commentId)
          .orElseThrow(() -> new NotFoundError("commentId", commentId));
      if (!comment.getUser().getId().equals(user.getId())) {
        throw new NotFoundError("commentId", commentId);
      }
      // xóa comment và các comment con của comment đó
      mongoTemplate.remove(Query.query(Criteria.where("left").gte(comment.getLeft()).lte(comment.getRight())),
          Comment.class);
      // update right của các comment có right > right của comment bị xóa giảm 2
      mongoTemplate.updateMulti(
          Query.query(Criteria.where("right").gt(comment.getRight())),
          new Update().inc("right", -2),
          Comment.class);
      // update left của các comment có left > right của comment bị xóa giảm 2
      mongoTemplate.updateMulti(
          Query.query(Criteria.where("left").gt(comment.getRight())),
          new Update().inc("left", -2),
          Comment.class);

      // update totalComment in news

      long remainingComments = mongoTemplate.count(
          Query.query(Criteria.where("newsId").is(comment.getNewsId())),
          Comment.class);

      mongoTemplate.updateFirst(
          Query.query(Criteria.where("id").is(comment.getNewsId())),
          new Update().set("totalComment", remainingComments),
          News.class);

      return true;
    } catch (NotFoundError e) {
      return false;
    }
  }

  public Comment likeComment(User user, String commentId) {
    Comment comment = commentRepo.findById(commentId)
        .orElseThrow(() -> new NotFoundError("commentId", commentId));
    if (comment.getLikes() == null)
      comment.setLikes(new ArrayList<>());
    if (comment.getLikes().contains(user.getId())) { // đã like
      comment.getLikes().remove(user.getId());
    } else {
      comment.getLikes().add(user.getId());
    }

    return commentRepo.save(comment);

  }
}
