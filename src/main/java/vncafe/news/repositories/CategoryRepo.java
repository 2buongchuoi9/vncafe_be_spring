package vncafe.news.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vncafe.news.entities.Category;

@Repository
public interface CategoryRepo extends MongoRepository<Category, String> {

  boolean existsByName(String name);

  boolean existsByNameAndParentId(String name, String parentId);
}
