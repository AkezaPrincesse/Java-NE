package com.exam.utility.dto.request.billing;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GenerateBillsRequest {

    @NotNull(message = "Billing year is required")
    @Min(value = 2020) @Max(value = 2100)
    private Integer billingYear;

    @NotNull(message = "Billing month is required")
    @Min(value = 1) @Max(value = 12)
    private Integer billingMonth;

    private Long customerId;
    private Long meterId;
}
