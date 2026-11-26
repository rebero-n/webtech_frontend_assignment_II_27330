package auca.example.clinicSystem.service;

import java.util.List;
import java.util.Optional;

import java.time.LocalDate;
import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.AppointmentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import auca.example.clinicSystem.model.Doctor;
import auca.example.clinicSystem.model.Office;
import auca.example.clinicSystem.model.Specialization;
import auca.example.clinicSystem.repository.AppointmentRepository;
import auca.example.clinicSystem.repository.DoctorRepository;
import auca.example.clinicSystem.repository.OfficeRepository;
import auca.example.clinicSystem.repository.SpecializationRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;
    private final SpecializationRepository specializationRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorService(
            DoctorRepository doctorRepository,
            OfficeRepository officeRepository,
            AppointmentRepository appointmentRepository,
            SpecializationRepository specializationRepository
    ) {

        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
        this.specializationRepository = specializationRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // CREATE
    public Doctor createDoctor(Doctor doctor) {
    if (doctor.getOffice() != null && doctor.getOffice().getId() != null) {
        Long officeId = doctor.getOffice().getId();
        Office office = officeRepository.findById(officeId)
                .orElseThrow(() -> new RuntimeException(
                        "Office not found with id: " + officeId));
        if (office.getDoctor() != null) {
            throw new RuntimeException("Office already belongs to doctor.");
        }
        doctor.setOffice(office);
    } else {
        doctor.setOffice(null);
    }
    return doctorRepository.save(doctor);
}
    // READ ALL
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // READ ONE
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(()
                        -> new RuntimeException(
                        "Doctor not found with id: " + id
                )
                );
    }

    // UPDATE
    public Doctor updateDoctor(Long id, Doctor doctorDetails) {

        Doctor doctor = getDoctorById(id);

        doctor.setFirstName(doctorDetails.getFirstName());
    
        doctor.setEmail(doctorDetails.getEmail());
        doctor.setDateOfBirth(doctorDetails.getDateOfBirth());

       
        doctor.setSpecializations(doctorDetails.getSpecializations());
    
        if (doctorDetails.getOffice() != null
                && doctorDetails.getOffice().getId() != null) {

            Long officeId = doctorDetails.getOffice().getId();

            Office office = officeRepository.findById(officeId)
                    .orElseThrow(()
                            -> new RuntimeException(
                            "Office not found with id: " + officeId
                    )
                    );

            doctor.setOffice(office);
        }

        return doctorRepository.save(doctor);
    }

    
    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (doctor.getOffice() != null) {
            Office office = doctor.getOffice();
            office.setDoctor(null);
            officeRepository.save(office);

            doctor.setOffice(null);
            doctorRepository.save(doctor);
        }

        doctorRepository.delete(doctor);
    }

    public List<Doctor> getDoctorsBySpecialization(String name) {
        return doctorRepository.findDoctorsBySpecialization(name);
    }

    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepository.findDoctorsWithoutOffice();
    }

    @Transactional
    public String addSpecialization(Long doctorId, Long specializationId) {
        Optional <Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if(doctorOpt.isEmpty()) {
            return "The Doctor with that Id does not exist";
        }
        Optional<Specialization> specOpt = specializationRepository.findById(specializationId);
        if(specOpt.isEmpty()) {
            return "That Specialization does not exist";
    
        }

        Doctor doctor = doctorOpt.get();
        Specialization specialization = specOpt.get();
        if (doctor.getSpecializations().contains(specialization)) {
            return "The doctor already has that specialization";

        }
        doctor.getSpecializations().add(specialization);
        doctorRepository.save(doctor);
        return "Specialization added to the doctor";
        }

        @Transactional
        public String removeSpecialization(Long doctorId, Long specializationId) {
            Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
            if (doctorOpt.isEmpty()) {
                return "The Doctor with that Id does not exist";
            }
            Optional<Specialization> specOpt = specializationRepository.findById(specializationId);
            if (specOpt.isEmpty()){
                return "That Specialization does not exist";
            }
            Doctor doctor = doctorOpt.get();
            Specialization specialization = specOpt.get();
            if (!doctor.getSpecializations().contains(specialization)) {
                return "The doctor does not have that specialization";
            }
            doctor.getSpecializations().remove(specialization);
            doctorRepository.save(doctor);
            return "Specialization removed from the doctor";

        }

        @Transactional
        public String changeOffice(Long doctorId, String officeNumber) {
            Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
            if (doctorOpt.isEmpty()) {
                return "The Doctor with that Id does not exist";

            }
            Optional<Office> officeOpt = officeRepository.findByRoomNumber(officeNumber);
            if (officeOpt.isEmpty()) {
                return "The office with that office number does not exist";
            }
            Doctor doctor = doctorOpt.get();
            Office office = officeOpt.get();

            boolean sameOffice = doctor.getOffice() != null && doctor.getOffice().getId().equals(office.getId());

            if (!sameOffice && doctorRepository.existsByOffice_Id(office.getId())) {
                return "That office is taken by another doctor";

            }
            doctor.setOffice(office);
            doctorRepository.save(doctor);
            return "Office changed successfully";

        } 
public boolean doctorExists(Long doctorId) {
    return doctorRepository.existsById(doctorId);
}
public List<Appointment> getAgenda(Long doctorId, LocalDate date) {
    return appointmentRepository.findByDoctor_IdAndAppointmentDateAndStatusNot(
            doctorId, date, AppointmentStatus.CANCELLED);
}
public List<Doctor> searchDoctors(String name) {
 return doctorRepository.searchDoctors(name);
}
public List<Object[]> getDoctorWorkload(LocalDate start, LocalDate end) {
return doctorRepository.findDoctorWorkload(AppointmentStatus.COMPLETED, start, end);
}
    }
