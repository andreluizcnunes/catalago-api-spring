package com.api.dscatalog.resources;

import com.api.dscatalog.dto.ProductDTO;
import com.api.dscatalog.factories.Factory;
import com.api.dscatalog.services.ProductService;
import com.api.dscatalog.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper mapper;

    ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws Exception {

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        nonExistingId = 2L;

        when(service.getAllProductsPaged(any())).thenReturn(page);

        when(service.getProductsById(existingId)).thenReturn(productDTO);
        when(service.getProductsById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(service.updateProducts(eq(existingId), any())).thenReturn(productDTO);
        when(service.updateProducts(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

    }

//    Modelo 1 -  menos didatico
//    @Test
//    public void getAllProductsDeveriaRetornarPagina() throws Exception {
//        mockMvc.perform(get("/products")).andExpect(status().isOk());
//    }

//    Modelo 2
//    @Test
//    public void getAllProductsDeveriaRetornarPagina() throws Exception {
//         mockMvc.perform(get("/products"))
//                 .andExpect(status()
//                         .isOk());
//    }

//    Modelo 3
    @Test
    public void getAllProductsDeveriaRetornarPagina() throws Exception {
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
    }

    @Test
    public void getProductByIdDeveRetornarProductQuandoIdExiste() throws Exception {

        ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.price").exists());

    }

    @Test
    public void getProductByIdDeveRetornarNotFoundQuandoIdNaoExiste() throws Exception {

        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());

    }

    @Test
    public void updateDeveAtualizarProductQuandoIdExiste() throws Exception {

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.price").exists());
    }

    @Test
    public void updateDeveRetornarNotFoundQuandoIdNaoExiste() throws Exception {

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }


}
