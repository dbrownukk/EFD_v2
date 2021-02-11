package efd.rest.repositories;
/*
    @Author david
    @Create 11/02/2021 09:48
*/

import com.openxava.naviox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByName(String name);
}
