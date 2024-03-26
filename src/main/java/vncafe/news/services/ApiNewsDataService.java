package vncafe.news.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import vncafe.news.configs.WebClientConfig;
import vncafe.news.entities.News;
import vncafe.news.repositories.NewsRepo;

@Service
@RequiredArgsConstructor
public class ApiNewsDataService {

  @Autowired
  @Qualifier(value = WebClientConfig.apiNewsdata)
  private WebClient apiNewsData;

  private final ObjectMapper objectMapper;

  private final NewsService newsService;

  @Value("${den.apiKey.newsdata}")
  private String apiKey_newsdata;

  // @Value("${den.apiKey.newsdata}")
  // private String apiKey_newsdata;

  public Mono<String> fetchNewsData(Optional<String> cate, Optional<String> keywords) {
    Map<String, String> uriParams = new HashMap<>();
    uriParams.put("apikey", apiKey_newsdata);
    // https://newsdata.io/api/1/news?apikey=pub_403025d6d0a712cb6d82a646548dc14d38140&country=vi&language=vi
    uriParams.put("country", "vi");
    uriParams.put("language", "vi");
    cate.ifPresent((v) -> uriParams.put("category", v));
    keywords.ifPresent((v) -> uriParams.put("q", v));

    // Tạo ra một đối tượng UriBuilder từ API base URI
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://newsdata.io/api/1")
        .path("/news"); // Đặt path là "/news"

    // Thêm các tham số vào UriBuilder
    uriParams.forEach(builder::queryParam);

    // Tạo URI từ UriBuilder
    URI uri = builder.build().toUri();

    // In ra URL hoàn chỉnh
    System.out.println("Complete URL: " + uri.toString());

    return apiNewsData.get()
        .uri("/news", uriParams)
        .retrieve() // Thực hiện yêu cầu HTTP và nhận phản hồi
        .bodyToMono(String.class); // Chuyển đổi phản hồi thành Mono<String>
  }

  public List<News> callApiAddNews(Optional<String> cate, Optional<String> keywords) {
    String jsonResponse = fetchNewsData(cate, keywords).block();
    try {
      List<News> listNews = new ArrayList<>();
      JsonNode responseNode = objectMapper.readTree(jsonResponse);

      System.out.println("status" + responseNode.get("status").asText());
      System.out.println("results" + responseNode.get("results").asText());

      JsonNode resultsNode = responseNode.get("results");
      if (resultsNode != null && resultsNode.isArray()) {
        for (JsonNode node : resultsNode) {
          Map<String, String> attributes = new HashMap<>();
          attributes.put("link", node.get("link").asText());
          attributes.put("video_url", node.get("video_url").asText());
          attributes.put("pubDate", node.get("pubDate").asText());
          attributes.put("source_url", node.get("source_url").asText());
          attributes.put("source_icon", node.get("source_icon").asText());
          attributes.put("category", node.get("category").asText());
          attributes.put("ai_tag", node.get("ai_tag").asText());

          String image_url = node.get("image_url").asText();
          attributes.put("image_url", image_url);

          News news = News.builder()
              .name(node.get("title").asText())
              .description(node.get("description").asText())
              .image(image_url)
              // .tags(List.of(null))
              .content(template(attributes))
              .isPublished(false)
              .build();
          newsService.addNewsV2(news);
          listNews.add(news);

        }
      }
      return listNews;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  private String template(Map<String, String> attributes) {
    String a = "";
    for (Map.Entry<String, String> entry : attributes.entrySet()) {
      a += "<div><strong>" + entry.getKey() + "</strong></br><p>" + entry.getValue() + "</p></div><hr/>";
    }
    return a;
  }

}
