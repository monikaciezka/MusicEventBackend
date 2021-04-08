package pl.iwa.ciezkamonika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iwa.ciezkamonika.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findById(long id);
}
