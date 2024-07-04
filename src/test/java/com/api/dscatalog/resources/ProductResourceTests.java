package com.api.dscatalog.resources;

import com.api.dscatalog.dto.ProductDTO;
import com.api.dscatalog.factories.Factory;
import com.api.dscatalog.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() throws Exception {

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(service.getAllProductsPaged(any())).thenReturn(page);
    }

    @Test
    public void getAllProductsDeveriaRetornarPagina() throws Exception {
        mockMvc.perform(get("/products")).andExpect(status().isOk());
    }
}
