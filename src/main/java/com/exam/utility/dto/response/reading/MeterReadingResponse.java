package com.exam.utility.dto.response.reading;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class MeterReadingResponse {
    private Long id;
    private Long meterId;
    private String meterNumber;
    private String meterType;
    private String customerName;
    private Double previousReading;
    private Double currentReading;
    private Double consumption;
    private LocalDate readingDate;
    private Integer readingYear;
    private Integer readingMonth;
    private String notes;
    private LocalDateTime createdAt;
    private String createdBy;
}
