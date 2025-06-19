package com.industry.company.Company_service;

import com.industry.company.Company_service.Dto.CompanyDto;
import com.industry.company.Company_service.Repository.CompanyRepository;
import com.industry.company.Company_service.ServiceImpl.ComplanyServiceImpl;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ComplanyServiceTest {

    @Autowired
    private ComplanyServiceImpl companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @AfterEach
    public void cleanup() {
        companyRepository.deleteAll();
    }

    @Test
    public void testAddAndGetCompanyByName() {
        CompanyDto dto = new CompanyDto();
        dto.setCompanyName("TestCo");
        dto.setAddress("123 Test Street");

        // Add company
        CompanyDto savedDto = companyService.addCompany(dto);
        assertNotNull(savedDto);
        assertEquals("TestCo", savedDto.getCompanyName());

        // Get company by name
        CompanyDto fetchedDto = companyService.getCompanyByName("TestCo");
        assertNotNull(fetchedDto);
        assertEquals("TestCo", fetchedDto.getCompanyName());
        assertEquals("123 Test Street", fetchedDto.getAddress());
    }

    @Test
    public void testGetCompanyByName_NotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            companyService.getCompanyByName("UnknownCo");
        });
    }
}
