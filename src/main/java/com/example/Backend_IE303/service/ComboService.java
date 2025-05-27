package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.ComboItemProductDTO;
import com.example.Backend_IE303.dto.ItemProductDTO;
import com.example.Backend_IE303.entity.Combo;
import com.example.Backend_IE303.entity.ComboProduct;
import com.example.Backend_IE303.dto.ComboDTO;
import com.example.Backend_IE303.entity.Product;
import com.example.Backend_IE303.entity.EmbeddedId.ComboProductId;
import com.example.Backend_IE303.repository.ComboRepository;
import com.example.Backend_IE303.repository.ProductRepository;
import com.example.Backend_IE303.repository.ComboProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComboService {
    private final ComboRepository repository;
    private final ComboProductRepository comboProductRepository;
    private final ProductRepository productRepository;

    public ComboService(ComboRepository repository, ComboProductRepository comboProductRepository,
            ProductRepository productRepository) {
        this.repository = repository;
        this.comboProductRepository = comboProductRepository;
        this.productRepository = productRepository;
    }

    public List<List<Integer>> getAllComboList() {
        return repository.findAll().stream()
                .map(combo -> combo.getComboProducts().stream()
                        .map(cp -> cp.getProduct().getId())
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public Page<Combo> getAllCombo(Pageable pageable, Boolean isActive) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return repository.findAllByExpirationStatus(isActive, currentTime, sortedPageable);
    }

    @Transactional
    public Combo createCombo(ComboDTO comboDTO) {
        Combo combo = new Combo();
        combo.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        combo.setTimeEnd(comboDTO.getTimeEnd());

        List<ComboProduct> comboProducts = comboDTO.getComboProducts().stream()
                .map(cp -> {
                    Product product = productRepository.findById(cp.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with id: " + cp.getProductId()));

                    ComboProduct comboProduct = new ComboProduct();
                    comboProduct.setId(new ComboProductId(combo.getId(), product.getId()));
                    comboProduct.setCombo(combo);
                    comboProduct.setProduct(product);
                    comboProduct.setPrice(cp.getPrice());
                    comboProduct.setQuantity(cp.getQuantity());
                    return comboProduct;
                })
                .collect(Collectors.toList());

        combo.setComboProducts(comboProducts);
        return repository.save(combo);
    }

    public Boolean deleteCombo(Integer id) {
        Combo combo = repository.findById(id)
                .orElse(null);

        if (combo == null) {
            return false;
        }

        List<ComboProduct> comboProducts = comboProductRepository.findByComboId(id);
        comboProductRepository.deleteAll(comboProducts);

        repository.deleteById(id);
        return true;
    }

    @Transactional
    public Combo updateComboTimeEnd(Integer id, Timestamp newTimeEnd) {
        Combo combo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Combo not found with id: " + id));

        combo.setTimeEnd(newTimeEnd);
        return repository.save(combo);
    }

    @Transactional
    public List<ComboItemProductDTO> getAllComboProducts() {
        List<Combo> combos = repository.findAll();
        return combos.stream().map(combo -> {
            ComboItemProductDTO comboItemProductDTO1 = new ComboItemProductDTO();
            comboItemProductDTO1.setProducts(comboProductRepository.findByComboId(combo.getId()).stream().map(e -> {
                ItemProductDTO itemProductDTO = new ItemProductDTO();
                itemProductDTO.setId(e.getProduct().getId());
                itemProductDTO.setPrice(e.getPrice());
                itemProductDTO.setQuantity(e.getQuantity());
                return itemProductDTO;
            }).collect(Collectors.toList()));
            return comboItemProductDTO1;
        }).toList();
    }
}
