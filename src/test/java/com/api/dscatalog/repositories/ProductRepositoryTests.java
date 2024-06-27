package com.api.dscatalog.repositories;

import com.api.dscatalog.entities.Product;
import com.api.dscatalog.factories.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private Long idExistente;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception{
        // Arrange
        idExistente = 1L;
        countTotalProducts = 25L;
    }

    @Test
    public void deveSalvarIdComAutoincrementoQuandoIdForNulo(){

        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void deveDeletarObjetoQuandoIdExistir(){
        // padrão AAA

        // Ação
        repository.deleteById(idExistente);

        Optional<Product> result = repository.findById(idExistente);

        Assertions.assertFalse(result.isPresent());

    }
}
