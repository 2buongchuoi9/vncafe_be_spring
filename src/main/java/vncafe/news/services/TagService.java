package vncafe.news.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vncafe.news.entities.Tag;
import vncafe.news.exceptions.DuplicateRecordError;
import vncafe.news.repositories.TagRepo;
import vncafe.news.repositories.repositoryUtil.PageCustom;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class TagService {
  final private TagRepo tagRepo;
  final private MongoTemplate mongoTemplate;

  public Tag addIfNotExist(String name) {
    return tagRepo.findByName(name).orElse(
        tagRepo.save(new Tag(name)));
  }

  public void addManyIfNotExist(List<String> tags) {
    // Set<Tag> tags_ = new HashSet<>(tags).stream().map(v -> new
    // Tag(v)).collect(Collectors.toSet());
    if (tags != null) {
      tags.stream().forEach(tag -> {
        Tag existingTag = mongoTemplate.findOne(Query.query(Criteria.where("name").is(tag)), Tag.class);
        if (existingTag == null) {

          mongoTemplate.save(new Tag(tag));
        }
      });
    }

  }
  // if (tagRepo.existsByName(name))
  // throw new DuplicateRecordError("tag", name);
  // return tagRepo.save(Tag.builder().name(name).build());

  public PageCustom<Tag> getAll(Pageable pageable) {
    Query query = new Query();
    long total = mongoTemplate.count(query, Tag.class);
    List<Tag> list = mongoTemplate.find(query, Tag.class);
    query.with(pageable);

    return new PageCustom<>(PageableExecutionUtils.getPage(list, pageable, () -> total));
  }

}
