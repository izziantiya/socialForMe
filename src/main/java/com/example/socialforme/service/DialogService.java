package com.example.socialforme.service;

import com.example.socialforme.model.Dialog;
import com.example.socialforme.repo.DialogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DialogService {
    private final DialogRepository dialogRepository;

    @Autowired
    public DialogService(DialogRepository dialogRepository) {
        this.dialogRepository = dialogRepository;
    }

    public Dialog getById(Long id) {
        return dialogRepository.findById(id).orElse(null);
    }

    public void save(Dialog dialog) {
        dialogRepository.save(dialog);
    }
}
