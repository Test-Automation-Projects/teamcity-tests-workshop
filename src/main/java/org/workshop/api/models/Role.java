package org.workshop.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Role {
    private String roleId;
    private String scope;
    private String href;
}
