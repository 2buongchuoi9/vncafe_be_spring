package vncafe.news.models.request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
public class NewsReq {
  @NotEmpty(message = "name is not null or empty")
  private String name;
  @NotEmpty(message = "content is not null or empty")
  private String content;
  @NotEmpty(message = "description is not null or empty")
  private String description;
  @NotEmpty(message = "categoryId is not null or empty")
  private String categoryId;
  private String image;
  @Default
  private Boolean isPublished = false;
  @Default
  private List<String> tags = new ArrayList<>();
}
