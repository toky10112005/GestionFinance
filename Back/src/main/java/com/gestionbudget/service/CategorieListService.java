package com.gestionbudget.service;

import com.gestionbudget.model.CategorieList;
import com.gestionbudget.repository.CategorieListRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieListService {
    private final CategorieListRepository categorieListRepository;

    public CategorieListService(CategorieListRepository categorieListRepository) {
        this.categorieListRepository = categorieListRepository;
    }

    public List<CategorieList> getAllCategories() {
        return categorieListRepository.findAll();
    }
}
