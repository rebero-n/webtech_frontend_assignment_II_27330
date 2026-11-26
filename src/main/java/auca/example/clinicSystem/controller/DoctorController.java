package auca.example.clinicSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import java.time.LocalDate;


import auca.example.clinicSystem.model.Doctor;
import auca.example.clinicSystem.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // CREATE
    @PostMapping("/save")
    public ResponseEntity<Doctor> createDoctor(
            @RequestBody Doctor doctor
    ) {
        return new ResponseEntity<>(
                doctorService.createDoctor(doctor),
                HttpStatus.CREATED
        );
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                doctorService.getDoctorById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor
    ) {
        return ResponseEntity.ok(
                doctorService.updateDoctor(id, doctor)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(
            @PathVariable Long id
    ) {
        doctorService.deleteDoctor(id);

        return ResponseEntity.ok(
                "Doctor deleted successfully."
        );
    }
    //GET DOCTORS BY SPECIALIZATION

    @GetMapping("/by-specialization")
public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(
        @RequestParam String name
) {
    return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(name));
  }  

  //GET DOCTORS WITHOUT AN OFFICE
  @GetMapping ("/without-office")
  public ResponseEntity<List<Doctor>> getDoctorsWithoutOffice() {
    return ResponseEntity.ok(doctorService.getDoctorsWithoutOffice());
  }

@PutMapping("/{doctorId}/specializations/{specializationId}")
public ResponseEntity<String> addSpecialization(
        @PathVariable Long doctorId,
        @PathVariable Long specializationId
) {
    String message = doctorService.addSpecialization(doctorId, specializationId);

    if (message.equals("The doctor with that id does not exist")
            || message.equals("That specialization does not exist")) {
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    } else if (message.equals("The doctor already has that specialization")) {
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(message, HttpStatus.OK);
}

@DeleteMapping("/{doctorId}/specializations/{specializationId}")
public ResponseEntity<String> removeSpecialization(
        @PathVariable Long doctorId,
        @PathVariable Long specializationId
) {
    String message = doctorService.removeSpecialization(doctorId, specializationId);

    if (message.equals("The doctor with that id does not exist")
            || message.equals("That specialization does not exist")) {
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    } else if (message.equals("The doctor does not have that specialization")) {
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(message, HttpStatus.OK);
}

@PatchMapping("/{doctorId}/office")
public ResponseEntity<String> changeOffice(
        @PathVariable Long doctorId,
        @RequestParam String officeNumber
) {
    String message = doctorService.changeOffice(doctorId, officeNumber);

    if (message.equals("The doctor with that id does not exist")
            || message.equals("The office with that office number does not exist")) {
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    } else if (message.equals("That office is taken by other doctor")) {
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(message, HttpStatus.OK);
}
@GetMapping("/{doctorId}/agenda")
public ResponseEntity<?> getAgenda(
        @PathVariable Long doctorId,
        @RequestParam String date
) {
    if (!doctorService.doctorExists(doctorId)) {
        return new ResponseEntity<>(
                "The doctor with that id does not exist",
                HttpStatus.NOT_FOUND
        ); }
    return ResponseEntity.ok(doctorService.getAgenda(doctorId, LocalDate.parse(date)));
}
@GetMapping("/search")
public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam String name) {
    return ResponseEntity.ok(doctorService.searchDoctors(name));
}
@GetMapping("/workload")
public ResponseEntity<List<Object[]>> getDoctorWorkload(
        @RequestParam String start,
        @RequestParam String end
) {
    return ResponseEntity.ok(
            doctorService.getDoctorWorkload(LocalDate.parse(start), LocalDate.parse(end))
    );
}

}