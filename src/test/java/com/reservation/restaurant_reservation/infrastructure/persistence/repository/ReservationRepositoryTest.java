// src/test/java/com/reservation/restaurant_reservation/infrastructure/persistence/repository/ReservationRepositoryTest.java
package com.reservation.restaurant_reservation.infrastructure.persistence.repository;

import com.reservation.restaurant_reservation.domain.entity.Reservation;
import com.reservation.restaurant_reservation.domain.entity.RestaurantTable;
import com.reservation.restaurant_reservation.domain.entity.User;
import com.reservation.restaurant_reservation.domain.enums.ReservationStatus;
import com.reservation.restaurant_reservation.domain.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest //H2'yi tetiklemeyi sağlar
@ActiveProfiles("test") // application-test.properties dosyasını okumasını sağlar
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findConflictingForUpdate_ShouldReturnConflicts_WhenTimeOverlaps() {
        // 1. Veritabanına Test Verisi Hazırla (H2 belleğine yazılıyor, PostgreSQL'e değil)
        User testUser = User.builder()
                .email("test@test.com").password("123").firstName("Ali").lastName("Veli").role(Role.CUSTOMER)
                .build();
        userRepository.save(testUser);

        RestaurantTable testTable = RestaurantTable.builder()
                .tableNumber("Masa-1").capacity(4).active(true)
                .build();
        tableRepository.save(testTable);

        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(20).withMinute(0);
        LocalDateTime endTime = startTime.plusHours(2); // 20:00 - 22:00 arası dolu

        Reservation reservation = Reservation.builder()
                .user(testUser).table(testTable)
                .startTime(startTime).endTime(endTime).guestCount(2).status(ReservationStatus.CONFIRMED)
                .build();
        reservationRepository.save(reservation);

        // 2. Metodu Test Et (Aynı saatte başka rezervasyon var mı?)
        List<Reservation> conflicts = reservationRepository.findConflictingForUpdate(
                testTable.getId(),
                startTime.plusMinutes(30), // örneğin 20:30'da (çakışan saat) deneme not:pesismistic_lock ile onu önlüyorduk
                endTime.plusMinutes(30),   // 22:30'a kadar
                ReservationStatus.CANCELLED
        );

        // 3. Doğrula
        assertEquals(1, conflicts.size(), "Çakışan bir rezervasyon bulunmalıydı!");
    }
}