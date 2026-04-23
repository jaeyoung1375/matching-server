package kr.co.teamo.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateUserRequest {
    private String name;
    private String password;
    private List<String> dtlCdIds;
}
