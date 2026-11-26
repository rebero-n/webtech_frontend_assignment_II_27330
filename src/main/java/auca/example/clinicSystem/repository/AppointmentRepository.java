package auca.example.clinicSystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByPatient_Id(Long patientId);
    boolean existsByDoctor_Id(Long doctorId);
    List<Appointment> findByPatient_Id(Long patientId);

    @Query("""
            SELECT a FROM Appointment a
            JOIN a.patient p
            WHERE p.ssn = :SocialSec """)
    List<Appointment> findByPatient_SSN(@Param("SocialSec") String ssn);

    List<Appointment> findByDoctorIdAndStatusOrderByAppointmentDateAsc(
            Long doctorId, AppointmentStatus appointmentStatus);

    @Query("""
            SELECT CONCAT(p.firstName, ' ', p.lastName),
                   CONCAT(d.firstName, ' ', d.lastName),
                   a.appointmentDate
            FROM Appointment a
            JOIN a.patient p
            JOIN a.doctor d
            WHERE a.status = :status
              AND a.appointmentDate < CURRENT_DATE
            """)
    List<Object[]> findScheduledAppointments(
            @Param("status") AppointmentStatus status);

    @Query("""
            SELECT d.id, d.firstName, d.lastName, COUNT(a)
            FROM Appointment a
            JOIN a.doctor d
            GROUP BY d.id, d.firstName, d.lastName
            ORDER BY COUNT(a) DESC
            """)
    List<Object[]> countAppointmentsPerDoctor();

    @Query("""
            SELECT a
            FROM Appointment a
            JOIN a.doctor d
            JOIN d.specializations s
            WHERE s.name = :specialization
            """)
    List<Appointment> findAppointmentsBySpecialization(
            @Param("specialization") String specialization);

    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);
    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status); 
    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(
            LocalDate start, LocalDate end);
	boolean existsByDoctor_IdAndAppointmentDateAndStatusNot(
        Long doctorId, LocalDate appointmentDate, AppointmentStatus status);

	@Query("""  
                  SELECT a.status, COUNT(a)
		  FROM Appointment a
		  GROUP BY a.status
	      """)
	List<Object[]> countAppointmentsByStatus();
	@Modifying
    @Query("""
        DELETE FROM Appointment a
        WHERE a.status = :status
          AND a.appointmentDate < :date
        """)
    int deleteCancelledBefore(
        @Param("status") AppointmentStatus status,
        @Param("date") LocalDate date);

    @Modifying
    @Query("""
        UPDATE Appointment a
        SET a.status = :cancelled
        WHERE a.doctor.id = :doctorId
          AND a.appointmentDate = :date
          AND a.status <> :completed
        """)
    int cancelDay(
        @Param("doctorId") Long doctorId,
        @Param("date") LocalDate date,
        @Param("cancelled") AppointmentStatus cancelled,
        @Param("completed") AppointmentStatus completed);
        List<Appointment> findByDoctor_IdAndAppointmentDateAndStatusNot(
          Long doctorId, LocalDate appointmentDate, AppointmentStatus status);
        List<Appointment> findByPatient_SsnAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
           String ssn, LocalDate date, List<AppointmentStatus> statuses);
        long countByAppointmentDate(LocalDate appointmentDate);
        long countByStatus(AppointmentStatus status);
			
}