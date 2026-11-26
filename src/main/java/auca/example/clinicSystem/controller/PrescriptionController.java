package auca.example.clinicSystem.controller;
import java.util.List;
import java.util.UUID;
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
import auca.example.clinicSystem.model.Prescription;
import auca.example.clinicSystem.service.PrescriptionService;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    public PrescriptionController(PrescriptionService prescriptionService){
        this.prescriptionService = prescriptionService;

    }
    @PostMapping("/save")
    public ResponseEntity<String> createPrescription(
            @RequestParam Long appointmentId,
           @RequestBody Prescription prescription
    ) {
        String message = prescriptionService.createPrescription(appointmentId, prescription);
        if (message.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }else if (message.equals("Prescriptions are only for COMPLETED appointments")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionsByAppointment(@PathVariable Long appointmentId) {
        if (!prescriptionService.appointmentExists(appointmentId)) {
            return new ResponseEntity<>(
                    "The appointment with that id does not exist",
                    HttpStatus.NOT_FOUND
            );
        }
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByAppointment(appointmentId));
    }
    @GetMapping("/patient/{ssNumber}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(@PathVariable String ssNumber) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsBySsn(ssNumber));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePrescription(
            @PathVariable UUID id,
            @RequestBody Prescription prescription) {
        String message = prescriptionService.updatePrescription(id, prescription);
        if (message.equals("The prescription with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable UUID id) {
        String message = prescriptionService.deletePrescription(id);
        if (message.equals("The prescription with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);}
        @GetMapping("/stats/top-medicines")
    public ResponseEntity<List<Object[]>> getTopMedicines() {
        return ResponseEntity.ok(prescriptionService.getTopMedicines());
    }
    
}
