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

    List<Report> findByBranchIdAndPeriodYear(Long branchId, Integer year);

    List<Report> findByBranchIdAndPeriod(Long branchId, YearMonth period);

    Optional<Report> findByBranchIdAndPeriodAndStatus(Long branchId, YearMonth period, Report.ReportStatus status);

    @Query("SELECT r FROM Report r WHERE r.branch.enterprise.id = :enterpriseId")
    List<Report> findByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    @Query("SELECT r FROM Report r WHERE r.branch.enterprise.id = :enterpriseId AND YEAR(r.period) = :year")
    List<Report> findByEnterpriseIdAndYear(@Param("enterpriseId") Long enterpriseId, @Param("year") Integer year);

    @Query("SELECT r FROM Report r WHERE r.branch.enterprise.association.id = :associationId")
    List<Report> findByAssociationId(@Param("associationId") Long associationId);

    @Query("SELECT r FROM Report r WHERE r.branch.enterprise.association.id = :associationId AND YEAR(r.period) = :year")
    List<Report> findByAssociationIdAndYear(@Param("associationId") Long associationId, @Param("year") Integer year);

    List<Report> findByStatus(Report.ReportStatus status);

    @Query("SELECT r FROM Report r WHERE r.branch.id = :branchId AND r.status = :status")
    List<Report> findByBranchIdAndStatus(@Param("branchId") Long branchId, @Param("status") Report.ReportStatus status);

    boolean existsByBranchIdAndPeriod(Long branchId, YearMonth period);

    @Query("SELECT COUNT(r) > 0 FROM Report r WHERE r.branch.id = :branchId AND YEAR(r.period) = :year AND MONTH(r.period) = :month")
    boolean existsByBranchIdAndYearAndMonth(@Param("branchId") Long branchId, @Param("year") Integer year, @Param("month") Integer month);
}
