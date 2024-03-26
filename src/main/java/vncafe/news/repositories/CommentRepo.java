package vncafe.news.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import vncafe.news.entities.Comment;

@Repository
public interface CommentRepo extends MongoRepository<Comment, String> {

  @Query(value = "{newsId: ?0}", sort = "{right: -1}")
  Comment findLastCommentByNewsId(String newsId);

  List<Comment> findByNewsIdOrderByLeftDesc(String newsId);

  List<Comment> findByNewsIdAndParentIdOrderByLeftDesc(String newsId, String parentId);

}
