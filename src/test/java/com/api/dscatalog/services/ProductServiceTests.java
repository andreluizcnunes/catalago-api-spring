package com.api.dscatalog.services;

import com.api.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private Long existeId;
    private Long naoExiteID;

    @BeforeEach
    void setUp() throws Exception {
        existeId = 1L;
        naoExiteID = 1000L;

        Mockito.when(repository.existsById(existeId)).thenReturn(true);
        Mockito.when(repository.existsById(naoExiteID)).thenReturn(false);
    }

    @Test
    public void deleteNaoDeveFazerNadaQuandoOIdExistir(){

        Assertions.assertDoesNotThrow(() -> {
            service.deleteProducts(existeId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(existeId);
    }

}
