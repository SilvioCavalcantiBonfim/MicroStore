package com.microsservicos.shoppingapi.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microsservicos.dto.ReportDto;
import com.microsservicos.dto.ShopDto;
import com.microsservicos.shoppingapi.repository.ShopRepository;
import com.microsservicos.shoppingapi.service.ReportService;
import com.microsservicos.shoppingapi.util.ShopToShopDtoConverter;

@Service
public class ReportServiceImpl implements ReportService {

  private final ShopRepository shopRepository;

  public ReportServiceImpl(ShopRepository shopRepository) {
    this.shopRepository = shopRepository;
  }
  
  @Override
  public List<ShopDto> getShopByFilters(Date start, Date end, Float min) {
    return shopRepository.getShopByFilters(start, end, min).stream().map(ShopToShopDtoConverter::convert).toList();
  }
  
  @Override
  public ReportDto getReportByDate(Date start, Date end) {
    return shopRepository.getReportByDate(start, end);
  }
  
}
