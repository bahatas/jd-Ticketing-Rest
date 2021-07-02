package com.ticketing.converter;

import com.ticketing.dto.ProjectDTO;
import com.ticketing.service.ProjectServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ProjectDtoConverter implements Converter<String, ProjectDTO> {

    @Autowired
    ProjectServise projectServise;

    @Override
    public ProjectDTO convert(String source) {
        return projectServise.getByProjectCode(source);
    }


}
