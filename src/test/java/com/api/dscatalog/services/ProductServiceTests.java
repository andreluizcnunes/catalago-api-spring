package com.api.dscatalog.services;

import com.api.dscatalog.dto.ProductDTO;
import com.api.dscatalog.entities.Product;
import com.api.dscatalog.factories.Factory;
import com.api.dscatalog.repositories.ProductRepository;
import com.api.dscatalog.services.exceptions.DatabaseException;
import com.api.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private Long existeId;
    private Long naoExiteID;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existeId = 1L;
        naoExiteID = 1000L;
        dependentId = 3L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existeId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(naoExiteID)).thenReturn(Optional.empty());


        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        Mockito.when(repository.existsById(existeId)).thenReturn(true);
        Mockito.when(repository.existsById(naoExiteID)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
    }

    @Test
    public void deleteNaoDeveFazerNadaQuandoOIdExistir(){

        Assertions.assertDoesNotThrow(() -> {
            service.deleteProducts(existeId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(existeId);
    }

    @Test
    public void deleteDeveLancarThrowResourceNotFoundExceptionQuandoIdNaoExistir(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteProducts(naoExiteID);
        });
    }

    @Test
    public void deleteDeveLancarDatabaseExceptionQuandoIdTemDependencia(){

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.deleteProducts(dependentId);
        });
    }

    @Test
    public void findAllPageDeveRetornarUmaPagina(){

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.getAllProductsPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);

    }

}
