package com.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(value = {"hibernateLazyInitilaizer"}, ignoreUnknown = true)
public class RoleDTO {


    private Long id;
    private String description;

}

