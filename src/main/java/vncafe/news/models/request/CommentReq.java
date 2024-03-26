package vncafe.news.models.request;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
public class CommentReq {
  @NotEmpty(message = "newsId is not null or empty")
  private String newsId;
  private String parentId;
  @NotEmpty(message = "content is not null or empty")
  private String content;
}
