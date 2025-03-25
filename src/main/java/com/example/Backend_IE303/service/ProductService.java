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


}
