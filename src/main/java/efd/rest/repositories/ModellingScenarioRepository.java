package efd.rest.repositories;
/*
    @Author david
    @Create 16/02/2021 17:41
*/

import efd.model.ModellingScenario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModellingScenarioRepository extends JpaRepository<ModellingScenario,String > {
    ModellingScenario findByTitle(String title);
    List<ModellingScenario> findAll();
}
