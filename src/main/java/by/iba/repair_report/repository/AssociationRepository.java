package by.iba.repair_report.repository;

import by.iba.repair_report.entity.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssociationRepository extends JpaRepository<Association, Long> {

    Optional<Association> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT a FROM Association a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Association> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT COUNT(a) > 0 FROM Association a WHERE a.name = :name AND a.id <> :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);
}
