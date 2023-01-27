package springboot.app.dao;

import springboot.app.model.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class RoleDAOimpl implements RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getAllRoles() {
        Query query = entityManager.createQuery("from Role");
        List<Role> roles = query.getResultList();
        return roles;
    }

    @Override
    public Role getRole(long id) {
        Role role = entityManager.find(Role.class, id);
        return role;
    }

    @Override
    public void saveRole(Role role) {
        if (role.getId() == 0) {
            entityManager.persist(role);
        } else {
            entityManager.merge(role);
        }
    }

    @Override
    public void deleteRole(long id) {
        Query query = entityManager.createQuery("delete from Role where id = :roleid");
        query.setParameter("roleid", id);
        query.executeUpdate();
    }
}
