package auca.example.clinicSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import auca.example.clinicSystem.model.Specialization;
import auca.example.clinicSystem.repository.SpecializationRepository;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public Specialization createSpecialization(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }

    public Specialization getSpecializationById(Long id) {
        return specializationRepository.findById(id)
                .orElseThrow(()
                        -> new RuntimeException("Specialization not found"));
    }

    public Specialization updateSpecialization(Long id, Specialization updatedSpecialization) {

        Specialization existing
                = specializationRepository.findById(id)
                        .orElseThrow(()
                                -> new RuntimeException("Specialization not found"));

        existing.setName(updatedSpecialization.getName());

        return specializationRepository.save(existing);
    }

    public void deleteSpecialization(Long id) {

        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(()
                        -> new RuntimeException("Specialization not found"));

        specializationRepository.delete(specialization);
    
    }

    public List<Specialization> getUnusedSpecializations() {
        return specializationRepository.findUnusedSpecializations();
    }
}
