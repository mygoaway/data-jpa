package study.dataspa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.dataspa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
