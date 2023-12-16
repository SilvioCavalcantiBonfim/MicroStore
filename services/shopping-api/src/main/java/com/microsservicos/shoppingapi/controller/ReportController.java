package com.microsservicos.shoppingapi.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microsservicos.dto.ReportDto;
import com.microsservicos.dto.ShopDto;
import com.microsservicos.shoppingapi.service.ReportService;

@RestController
@RequestMapping("/shopping")
public class ReportController {

  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping("/search")
  public List<ShopDto> getShopsByFilter(
      @RequestParam(name = "start", required = true) 
      @DateTimeFormat(pattern = "dd/MM/yyyy") 
      Date start,
      @RequestParam(name = "end", required = false) 
      @DateTimeFormat(pattern = "dd/MM/yyyy") 
      Date end, 
      @RequestParam(name = "min", required = false) 
      Float min) {
    return reportService.getShopByFilters(start, end, min);
  }

  @GetMapping("/report")
  public ReportDto getReportByDate(
      @RequestParam(name = "start", required = true) 
      @DateTimeFormat(pattern = "dd/MM/yyyy") 
      Date start,
      @RequestParam(name = "end", required = true) 
      @DateTimeFormat(pattern = "dd/MM/yyyy") 
      Date end) {
    return reportService.getReportByDate(start, end);
  }
}
