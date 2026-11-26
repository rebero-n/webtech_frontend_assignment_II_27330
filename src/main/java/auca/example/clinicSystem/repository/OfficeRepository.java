package auca.example.clinicSystem.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import auca.example.clinicSystem.model.Office;

public interface OfficeRepository extends JpaRepository<Office, Long> {

    boolean existsByRoomNumber(String roomNumber);
    @Query("""
            SELECT o.name, o.roomNumber, COUNT(a)
            FROM Appointment a
            JOIN a.doctor d
            JOIN d.office o
            GROUP BY o.id, o.name, o.roomNumber
            ORDER BY COUNT(a) DESC
            """)
    List<Object[]> findBusiestOffice(Pageable pageable);
    List<Office> findByNameContainingIgnoreCaseOrRoomNumberContainingIgnoreCase(String name, String roomNumber);

    Optional<Office> findByRoomNumber(String roomNumber);

    @Query("""
        SELECT o FROM Office o
        WHERE NOT EXISTS (SELECT d FROM Doctor d WHERE d.office = o)
        ORDER BY o.roomNumber ASC
        """)
List<Office> findFreeOffices();
        
}