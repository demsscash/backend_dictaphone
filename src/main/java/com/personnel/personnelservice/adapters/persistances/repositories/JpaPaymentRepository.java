package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaPaymentRepository extends JpaBaseRepository<Payment> {

    /**
     * Find all payments by user
     * @param userId the user id
     * @return list of payments
     */
    List<Payment> findByUserId(UUID userId);

    /**
     * Find all active payments by user
     * @param userId the user id
     * @return list of active payments
     */
    List<Payment> findByUserIdAndActiveTrue(UUID userId);

    /**
     * Find latest active payment by user
     * @param userId the user id
     * @return the latest active payment if found
     */
    Optional<Payment> findTopByUserIdAndActiveTrueOrderByEndDateDesc(UUID userId);

    /**
     * Find payment by purchase token
     * @param purchaseToken the purchase token
     * @return the payment if found
     */
    Optional<Payment> findByPurchaseToken(String purchaseToken);

    /**
     * Find payment by receipt data
     * @param receiptData the receipt data
     * @return the payment if found
     */
    Optional<Payment> findByReceiptData(String receiptData);

    /**
     * Find paginated payments by user
     * @param userId the user id
     * @param pageable pagination information
     * @return paginated list of payments
     */
    Page<Payment> findByUserId(UUID userId, Pageable pageable);

    /**
     * Find paginated payments by platform
     * @param platform the platform
     * @param pageable pagination information
     * @return paginated list of payments
     */
    Page<Payment> findByPlatform(String platform, Pageable pageable);

    /**
     * Find payments by product id
     * @param productId the product id
     * @return list of payments
     */
    List<Payment> findByProductId(String productId);

    /**
     * Find payments expiring soon
     * @param endDate the end date threshold
     * @return list of payments expiring soon
     */
    List<Payment> findByActiveTrueAndEndDateBefore(LocalDateTime endDate);

    /**
     * Count active subscriptions
     * @return number of active subscriptions
     */
    long countByActiveTrue();
}