package com.microsservicos.shoppingapi.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.microsservicos.dto.ReportDto;
import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.shoppingapi.repository.ShopRepository;
import com.microsservicos.shoppingapi.service.ReportService;
import com.microsservicos.shoppingapi.util.Mapper;

@Service
public class ReportServiceImpl implements ReportService {

  private final ShopRepository shopRepository;

  public ReportServiceImpl(ShopRepository shopRepository) {
    this.shopRepository = shopRepository;
  }
  
  @Override
  public List<ShopOutputDto> getShopByFilters(LocalDate start, LocalDate end, Float min) {
    validateStartDate(start);
    return shopRepository.getShopByFilters(start, end, min).stream().map(Mapper::shopToShopDto).toList();
  }

  private void validateStartDate(LocalDate start) {
    if (Objects.isNull(start)) {
      throw new IllegalArgumentException("Start date cannot be null.");
    }
  }
  
  @Override
  public ReportDto getReportByDate(LocalDate start, LocalDate end) {
    validateStartDate(start);
    return shopRepository.getReportByDate(start, end);
  }
  
}
