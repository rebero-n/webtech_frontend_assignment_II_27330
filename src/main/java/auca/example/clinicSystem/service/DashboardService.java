package auca.example.clinicSystem.service;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import auca.example.clinicSystem.model.AppointmentStatus;
import auca.example.clinicSystem.repository.AppointmentRepository;
import auca.example.clinicSystem.repository.DoctorRepository;
import auca.example.clinicSystem.repository.OfficeRepository;
import auca.example.clinicSystem.repository.PatientRepository;
import auca.example.clinicSystem.repository.PrescriptionRepository;

@Service
public class DashboardService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    public DashboardService(
         PatientRepository patientRepository,
         
         DoctorRepository doctorRepository,
            OfficeRepository officeRepository,
            AppointmentRepository appointmentRepository,
            PrescriptionRepository prescriptionRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }
    public Map<String, Long> getSummary() {
     Map<String, Long> summary = new LinkedHashMap<>();
        long freeOffices = officeRepository.findFreeOffices().size();
        summary.put("patients", patientRepository.count());
        summary.put("doctors", doctorRepository.count());
        summary.put("freeOffices", freeOffices);
        summary.put("appointmentsToday", appointmentRepository.countByAppointmentDate(LocalDate.now()));
        summary.put("scheduled", appointmentRepository.countByStatus(AppointmentStatus.SCHEDULED));
        summary.put("prescriptions", prescriptionRepository.count());
        return summary;
    }
}