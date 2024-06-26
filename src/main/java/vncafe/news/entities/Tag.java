package vncafe.news.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document
@Data
public class Tag {
  @Id
  private String id;
  @Indexed(unique = true)
  private String name;

  public Tag() {
  }

  public Tag(String name) {
    this.name = name;
  }

  public String toString() {
    return this.name;
  }

}
