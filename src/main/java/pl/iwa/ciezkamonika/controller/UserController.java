package pl.iwa.ciezkamonika.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import pl.iwa.ciezkamonika.model.Event;
import pl.iwa.ciezkamonika.model.User;
import pl.iwa.ciezkamonika.repository.EventRepository;
import pl.iwa.ciezkamonika.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping(value = "/user")
public class UserController {

    private UserRepository userRepository;
    private EventRepository eventRepository;
    @Autowired
    public UserController(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }
//find individual users by id
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User getUser(@PathVariable String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username: " + username));

    }
//get list of all users
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

//get events of user
    @RequestMapping(value = "/event/{username}", method = RequestMethod.GET)
    public List<Event> getUserEvents(@PathVariable("username") String username) {
        System.out.println("method entered");
        User user = this.userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username: " + username));
        return user.getEventList();
    }

//add new user
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity<User>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        User user = userRepository.findById(id);
        if(user == null){
            System.out.println("Not found a user");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        userRepository.delete(user);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

    /* method that lets user add event to favourites */
    @RequestMapping(value = "/event/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addEventToFav(@PathVariable("id") long id, @RequestBody String username){
        Event event = eventRepository.findById(id);
        System.out.println(event.getBand());
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username: " + username));
        System.out.println(user.getName());

        if(!user.getEventList().contains(event)) {
            user.getEventList().add(event);
            userRepository.save(user);
            System.out.println("Event added to favourite");
        }

        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }


    //delete event from favorites
    @RequestMapping(value = "/event/{event_id}/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEventFav( @PathVariable("event_id") long event_id, @PathVariable("username") String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username: " + username));
        System.out.println(user.getName());
        Event event = eventRepository.findById(event_id);
        System.out.println(event.getBand());
        if(user.getEventList().contains(event)){
            System.out.println("Event is in faves of the user");
            user.getEventList().remove(event);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
