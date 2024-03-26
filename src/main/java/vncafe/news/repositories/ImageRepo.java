package vncafe.news.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vncafe.news.entities.Image;

@Repository
public interface ImageRepo extends MongoRepository<Image, String> {

}
