package com.api.dscatalog.services;

import com.api.dscatalog.dto.CategoryDTO;
import com.api.dscatalog.entities.Category;
import com.api.dscatalog.repositories.CategoryRepository;
import com.api.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        List<Category> result = categoryRepository.findAll();
        return result.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> obj = categoryRepository.findById(id);
        Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entidade não encontrada"));
        return new CategoryDTO(entity);
    }
}
