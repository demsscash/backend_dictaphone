package com.personnel.personnelservice.core.ports.services;

import com.personnel.personnelservice.core.models.dtos.PaymentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing payments
 */
public interface PaymentService {

    /**
     * Create a new payment
     * @param paymentDto payment data
     * @return the created payment
     */
    PaymentDto createPayment(PaymentDto paymentDto);

    /**
     * Get a payment by id
     * @param id payment id
     * @return the payment
     */
    PaymentDto getPaymentById(UUID id);

    /**
     * Update a payment
     * @param paymentDto payment data
     * @return the updated payment
     */
    PaymentDto updatePayment(PaymentDto paymentDto);

    /**
     * Delete a payment
     * @param id payment id
     */
    void deletePayment(UUID id);

    /**
     * Get all payments
     * @return list of all payments
     */
    List<PaymentDto> getAllPayments();

    /**
     * Get paginated payments
     * @param pageable pagination information
     * @return paginated list of payments
     */
    Page<PaymentDto> getAllPayments(Pageable pageable);

    /**
     * Get all payments by user id
     * @param userId user id
     * @return list of payments
     */
    List<PaymentDto> getPaymentsByUserId(UUID userId);

    /**
     * Get paginated payments by user id
     * @param userId user id
     * @param pageable pagination information
     * @return paginated list of payments
     */
    Page<PaymentDto> getPaymentsByUserId(UUID userId, Pageable pageable);

    /**
     * Get all active payments by user id
     * @param userId user id
     * @return list of active payments
     */
    List<PaymentDto> getActivePaymentsByUserId(UUID userId);

    /**
     * Get latest active payment by user id
     * @param userId user id
     * @return the latest active payment if found
     */
    Optional<PaymentDto> getLatestActivePaymentByUserId(UUID userId);

    /**
     * Get a payment by purchase token
     * @param purchaseToken purchase token
     * @return the payment if found
     */
    Optional<PaymentDto> getPaymentByPurchaseToken(String purchaseToken);

    /**
     * Get a payment by receipt data
     * @param receiptData receipt data
     * @return the payment if found
     */
    Optional<PaymentDto> getPaymentByReceiptData(String receiptData);

    /**
     * Get paginated payments by platform
     * @param platform platform name
     * @param pageable pagination information
     * @return paginated list of payments
     */
    Page<PaymentDto> getPaymentsByPlatform(String platform, Pageable pageable);

    /**
     * Get payments by product id
     * @param productId product id
     * @return list of payments
     */
    List<PaymentDto> getPaymentsByProductId(String productId);

    /**
     * Get payments expiring soon
     * @param endDate end date threshold
     * @return list of payments expiring soon
     */
    List<PaymentDto> getPaymentsExpiringSoon(LocalDateTime endDate);

    /**
     * Count active subscriptions
     * @return number of active subscriptions
     */
    long countActiveSubscriptions();

    /**
     * Activate a payment
     * @param paymentId payment id
     * @return the activated payment
     */
    PaymentDto activatePayment(UUID paymentId);

    /**
     * Deactivate a payment
     * @param paymentId payment id
     * @return the deactivated payment
     */
    PaymentDto deactivatePayment(UUID paymentId);

    /**
     * Extend payment end date
     * @param paymentId payment id
     * @param newEndDate new end date
     * @return the updated payment
     */
    PaymentDto extendPaymentEndDate(UUID paymentId, LocalDateTime newEndDate);

    /**
     * Check if user has active subscription
     * @param userId user id
     * @return true if user has active subscription, false otherwise
     */
    boolean hasActiveSubscription(UUID userId);
}