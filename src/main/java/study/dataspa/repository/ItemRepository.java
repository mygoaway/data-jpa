package study.dataspa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.dataspa.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
