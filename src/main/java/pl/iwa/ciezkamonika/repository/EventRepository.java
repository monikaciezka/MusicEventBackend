package pl.iwa.ciezkamonika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.iwa.ciezkamonika.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findById(long id);
    List<Event> findAllByBand(String band);
}
