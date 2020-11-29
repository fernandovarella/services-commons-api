package com.fernando.services.commons.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class ApiError {

    private Integer code;
    private Integer status;
    private String type;
    private String title;
    private String detail;
    private String trace;
    private String path;
    private List<String> subErrors;
    
}
