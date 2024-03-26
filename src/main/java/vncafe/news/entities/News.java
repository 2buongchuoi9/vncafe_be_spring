package vncafe.news.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;
import vncafe.news.configs.WebMvcConfig;

@Document
@Data
@Builder
public class News {
  @Id
  private String id;
  @Field("slug")
  private String slug;
  private String name;
  private String content;
  private String description;
  private String image;
  private Boolean isPublished;
  private Category category;
  @Default
  private Integer totalComment = 0;
  @Default
  private List<String> tags = new ArrayList<>();
  @CreatedDate
  @JsonFormat(pattern = WebMvcConfig.dateTimeFormat)
  private LocalDateTime createAt;

  @CreatedBy
  private User user;

}
