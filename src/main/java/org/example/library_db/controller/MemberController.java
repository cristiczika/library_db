package org.example.library_db.controller;

import jakarta.validation.Valid;
import org.example.library_db.model.Member;
import org.example.library_db.service.LibraryService;
import org.example.library_db.service.LoanService;
import org.example.library_db.service.MemberService;
import org.example.library_db.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService members;
    private final LibraryService libraries;

    public MemberController(MemberService members, LibraryService libraries) {
        this.members = members;
        this.libraries = libraries;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String library,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model
    ) {
        model.addAttribute("members",
                members.filter(name, email, library, sort, dir));

        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("library", library);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        return "members/index";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        Member m = members.getMemberById(id);
        if (m == null) return "redirect:/members";

        model.addAttribute("member", m);
        model.addAttribute("loans", members.getLoansByMemberId(id));
        model.addAttribute("reservations", members.getReservationsByMemberId(id));
        return "members/details";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("libraries", libraries.getAllLibraries());
        return "members/form";
    }

    @PostMapping
    public String add(@Valid @ModelAttribute("member") Member member,
                      BindingResult result,
                      Model model) {

        if (result.hasErrors()) {
            model.addAttribute("libraries", libraries.getAllLibraries());
            return "members/form";
        }

        members.addMember(member);
        return "redirect:/members";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Member m = members.getMemberById(id);
        if (m == null) return "redirect:/members";

        model.addAttribute("member", m);
        model.addAttribute("libraries", libraries.getAllLibraries());
        return "members/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("member") Member member, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("libraries", libraries.getAllLibraries());
            return "members/form";
        }

        members.updateMember(id, member);
        return "redirect:/members";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        members.removeMember(id);
        return "redirect:/members";
    }
}