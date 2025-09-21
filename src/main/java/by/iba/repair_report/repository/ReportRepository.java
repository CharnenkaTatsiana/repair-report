package by.iba.repair_report.repository;

import by.iba.repair_report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByBranchId(Long branchId);

    @Query("SELECT r FROM Report r WHERE r.branch.id = :branchId AND r.period = :period")
    Optional<Report> findByBranchIdAndPeriod(@Param("branchId") Long branchId, @Param("period") YearMonth period);

    @Query("SELECT r FROM Report r WHERE r.branch.enterprise.id = :enterpriseId")
    List<Report> findByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    @Query("SELECT r FROM Report r WHERE r.branch.enterprise.association.id = :associationId")
    List<Report> findByAssociationId(@Param("associationId") Long associationId);

    List<Report> findByStatus(Report.ReportStatus status);

    @Query("SELECT r FROM Report r WHERE r.branch.id = :branchId AND r.status = :status")
    List<Report> findByBranchIdAndStatus(@Param("branchId") Long branchId, @Param("status") Report.ReportStatus status);

    @Query("SELECT COUNT(r) > 0 FROM Report r WHERE r.branch.id = :branchId AND r.period = :period")
    boolean existsByBranchIdAndPeriod(@Param("branchId") Long branchId, @Param("period") YearMonth period);
}