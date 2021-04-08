package pl.iwa.ciezkamonika.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.iwa.ciezkamonika.model.Comment;
import pl.iwa.ciezkamonika.model.Event;
import pl.iwa.ciezkamonika.repository.CommentRepository;
import pl.iwa.ciezkamonika.repository.EventRepository;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class CommentController {
    private CommentRepository commentRepository;
    private EventRepository eventRepository;

    public CommentController(EventRepository eventRepository, CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
    }

    @RequestMapping(value = "event/{id}/comments", method = RequestMethod.GET)
    public List<Comment> getComments(@PathVariable("id") long id) {
        Event event = eventRepository.findById(id);
        return event.getComments();
    }

    @RequestMapping(value = "event/{id}/comments", method = RequestMethod.POST)
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment, @PathVariable("id") long id) {
        Event event = eventRepository.findById(id);
        commentRepository.save(comment);
        event.addComment(comment);
        eventRepository.save(event);
        return new ResponseEntity<Comment>(comment, HttpStatus.CREATED);
    }



    @RequestMapping(value = "event/{id}/comments/{comment_id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Comment> deleteComment(@PathVariable("comment_id") long comment_id, @PathVariable("id") long id) {
        System.out.println("comment to delete");
        Comment comment = commentRepository.findById(comment_id);
        if(comment == null) {
            System.out.println("Comment does not exists");
            return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
        }
        Event event = eventRepository.findById(id);
        event.getComments().remove(comment);
        eventRepository.save(event);
        commentRepository.deleteById(comment_id);
        return new ResponseEntity<Comment>(HttpStatus.NO_CONTENT);
    }
}
