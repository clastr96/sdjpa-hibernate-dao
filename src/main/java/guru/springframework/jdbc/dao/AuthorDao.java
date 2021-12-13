package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;

import java.util.List;

/**
 * Created by jt on 8/22/21.
 */
public interface AuthorDao {
    Author findAuthorByNameCriteria(String firstName, String lastName);

    List<Author> findAll();

    List<Author> listAuthorByNameLike(String lastName);

    Author getById(Long id);

    Author findAuthorByName(String firstName, String lastName);

    Author saveNewAuthor(Author author);

    Author updateAuthor(Author author);

    void deleteAuthorById(Long id);
}
