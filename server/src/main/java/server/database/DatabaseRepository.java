package server.database;

import commons.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseRepository extends JpaRepository<Person, Long> {
}
