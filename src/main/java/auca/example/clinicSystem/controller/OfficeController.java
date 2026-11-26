package auca.example.clinicSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import auca.example.clinicSystem.model.Office;
import auca.example.clinicSystem.service.OfficeService;

@RestController
@RequestMapping("/api/offices")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    // CREATE
    // @PostMapping(value="/save")
    @PostMapping("/save")
    // @PostMapping
    public ResponseEntity<Office> createOffice(
            @RequestBody Office office
    ) {
        return new ResponseEntity<>(
                officeService.createOffice(office),
                HttpStatus.CREATED
        );
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Office>> getAllOffices() {
        return ResponseEntity.ok(
                officeService.getAllOffices()
        );
    }
    @GetMapping("/search")
    public ResponseEntity<List<Office>> searchOffices(@RequestParam(defaultValue = "") String keyword) {
       return ResponseEntity.ok(officeService.searchOffices(keyword));
}

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Office> getOfficeById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                officeService.getOfficeById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Office> updateOffice(
            @PathVariable Long id,
            @RequestBody Office office
    ) {
        return ResponseEntity.ok(
                officeService.updateOffice(id, office)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOffice(
            @PathVariable Long id
    ) {
        officeService.deleteOffice(id);

        return ResponseEntity.ok(
                "Office deleted successfully."
        );
    }

        @DeleteMapping("/deleteByNumber/{roomNumber}")
    public ResponseEntity<String> deleteOfficeByNumber(
            @PathVariable String roomNumber
    ) {
        officeService.deleteOfficeByNumber(roomNumber);

        return ResponseEntity.ok(
                "Office deleted successfully."
        );
    }

    @GetMapping("/busiest")
    public ResponseEntity<Object> getBusiestOffice() {
        return ResponseEntity.ok(officeService.getBusiestOffice());
    }

    @GetMapping("/free")
    public ResponseEntity<List<Office>> getFreeOffices() {
        return ResponseEntity.ok(officeService.getFreeOffices());
        
    }
}