package vncafe.news.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SuppressWarnings("null")
public class WebMvcConfig {

  // private static final String dateFormat = "dd-MM-yyyy";
  @Value("${den.date.format}")
  public static final String dateTimeFormat = "dd-MM-yyyy HH:mm:ss";

  public static final String regexpEmail = "^[\\w\\.-]+@[a-zA-Z\\d\\.-]+\\.[a-zA-Z]{2,}$";

  // @Bean
  // public Formatter<LocalDateTime> localDateTimeFormatter() {
  // return new Formatter<LocalDateTime>() {
  // @Override
  // public LocalDateTime parse(String text, Locale locale) throws ParseException
  // {
  // return LocalDateTime.parse(text,
  // DateTimeFormatter.ofPattern(dateTimeFormat));
  // }

  // @Override
  // public String print(LocalDateTime object, Locale locale) {
  // return DateTimeFormatter.ofPattern(dateTimeFormat).format(object);
  // }
  // };
  // }

  @Bean
  public WebMvcConfigurer corsConfig() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedHeaders("*")
            .allowedMethods("GET", "POST", "OPTIONS", "DELETE", "PUT")
            // "http://localhost:5173"
            .allowedOrigins("http://localhost:5172", "http://192.168.1.12:5173", "http://localhost:5173",
                "https://www.vncafe.click"
        // "http://192.168.109.26:5173
        )
            .allowCredentials(true);

        // registry.addMapping("/**")
        // .allowedOrigins("/*")
        // .allowedMethods("GET", "POST", "OPTIONS")
        // .allowedHeaders("/*")
        // .allowCredentials(true);

      }
    };
  }
}
