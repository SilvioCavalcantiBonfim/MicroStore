package com.microsservicos.shoppingapi.repository;

import java.util.Date;
import java.util.List;

import com.microsservicos.dto.ReportDto;
import com.microsservicos.shoppingapi.model.Shop;

public interface ReportRepository {
  List<Shop> getShopByFilters(Date start, Date end, Float min);
  ReportDto getReportByDate(Date start, Date end);
}
