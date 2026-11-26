package auca.example.clinicSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import auca.example.clinicSystem.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByEmail(String email);    
    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);  
    @Query("""
            SELECT DISTINCT p FROM Patient p
            JOIN p.appointments a
            WHERE a.doctor.id = :doctorId
            """)
    List<Patient> findPatientsOfDoctor(@Param("doctorId") Long doctorId);
    @Query("""
            SELECT p FROM Patient p
            JOIN p.appointments a
            GROUP BY p
            HAVING COUNT (a) >= :min
            ORDER BY COUNT (a) DESC
            """)
    List<Patient> findFrequentPatients(@Param ("min") Long min);
    boolean existsBySsn(String ssn);
}