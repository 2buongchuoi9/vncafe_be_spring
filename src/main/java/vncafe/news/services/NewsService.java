package vncafe.news.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vncafe.news.entities.Category;
import vncafe.news.entities.News;
import vncafe.news.entities.Tag;
import vncafe.news.exceptions.DuplicateRecordError;
import vncafe.news.exceptions.NotFoundError;
import vncafe.news.models.paramsRequest.NewsParamsReq;
import vncafe.news.models.request.NewsReq;
import vncafe.news.repositories.CategoryRepo;
import vncafe.news.repositories.NewsRepo;
import vncafe.news.repositories.repositoryUtil.PageCustom;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class NewsService {
  final private NewsRepo newsRepo;
  final private TagService tagService;
  final private MongoTemplate mongoTemplate;
  final private CategoryRepo cateRepo;

  public News addNews(NewsReq newsReq) {
    if (newsRepo.existsByName(newsReq.getName()))
      throw new DuplicateRecordError("name", newsReq.getName());

    Category cate = cateRepo.findById(newsReq.getCategoryId())
        .orElseThrow(() -> new NotFoundError("categoryId", newsReq.getCategoryId()));

    // check tags
    if (newsReq.getTags() != null && newsReq.getTags().size() > 0) {
      List<String> _tags = newsReq.getTags();
      _tags.removeIf(String::isEmpty);
      newsReq.setTags(_tags);
      tagService.addManyIfNotExist((_tags));
    }

    return newsRepo.save(News.builder()
        .name(newsReq.getName())
        .content(newsReq.getContent())
        .description(newsReq.getDescription())
        .tags(newsReq.getTags())
        .isPublished(newsReq.getIsPublished())
        .image(newsReq.getImage())
        .category(cate)
        .build());
  }

  public News addNewsV2(News news) {
    if (newsRepo.existsByName(news.getName()))
      throw new DuplicateRecordError("name", news.getName());

    // check tags
    if (news.getTags() != null && news.getTags().size() > 0) {
      // news.getTags().removeIf(v -> v.getName() == null || v.getName().isEmpty());
      List<String> _tags = news.getTags();
      _tags.removeIf(v -> v == null || v.isEmpty());
      if (_tags != null || !_tags.isEmpty() || _tags.size() > 0)
        tagService.addManyIfNotExist((_tags));

      news.setTags(_tags);

    }

    return newsRepo.save(news);
  }

  public News getOne(String id) {
    return newsRepo.findById(id).orElseThrow(() -> new NotFoundError("id", id));
  }

  public List<News> findAll() {
    return newsRepo.findAll();
  }

  public PageCustom<News> find(Pageable pageable, NewsParamsReq params) {
    String keySearch = params.getKeySearch();
    String categoryId = params.getCategoryId();
    Boolean isPublished = params.getIsPublished();
    LocalDateTime startDate = params.getStartDate();
    LocalDateTime endDate = params.getEndDate();

    Query query = new Query();

    // keySearch
    if (keySearch != null && !keySearch.isEmpty()) {
      String regexPattern = "(?i)" + keySearch.trim(); // Thêm ?i để không phân biệt chữ hoa chữ thường
      query.addCriteria(new Criteria().orOperator(
          Criteria.where("name").regex(regexPattern),
          Criteria.where("description").regex(regexPattern),
          Criteria.where("content").regex(regexPattern),

          Criteria.where("tags").elemMatch(
              Criteria.where("$regex").regex(regexPattern)),

          new Criteria().orOperator(Criteria.where("category.name").regex(regexPattern),
              Criteria.where("category.parentName").regex(regexPattern))));
    }

    // isPublished
    if (isPublished != null)
      query.addCriteria(Criteria.where("isPublished").is(isPublished));

    // category
    if (categoryId != null && !categoryId.isEmpty())
      query.addCriteria(new Criteria().orOperator(
          Criteria.where("category.id").is(categoryId),
          Criteria.where("category.parentId").is(categoryId)));

    // date
    if (startDate != null && endDate != null)
      query.addCriteria(Criteria.where("createAt").gte(startDate).lte(endDate));
    else if (startDate != null)
      query.addCriteria(Criteria.where("createAt").gte(startDate));
    else if (endDate != null)
      query.addCriteria(Criteria.where("createAt").lte(endDate));

    long total = mongoTemplate.count(query, News.class);
    query.with(pageable);
    List<News> list = mongoTemplate.find(query, News.class);
    return new PageCustom<>(PageableExecutionUtils.getPage(list, pageable, () -> total));
  }

  public News update(String id, NewsReq newsReq) {
    News news = newsRepo.findById(id).orElseThrow(() -> new NotFoundError("id", id));

    Category cate = cateRepo.findById(newsReq.getCategoryId())
        .orElseThrow(() -> new NotFoundError("categoryId", newsReq.getCategoryId()));

    if (newsRepo.existsByNameAndIdNot(newsReq.getName(), id))
      throw new DuplicateRecordError("name", newsReq.getName());

    // check tags
    if (newsReq.getTags() != null && newsReq.getTags().size() > 0) {
      List<String> _tags = newsReq.getTags();
      _tags.removeIf(String::isEmpty);
      newsReq.setTags(_tags);
      tagService.addManyIfNotExist((_tags));
    }
    news.setCategory(cate);
    news.setName(newsReq.getName());
    news.setContent(newsReq.getContent());
    news.setDescription(newsReq.getDescription());
    news.setTags(newsReq.getTags());
    news.setIsPublished(newsReq.getIsPublished());
    news.setImage(newsReq.getImage() != null && !newsReq.getImage().isEmpty() ? newsReq.getImage() : null);

    return newsRepo.save(news);
  }
}
