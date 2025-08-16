package in.house.financial.repository;

import in.house.financial.entity.UserAccessKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccessKeyRepository extends MongoRepository<UserAccessKey, String> {
}
