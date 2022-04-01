package study.dataspa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.dataspa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
