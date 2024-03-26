package vncafe.news.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import vncafe.news.entities.News;

@Repository
public interface NewsRepo extends MongoRepository<News, String> {

  boolean existsByName(String name);

  boolean existsByNameAndIdNot(String name, String id);

  // count comment in news
  @Query(value = "{newsId: ?0, isDelete: false}")
  Integer countByComment(String newsId);

}
