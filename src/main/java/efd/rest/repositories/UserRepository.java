package efd.rest.repositories;
/*
    @Author david
    @Create 11/02/2021 09:48
*/

import efd.model.EfdUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;
@RestResource(exported = false)
public interface UserRepository extends JpaRepository<EfdUser,String> {
    Optional<EfdUser> findByName(String name);
}
