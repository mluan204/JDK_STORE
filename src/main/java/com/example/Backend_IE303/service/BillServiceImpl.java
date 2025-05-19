package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.BillDTO;
import com.example.Backend_IE303.dto.BillDetailDTO;
import com.example.Backend_IE303.entity.*;
import com.example.Backend_IE303.entity.EmbeddedId.BillProId;
import com.example.Backend_IE303.repository.*;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import java.util.List;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public BillServiceImpl(
            BillRepository billRepository,
            BillDetailRepository billDetailRepository,
            EmployeeRepository employeeRepository,
            CustomerService customerService,
            ProductService productService, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.billRepository = billRepository;
        this.billDetailRepository = billDetailRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Integer getDailyRevenue() {
        return billRepository.getDailyRevenue();
    }

    @Override
    public Integer getYesterdayRevenue() {
        return billRepository.getYesterdayRevenue(java.time.LocalDate.now().minusDays(1));
    }

    @Override
    public Integer getNumberOfBills() {
        return billRepository.getNumberOfBills();
    }

    @Override
    public Integer getYesterdayNumberOfBills() {
        return billRepository.getYesterdayNumberOfBills(java.time.LocalDate.now().minusDays(1));
    }

    @Override
    public Page<BillDTO> getAllBills(Pageable pageable, String keyword, String startDateStr, String endDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = null;
        LocalDate endDate = null;

        if (startDateStr != null && !startDateStr.isEmpty()) {
            startDate = LocalDate.parse(startDateStr, formatter);
        }

        if (endDateStr != null && !endDateStr.isEmpty()) {
            endDate = LocalDate.parse(endDateStr, formatter);
        }

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().and(Sort.by("isDeleted").ascending()).and(Sort.by("createdAt").descending()));

        // Trường hợp không lọc thời gian
        if (startDate == null || endDate == null) {
            if (keyword == null || keyword.trim().isEmpty()) {
                return billRepository.findAll(sortedPageable).map(BillDTO::fromEntity);
            } else {
                return billRepository.searchBillsByKeyword(keyword, sortedPageable).map(BillDTO::fromEntity);
            }
        }

        // Có lọc thời gian
        LocalDateTime from = startDate.atStartOfDay();
        LocalDateTime to = endDate.plusDays(1).atStartOfDay();

        if (keyword == null || keyword.trim().isEmpty()) {
            return billRepository.findByCreatedAtBetweenAndIsDeletedFalse(from, to, sortedPageable)
                    .map(BillDTO::fromEntity);
        } else {
            return billRepository.searchBillsByKeywordAndCreatedAtBetween(keyword, from, to, sortedPageable)
                    .map(BillDTO::fromEntity);
        }
    }

    @Override
    public List<BillDTO> getAllBillsList() {
        return billRepository.findAll().stream().map(BillDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public BillDTO getBillById(Integer id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + id));
        return BillDTO.fromEntity(bill);
    }

    @Transactional
    @Override
    public BillDTO createBill(BillDTO request, int pointsToUse) {
        // Tìm nhân viên
        Employee employee = employeeRepository.findById(request.getEmployee().getId())
                .orElseThrow(
                        () -> new RuntimeException("Employee not found with id: " + request.getEmployee().getId()));

        // Tìm khách hàng
        Customer customer = customerRepository.findById(request.getCustomer().getId())
                .orElseThrow(
                        () -> new RuntimeException("Customer not found with id: " + request.getCustomer().getId()));

        // Tạo hóa đơn mới
        Bill bill = new Bill();
        bill.setEmployee(employee);
        bill.setCustomer(customer);
        bill.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        bill.setNotes(request.getNotes());

        // Lưu hóa đơn trước để lấy id
        Bill savedBill = billRepository.save(bill);

        // Khai báo biến tổng tiền bằng AtomicInteger
        AtomicInteger totalCost = new AtomicInteger(0);
        // Khai báo biến tổng số lượng bằng AtomicInteger
        AtomicInteger totalQuantity = new AtomicInteger(0);

        // Duyệt qua danh sách sản phẩm và tạo BillDetail
        List<BillDetail> billDetails = request.getBillDetails().stream().map(detailDTO -> {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + detailDTO.getProductId()));

            BillDetail billDetail = new BillDetail();
            billDetail.setId(new BillProId(savedBill.getId(), product.getId()));
            billDetail.setBill(savedBill);
            billDetail.setProduct(product);
            billDetail.setPrice(product.getPrice());
            if (detailDTO.getAfterDiscount() != null) {
                billDetail.setAfter_discount(detailDTO.getAfterDiscount());
            } else {
                billDetail.setAfter_discount(null);
            }
            billDetail.setQuantity(detailDTO.getQuantity());

            // Cập nhật tổng tiền bằng AtomicInteger
            if (detailDTO.getAfterDiscount() != null) {
                totalCost.addAndGet(detailDTO.getAfterDiscount() * detailDTO.getQuantity());
            } else {
                totalCost.addAndGet(product.getPrice() * detailDTO.getQuantity());
            }

            // Cập nhật tổng số lượng sản phẩm
            totalQuantity.addAndGet(detailDTO.getQuantity());

            product.setQuantity_available(product.getQuantity_available() - detailDTO.getQuantity());

            return billDetail;
        }).collect(Collectors.toList());

        // Cập nhật tổng số lượng sản phẩm vào hóa đơn
        savedBill.setTotalQuantity(totalQuantity.get());

        // Lưu danh sách chi tiết hóa đơn
        billDetailRepository.saveAll(billDetails);

        // Tính toán điểm và giảm giá
        int currentPoints = customer.getScore(); // Điểm hiện có của khách

        // Xác định số điểm có thể sử dụng (không vượt quá số điểm khách có)
        int validPointsToUse = Math.min(pointsToUse, currentPoints);
        int discountFromPoints = validPointsToUse * 100; // Mỗi điểm giảm 100 VNĐ

        // Đảm bảo số tiền giảm không vượt quá tổng tiền
        discountFromPoints = Math.min(discountFromPoints, totalCost.get() / 2);// Số tiền giảm không được quá 50% tổng
                                                                               // bill

        int finalTotal = totalCost.get() - discountFromPoints; // Tổng tiền sau giảm
        int earnedPoints = finalTotal / 10000; // Điểm thưởng mới

        // Cập nhật tổng tiền hóa đơn
        savedBill.setTotal_cost(totalCost.get());
        savedBill.setAfter_discount(finalTotal);
        savedBill.setBillDetails(billDetails);

        billRepository.save(savedBill);

        customer.setScore(currentPoints - (discountFromPoints / 100) + earnedPoints);
        customerRepository.save(customer);

        return BillDTO.fromEntity(savedBill);
    }

    @Transactional
    @Override
    public BillDTO updateBill(Integer billId, BillDTO request, int pointsToUse) {
        // Tìm hóa đơn hiện tại
        Bill existingBill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        // Tìm nhân viên
        Employee employee = employeeRepository.findById(request.getEmployee().getId())
                .orElseThrow(
                        () -> new RuntimeException("Employee not found with id: " + request.getEmployee().getId()));

        // Tìm khách hàng
        Customer customer = customerRepository.findById(request.getCustomer().getId())
                .orElseThrow(
                        () -> new RuntimeException("Customer not found with id: " + request.getCustomer().getId()));

        // Cộng lại số điểm đã trừ trước đó
        int previousAfterDiscount = existingBill.getAfter_discount() != null ? existingBill.getAfter_discount() : 0;
        int previousTotal = existingBill.getTotal_cost() != 0 ? existingBill.getTotal_cost() : 0;
        int oldUsedPoints = (previousTotal - previousAfterDiscount) / 100;
        int oldEarnedPoints = previousAfterDiscount / 10000;
        customer.setScore(customer.getScore() + oldUsedPoints - oldEarnedPoints);

        // Khôi phục số lượng sản phẩm đã trừ trước đó
        existingBill.getBillDetails().forEach(detail -> {
            Product product = detail.getProduct();
            product.setQuantity_available(product.getQuantity_available() + detail.getQuantity());
            productRepository.save(product);
        });

        // Xóa toàn bộ chi tiết hóa đơn cũ
        billDetailRepository.deleteByBillId(billId);

        // Cập nhật thông tin hóa đơn
        existingBill.setEmployee(employee);
        existingBill.setCustomer(customer);
        existingBill.setNotes(request.getNotes());

        // Tính lại tổng tiền và số lượng
        AtomicInteger totalCost = new AtomicInteger(0);
        AtomicInteger totalQuantity = new AtomicInteger(0);

        List<BillDetail> updatedDetails = request.getBillDetails().stream().map(detailDTO -> {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + detailDTO.getProductId()));

            BillDetail billDetail = new BillDetail();
            billDetail.setId(new BillProId(existingBill.getId(), product.getId()));
            billDetail.setBill(existingBill);
            billDetail.setProduct(product);
            billDetail.setPrice(product.getPrice());

            if (detailDTO.getAfterDiscount() != null) {
                billDetail.setAfter_discount(detailDTO.getAfterDiscount());
                totalCost.addAndGet(detailDTO.getAfterDiscount() * detailDTO.getQuantity());
            } else {
                billDetail.setAfter_discount(null);
                totalCost.addAndGet(product.getPrice() * detailDTO.getQuantity());
            }

            billDetail.setQuantity(detailDTO.getQuantity());
            totalQuantity.addAndGet(detailDTO.getQuantity());

            product.setQuantity_available(product.getQuantity_available() - detailDTO.getQuantity());
            productRepository.save(product);

            return billDetail;
        }).collect(Collectors.toList());

        // Lưu các chi tiết mới
        billDetailRepository.saveAll(updatedDetails);

        // Cập nhật điểm giảm
        int currentPoints = customer.getScore();
        int validPointsToUse = Math.min(pointsToUse, currentPoints);
        int discountFromPoints = Math.min(validPointsToUse * 100, totalCost.get() / 2);
        int finalTotal = totalCost.get() - discountFromPoints;
        int earnedPoints = finalTotal / 10000;

        // Cập nhật hóa đơn
        existingBill.setTotal_cost(totalCost.get());
        existingBill.setAfter_discount(finalTotal);
        existingBill.setTotalQuantity(totalQuantity.get());
        existingBill.setBillDetails(updatedDetails);
        billRepository.save(existingBill);

        // Cập nhật lại điểm khách hàng
        customer.setScore(currentPoints - (discountFromPoints / 100) + earnedPoints);
        customerRepository.save(customer);

        return BillDTO.fromEntity(existingBill);
    }

    @Override
    public Boolean deleteBill(Integer Id) {
        Bill bill = billRepository.findById(Id)
                .orElse(null);

        if (bill == null || Boolean.TRUE.equals(bill.getIsDeleted())) {
            return false; // Hóa đơn không tồn tại hoặc đã bị xóa trước đó
        }

        // Lấy danh sách billdetail
        List<BillDetail> billDetails = billDetailRepository.findByBillId(Id);

        // Hoàn trả số lượng sản phẩm về kho
        for (BillDetail detail : billDetails) {
            Product product = detail.getProduct();
            product.setQuantity_available(product.getQuantity_available() + detail.getQuantity());
            productRepository.save(product); // Cập nhật số lượng sản phẩm trong kho
        }

        // Cập nhật hóa đơn thành đã bị xóa
        bill.setIsDeleted(true);
        bill.setIs_error(true);
        billRepository.save(bill);

        return true;

    }

}
