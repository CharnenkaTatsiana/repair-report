package by.iba.repair_report.repository;

import by.iba.repair_report.entity.PlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanItemRepository extends JpaRepository<PlanItem, Long> {

    List<PlanItem> findByPlanId(Long planId);

    @Query("SELECT pi FROM PlanItem pi WHERE pi.plan.id = :planId AND pi.workCategory.id = :workCategoryId")
    Optional<PlanItem> findByPlanIdAndWorkCategoryId(@Param("planId") Long planId, @Param("workCategoryId") Long workCategoryId);

    @Query("SELECT pi FROM PlanItem pi WHERE pi.plan.branch.id = :branchId AND pi.workCategory.id = :workCategoryId")
    List<PlanItem> findByBranchIdAndWorkCategoryId(@Param("branchId") Long branchId, @Param("workCategoryId") Long workCategoryId);

    @Query("SELECT pi FROM PlanItem pi WHERE pi.plan.branch.enterprise.id = :enterpriseId AND pi.workCategory.id = :workCategoryId")
    List<PlanItem> findByEnterpriseIdAndWorkCategoryId(@Param("enterpriseId") Long enterpriseId, @Param("workCategoryId") Long workCategoryId);

    @Query("SELECT pi FROM PlanItem pi WHERE pi.plan.branch.enterprise.association.id = :associationId AND pi.workCategory.id = :workCategoryId")
    List<PlanItem> findByAssociationIdAndWorkCategoryId(@Param("associationId") Long associationId, @Param("workCategoryId") Long workCategoryId);

    @Query("SELECT pi FROM PlanItem pi WHERE pi.plan.id = :planId AND pi.workCategory.networkType = :networkType")
    List<PlanItem> findByPlanIdAndNetworkType(@Param("planId") Long planId, @Param("networkType") String networkType);

    void deleteByPlanId(Long planId);

    boolean existsByPlanIdAndWorkCategoryId(Long planId, Long workCategoryId);
}
