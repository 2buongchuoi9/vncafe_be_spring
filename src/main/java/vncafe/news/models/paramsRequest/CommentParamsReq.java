package vncafe.news.models.paramsRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentParamsReq {
  private String newsId;
  private String parentId;
  private String userId;
}
