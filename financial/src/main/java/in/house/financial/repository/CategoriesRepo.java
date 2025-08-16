package in.house.financial.repository;

import in.house.financial.entity.Categories;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepo extends MongoRepository<Categories, String> {

    void deleteByIdAndUserId(String categoryId, String id);

    List<Categories> findByUserIdOrUserIdIsNull(String userId);
}
