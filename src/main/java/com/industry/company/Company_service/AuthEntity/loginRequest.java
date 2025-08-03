package com.industry.company.Company_service.AuthEntity;


import lombok.Data;

@Data
public class loginRequest {

    private String email;
    private String password;
    private String newPassword;

}
