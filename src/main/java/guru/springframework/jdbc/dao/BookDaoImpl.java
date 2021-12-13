package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

@Component
public class BookDaoImpl implements BookDao {
    private final EntityManagerFactory emf;

    public BookDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Book findBookByTitleCriteria(String title) {
        EntityManager em = getEntityManager();

        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);

            Root<Book> root = criteriaQuery.from(Book.class);

            ParameterExpression<String> titleParam = criteriaBuilder.parameter(String.class);


            Predicate titlePred = criteriaBuilder.equal(root.get("title"), titleParam);

            criteriaQuery.select(root).where(titlePred);

            TypedQuery<Book> typedQuery = em.createQuery(criteriaQuery);
            typedQuery.setParameter(titleParam, title);

            return typedQuery.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Book> findAll() {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<Book> query = em.createNamedQuery("book_find_all", Book.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Book findByISBN(String isbn) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
            query.setParameter("isbn", isbn);

            Book book = query.getSingleResult();

            return book;
        } finally {
            em.close();
        }


    }

    @Override
    public Book getById(Long id) {
        EntityManager em = getEntityManager();

        Book book = em.find(Book.class, id);
        em.close();
        return book;
    }

    @Override
    public Book findBookByTitle(String title) {
        EntityManager em = getEntityManager();
//        TypedQuery<Book> query = getEntityManager().createQuery("SELECT b from Book b WHERE b.title = " +
//                ":title", Book.class);
        TypedQuery<Book> query = em.createNamedQuery("find_by_title", Book.class);
        query.setParameter("title", title);

        Book book = query.getSingleResult();
        em.close();
        return book;
    }

    @Override
    public Book saveNewBook(Book book) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        em.persist(book);
        em.flush();
        em.getTransaction().commit();
        em.close();

        return book;
    }

    @Override
    public Book updateBook(Book book) {
        EntityManager em = getEntityManager();

        em.joinTransaction();
        em.merge(book);
        em.flush();
        em.clear();
        Book bookSave = em.find(Book.class, book.getId());
        em.getTransaction().commit();
        em.close();
        return bookSave;
    }

    @Override
    public void deleteBookById(Long id) {
        var em = getEntityManager();
        em.getTransaction().begin();
        Book book = em.find(Book.class, id);
        em.remove(book);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Book findBookByTitleNative(String title) {
        EntityManager em = getEntityManager();

        try {
            Query query = em.createNativeQuery("SELECT * FROM book b WHERE b.title = :title",
                    Book.class);
            query.setParameter("title", title);

            return (Book) query.getSingleResult();
        } finally {
            em.close();
        }
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
