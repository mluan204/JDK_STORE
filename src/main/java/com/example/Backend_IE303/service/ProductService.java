package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.ProductDTO;
import com.example.Backend_IE303.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    //Lấy sản phẩm không phân trang
    List<ProductDTO> getAllProducts();
    //Lấy sản phẩm có phân trang
    Page<ProductDTO> getAllProducts(Pageable pageable);  // Thêm phân trang

    //Lấy sản phẩm theo id
    ProductDTO getProductById(Integer id);

    //Thêm sản phẩm mới
    ProductDTO addProduct(Product product);

    ProductDTO updateProduct(Integer id, ProductDTO productDTO);

    void deleteProduct(Integer id);

    Page<ProductDTO> getProductsByCategoryId(Integer categoryId, Pageable pageable);

    Page<ProductDTO> searchProducts(String keyword, Integer categoryId, Pageable pageable);
}
