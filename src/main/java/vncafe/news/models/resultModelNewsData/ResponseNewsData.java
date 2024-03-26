package vncafe.news.models.resultModelNewsData;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import vncafe.news.entities.NewsDataModel;

@Data
@Builder
public class ResponseNewsData {
  private String status;
  private long totalResults;
  private String nextPage;
  private List<NewsDataModel> results;
}
