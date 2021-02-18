package efd.rest.repositories;
/*
    @Author david
    @Create 11/02/2021 09:48
*/

import efd.model.EfdRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<EfdRole,String> {
    public List<EfdRole> findDistinctByOxusersName(String userName);


}
