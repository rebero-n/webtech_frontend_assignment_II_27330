package auca.example.clinicSystem.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;

import auca.example.clinicSystem.model.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    boolean existsByName(String name);
@Query("SELECT  s FROM Specialization s WHERE s.doctors IS EMPTY")
    List<Specialization> findUnusedSpecializations();
}