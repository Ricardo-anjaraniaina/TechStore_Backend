package com.computerstore.backend.controller;

import com.computerstore.backend.dto.PaymentRequest;
import com.computerstore.backend.dto.PaymentResponse;
import com.computerstore.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Client : soumettre les infos de paiement
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse submitPayment(@RequestBody PaymentRequest request) {
        return paymentService.submitPayment(request);
    }

    // Client : uploader la preuve de paiement
    @PostMapping(value = "/{paymentId}/proof", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PaymentResponse uploadProof(@PathVariable Long paymentId,
                                       @RequestParam("file") MultipartFile file) throws IOException {
        return paymentService.uploadProof(paymentId, file);
    }

    // Client : voir le paiement d'une commande
    @GetMapping("/order/{orderId}")
    public PaymentResponse getPaymentByOrder(@PathVariable Long orderId) {
        return paymentService.getPaymentByOrder(orderId);
    }

    // Admin : voir tous les paiements en attente
    @GetMapping("/pending")
    public List<PaymentResponse> getPendingPayments() {
        return paymentService.getPendingPayments();
    }

    // Admin : valider un paiement
    @PatchMapping("/{paymentId}/verify")
    public PaymentResponse verifyPayment(@PathVariable Long paymentId,
                                         @RequestParam(required = false) String adminNote) {
        return paymentService.verifyPayment(paymentId, adminNote);
    }

    // Admin : rejeter un paiement
    @PatchMapping("/{paymentId}/reject")
    public PaymentResponse rejectPayment(@PathVariable Long paymentId,
                                         @RequestParam(required = false) String adminNote) {
        return paymentService.rejectPayment(paymentId, adminNote);
    }
}
