package auca.example.clinicSystem.repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import auca.example.clinicSystem.model.AppointmentStatus;
import auca.example.clinicSystem.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByEmail(String email);
    boolean existsByOffice_Id(Long officeId);

 @Query("""
         SELECT d FROM Doctor d
            JOIN d.specializations s
            WHERE LOWER (s.name) = LOWER(:name)
            """)
    List<Doctor> findDoctorsBySpecialization(@Param("name") String name);

    @Query ("""
            SELECT d FROM Doctor d
            WHERE d.office IS NULL
            ORDER BY d.lastName ASC
            """)
    List<Doctor> findDoctorsWithoutOffice();
@Query("""
        SELECT d FROM Doctor d
        WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
           OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
        ORDER BY d.lastName ASC
        """)
List<Doctor> searchDoctors(@Param("name") String name);
@Query("""
        SELECT d.firstName, d.lastName, COUNT(a)
        FROM Appointment a
        JOIN a.doctor d
        WHERE a.status = :status
          AND a.appointmentDate BETWEEN :start AND :end
        GROUP BY d.id, d.firstName, d.lastName
        ORDER BY COUNT(a) DESC
        """)
List<Object[]> findDoctorWorkload(
        @Param("status") AppointmentStatus status,
        @Param("start") LocalDate start,
        @Param("end") LocalDate end);
}