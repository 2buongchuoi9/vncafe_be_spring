// package vncafe.news.entities.listeners;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
// import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
// import org.springframework.stereotype.Component;

// import lombok.RequiredArgsConstructor;
// import vncafe.news.entities.Category;
// import vncafe.news.exceptions.NotFoundError;
// import vncafe.news.repositories.CategoryRepo;

// @Component
// public class CategoryListener extends AbstractMongoEventListener<Category> {
// @Autowired
// private CategoryRepo cateRepo;

// @SuppressWarnings("null")
// @Override
// public void onBeforeSave(BeforeSaveEvent<Category> event) {
// Category cate = event.getSource();
// if (cate.getParentId() != null) {
// String parentName = cateRepo.findById(cate.getParentId())
// .orElseThrow(() -> new NotFoundError("parentId", cate.getParentId()))
// .getName();
// cate.setParentName(parentName);
// }
// super.onBeforeSave(event);

// }

// }
