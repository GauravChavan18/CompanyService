package com.industry.company.Company_service.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.industry.company.Company_service.AuthEntity.AdminEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "companyTable")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;


    @Column(name = "company_name" ,nullable = false)
    public String companyName;

    @Column(name="address" ,nullable = false)
    public String address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    public List<EmployeeEntity> employee;

    @OneToMany(mappedBy = "company" , cascade = CascadeType.ALL)
    @JsonIgnore
    public List<AdminEntity> admin;

}
