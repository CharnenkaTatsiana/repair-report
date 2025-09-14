package by.iba.repair_report.repository;

import by.iba.repair_report.entity.WorkCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkCategoryRepository extends JpaRepository<WorkCategory, Long> {

    List<WorkCategory> findByIsActiveTrue();

    List<WorkCategory> findByIsActive(Boolean isActive);

    List<WorkCategory> findByNetworkType(WorkCategory.NetworkType networkType);

    List<WorkCategory> findByNetworkTypeAndIsActive(WorkCategory.NetworkType networkType, Boolean isActive);

    Optional<WorkCategory> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT wc FROM WorkCategory wc WHERE LOWER(wc.name) LIKE LOWER(CONCAT('%', :name, '%')) AND wc.isActive = true")
    List<WorkCategory> findByNameContainingIgnoreCaseAndActive(@Param("name") String name);

    @Query("SELECT wc FROM WorkCategory wc WHERE wc.networkType = :networkType AND wc.isActive = true ORDER BY wc.name")
    List<WorkCategory> findByNetworkTypeAndActiveOrderByName(@Param("networkType") WorkCategory.NetworkType networkType);

    @Query("SELECT COUNT(wc) > 0 FROM WorkCategory wc WHERE wc.name = :name AND wc.id <> :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);
}
