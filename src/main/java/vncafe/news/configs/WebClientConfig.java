package vncafe.news.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Value("${den.apiKey.newsdata}")
  private String apiKey_newsdata;
  public static final String apiShopipi = "webClient_shopipi";
  public static final String apiNewsdata = "webClient_newsdata";

  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean(name = apiShopipi)
  public WebClient webClient_shopipi() {

    WebClient api = webClientBuilder()
        .baseUrl("https://shopipi.click")
        .defaultHeader("x-client-id", "abcd")
        .defaultHeader("authorization", "abcd")
        .build();
    return api;
  }

  @Bean(name = apiNewsdata)
  public WebClient webClient_newsdata() {

    WebClient api = webClientBuilder()
        .baseUrl("https://newsdata.io/api/1")
        .defaultHeader("X-ACCESS-KEY", apiKey_newsdata)
        .build();
    return api;
  }
}
