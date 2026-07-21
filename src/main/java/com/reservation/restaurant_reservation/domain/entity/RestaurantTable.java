package com.reservation.restaurant_reservation.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_tables")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTable extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String tableNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Column(length = 100)
    private String location;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "table")
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();


    public boolean canAcceptGuestCount(int count) {
        return this.active && this.capacity >= count;
    }
}