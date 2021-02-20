package efd.rest.mapper;


import efd.model.Study;
import efd.rest.dto.StudyDTO;
import org.mapstruct.Mapper;

/*
    @Author david
    @Create 16/02/2021 15:52
*/
@Mapper(componentModel = "spring")

public interface StudyMapper {


    StudyDTO studyTostudynDto(Study study);

    Study studyDtoTostudy(StudyDTO studyDto);

}
