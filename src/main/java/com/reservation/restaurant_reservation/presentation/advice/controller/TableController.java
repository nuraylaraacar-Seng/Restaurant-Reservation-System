package com.reservation.restaurant_reservation.presentation.advice.controller;

import com.reservation.restaurant_reservation.application.dto.request.CreateTableRequest;
import com.reservation.restaurant_reservation.application.dto.response.TableResponse;
import com.reservation.restaurant_reservation.application.service.TableService;
import com.reservation.restaurant_reservation.application.dto.request.UpdateTableRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping
    public TableResponse createTable(@Valid @RequestBody CreateTableRequest request) {
        return tableService.createTable(request);
    }

    @GetMapping
    public List<TableResponse> getAllTables() {
        return tableService.getAllTables();
    }

    @PutMapping("/{id}")
    public TableResponse updateTable(@PathVariable Long id, @Valid @RequestBody UpdateTableRequest request) {
        return tableService.updateTable(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Çıkış yapıldı.");
    }
}
