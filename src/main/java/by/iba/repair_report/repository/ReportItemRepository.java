package by.iba.repair_report.repository;

import by.iba.repair_report.entity.ReportItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportItemRepository extends JpaRepository<ReportItem, Long> {

    List<ReportItem> findByReportId(Long reportId);

    @Query("SELECT ri FROM ReportItem ri WHERE ri.report.id = :reportId AND ri.workCategory.id = :workCategoryId")
    Optional<ReportItem> findByReportIdAndWorkCategoryId(@Param("reportId") Long reportId, @Param("workCategoryId") Long workCategoryId);

    @Query("SELECT ri FROM ReportItem ri WHERE ri.report.branch.id = :branchId AND ri.workCategory.id = :workCategoryId")
    List<ReportItem> findByBranchIdAndWorkCategoryId(@Param("branchId") Long branchId, @Param("workCategoryId") Long workCategoryId);

    @Query("SELECT ri FROM ReportItem ri WHERE ri.report.branch.enterprise.id = :enterpriseId AND ri.workCategory.id = :workCategoryId")
    List<ReportItem> findByEnterpriseIdAndWorkCategoryId(@Param("enterpriseId") Long enterpriseId, @Param("workCategoryId") Long workCategoryId);

    @Query("SELECT ri FROM ReportItem ri WHERE ri.report.branch.enterprise.association.id = :associationId AND ri.workCategory.id = :workCategoryId")
    List<ReportItem> findByAssociationIdAndWorkCategoryId(@Param("associationId") Long associationId, @Param("workCategoryId") Long workCategoryId);

    @Query("SELECT ri FROM ReportItem ri WHERE ri.report.id = :reportId AND ri.workCategory.networkType = :networkType")
    List<ReportItem> findByReportIdAndNetworkType(@Param("reportId") Long reportId, @Param("networkType") String networkType);

    @Query("SELECT ri FROM ReportItem ri WHERE ri.report.branch.id = :branchId AND YEAR(ri.report.period) = :year AND MONTH(ri.report.period) = :month")
    List<ReportItem> findByBranchIdAndYearAndMonth(@Param("branchId") Long branchId, @Param("year") Integer year, @Param("month") Integer month);

    void deleteByReportId(Long reportId);

    boolean existsByReportIdAndWorkCategoryId(Long reportId, Long workCategoryId);
}
