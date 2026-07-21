package com.reservation.restaurant_reservation.application.service;

import com.reservation.restaurant_reservation.application.dto.request.CreateTableRequest;
import com.reservation.restaurant_reservation.application.dto.request.UpdateTableRequest;
import com.reservation.restaurant_reservation.application.dto.response.TableResponse;
import com.reservation.restaurant_reservation.application.mapper.TableMapper;
import com.reservation.restaurant_reservation.domain.entity.RestaurantTable;
import com.reservation.restaurant_reservation.domain.exception.ResourceNotFoundException;
import com.reservation.restaurant_reservation.infrastructure.persistence.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final TableMapper tableMapper;

    // Masaları Listeleme (GET)
    public List<TableResponse> getAllTables() {
        List<RestaurantTable> tables = tableRepository.findAll();
        return tableMapper.toResponseList(tables);
    }

    // Yeni Masa Ekleme (POST)
    @Transactional
    public TableResponse createTable(CreateTableRequest request) {
        RestaurantTable table = RestaurantTable.builder()
                .tableNumber(request.getTableNumber())
                .capacity(request.getCapacity())
                .location(request.getLocation())
                .active(true)
                .build();

        return tableMapper.toResponse(tableRepository.save(table));
    }

    // Masa Güncelleme (PUT)
    @Transactional
    public TableResponse updateTable(Long id, UpdateTableRequest request) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Masa bulunamadı."));

        table.setCapacity(request.getCapacity());
        table.setLocation(request.getLocation());
        table.setActive(request.getActive());

        return tableMapper.toResponse(tableRepository.save(table));
    }

    // Masa Silme (DELETE)
    @Transactional
    public void deleteTable(Long id) {
        if (!tableRepository.existsById(id)) {
            throw new ResourceNotFoundException("Masa bulunamadı.");
        }
        tableRepository.deleteById(id);
    }
}