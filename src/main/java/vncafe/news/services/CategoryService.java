package vncafe.news.services;

import org.springframework.stereotype.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;

import vncafe.news.entities.Category;
import vncafe.news.exceptions.DuplicateRecordError;
import vncafe.news.exceptions.NotFoundError;
import vncafe.news.repositories.CategoryRepo;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class CategoryService {
  final private CategoryRepo cateRepo;

  public Category addCate(Category cate) {

    if (cateRepo.existsByNameAndParentId(cate.getName(), cate.getParentId()))
      throw new DuplicateRecordError("name, parentId", cate.getName() + ", " + cate.getParentId());

    if (cate.getParentId() == null)
      return cateRepo.save(cate);

    Category parentCate = cateRepo.findById(cate.getParentId())
        .orElseThrow(() -> new NotFoundError("parentId", cate.getParentId()));

    cate.setParentName(parentCate.getName());
    return cateRepo.save(cate);

  }

  public List<Category> getAllCate() {
    return cateRepo.findAll();
  }

}
