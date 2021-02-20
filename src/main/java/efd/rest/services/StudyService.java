package efd.rest.services;
/*
    @Author david
    @Create 17/02/2021 13:38
*/

import efd.rest.dto.StudyDTO;

import java.util.List;


public interface StudyService {
    List<StudyDTO> findStudies();

    StudyDTO getStudyDto(String name);


}
