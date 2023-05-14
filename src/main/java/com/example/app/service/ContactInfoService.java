package com.example.app.service;

import com.example.app.model.ContactInfo;
import com.example.app.repository.ContactInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;

    public ContactInfo getById(Long id){
        return contactInfoRepository.findContactInfoById(id);
    }
}
