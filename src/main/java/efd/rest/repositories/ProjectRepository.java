package efd.rest.repositories;


import efd.rest.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jt on 9/24/17.
 */

public interface ProjectRepository extends JpaRepository<Project, String> {

    Project findByProjectid(String id);

}
