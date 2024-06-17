package com.api.dscatalog.services;

import com.api.dscatalog.dto.ProductDTO;
import com.api.dscatalog.entities.Product;
import com.api.dscatalog.repositories.ProductRepository;
import com.api.dscatalog.services.exceptions.DatabaseException;
import com.api.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        List<Product> result = productRepository.findAll();
        return result.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProductsPaged(PageRequest pageRequest) {
        Page<Product> result = productRepository.findAll(pageRequest);
        return result.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductsById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO createProducts(ProductDTO dto) {
        Product entity = new Product();
//        entity.setName(dto.getName());
        entity = productRepository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO updateProducts(Long id, ProductDTO dto) {
        try {
            Product entity = productRepository.getReferenceById(id);
//            entity.setName(dto.getName());

            entity = productRepository.save(entity);

            return new ProductDTO(entity);

        }catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Product not found for id: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteProducts(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        try {
            productRepository.deleteById(id);

        }catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
}
