package org.example.library_db.controller;

import jakarta.validation.Valid;
import org.example.library_db.model.Reservation;
import org.example.library_db.model.ReservationStatus;
import org.example.library_db.service.LoanService;
import org.example.library_db.service.MemberService;
import org.example.library_db.service.ReadableItemService;
import org.example.library_db.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservations;
    private final MemberService members;
    private final ReadableItemService items;
    private final LoanService loans;

    public ReservationController(ReservationService reservations, MemberService members, ReadableItemService items, LoanService loans) {
        this.reservations = reservations;
        this.members = members;
        this.items = items;
        this.loans = loans;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("reservations", reservations.getAllReservations());
        return "reservations/index";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Reservation r = reservations.getReservationById(id);
        if (r == null) return "redirect:/reservations";

        model.addAttribute("reservation", r);
        return "reservations/details";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("members", members.getAllMembers());
        model.addAttribute("items", items.getAllReadableItems());
        model.addAttribute("loans", loans.getAllLoans());
        model.addAttribute("statuses", ReservationStatus.values());
        return "reservations/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("reservation") Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("members", members.getAllMembers());
            model.addAttribute("items", items.getAllReadableItems());
            model.addAttribute("loans", loans.getAllLoans());
            model.addAttribute("statuses", ReservationStatus.values());
            return "reservations/form";
        }

        reservations.addReservation(reservation);
        return "redirect:/reservations";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Reservation r = reservations.getReservationById(id);
        if (r == null) return "redirect:/reservations";

        model.addAttribute("reservation", r);
        model.addAttribute("members", members.getAllMembers());
        model.addAttribute("items", items.getAllReadableItems());
        model.addAttribute("loans", loans.getAllLoans());
        model.addAttribute("statuses", ReservationStatus.values());
        return "reservations/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("reservation") Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("members", members.getAllMembers());
            model.addAttribute("items", items.getAllReadableItems());
            model.addAttribute("loans", loans.getAllLoans());
            model.addAttribute("statuses", ReservationStatus.values());
            return "reservations/form";
        }

        reservations.updateReservation(id, reservation);
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        reservations.removeReservation(id);
        return "redirect:/reservations";
    }
}