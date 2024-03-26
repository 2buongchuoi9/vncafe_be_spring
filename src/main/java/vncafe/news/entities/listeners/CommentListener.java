package vncafe.news.entities.listeners;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vncafe.news.entities.Comment;
import vncafe.news.entities.News;
import vncafe.news.repositories.NewsRepo;

@Component
@RequiredArgsConstructor
public class CommentListener extends AbstractMongoEventListener<Comment> {
  final NewsRepo newsRepo;
  final MongoTemplate mongoTemplate;

  @Override
  public void onAfterSave(AfterSaveEvent<Comment> event) {

    Comment comment = event.getSource();

    // update totalComment in news
    // create query
    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(comment.getNewsId()));
    // create update
    Update update = new Update();
    update.inc("totalComment", 1);
    // execute update operation
    mongoTemplate.updateFirst(query, update, News.class);
    super.onAfterSave(event);

  }

}
