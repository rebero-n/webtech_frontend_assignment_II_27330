package auca.example.clinicSystem.repository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.Prescription;

public interface PrescriptionRepository extends JpaRepository <Prescription, UUID> {
    List <Prescription> findByAppointment (Appointment appointment);

    boolean existsByAppointment_Id(Long appointmentId);
    @Query ("""
            SELECT p FROM Prescription p
            WHERE p.appointment.patient.ssn = :ssn
            ORDER BY p.issuedDate DESC
            """)
            List<Prescription> findPrescriptionsBySsn(@Param("ssn") String ssn);
    @Query("""
            SELECT p.medicineName, COUNT(p)
            FROM Prescription p
            GROUP BY p.medicineName
            ORDER BY COUNT(p) DESC
            """)
    List<Object[]> findTopMedicines();
}
