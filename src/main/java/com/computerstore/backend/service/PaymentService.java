package com.computerstore.backend.service;

import com.computerstore.backend.dto.PaymentRequest;
import com.computerstore.backend.dto.PaymentResponse;
import com.computerstore.backend.entity.*;
import com.computerstore.backend.exception.ResourceNotFoundException;
import com.computerstore.backend.repository.OrderRepository;
import com.computerstore.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Value("${app.upload.dir:uploads/payments}")
    private String uploadDir;

    @Transactional
    public PaymentResponse submitPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée"));

        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new IllegalStateException("Un paiement existe déjà pour cette commande");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setSenderNumber(request.getSenderNumber());
        payment.setSenderName(request.getSenderName());
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);

        order.setStatus(OrderStatus.PAYMENT_PENDING);
        orderRepository.save(order);

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse uploadProof(Long paymentId, MultipartFile file) throws IOException {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé"));

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        // Supprimer l'ancienne preuve si elle existe
        if (payment.getProofImagePath() != null) {
            Files.deleteIfExists(Paths.get(payment.getProofImagePath()));
        }

        payment.setProofImagePath(filePath.toString());
        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse verifyPayment(Long paymentId, String adminNote) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé"));

        payment.setStatus(PaymentStatus.VERIFIED);
        payment.setAdminNote(adminNote);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse rejectPayment(Long paymentId, String adminNote) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé"));

        payment.setStatus(PaymentStatus.REJECTED);
        payment.setAdminNote(adminNote);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse getPaymentByOrder(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé"));
        return toResponse(payment);
    }

    public List<PaymentResponse> getPendingPayments() {
        return paymentRepository.findByStatus(PaymentStatus.PENDING)
            .stream().map(this::toResponse).toList();
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setOrderId(payment.getOrder().getId());
        response.setOrderNumber(payment.getOrder().getOrderNumber());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setSenderNumber(payment.getSenderNumber());
        response.setSenderName(payment.getSenderName());
        response.setAmount(payment.getAmount());
        response.setProofImagePath(payment.getProofImagePath());
        response.setStatus(payment.getStatus().name());
        response.setAdminNote(payment.getAdminNote());
        response.setCreatedAt(payment.getCreatedAt());
        return response;
    }
}
