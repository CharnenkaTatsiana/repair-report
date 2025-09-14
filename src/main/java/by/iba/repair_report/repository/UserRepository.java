package by.iba.repair_report.repository;

import by.iba.repair_report.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Optional<User> findByBranchId(Long branchId);
    Optional<User> findByEnterpriseId(Long enterpriseId);
}
