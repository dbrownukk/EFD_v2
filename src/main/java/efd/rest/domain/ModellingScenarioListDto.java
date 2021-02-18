package efd.rest.domain;
/*
    @Author david
    @Create 17/02/2021 16:38
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModellingScenarioListDto {

    List<ModellingScenarioDto> modellingScenarioDtos;

}
