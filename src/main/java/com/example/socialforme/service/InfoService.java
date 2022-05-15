package com.example.socialforme.service;

import com.example.socialforme.model.Info;
import com.example.socialforme.repo.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoService {
    private final InfoRepository infoRepository;

    @Autowired
    public InfoService(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    public void save(Info info) {
        infoRepository.save(info);
    }
}
