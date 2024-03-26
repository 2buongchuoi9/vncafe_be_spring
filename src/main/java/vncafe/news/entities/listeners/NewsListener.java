package vncafe.news.entities.listeners;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import com.github.slugify.Slugify;

import vncafe.news.entities.News;

@Component
@SuppressWarnings("null")
public class NewsListener extends AbstractMongoEventListener<News> {

  @Override
  public void onBeforeSave(BeforeSaveEvent<News> event) {
    News news = event.getSource();
    addSlug(news);
    super.onBeforeSave(event);
  }

  private void addSlug(News news) {
    if (news != null) {
      String generatedSlug = Slugify.builder().build().slugify(news.getName());
      news.setSlug(generatedSlug);
    }

  }
}
