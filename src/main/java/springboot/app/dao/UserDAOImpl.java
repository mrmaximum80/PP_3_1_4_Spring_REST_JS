package springboot.app.dao;

import springboot.app.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        Query query = entityManager.createQuery("from User");
        List<User> users = query.getResultList();
        return users;
    }

    @Override
    public User getUser(long id) {
        User user = entityManager.find(User.class, id);
        return user;
    }

    @Override
    public void saveUser(User user) {
        if (user.getId() == 0) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }

    @Override
    public void deleteUser(long id) {
        Query query = entityManager.createQuery("delete from User where id = :userid");
        query.setParameter("userid", id);
        query.executeUpdate();
    }

    @Override
    public User getUserByUsername(String username) {
        Query query = entityManager.createQuery("from User where username = :userlogin");
        query.setParameter("userlogin", username);
        if (!query.getResultList().isEmpty()) {
            return (User) query.getSingleResult();
        }
        return null;
    }

}
