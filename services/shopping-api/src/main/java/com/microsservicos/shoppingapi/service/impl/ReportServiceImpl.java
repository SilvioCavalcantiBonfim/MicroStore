package com.microsservicos.shoppingapi.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microsservicos.shoppingapi.dto.ReportDto;
import com.microsservicos.shoppingapi.dto.ShopDto;
import com.microsservicos.shoppingapi.repository.ShopRepository;
import com.microsservicos.shoppingapi.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

  private final ShopRepository shopRepository;

  public ReportServiceImpl(ShopRepository shopRepository) {
    this.shopRepository = shopRepository;
  }
  
  @Override
  public List<ShopDto> getShopByFilters(Date start, Date end, Float min) {
    return shopRepository.getShopByFilters(start, end, min).stream().map(ShopDto::convert).toList();
  }
  
  @Override
  public ReportDto getReportByDate(Date start, Date end) {
    return shopRepository.getReportByDate(start, end);
  }
  
}
