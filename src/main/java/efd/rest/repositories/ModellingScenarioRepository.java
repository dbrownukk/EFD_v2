package efd.rest.repositories;
/*
    @Author david
    @Create 16/02/2021 17:41
*/

import efd.rest.domain.ModellingScenarioDto;
import org.springframework.data.repository.CrudRepository;

public interface ModellingScenarioRepository extends CrudRepository<ModellingScenarioDto,String > {
}
