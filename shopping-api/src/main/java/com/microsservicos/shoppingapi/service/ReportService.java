package com.microsservicos.shoppingapi.service;

import java.time.LocalDate;
import java.util.List;

import com.microsservicos.dto.ReportDto;
import com.microsservicos.dto.ShopOutputDto;

public interface ReportService {
  List<ShopOutputDto> getShopByFilters(LocalDate start, LocalDate end, Float min);
  ReportDto getReportByDate(LocalDate start, LocalDate end);
}
