package auca.example.clinicSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.AppointmentStatus;

import auca.example.clinicSystem.model.Patient;
import auca.example.clinicSystem.repository.AppointmentRepository;
import auca.example.clinicSystem.repository.DoctorRepository;
import auca.example.clinicSystem.repository.PatientRepository;


@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository
    ) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    // CREATE
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // READ ALL
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // READ ONE
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: " + id
                        )
                );
    }

    // UPDATE
    public Patient updatePatient(Long id, Patient patientDetails) {

        Patient patient = getPatientById(id);

        patient.setFirstName(patientDetails.getFirstName());
        patient.setLastName(patientDetails.getLastName());
        patient.setEmail(patientDetails.getEmail());
        patient.setPhone(patientDetails.getPhone());

        return patientRepository.save(patient);
    }

    // DELETE
    public void deletePatient(Long id) {

        Patient patient = getPatientById(id);

        if (appointmentRepository.existsByPatient_Id(id)) {
            throw new IllegalStateException(
                    "Cannot delete patient because the patient has appointments."
            );
        }

        patientRepository.delete(patient);
    }

    
    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    
    public boolean doctorExists(Long doctorId) {
        return doctorRepository.existsById(doctorId);
    }

    
    public List<Patient> getPatientsOfDoctor(Long doctorId) {
        return patientRepository.findPatientsOfDoctor(doctorId);
    }

    public List<Patient> getFrequentPatients(Long min) {
        return patientRepository.findFrequentPatients(min);
    }
public boolean patientExists(String ssn) {
    return patientRepository.existsBySsn(ssn);
}
public List<Appointment> getUpcomingAppointments(String ssn){
 List<AppointmentStatus> statuses = new ArrayList<>();
    statuses.add(AppointmentStatus.SCHEDULED);
    statuses.add(AppointmentStatus.CONFIRMED);
    return appointmentRepository
         .findByPatient_SsnAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
         ssn, LocalDate.now(), statuses);
}
}