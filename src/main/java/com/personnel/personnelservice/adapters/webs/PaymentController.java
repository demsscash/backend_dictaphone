package com.personnel.personnelservice.adapters.webs;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.dtos.PaymentDto;
import com.personnel.personnelservice.core.models.views.Views;
import com.personnel.personnelservice.core.ports.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing payments
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Controller", description = "Operations related to payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Create a new payment", description = "Creates a new payment with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Payment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<PaymentDto> createPayment(
            @Valid @RequestBody @JsonView(Views.Create.class) PaymentDto paymentDto) {
        PaymentDto createdPayment = paymentService.createPayment(paymentDto);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a payment by ID", description = "Returns a payment as per the ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the payment"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<PaymentDto> getPaymentById(
            @Parameter(description = "ID of the payment", required = true) @PathVariable UUID id) {
        PaymentDto payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a payment", description = "Updates an existing payment with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Payment or user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<PaymentDto> updatePayment(
            @Parameter(description = "ID of the payment", required = true) @PathVariable UUID id,
            @Valid @RequestBody @JsonView(Views.Update.class) PaymentDto paymentDto) {
        paymentDto.setId(id);
        PaymentDto updatedPayment = paymentService.updatePayment(paymentDto);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a payment", description = "Deletes a payment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Payment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deletePayment(
            @Parameter(description = "ID of the payment", required = true) @PathVariable UUID id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all payments", description = "Returns a list of all payments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of payments"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated payments", description = "Returns a paginated list of payments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of payments"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<PaymentDto>> getAllPaymentsPaginated(Pageable pageable) {
        Page<PaymentDto> payments = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get payments by user", description = "Returns a list of payments for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of payments"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<PaymentDto>> getPaymentsByUserId(
            @Parameter(description = "ID of the user", required = true) @PathVariable UUID userId) {
        List<PaymentDto> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/user/{userId}/paginated")
    @Operation(summary = "Get paginated payments by user", description = "Returns a paginated list of payments for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of payments"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<PaymentDto>> getPaymentsByUserIdPaginated(
            @Parameter(description = "ID of the user", required = true) @PathVariable UUID userId,
            Pageable pageable) {
        Page<PaymentDto> payments = paymentService.getPaymentsByUserId(userId, pageable);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/user/{userId}/active")
    @Operation(summary = "Get active payments by user", description = "Returns a list of active payments for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of active payments"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<PaymentDto>> getActivePaymentsByUserId(
            @Parameter(description = "ID of the user", required = true) @PathVariable UUID userId) {
        List<PaymentDto> payments = paymentService.getActivePaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/user/{userId}/latest-active")
    @Operation(summary = "Get latest active payment by user", description = "Returns the latest active payment for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the latest active payment"),
            @ApiResponse(responseCode = "404", description = "User not found or no active payments"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<PaymentDto> getLatestActivePaymentByUserId(
            @Parameter(description = "ID of the user", required = true) @PathVariable UUID userId) {
        return paymentService.getLatestActivePaymentByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/token/{purchaseToken}")
    @Operation(summary = "Get payment by purchase token", description = "Returns a payment by purchase token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the payment"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<PaymentDto> getPaymentByPurchaseToken(
            @Parameter(description = "Purchase token", required = true) @PathVariable String purchaseToken) {
        return paymentService.getPaymentByPurchaseToken(purchaseToken)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/receipt/{receiptData}")
    @Operation(summary = "Get payment by receipt data", description = "Returns a payment by receipt data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the payment"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<PaymentDto> getPaymentByReceiptData(
            @Parameter(description = "Receipt data", required = true) @PathVariable String receiptData) {
        return paymentService.getPaymentByReceiptData(receiptData)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/platform/{platform}")
    @Operation(summary = "Get payments by platform", description = "Returns paginated payments by platform")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of payments"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<PaymentDto>> getPaymentsByPlatform(
            @Parameter(description = "Platform name", required = true) @PathVariable String platform,
            Pageable pageable) {
        Page<PaymentDto> payments = paymentService.getPaymentsByPlatform(platform, pageable);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get payments by product ID", description = "Returns a list of payments by product ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of payments"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<PaymentDto>> getPaymentsByProductId(
            @Parameter(description = "Product ID", required = true) @PathVariable String productId) {
        List<PaymentDto> payments = paymentService.getPaymentsByProductId(productId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/expiring")
    @Operation(summary = "Get payments expiring soon", description = "Returns a list of payments expiring before a specified date")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of expiring payments"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<PaymentDto>> getPaymentsExpiringSoon(
            @Parameter(description = "End date threshold", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<PaymentDto> payments = paymentService.getPaymentsExpiringSoon(endDate);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/count-active")
    @Operation(summary = "Count active subscriptions", description = "Returns the number of active subscriptions")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully counted active subscriptions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> countActiveSubscriptions() {
        long count = paymentService.countActiveSubscriptions();
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a payment", description = "Activates a payment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment activated successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<PaymentDto> activatePayment(
            @Parameter(description = "ID of the payment", required = true) @PathVariable UUID id) {
        PaymentDto activatedPayment = paymentService.activatePayment(id);
        return ResponseEntity.ok(activatedPayment);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a payment", description = "Deactivates a payment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<PaymentDto> deactivatePayment(
            @Parameter(description = "ID of the payment", required = true) @PathVariable UUID id) {
        PaymentDto deactivatedPayment = paymentService.deactivatePayment(id);
        return ResponseEntity.ok(deactivatedPayment);
    }

    @PatchMapping("/{id}/extend")
    @Operation(summary = "Extend payment end date", description = "Extends the end date of a payment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment end date extended successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<PaymentDto> extendPaymentEndDate(
            @Parameter(description = "ID of the payment", required = true) @PathVariable UUID id,
            @Parameter(description = "New end date", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newEndDate) {
        PaymentDto updatedPayment = paymentService.extendPaymentEndDate(id, newEndDate);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping("/user/{userId}/has-active-subscription")
    @Operation(summary = "Check if user has active subscription", description = "Checks if a user has an active subscription")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully checked subscription status"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> hasActiveSubscription(
            @Parameter(description = "ID of the user", required = true) @PathVariable UUID userId) {
        boolean hasActiveSubscription = paymentService.hasActiveSubscription(userId);
        return ResponseEntity.ok(hasActiveSubscription);
    }
}