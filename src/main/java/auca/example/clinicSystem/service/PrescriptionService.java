package auca.example.clinicSystem.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.AppointmentStatus;
import auca.example.clinicSystem.model.Prescription;
import auca.example.clinicSystem.repository.AppointmentRepository;
import auca.example.clinicSystem.repository.PrescriptionRepository;

@Service
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
   private final AppointmentRepository appointmentRepository;
    public PrescriptionService(
        PrescriptionRepository prescriptionRepository,
            AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }
    public String createPrescription(Long appointmentId, Prescription prescription) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment appointment = appointmentOpt.get();
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return "Prescriptions are only for COMPLETED appointments";
     }
        prescription.setIssuedDate(LocalDate.now());
        prescription.setAppointment(appointment);
        prescriptionRepository.save(prescription);
        return "Prescription saved successfully";
    }
    public boolean appointmentExists(Long appointmentId) {
        return appointmentRepository.existsById(appointmentId);
    }
    public List<Prescription> getPrescriptionsByAppointment(Long appointmentId) {
     Appointment appointment = appointmentRepository.findById(appointmentId).get();
    return prescriptionRepository.findByAppointment(appointment);
    }
    public List<Prescription> getPrescriptionsBySsn(String ssn){
        return prescriptionRepository.findPrescriptionsBySsn(ssn);
    }
    public String updatePrescription(UUID id, Prescription details){
        Optional<Prescription> opt = prescriptionRepository.findById(id);
        if(opt.isEmpty()){
            return "The Prescription with that id does not exist";
            
        }
        Prescription prescription = opt.get();
        prescription.setMedicineName(details.getMedicineName());
        prescription.setDosage(details.getDosage());
        prescription.setDurationDays(details.getDurationDays());
        prescriptionRepository.save(prescription);
        return "Prescription updated successfully";
    }
    public String deletePrescription(UUID id) {
    Optional<Prescription> opt = prescriptionRepository.findById(id);
        if (opt.isEmpty()) {
            return "The prescription with that id does not exist";

        }
        prescriptionRepository.delete(opt.get());
        return "Prescription deleted successfully";
    }
    public List<Object[]> getTopMedicines() {
        return prescriptionRepository.findTopMedicines();
    }
}
    
