package com.microsservicos.shoppingapi.service;

import java.util.Date;
import java.util.List;

import com.microsservicos.shoppingapi.dto.ReportDto;
import com.microsservicos.shoppingapi.dto.ShopDto;

public interface ReportService {
  List<ShopDto> getShopByFilters(Date start, Date end, Float min);
  ReportDto getReportByDate(Date start, Date end);
}
