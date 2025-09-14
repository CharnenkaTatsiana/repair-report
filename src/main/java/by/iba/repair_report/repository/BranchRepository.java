package by.iba.repair_report.repository;

import by.iba.repair_report.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByEnterpriseId(Long enterpriseId);

    Optional<Branch> findByNameAndEnterpriseId(String name, Long enterpriseId);

    boolean existsByNameAndEnterpriseId(String name, Long enterpriseId);

    @Query("SELECT b FROM Branch b WHERE b.enterprise.id = :enterpriseId AND LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Branch> findByEnterpriseIdAndNameContaining(@Param("enterpriseId") Long enterpriseId, @Param("name") String name);

    @Query("SELECT b FROM Branch b WHERE b.enterprise.association.id = :associationId")
    List<Branch> findByAssociationId(@Param("associationId") Long associationId);

    @Query("SELECT COUNT(b) > 0 FROM Branch b WHERE b.name = :name AND b.enterprise.id = :enterpriseId AND b.id <> :id")
    boolean existsByNameAndEnterpriseIdAndIdNot(@Param("name") String name, @Param("enterpriseId") Long enterpriseId, @Param("id") Long id);

    @Query("SELECT b FROM Branch b WHERE b.enterprise.id = :enterpriseId ORDER BY b.name")
    List<Branch> findAllByEnterpriseIdOrderByName(@Param("enterpriseId") Long enterpriseId);

    @Query("SELECT b FROM Branch b WHERE b.enterprise.association.id = :associationId ORDER BY b.enterprise.name, b.name")
    List<Branch> findAllByAssociationIdOrderByEnterpriseNameAndBranchName(@Param("associationId") Long associationId);

    // Добавьте этот метод для подсчета филиалов по enterpriseId
    @Query("SELECT COUNT(b) FROM Branch b WHERE b.enterprise.id = :enterpriseId")
    long countByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}
