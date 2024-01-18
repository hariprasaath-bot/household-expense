package in.house.financial.repository;

import ch.qos.logback.core.model.INamedModel;
import in.house.financial.entity.UserAccessKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccessKeyRepository extends JpaRepository<UserAccessKey, Integer> {
}
