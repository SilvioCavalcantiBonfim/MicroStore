package com.microsservicos.shoppingapi.repository;

import java.time.LocalDate;
import java.util.List;

import com.microsservicos.dto.ReportDto;
import com.microsservicos.shoppingapi.model.Shop;

public interface ReportRepository {
  List<Shop> retrieveShopsByDateAndTotal(LocalDate start, LocalDate end, Float min);
  ReportDto generateReportByDate(LocalDate start, LocalDate end);
}
