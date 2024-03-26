package vncafe.news.models.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import vncafe.news.configs.WebMvcConfig;

@Data
@Builder
public class UpdateUserReq {
  // @NotEmpty(message = "name not null or empty")
  private String name;
  // @NotEmpty(message = "email not null or empty")
  // @Pattern(regexp = WebMvcConfig.regexpEmail, message = "email is not valid")
  private String email;
  private String image;

}
