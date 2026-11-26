package auca.example.clinicSystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import auca.example.clinicSystem.repository.PrescriptionRepository;

import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.AppointmentStatus;
import auca.example.clinicSystem.repository.AppointmentRepository;
import auca.example.clinicSystem.repository.PatientRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {
private final AppointmentRepository appointmentRepository;
private final PrescriptionRepository prescriptionRepository;

public AppointmentService(
        PatientRepository patientRepository,
        AppointmentRepository appointmentRepository,
        PrescriptionRepository prescriptionRepository) {
    this.appointmentRepository = appointmentRepository;
    this.prescriptionRepository = prescriptionRepository;
}

    // CREATE
    public String createAppointment(Appointment appointment){
        if(appointment.getAppointmentDate().isBefore(LocalDate.now())){
            return "Appointment date cannot be in the past";
        }
        boolean booked = appointmentRepository
        .existsByDoctor_IdAndAppointmentDateAndStatusNot(appointment.getDoctor().getId(), appointment.getAppointmentDate(), AppointmentStatus.CANCELLED);
        if (booked) {
            return "Doctor is already booked on that date";

        }
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
        return "Appointment saved successfully";
    }
    // READ ALL
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // READ ONE
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Appointment not found with id: " + id));
    }

    public List<Appointment> getAllAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatient_Id(patientId);
    }

    public List<Appointment> getAllAppointmentsByPatientSSN(String ssn) {
        return appointmentRepository.findByPatient_SSN(ssn);
    }

    public List<Appointment> getPendingAppointmentsByDoctorIDSortedByDate(Long doctorID) {
        return appointmentRepository.findByDoctorIdAndStatusOrderByAppointmentDateAsc(
                doctorID, AppointmentStatus.SCHEDULED);
    }

    public List<Object[]> getScheduledAppointments() {
        return appointmentRepository.findScheduledAppointments(
                AppointmentStatus.SCHEDULED);
    }

    public List<Object[]> getAppointmentsCountPerDoctor() {
        return appointmentRepository.countAppointmentsPerDoctor();
    }

    public List<Appointment> getAppointmentsBySpecialization(String specialization) {
        return appointmentRepository.findAppointmentsBySpecialization(specialization);
    }

    public Page<Appointment> pageByDoctorID(Long doctorId) {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("appointmentDate").ascending());

        return appointmentRepository.findByDoctorId(doctorId, pageable);
    }

    // A2
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatusOrderByAppointmentDateAsc(status);
    }

    // A3
    public List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end) {
        return appointmentRepository
                .findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }
    public String deleteAppointment(Long id) {
    Optional<Appointment> opt = appointmentRepository.findById(id);
    if (opt.isEmpty()) {
        return "The appointment with that id does not exist";
    }if (prescriptionRepository.existsByAppointment_Id(id)) {
        return "That appointment has prescriptions, delete them first";
    }
    appointmentRepository.delete(opt.get());
    return "Appointment deleted successfully";
}public List<Object[]> getCountByStatus(){
       return appointmentRepository.countAppointmentsByStatus();
}
@Transactional
public String cancelDay(Long doctorId, LocalDate date) {
    int count = appointmentRepository.cancelDay(
            doctorId, date,
            AppointmentStatus.CANCELLED,
            AppointmentStatus.COMPLETED);
    return count + " appointments cancelled";
}
public Page<Appointment> getAppointmentsPage(Pageable pageable) {
    return appointmentRepository.findAll(pageable);
}

@Transactional
public String deleteCancelledBefore(LocalDate date) {
    int count = appointmentRepository.deleteCancelledBefore(
            AppointmentStatus.CANCELLED, date);
    return count + " appointments deleted";
}

public String updateStatus(Long id, AppointmentStatus newStatus) {
    Optional<Appointment> opt = appointmentRepository.findById(id);
    if (opt.isEmpty()) {
        return "The appointment with that id does not exist";
    }
    Appointment appointment = opt.get();
    AppointmentStatus current = appointment.getStatus();

    boolean allowed =
            (current == AppointmentStatus.SCHEDULED
                    && (newStatus == AppointmentStatus.CONFIRMED
                    || newStatus == AppointmentStatus.CANCELLED))
            || (current == AppointmentStatus.CONFIRMED
                    && (newStatus == AppointmentStatus.COMPLETED
                    || newStatus == AppointmentStatus.CANCELLED));

    if (!allowed) {
        return "Cannot change status from " + current + " to " + newStatus;
    }
appointment.setStatus(newStatus);
    appointmentRepository.save(appointment);
    return "Appointment status updated successfully";
}

public String rescheduleAppointment(Long id, LocalDate newDate) {
    Optional<Appointment> opt = appointmentRepository.findById(id);
    if (opt.isEmpty()) {
        return "The appointment with that id does not exist";
    }
    Appointment appointment = opt.get();

    if (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED) {
        return "A " + appointment.getStatus() + " appointment cannot be rescheduled";
    }
    if (newDate.isBefore(LocalDate.now())) {
        return "Appointment date cannot be in the past";
    }
    boolean booked = appointmentRepository
            .existsByDoctor_IdAndAppointmentDateAndStatusNot(
                appointment.getDoctor().getId(),
                    newDate,
                    AppointmentStatus.CANCELLED);
    if (booked) {
        return "Doctor is already booked on that date";
    }
    appointment.setAppointmentDate(newDate);
    appointment.setStatus(AppointmentStatus.SCHEDULED);
    appointmentRepository.save(appointment);
    return "Appointment rescheduled successfully";
}
}