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
import com.example.Backend_IE303.repository.ReceiptRepository;
import io.jsonwebtoken.impl.security.EdwardsCurve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public Page<ReceiptDTO> getReceiptsPaginated(int page, int size) {
        Page<Receipt> receiptsPage = receiptRepository.findAll(PageRequest.of(page, size));

        return receiptsPage.map(receipt -> new ReceiptDTO(
                receipt.getId(),
                receipt.getCreated_at(),
                receipt.getTotal_cost(),
                receipt.getNote(),
                receipt.getEmployee() != null ? receipt.getEmployee().getName() : "N/A"
        ));
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


    public void addReceipt(ReceiptDTO dto) {
        Receipt receipt = new Receipt();
        receipt.setCreated_at(dto.getCreated_at());
        receipt.setTotal_cost(dto.getTotal_cost());
        receipt.setNote(dto.getNote());

        Employee employee = (Employee) employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        receipt.setEmployee(employee);

        List<ReceiptDetail> details = new ArrayList<>();

        for (ReceiptDetailDTO detailDTO : dto.receiptDetails) {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

            ReceiptDetail detail = new ReceiptDetail();
            detail.setProduct(product);
            detail.setReceipt(receipt);
            detail.setSupplier(detailDTO.getSupplier());
            detail.setQuantity(detailDTO.getQuantity());
            detail.setInput_price(detailDTO.getInput_price());
            detail.setCheck(detailDTO.isCheck());

            // Tạo composite key
            ReceiptProId id = new ReceiptProId();
            detail.setId(id); // sẽ được gán tự động khi persist nếu receipt chưa có ID

            details.add(detail);
        }

        receipt.setReceiptDetails(details);
        receiptRepository.save(receipt);
    }
}
