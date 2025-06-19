package com.industry.company.Company_service.Service;


import com.industry.company.Company_service.Dto.CompanyDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CompanyService {

    CompanyDto addCompany(CompanyDto companyDto);

    List<CompanyDto> getAllCompanies();

    CompanyDto getCompanyByName(String name);
}
