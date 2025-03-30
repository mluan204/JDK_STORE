package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.BillDTO;
import com.example.Backend_IE303.dto.BillDetailDTO;
import com.example.Backend_IE303.entity.*;
import com.example.Backend_IE303.entity.EmbeddedId.BillProId;
import com.example.Backend_IE303.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final CustomerService customerService;
    private final ProductService productService;
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
        this.customerService = customerService;
        this.productService = productService;
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
        return billRepository.getNumberOfBills();}

    @Override
    public Integer getYesterdayNumberOfBills() {
        return billRepository.getYesterdayNumberOfBills(java.time.LocalDate.now().minusDays(1));
    }

    @Override
    public List<BillDTO> getAllBills() {
        return billRepository.findAll().stream()
                .map(BillDTO::fromEntity)
                .sorted(Comparator.comparing(BillDTO::getIsDeleted))
                .collect(Collectors.toList());

    }

    @Override
    public  Page<BillDTO> getAllBills(Pageable pageable){
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().and(Sort.by("isDeleted").ascending()) // Sắp xếp is_deleted=false trước
        );
        return billRepository.findAll(sortedPageable).map(BillDTO::fromEntity);
    }

    @Override
    public  BillDTO getBillById(Integer id){
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + id));
        return BillDTO.fromEntity(bill);
    }


    @Transactional
    @Override
    public BillDTO createBill(BillDTO request, int pointsToUse) {
        //Tìm nhân viên
        Employee employee = employeeRepository.findById(request.getEmployee_id())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.getEmployee_id()));

        //Tìm khách hàng
        Customer customer = customerRepository.findById(request.getCustomer_id())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + request.getCustomer_id()));

        //Tạo hóa đơn mới
        Bill bill = new Bill();
        bill.setEmployee(employee);
        bill.setCustomer(customer);
        bill.setCreated_at(new Timestamp(System.currentTimeMillis()));

        //Lưu hóa đơn trước để lấy id
        Bill savedBill = billRepository.save(bill);

        // Khai báo biến tổng tiền bằng AtomicInteger
        AtomicInteger totalCost = new AtomicInteger(0);

        //Duyệt qua danh sách sản phẩm và tạo BillDetail
        List<BillDetail> billDetails = request.getBillDetails().stream().map(detailDTO ->{
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
            product.setQuantity_available(product.getQuantity_available() - detailDTO.getQuantity());

            return billDetail;
        }).collect(Collectors.toList());

        // Lưu danh sách chi tiết hóa đơn
        billDetailRepository.saveAll(billDetails);

        //Tính toán điểm và giảm giá
        int currentPoints = customer.getScore();  // Điểm hiện có của khách

        // Xác định số điểm có thể sử dụng (không vượt quá số điểm khách có)
        int validPointsToUse = Math.min(pointsToUse, currentPoints);
        int discountFromPoints = validPointsToUse * 100; // Mỗi điểm giảm 100 VNĐ

        // Đảm bảo số tiền giảm không vượt quá tổng tiền
        discountFromPoints = Math.min(discountFromPoints, totalCost.get() / 2);//Số tiền giảm không được quá 50% tổng bill

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


    @Override
    public Boolean deleteBill(Integer Id) {
        Bill bill = billRepository.findById(Id)
                .orElse(null);

        if (bill == null || Boolean.TRUE.equals(bill.getIsDeleted())) {
            return false; // Hóa đơn không tồn tại hoặc đã bị xóa trước đó
        }

        bill.setIsDeleted(true);
        billRepository.save(bill);
        return true; // Xóa thành công
    }

    @Override
    public Boolean deleteErrorBill(Integer Id){
        Bill bill = billRepository.findById(Id)
                .orElse(null);

        if (bill == null || Boolean.TRUE.equals(bill.getIsDeleted())) {
            return false; // Hóa đơn không tồn tại hoặc đã bị xóa trước đó
        }

        //Lấy danh sách billdetail
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
