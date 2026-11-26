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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.example.clinicSystem.model.Patient;
import auca.example.clinicSystem.service.PatientService;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // CREATE
    @PostMapping("/save")
    public ResponseEntity<Patient> createPatient(
            @RequestBody Patient patient
    ) {
        return new ResponseEntity<>(
                patientService.createPatient(patient),
                HttpStatus.CREATED
        );
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(
                patientService.getAllPatients()
        );
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                patientService.getPatientById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient
    ) {
        return ResponseEntity.ok(
                patientService.updatePatient(id, patient)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(
            @PathVariable Long id
    ) {
        patientService.deletePatient(id);

        return ResponseEntity.ok(
                "Patient deleted successfully."
        );
    }

    @GetMapping("/of-doctor/{doctorId}")
    public ResponseEntity<?> getPatientsOfDoctor(@PathVariable Long doctorId) {
    if (!patientService.doctorExists(doctorId)) {
        return new ResponseEntity<>(
                "The doctor with that id does not exist",
                HttpStatus.NOT_FOUND
        );
    }
    return ResponseEntity.ok(patientService.getPatientsOfDoctor(doctorId));
  }

  @GetMapping("/by-last-name")
public ResponseEntity<List<Patient>> getPatientsByLastName(
        @RequestParam String lastName
) {
    return ResponseEntity.ok(
            patientService.getPatientsByLastName(lastName)
    );
  }

  @GetMapping("/frequent")
public ResponseEntity<List<Patient>> getFrequentPatients(
        @RequestParam Long min
) {
    return ResponseEntity.ok(patientService.getFrequentPatients(min));
}
@GetMapping("/{ssNumber}/upcoming")
public ResponseEntity<?> getUpcomingAppointments(@PathVariable String ssNumber) {
    if (!patientService.patientExists(ssNumber)) {
        return new ResponseEntity<>(
          "That patient with that ssnumber does not exist",
        HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(patientService.getUpcomingAppointments(ssNumber));
}
}