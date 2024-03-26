package vncafe.news.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;

@Document
@Data
@Builder
public class Category {
  @Id
  private String id;
  private String parentId;
  @NotEmpty(message = "name is not null or empty")
  private String name;
  private String parentName;
}
