package auca.example.clinicSystem.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;

import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.AppointmentStatus;
import auca.example.clinicSystem.service.AppointmentService;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // CREATE
     @PostMapping("/save")
      public ResponseEntity<String> createAppointment(
        @RequestBody Appointment appointment) {
    String message = appointmentService.createAppointment(appointment);

    if (message.equals("Appointment date cannot be in the past")) {
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    } else if (message.equals("Doctor is already booked on that date")) {
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    // READ ALL
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }

    // A2: by status
    @GetMapping("/by-status")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(
            @RequestParam AppointmentStatus status
    ) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByStatus(status)
        );
    }

    // A3: date range
    @GetMapping("/between")
    public ResponseEntity<List<Appointment>> getAppointmentsBetween(
            @RequestParam String start,
            @RequestParam String end
    ) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsBetween(
                        LocalDate.parse(start),
                        LocalDate.parse(end)
                )
        );
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id)
        );
    }

    // READ FROM PATIENT ID
    @GetMapping("/getFromPatientID/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentByPatientID(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                appointmentService.getAllAppointmentsByPatientId(id)
        );
    }

    // READ FROM PATIENT SSN
    @GetMapping("/getFromPatientSSN/{ssn}")
    public ResponseEntity<List<Appointment>> getAppointmentByPatientSSN(
            @PathVariable String ssn
    ) {
        return ResponseEntity.ok(
                appointmentService.getAllAppointmentsByPatientSSN(ssn)
        );
    }

    @GetMapping("/getPending/{doctorID}")
    public ResponseEntity<List<Appointment>> getPendingAppointmentsByDoctorID(
            @PathVariable Long doctorID
    ) {
        return ResponseEntity.ok(
                appointmentService.getPendingAppointmentsByDoctorIDSortedByDate(doctorID)
        );
    }

    @GetMapping("/getPastPending")
    public ResponseEntity<List<Object[]>> getPastPending() {
        return ResponseEntity.ok(
                appointmentService.getScheduledAppointments()
        );
    }

    @GetMapping("/countAppointments")
    public ResponseEntity<List<Object[]>> getCountsPerDoctor() {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsCountPerDoctor()
        );
    }

    @GetMapping("/getBySpecialization/{specialization}")
    public ResponseEntity<List<Appointment>> getAppointmentsBySpecialization(
            @PathVariable String specialization
    ) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsBySpecialization(specialization)
        );
    }

    @GetMapping("/pageByDoctorID/{doctorID}")
    public ResponseEntity<Page<Appointment>> pageAppointmentsByDoctorID(
            @PathVariable Long doctorID
    ) {
        return ResponseEntity.ok(
                appointmentService.pageByDoctorID(doctorID)
        );
    }

   @GetMapping("/stats/by-status")
   public ResponseEntity<List<Object[]>> getCountByStatus() {
       return ResponseEntity.ok(
               appointmentService.getCountByStatus());
   }

   @PatchMapping("/cancel-day")
public ResponseEntity<String> cancelDay(
        @RequestParam Long doctorId,
        @RequestParam String date
) {
    return ResponseEntity.ok(
            appointmentService.cancelDay(doctorId, LocalDate.parse(date))
    );
}

@GetMapping("/page")
public ResponseEntity<Page<Appointment>> getAppointmentsPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "appointmentDate,desc") String sort
) {
    String[] parts = sort.split(",");
    Sort.Direction direction =
            (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, parts[0]));

    return ResponseEntity.ok(appointmentService.getAppointmentsPage(pageable));
}

@DeleteMapping("/{id}")
public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
    String message = appointmentService.deleteAppointment(id);
    if (message.equals("The appointment with that id does not exist")) {
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    } else if (message.equals("That appointment has prescriptions, delete them first")) {
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
}
    return new ResponseEntity<>(message, HttpStatus.OK);
}
@PatchMapping("/{id}/status")
public ResponseEntity<String> updateStatus(
        @PathVariable Long id,
        @RequestParam AppointmentStatus status
) {String message = appointmentService.updateStatus(id, status);

    if (message.equals("The appointment with that id does not exist")) {
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    } else if (message.startsWith("Cannot change status")) {
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
}
    return new ResponseEntity<>(message, HttpStatus.OK);
}
@PatchMapping("/{id}/reschedule")
public ResponseEntity<String> rescheduleAppointment(
        @PathVariable Long id,
        @RequestParam String newDate
) {
    String message = appointmentService.rescheduleAppointment(id, LocalDate.parse(newDate));

    if (message.equals("The appointment with that id does not exist")) {
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
} else if (message.equals("Doctor is already booked on that date")) {
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
} else if (message.equals("Appointment rescheduled successfully")) {
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
}

}
