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
        validateMember(member, null);
        return repository.save(member);
    }

    public void updateMember(Long id, Member update) {
        validateMember(update, id);
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

    private void validateMember(Member member, Long currentId) {
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty.");
        }
        if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Member email cannot be empty.");
        }
        if (member.getLibrary() == null || member.getLibrary().getId() == null) {
            throw new IllegalArgumentException("Member must belong to a library.");
        }

        boolean exists = (currentId == null)
                ? repository.existsByEmailIgnoreCase(member.getEmail())
                : repository.existsByEmailIgnoreCaseAndIdNot(member.getEmail(), currentId);

        if (exists) {
            throw new IllegalArgumentException("A member with this email already exists.");
        }
    }
}