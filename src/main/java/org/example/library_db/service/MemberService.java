package org.example.library_db.service;

import org.example.library_db.model.Member;
import org.example.library_db.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public Member addMember(Member member) {
        return repository.save(member);
    }

    public void updateMember(Long id, Member update) {
        update.setId(id);
        repository.save(update);
    }

    public void removeMember(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    public Member getMemberById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Member> getAllMembers() {
        return repository.findAll();
    }

}