package com.microsservicos.shoppingapi.repository;

import java.time.LocalDate;
import java.util.List;

import com.microsservicos.dto.ReportDto;
import com.microsservicos.shoppingapi.model.Shop;

public interface ReportRepository {
  List<Shop> getShopByFilters(LocalDate start, LocalDate end, Float min);
  ReportDto getReportByDate(LocalDate start, LocalDate end);
}
