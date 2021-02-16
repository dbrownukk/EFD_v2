package efd.rest.repositories;


import efd.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by jt on 9/24/17.
 */
@RestResource(exported = false)
public interface ProjectRepository extends JpaRepository<Project, String> {

    Project findByProjectid(String id);

}
