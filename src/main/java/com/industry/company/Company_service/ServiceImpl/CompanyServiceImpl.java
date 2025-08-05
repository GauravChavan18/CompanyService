package com.industry.company.Company_service.ServiceImpl;

import com.industry.company.Company_service.Dto.CompanyDto;
import com.industry.company.Company_service.Entity.CompanyEntity;
import com.industry.company.Company_service.Repository.CompanyRepository;
import com.industry.company.Company_service.Service.CompanyService;
import com.industry.company.Company_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;

    @Override
    public CompanyDto addCompany(CompanyDto companyDto) {

        CompanyEntity company = modelMapper.map(companyDto , CompanyEntity.class);


        if(companyRepository.findAll().stream().toList().size()==0)
        {
            companyRepository.save(company);
        }
        else if(companyRepository.findByCompanyName(company.getCompanyName()).stream().toList().size()==1)
        {

        }
        else{
            companyRepository.save(company);
        }

        return modelMapper.map(company ,CompanyDto.class);
    }

    @Override
    public List<CompanyDto> getAllCompanies() {

        List<CompanyEntity> companyList = companyRepository.findAll();

        List<CompanyDto> companyDtoList=companyList
                .stream()
                .map(company ->modelMapper.map(company,CompanyDto.class))
                .toList();

        return companyDtoList;

    }

    @Override
    public CompanyDto getCompanyByName(String name)
    {

        CompanyEntity company =companyRepository
                            .findByCompanyName(name)
                            .orElseThrow(()-> new ResourceNotFoundException("Company Not Found With Name "+ name));

        return modelMapper.map(company,CompanyDto.class);
    }
}
