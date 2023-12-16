package com.microsservicos.dto;

import java.math.BigInteger;

public record ReportDto(BigInteger count, Double total, Double mean) {}