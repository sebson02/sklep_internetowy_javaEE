package controller;

import org.example.sklep_internetowy.controller.ProductViewController;
import org.example.sklep_internetowy.model.Product;
import org.example.sklep_internetowy.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductViewController.class)
class ProductViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product sampleProduct() {
        Product p = new Product("Laptop", "High-end", 2999.99, 5);
        p.setId(1L);
        return p;
    }

    @Test
    void shouldReturnProductListView() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(sampleProduct()));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/list"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("currentPage", 0));
    }

    @Test
    void shouldReturnProductDetailsViewWhenProductExists() throws Exception {
        when(productService.getProduct(1L)).thenReturn(sampleProduct());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/details"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void shouldRedirectToListWhenProductNotFoundOnDetails() throws Exception {
        when(productService.getProduct(99L)).thenReturn(null);

        mockMvc.perform(get("/products/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    void shouldReturnNewProductFormView() throws Exception {
        mockMvc.perform(get("/products/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/form"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("isNew", true));
    }

    @Test
    void shouldReturnEditProductFormViewWhenProductExists() throws Exception {
        when(productService.getProduct(1L)).thenReturn(sampleProduct());

        mockMvc.perform(get("/products/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/form"))
                .andExpect(model().attribute("isNew", false))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void shouldRedirectToListWhenEditingNonexistentProduct() throws Exception {
        when(productService.getProduct(123L)).thenReturn(null);

        mockMvc.perform(get("/products/123/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    void shouldSaveNewProductAndRedirect() throws Exception {
        mockMvc.perform(post("/products")
                        .param("name", "Phone")
                        .param("description", "Smartphone")
                        .param("price", "999.99")
                        .param("quantity", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productService).addProduct(any(Product.class));
    }

    @Test
    void shouldUpdateExistingProductAndRedirect() throws Exception {
        mockMvc.perform(post("/products")
                        .param("id", "1")
                        .param("name", "Updated Phone")
                        .param("description", "Smartphone")
                        .param("price", "1099.99")
                        .param("quantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productService).updateProduct(any(Product.class));
    }

    @Test
    void shouldDeleteProductAndRedirect() throws Exception {
        mockMvc.perform(post("/products/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productService).deleteProduct(1L);
    }
}

