package vncafe.news.entities;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "NewsDataModels")
@Data
@Builder
public class NewsDataModel {
  @Id
  private String article_id;
  private String title;
  private String link;
  private List<String> keywords;
  private List<String> creator;
  private String video_url;
  private String description;
  private String content;
  private String pubDate;
  private String image_url;
  private String source_id;
  private String source_url;
  private String source_icon;
  private String source_priority;
  private List<String> country;
  private List<String> category;
  private String language;
  private List<String> ai_tag;
  private String ai_region;
  private String sentiment;
  private Map<String, String> sentiment_stats;

}
