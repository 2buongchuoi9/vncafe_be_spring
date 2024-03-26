package vncafe.news.models.paramsRequest;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;
import vncafe.news.configs.WebMvcConfig;

@Data
@Builder
public class NewsParamsReq {
  private String keySearch;
  private String categoryId;
  private Boolean isPublished;

  @DateTimeFormat
  @JsonFormat(pattern = WebMvcConfig.dateTimeFormat)
  private LocalDateTime startDate;

  @DateTimeFormat
  @JsonFormat(pattern = WebMvcConfig.dateTimeFormat)
  private LocalDateTime endDate;
}
