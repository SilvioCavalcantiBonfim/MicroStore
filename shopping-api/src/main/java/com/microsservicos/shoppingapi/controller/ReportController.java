package com.microsservicos.shoppingapi.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microsservicos.dto.ReportDto;
import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.shoppingapi.service.ReportService;

@RestController
@RequestMapping("/shopping")
public class ReportController {

  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public List<ShopOutputDto> getShopsByFilter(
      @RequestParam(name = "start", required = true) 
      @DateTimeFormat(pattern = "dd/MM/yyyy") 
      LocalDate start,
      @RequestParam(name = "end", required = false) 
      @DateTimeFormat(pattern = "dd/MM/yyyy") 
      LocalDate end, 
      @RequestParam(name = "min", required = false) 
      Float min) {
    return reportService.getShopByFilters(start, end, min);
  }

  @GetMapping("/report")
  @ResponseStatus(HttpStatus.OK)
  public ReportDto getReportByDate(
      @RequestParam(name = "start", required = true) 
      @DateTimeFormat(pattern = "dd/MM/yyyy") 
      LocalDate start,
      @RequestParam(name = "end", required = true) 
      @DateTimeFormat(pattern = "dd/MM/yyyy") 
      LocalDate end) {
    return reportService.getReportByDate(start, end);
  }
}
