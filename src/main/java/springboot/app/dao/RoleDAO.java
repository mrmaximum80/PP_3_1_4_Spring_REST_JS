package springboot.app.dao;

import springboot.app.model.Role;

import java.util.List;

public interface RoleDAO {

    List<Role> getAllRoles();

    Role getRole(long id);

    void saveRole(Role user);

    void deleteRole(long id);
}
