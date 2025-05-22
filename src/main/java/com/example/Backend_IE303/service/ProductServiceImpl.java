package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.ProductDTO;
import com.example.Backend_IE303.entity.Category;
import com.example.Backend_IE303.entity.Product;
import com.example.Backend_IE303.exceptions.CustomException;
import com.example.Backend_IE303.repository.CategoryRepository;
import com.example.Backend_IE303.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private  final  CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, CategoryRepository categoryRepository1) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository1;
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
    public ProductDTO addProduct(Product product) {
        Optional<Product> existingProduct = productRepository.findByName(product.getName());
        if (existingProduct.isPresent()) {
            throw new CustomException(101, "Sản phẩm đã tồn tại");
        }
        return ProductDTO.fromEntity(productRepository.save(product)) ;
    }

    @Override
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setSuppliers(productDTO.getSuppliers());
        product.setQuantity_available(productDTO.getQuantityAvailable());
        product.setDate_expired(productDTO.getDateExpired());
        product.setSale_price(productDTO.getSalePrice());
        product.setInput_price(productDTO.getInputPrice());
        product.setPrice(productDTO.getPrice());

        Category category = (Category) categoryRepository.findByName(productDTO.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với tên: " + productDTO.getCategoryName()));
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return ProductDTO.fromEntity(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
        productRepository.delete(product);
    }

    @Override
    public Page<ProductDTO> getProductsByCategoryId(Integer categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(ProductDTO::fromEntity);
    }

    @Override
    public Page<ProductDTO> searchProducts(String keyword, Integer categoryId, Pageable pageable) {
        // Nếu keyword null hoặc rỗng thì gán thành chuỗi rỗng
        if (keyword == null) {
            keyword = "";
        }

        if (categoryId != null) {
            // Tìm theo cả keyword và categoryId
            return productRepository.findByNameContainingIgnoreCaseAndCategoryId(keyword, categoryId, pageable)
                    .map(ProductDTO::fromEntity);
        } else {
            // Tìm theo keyword thôi
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable)
                    .map(ProductDTO::fromEntity);
        }
    }
    




}
