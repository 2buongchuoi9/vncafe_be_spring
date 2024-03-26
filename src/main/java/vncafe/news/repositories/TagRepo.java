package vncafe.news.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vncafe.news.entities.Tag;

@Repository
public interface TagRepo extends MongoRepository<Tag, String> {

  boolean existsByName(String name);

  Optional<Tag> findByName(String name);

}
