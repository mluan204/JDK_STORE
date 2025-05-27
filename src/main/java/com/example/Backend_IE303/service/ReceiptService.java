package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.ReceiptDTO;
import com.example.Backend_IE303.dto.ReceiptDetailDTO;
import com.example.Backend_IE303.dto.ReceiptDetailResponseDTO;
import com.example.Backend_IE303.entity.EmbeddedId.ReceiptProId;
import com.example.Backend_IE303.entity.Employee;
import com.example.Backend_IE303.entity.Product;
import com.example.Backend_IE303.entity.Receipt;
import com.example.Backend_IE303.entity.ReceiptDetail;
import com.example.Backend_IE303.repository.EmployeeRepository;
import com.example.Backend_IE303.repository.ProductRepository;
import com.example.Backend_IE303.repository.ReceiptDetailRepository;
import com.example.Backend_IE303.repository.ReceiptRepository;
import io.jsonwebtoken.impl.security.EdwardsCurve;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;

    public List<ReceiptDTO> getAllReceipts() {
        List<Receipt> receipts = receiptRepository.findAll();
        return receipts.stream()
                .map(receipt -> new ReceiptDTO(
                        receipt.getId(),
                        receipt.getCreated_at(),
                        receipt.getTotal_cost(),
                        receipt.getNote(),
                        receipt.getEmployee() != null ? receipt.getEmployee().getName() : "N/A"
                ))
                .collect(Collectors.toList());
    }

    public Page<ReceiptDetailResponseDTO> getReceiptsPaginated(int page, int size) {
        Page<Receipt> receiptsPage = receiptRepository.findAll(PageRequest.of(page, size));

        return receiptsPage.map(receipt -> {
            List<ReceiptDetailDTO> detailDTOs = receipt.getReceiptDetails().stream().map(detail -> {
                ReceiptDetailDTO dto = new ReceiptDetailDTO();
                dto.setProductId(detail.getProduct() != null ? detail.getProduct().getId() : null);
                dto.setProductName(detail.getProduct() != null ? detail.getProduct().getName() : null);
                dto.setSupplier(detail.getSupplier());
                dto.setQuantity(detail.getQuantity());
                dto.setInput_price(detail.getInputPrice());
                dto.setCheck(detail.isCheck());
                return dto;
            }).collect(Collectors.toList());


            ReceiptDetailResponseDTO dto = new ReceiptDetailResponseDTO();
            dto.setId(receipt.getId());
            dto.setCreated_at(receipt.getCreated_at());
            dto.setTotal_cost(receipt.getTotal_cost());
            dto.setNote(receipt.getNote());
            dto.setEmployee_name(receipt.getEmployee() != null ? receipt.getEmployee().getName() : "N/A");
            dto.setReceipt_details(detailDTOs);

            return dto;
        });
    }




    public ReceiptDTO getReceiptById(Integer id) {
        Optional<Receipt> receiptOptional = receiptRepository.findById(id);
        if (receiptOptional.isEmpty()) {
            return null; // Xử lý lỗi ở controller
        }
        Receipt receipt = receiptOptional.get();
        return new ReceiptDTO(
                receipt.getId(),
                receipt.getCreated_at(),
                receipt.getTotal_cost(),
                receipt.getNote(),
                receipt.getEmployee() != null ? receipt.getEmployee().getName() : "N/A"
        );
    }


    public ReceiptDetailResponseDTO getReceiptDetailsById(Integer id) {
        Optional<Receipt> receiptOptional = receiptRepository.findById(id);
        if (receiptOptional.isEmpty()) {
            return null; // Xử lý lỗi ở controller
        }

        Receipt receipt = receiptOptional.get();

        // Chuyển danh sách ReceiptDetail thành ReceiptDetailDTO
        List<ReceiptDetailDTO> receiptDetails = receipt.getReceiptDetails().stream()
                .map(detail -> new ReceiptDetailDTO(
                        detail.getProduct().getId(),
                        detail.getProduct().getName(),
                        detail.getQuantity(),
                        detail.getInput_price()
                ))
                .collect(Collectors.toList());

        return new ReceiptDetailResponseDTO(
                receipt.getId(),
                receipt.getCreated_at(),
                receipt.getTotal_cost(),
                receipt.getNote(),
                receipt.getEmployee() != null ? receipt.getEmployee().getName() : "N/A",
                receiptDetails
        );
    }

    @Transactional
    public boolean deleteReceiptById(Integer id) {
        Optional<Receipt> optionalReceipt = receiptRepository.findById(id);
        if (optionalReceipt.isEmpty()) {
            return false;  // hoặc có thể ném exception tùy thiết kế
        }

        Receipt receipt = optionalReceipt.get();

        if (receipt.getReceiptDetails() != null) {
            receiptDetailRepository.deleteAll(receipt.getReceiptDetails());
        }

        receiptRepository.delete(receipt);
        return true;
    }


    public void addReceipt(ReceiptDTO dto) {
        Receipt receipt = new Receipt();
        receipt.setCreated_at(dto.getCreated_at());
        receipt.setTotal_cost(dto.getTotal_cost());
        receipt.setNote(dto.getNote());

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        receipt.setEmployee(employee);

        List<ReceiptDetail> details = new ArrayList<>();

        for (ReceiptDetailDTO detailDTO : dto.receiptDetails) {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

            // Cập nhật giá nhập trung bình có trọng số và số lượng tồn kho
            int currentQuantity = product.getQuantity_available();
            int currentInputPrice = product.getInput_price();

            int newQuantity = detailDTO.getQuantity();
            int newInputPrice = detailDTO.getInput_price();

            // Tính giá nhập mới theo công thức trung bình có trọng số
            int updatedInputPrice = 0;
            if (currentQuantity + newQuantity > 0) {
                updatedInputPrice = (currentInputPrice * currentQuantity + newInputPrice * newQuantity) / (currentQuantity + newQuantity);
            }

            int updatedQuantity = currentQuantity + newQuantity;

            product.setInput_price(updatedInputPrice);
            product.setQuantity_available(updatedQuantity);

            // Lưu cập nhật product ngay
            productRepository.save(product);

            ReceiptDetail detail = new ReceiptDetail();
            detail.setProduct(product);
            detail.setReceipt(receipt);
            detail.setSupplier(detailDTO.getSupplier());
            detail.setQuantity(newQuantity);
            detail.setInput_price(newInputPrice);
            detail.setCheck(detailDTO.isCheck());

            // Tạo composite key
            ReceiptProId id = new ReceiptProId();
            detail.setId(id);

            details.add(detail);
        }

        receipt.setReceiptDetails(details);
        receiptRepository.save(receipt);
    }

    public Page<ReceiptDetailResponseDTO> getReceiptsPaginatedFiltered(int page, int size, LocalDate fromDate, LocalDate toDate, String employeeName) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Receipt> spec = Specification.where(null);

        // Lọc theo ngày tạo (nếu có)
        if (fromDate != null || toDate != null) {
            spec = spec.and(ReceiptSpecification.hasCreatedDateBetween(fromDate, toDate));
        }

        // Lọc theo tên nhân viên (nếu có)
        if (employeeName != null && !employeeName.trim().isEmpty()) {
            spec = spec.and(ReceiptSpecification.hasEmployeeName(employeeName));
        }

        Page<Receipt> receiptsPage = receiptRepository.findAll(spec, pageable);

        return receiptsPage.map(receipt -> {
            List<ReceiptDetailDTO> detailDTOs = receipt.getReceiptDetails().stream().map(detail -> {
                ReceiptDetailDTO dto = new ReceiptDetailDTO();
                dto.setProductId(detail.getProduct() != null ? detail.getProduct().getId() : null);
                dto.setProductName(detail.getProduct() != null ? detail.getProduct().getName() : null);
                dto.setSupplier(detail.getSupplier());
                dto.setQuantity(detail.getQuantity());
                dto.setInput_price(detail.getInputPrice());
                dto.setCheck(detail.isCheck());
                return dto;
            }).collect(Collectors.toList());

            ReceiptDetailResponseDTO dto = new ReceiptDetailResponseDTO();
            dto.setId(receipt.getId());
            dto.setCreated_at(receipt.getCreated_at());
            dto.setTotal_cost(receipt.getTotal_cost());
            dto.setNote(receipt.getNote());
            dto.setEmployee_name(receipt.getEmployee() != null ? receipt.getEmployee().getName() : "N/A");
            dto.setReceipt_details(detailDTOs);

            return dto;
        });
    }


    }
