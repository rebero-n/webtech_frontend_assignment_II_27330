package auca.example.clinicSystem.service;

import java.util.List;
import org.springframework.data.domain.PageRequest;


import org.springframework.stereotype.Service;

import auca.example.clinicSystem.model.Office;
import auca.example.clinicSystem.repository.DoctorRepository;
import auca.example.clinicSystem.repository.OfficeRepository;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final DoctorRepository doctorRepository;

    public OfficeService(
            OfficeRepository officeRepository,
            DoctorRepository doctorRepository
    ) {
        this.officeRepository = officeRepository;
        this.doctorRepository = doctorRepository;
    }

    // CREATE
    public Office createOffice(Office office) {
        return officeRepository.save(office);
    }

    // READ ALL
    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    // READ ONE
    public Office getOfficeById(Long id) {
        return officeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Office not found with id: " + id)
                );
    }

    // UPDATE
    public Office updateOffice(Long id, Office officeDetails) {

        Office office = getOfficeById(id);

        office.setRoomNumber(officeDetails.getRoomNumber());
        office.setFloor(officeDetails.getFloor());

        return officeRepository.save(office);
    }

    // DELETE
    public void deleteOffice(Long id) {

        Office office = getOfficeById(id);

        if (doctorRepository.existsByOffice_Id(id)) {
            throw new IllegalStateException(
                    "Cannot delete office because it is assigned to a doctor."
            );
        }

        officeRepository.delete(office);
    }

    public Object getBusiestOffice() {
    List<Object[]> rows = officeRepository.findBusiestOffice(PageRequest.of(0, 1));
    if (rows.isEmpty()) {
        return "No appointments yet";
    }
    return rows.get(0);
    }
    public List<Office> getFreeOffices() {
       return officeRepository.findFreeOffices();
    }
        public void deleteOfficeByNumber(String roomNumber) {
        Office office = officeRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() ->
                        new RuntimeException("Office not found with number: " + roomNumber)
                );

        deleteOffice(office.getId());
    }

    public List<Office> searchOffices(String keyword) {
    if (keyword == null || keyword.trim().isEmpty()) {
        return officeRepository.findAll();
    }
    return officeRepository
            .findByNameContainingIgnoreCaseOrRoomNumberContainingIgnoreCase(keyword.trim(), keyword.trim());
}
}