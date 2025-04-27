package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.adapters.persistances.entities.Payment;
import com.personnel.personnelservice.adapters.persistances.entities.User;
import com.personnel.personnelservice.adapters.persistances.mappers.PaymentMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPaymentRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaUserRepository;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.models.dtos.PaymentDto;
import com.personnel.personnelservice.core.ports.services.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the PaymentService interface
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final JpaPaymentRepository jpaPaymentRepository;
    private final JpaUserRepository jpaUserRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Payment payment = paymentMapper.toEntity(paymentDto);

        // Set the user
        User user = jpaUserRepository.findById(paymentDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + paymentDto.getUserId()));
        payment.setUser(user);

        Payment savedPayment = jpaPaymentRepository.save(payment);
        return paymentMapper.toDTO(savedPayment);
    }

    @Override
    public PaymentDto getPaymentById(UUID id) {
        Payment payment = jpaPaymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toDTO(payment);
    }

    @Override
    @Transactional
    public PaymentDto updatePayment(PaymentDto paymentDto) {
        Payment payment = jpaPaymentRepository.findById(paymentDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentDto.getId()));

        paymentMapper.updateEntity(paymentDto, payment);

        // Update the user if changed
        if (paymentDto.getUserId() != null &&
                !payment.getUser().getId().equals(paymentDto.getUserId())) {
            User user = jpaUserRepository.findById(paymentDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + paymentDto.getUserId()));
            payment.setUser(user);
        }

        Payment updatedPayment = jpaPaymentRepository.save(payment);
        return paymentMapper.toDTO(updatedPayment);
    }

    @Override
    @Transactional
    public void deletePayment(UUID id) {
        if (!jpaPaymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Payment not found with id: " + id);
        }
        jpaPaymentRepository.deleteById(id);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return jpaPaymentRepository.findAll().stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PaymentDto> getAllPayments(Pageable pageable) {
        return jpaPaymentRepository.findAll(pageable)
                .map(paymentMapper::toDTO);
    }

    @Override
    public List<PaymentDto> getPaymentsByUserId(UUID userId) {
        return jpaPaymentRepository.findByUserId(userId).stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PaymentDto> getPaymentsByUserId(UUID userId, Pageable pageable) {
        return jpaPaymentRepository.findByUserId(userId, pageable)
                .map(paymentMapper::toDTO);
    }

    @Override
    public List<PaymentDto> getActivePaymentsByUserId(UUID userId) {
        return jpaPaymentRepository.findByUserIdAndActiveTrue(userId).stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentDto> getLatestActivePaymentByUserId(UUID userId) {
        return jpaPaymentRepository.findTopByUserIdAndActiveTrueOrderByEndDateDesc(userId)
                .map(paymentMapper::toDTO);
    }

    @Override
    public Optional<PaymentDto> getPaymentByPurchaseToken(String purchaseToken) {
        return jpaPaymentRepository.findByPurchaseToken(purchaseToken)
                .map(paymentMapper::toDTO);
    }

    @Override
    public Optional<PaymentDto> getPaymentByReceiptData(String receiptData) {
        return jpaPaymentRepository.findByReceiptData(receiptData)
                .map(paymentMapper::toDTO);
    }

    @Override
    public Page<PaymentDto> getPaymentsByPlatform(String platform, Pageable pageable) {
        return jpaPaymentRepository.findByPlatform(platform, pageable)
                .map(paymentMapper::toDTO);
    }

    @Override
    public List<PaymentDto> getPaymentsByProductId(String productId) {
        return jpaPaymentRepository.findByProductId(productId).stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getPaymentsExpiringSoon(LocalDateTime endDate) {
        return jpaPaymentRepository.findByActiveTrueAndEndDateBefore(endDate).stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long countActiveSubscriptions() {
        return jpaPaymentRepository.countByActiveTrue();
    }

    @Override
    @Transactional
    public PaymentDto activatePayment(UUID paymentId) {
        Payment payment = jpaPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));

        payment.setActive(true);
        Payment activatedPayment = jpaPaymentRepository.save(payment);
        return paymentMapper.toDTO(activatedPayment);
    }

    @Override
    @Transactional
    public PaymentDto deactivatePayment(UUID paymentId) {
        Payment payment = jpaPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));

        payment.setActive(false);
        Payment deactivatedPayment = jpaPaymentRepository.save(payment);
        return paymentMapper.toDTO(deactivatedPayment);
    }

    @Override
    @Transactional
    public PaymentDto extendPaymentEndDate(UUID paymentId, LocalDateTime newEndDate) {
        Payment payment = jpaPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));

        payment.setEndDate(newEndDate);
        Payment updatedPayment = jpaPaymentRepository.save(payment);
        return paymentMapper.toDTO(updatedPayment);
    }

    @Override
    public boolean hasActiveSubscription(UUID userId) {
        return jpaPaymentRepository.findByUserIdAndActiveTrue(userId).size() > 0;
    }
}