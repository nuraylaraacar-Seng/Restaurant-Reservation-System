package com.reservation.restaurant_reservation.presentation.controller;

import com.reservation.restaurant_reservation.application.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // RestController kulandmadım, sadece Controller kullandım!
@RequiredArgsConstructor
public class TableWebController {

    private final TableService tableService;

    @GetMapping("/tables-dashboard")
    public String showTables(Model model) {
        // veriyi  Backend'den alıp "masaListesi" adıyla HTML'e yolluyoruz
        model.addAttribute("masaListesi", tableService.getAllTables());

        // Bu, templates klasöründeki tables.html dosyanın adıdır.
        return "tables";
    }
}