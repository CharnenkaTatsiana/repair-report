package by.iba.repair_report.repository;

import by.iba.repair_report.entity.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {

    List<Enterprise> findByAssociationId(Long associationId);

    Optional<Enterprise> findByNameAndAssociationId(String name, Long associationId);

    boolean existsByNameAndAssociationId(String name, Long associationId);

    @Query("SELECT e FROM Enterprise e WHERE e.association.id = :associationId AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Enterprise> findByAssociationIdAndNameContaining(@Param("associationId") Long associationId, @Param("name") String name);

    @Query("SELECT COUNT(e) > 0 FROM Enterprise e WHERE e.name = :name AND e.association.id = :associationId AND e.id <> :id")
    boolean existsByNameAndAssociationIdAndIdNot(@Param("name") String name, @Param("associationId") Long associationId, @Param("id") Long id);

    @Query("SELECT e FROM Enterprise e WHERE e.association.id = :associationId ORDER BY e.name")
    List<Enterprise> findAllByAssociationIdOrderByName(@Param("associationId") Long associationId);

    // Добавьте этот метод для подсчета предприятий по associationId
    @Query("SELECT COUNT(e) FROM Enterprise e WHERE e.association.id = :associationId")
    long countByAssociationId(@Param("associationId") Long associationId);
}
