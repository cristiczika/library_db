package org.example.library_db.controller;

import org.example.library_db.model.Loan;
import org.example.library_db.model.Member;
import org.example.library_db.model.ReadableItem;
import org.example.library_db.service.LoanService;
import org.example.library_db.service.MemberService;
import org.example.library_db.service.ReadableItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loans;
    private final MemberService members;
    private final ReadableItemService items;

    public LoanController(LoanService loans, MemberService members, ReadableItemService items) {
        this.loans = loans;
        this.members = members;
        this.items = items;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String member,
            @RequestParam(required = false) String barcode,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model
    ) {
        model.addAttribute("loans",
                loans.filter(member, barcode, date, sort, dir));

        model.addAttribute("member", member);
        model.addAttribute("barcode", barcode);
        model.addAttribute("date", date);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        return "loans/index";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Loan loan = loans.getLoanById(id);
        if (loan == null) return "redirect:/loans";

        model.addAttribute("loan", loan);
        model.addAttribute("member", loan.getMember());
        model.addAttribute("items", loan.getItems());
        model.addAttribute("reservations", loan.getReservations());
        return "loans/details";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("loan", new Loan());
        model.addAttribute("members", members.getAllMembers());
        model.addAttribute("items", items.getAllReadableItems());
        return "loans/form";
    }

    @PostMapping
    public String create(@RequestParam Long memberId,
                         @RequestParam(required = false) List<Long> itemIds,
                         Model model) {

        Member member = members.getMemberById(memberId);
        if (member == null) {
            model.addAttribute("error", "Invalid member");
            return "loans/form";
        }

        Loan loan = new Loan(member, LocalDate.now());

        if (itemIds != null) {
            for (Long itemId : itemIds) {
                ReadableItem it = items.getReadableItemById(itemId);
                if (it != null) loan.addItem(it);
            }
        }

        loans.addLoan(loan);
        return "redirect:/loans";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Loan loan = loans.getLoanById(id);
        if (loan == null) return "redirect:/loans";

        model.addAttribute("loan", loan);
        model.addAttribute("members", members.getAllMembers());
        model.addAttribute("items", items.getAllReadableItems());
        return "loans/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam Long memberId,
                         @RequestParam(required = false) List<Long> itemIds,
                         Model model) {

        Member member = members.getMemberById(memberId);
        if (member == null) {
            model.addAttribute("error", "Invalid member");
            return "loans/form";
        }

        loans.update(id, member, itemIds);
        return "redirect:/loans";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        loans.removeLoan(id);
        return "redirect:/loans";
    }
}