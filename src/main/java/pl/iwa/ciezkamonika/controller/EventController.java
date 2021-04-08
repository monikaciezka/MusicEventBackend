package pl.iwa.ciezkamonika.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.iwa.ciezkamonika.model.Event;
import pl.iwa.ciezkamonika.model.User;
import pl.iwa.ciezkamonika.repository.EventRepository;
import pl.iwa.ciezkamonika.repository.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/event")
public class EventController {
    private EventRepository eventRepository;
    private UserRepository userRepository;

    @Autowired
    public EventController(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Event getEvent(@PathVariable("id") long id) {
        return eventRepository.findById(id);
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        System.out.println("Add event method called");
        eventRepository.save(event);
        return new ResponseEntity<Event>(HttpStatus.CREATED);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> deleteEvent(@PathVariable("id") long id) {
        Event event = eventRepository.findById(id);
        if (event == null) {
            System.out.println("Event not found");
            return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
        }
        eventRepository.deleteById(id);
        return new ResponseEntity<Event>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/band", method = RequestMethod.GET)
    public List<Event> getBandConcerts(@RequestBody String bandName) {
        return eventRepository.findAllByBand(bandName);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> updateEvent(@RequestBody Map<String, Object> updates, @PathVariable("id") long id){
        Event event = eventRepository.findById(id);
        if( event == null) {
            return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
        }
        try {
            partialUpdate(event, updates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Event>(HttpStatus.NO_CONTENT);

    }

    private void partialUpdate(Event event, Map<String, Object> updates) throws ParseException {
        if(updates.containsKey("description")) {
            event.setDescription( (String) updates.get("description") );
        }
        if(updates.containsKey("band")) {
            event.setBand((String) updates.get("band"));
        }
        if(updates.containsKey("city")) {
            event.setCity((String) updates.get("city"));
        }
        if(updates.containsKey("description")) {
            event.setDescription((String) updates.get("description"));
        }
        if(updates.containsKey("price")) {
            event.setPrice((double) updates.get("price"));
        }
        if(updates.containsKey("date")) {
            String dateString = (String) updates.get("date");
            Date dateOfEvent = new SimpleDateFormat("yyyy/MM/dd").parse(dateString);
            event.setDate(dateOfEvent);
        }
        eventRepository.save(event);
    }


}
