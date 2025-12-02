package org.example.library_db.controller;

import jakarta.validation.Valid;
import org.example.library_db.model.MagazineDetails;
import org.example.library_db.service.MagazineDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/magazines")
public class MagazineDetailsController {

    private final MagazineDetailsService magazines;

    public MagazineDetailsController(MagazineDetailsService magazines) {
        this.magazines = magazines;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("magazines", magazines.getAllMagazineDetails());
        return "magazines/index";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        MagazineDetails m = magazines.getMagazineDetailsById(id);
        if (m == null)
            return "redirect:/magazines";

        model.addAttribute("magazine", m);
        return "magazines/details";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("magazine", new MagazineDetails());
        return "magazines/form";
    }

    @PostMapping
    public String add(@Valid @ModelAttribute("magazine") MagazineDetails magazine, BindingResult result) {
        if (result.hasErrors()) {
            return "magazines/form";
        }

        magazines.addMagazineDetails(magazine);
        return "redirect:/magazines";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        MagazineDetails m = magazines.getMagazineDetailsById(id);
        if (m == null)
            return "redirect:/magazines";

        model.addAttribute("magazine", m);
        return "magazines/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("magazine") MagazineDetails magazine, BindingResult result) {
        if (result.hasErrors()) {
            return "magazines/form";
        }

        magazines.updateMagazineDetails(id, magazine);
        return "redirect:/magazines";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        magazines.removeMagazineDetails(id);
        return "redirect:/magazines";
    }
}