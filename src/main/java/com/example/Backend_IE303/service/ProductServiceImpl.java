package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.ProductDTO;
import com.example.Backend_IE303.entity.Product;
import com.example.Backend_IE303.repository.CategoryRepository;
import com.example.Backend_IE303.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
    }

    // Lấy toàn bộ sản phẩm (không phân trang)
    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Lấy danh sách sản phẩm có phân trang
    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDTO::fromEntity);
    }

    @Override
    public ProductDTO getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("San pham voi" + id +" khong ton tại"));
        return ProductDTO.fromEntity(product);
    }


    @Override
    public String addProduct(Product product) {
        Optional<Product> existingProduct = productRepository.findByName(product.getName());
        if (existingProduct.isPresent()) {
            return "Sản phẩm đã tồn tại";
        }
        productRepository.save(product);

        return "Sản phẩm đã được thêm thành công";
    }
}
