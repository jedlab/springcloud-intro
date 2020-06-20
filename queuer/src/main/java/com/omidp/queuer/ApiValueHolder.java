package com.omidp.queuer;

import java.io.Serializable;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiValueHolder implements Serializable
{
    /**
     * user-service
     */
    private String serviceName;

    /**
     * json request body
     */
    @JsonRawValue
    private String payload;

    /**
     * <p>
     * /v1/api/test
     * </p>
     */
    private String path;

    private HttpMethod method;

}
