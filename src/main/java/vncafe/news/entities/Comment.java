package vncafe.news.entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import vncafe.news.configs.WebMvcConfig;
import lombok.Builder.Default;

@Document
@CompoundIndex(def = "{'newsId': 1, 'left': 1, 'right': 1}")
@CompoundIndex(def = "{'newsId': 1, 'left': 1, 'right': 1, 'parentId': 1, 'user': 1}")
@CompoundIndex(def = "{'newsId': 1, 'left': 1, 'right': 1, 'parentId': 1, 'user': 1, 'isDelete': 1}")
@Data
@Builder
public class Comment {
  @Id
  private String id;
  private String newsId;
  private User user;
  private String content;
  private Integer left;
  private Integer right;
  @Default
  private List<String> likes = new ArrayList();
  private String parentId;
  @Default
  private Boolean isDelete = false;
  @CreatedDate
  @JsonFormat(pattern = WebMvcConfig.dateTimeFormat)
  private LocalDateTime createAt;
}
