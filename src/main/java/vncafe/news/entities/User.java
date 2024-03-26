package vncafe.news.entities;

import java.util.Set;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;
import vncafe.news.configs.WebMvcConfig;
import vncafe.news.utils._enum.AuthTypeEnum;
import vncafe.news.utils._enum.UserRoleEnum;

@Document
@Data
@Builder
public class User {
  @Id
  private String id;
  private String name;
  private String email;
  private String image;
  @CreatedDate
  @Field("createAt")
  @JsonFormat(pattern = WebMvcConfig.dateTimeFormat)
  private LocalDateTime createAt;
  @JsonIgnore
  private String password;
  @Default
  private AuthTypeEnum authType = AuthTypeEnum.LOCAL;
  @Default
  private Set<UserRoleEnum> roles = Set.of(UserRoleEnum.USER);
  @Builder.Default
  private String oAuth2Id = null;
}
