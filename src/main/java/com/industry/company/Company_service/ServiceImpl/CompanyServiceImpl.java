package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.Dto.CompanyDto;
import com.industry.company.Company_service.Entity.CompanyEntity;
import com.industry.company.Company_service.Repository.CompanyRepository;
import com.industry.company.Company_service.Service.CompanyService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;

    @Override
    public CompanyDto addCompany(CompanyDto companyDto) {
        log.info("Attempting to add company: {}", companyDto.getCompanyName());

        CompanyEntity company = modelMapper.map(companyDto, CompanyEntity.class);

        try {
            long count = companyRepository.count();
            log.debug("Total companies in DB before insert: {}", count);

            if (count == 0) {
                log.info("No companies found in DB. Saving first company: {}", company.getCompanyName());
                companyRepository.save(company);
            } else if (companyRepository.findByCompanyName(company.getCompanyName()).isPresent()) {
                log.warn("Company with name '{}' already exists. Skipping save.", company.getCompanyName());
            } else {
                log.info("Saving new company: {}", company.getCompanyName());
                companyRepository.save(company);
            }

            log.info("Company save process completed for: {}", company.getCompanyName());
            return modelMapper.map(company, CompanyDto.class);
        } catch (Exception e) {
            log.error("Error occurred while adding company: {}", companyDto.getCompanyName(), e);
            throw e;
        }
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        log.info("Fetching all companies from DB");
        try {
            List<CompanyEntity> companyList = companyRepository.findAll();
            log.debug("Number of companies retrieved: {}", companyList.size());

            List<CompanyDto> companyDtoList = companyList
                    .stream()
                    .map(company -> modelMapper.map(company, CompanyDto.class))
                    .toList();

            log.info("Returning {} companies", companyDtoList.size());
            return companyDtoList;
        } catch (Exception e) {
            log.error("Error fetching all companies", e);
            throw e;
        }
    }

    @Override
    public CompanyDto getCompanyByName(String name) {
        log.info("Fetching company by name: {}", name);
        try {
            CompanyEntity company = companyRepository
                    .findByCompanyName(name)
                    .orElseThrow(() -> {
                        log.warn("No company found with name: {}", name);
                        return new ResourceNotFoundException("Company Not Found With Name " + name);
                    });

            log.info("Company found: {}", company.getCompanyName());
            return modelMapper.map(company, CompanyDto.class);
        } catch (ResourceNotFoundException e) {
            throw e; // already logged above
        } catch (Exception e) {
            log.error("Error occurred while fetching company by name: {}", name, e);
            throw e;
        }
    }
}
