package efd.rest.repositories;
/*
    @Author david
    @Create 11/02/2021 09:48
*/

import efd.model.EfdRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
@RestResource(exported = false)
public interface RoleRepository extends JpaRepository<EfdRole,String> {
    public List<EfdRole> findDistinctByOxusersName(String userName);


}
