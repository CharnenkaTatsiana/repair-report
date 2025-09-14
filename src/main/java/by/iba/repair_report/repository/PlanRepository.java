package by.iba.repair_report.repository;

import by.iba.repair_report.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    // Добавьте этот метод
    boolean existsByBranchIdAndYear(Long branchId, Integer year);

    // Добавьте этот метод если нужно проверять с исключением текущего плана
    boolean existsByBranchIdAndYearAndIdNot(Long branchId, Integer year, Long excludePlanId);

    List<Plan> findByBranchId(Long branchId);

    Optional<Plan> findByBranchIdAndYear(Long branchId, Integer year);

    @Query("SELECT p FROM Plan p WHERE p.branch.enterprise.id = :enterpriseId")
    List<Plan> findByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    @Query("SELECT p FROM Plan p WHERE p.branch.enterprise.association.id = :associationId")
    List<Plan> findByAssociationId(@Param("associationId") Long associationId);

    // Добавьте метод для поиска по статусу если нужно
    List<Plan> findByStatus(Plan.PlanStatus status);

    // Добавьте метод для поиска по филиалу и статусу если нужно
    List<Plan> findByBranchIdAndStatus(Long branchId, Plan.PlanStatus status);
}
