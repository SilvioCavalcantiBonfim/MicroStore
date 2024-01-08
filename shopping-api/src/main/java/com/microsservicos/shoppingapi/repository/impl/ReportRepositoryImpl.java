package com.microsservicos.shoppingapi.repository.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.microsservicos.dto.ReportDto;
import com.microsservicos.shoppingapi.model.Shop;
import com.microsservicos.shoppingapi.repository.ReportRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

public class ReportRepositoryImpl implements ReportRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Shop> retrieveShopsByDateAndTotal(LocalDate start, LocalDate end, Float min) {
    validateStartDate(start);
    StringBuilder str = buildQueryForShops(end, min);
    Query query = createQueryWithDateAndTotalParameters(start, end, min, str);
    List<?> result = query.getResultList();
    return result.stream().map(Shop.class::cast).toList();
  }

  private void validateStartDate(LocalDate start) {
    if (Objects.isNull(start)) {
      throw new IllegalArgumentException("Start date cannot be null.");
    }
  }

  private Query createQueryWithDateAndTotalParameters(LocalDate start, LocalDate end, Float min, StringBuilder str) {
    Query query = entityManager.createQuery(str.toString());
    query.setParameter("start", start);
    if (Objects.nonNull(end)) {
      query.setParameter("end", end);
    }
    if (Objects.nonNull(min)) {
      query.setParameter("min", min);
    }
    return query;
  }

  private StringBuilder buildQueryForShops(LocalDate end, Float min) {
    StringBuilder str = new StringBuilder();
    str.append("SELECT s FROM shop s WHERE s.date >= CAST(:start AS DATE)");
    if (Objects.nonNull(end)) {
      str.append(" and s.date <= CAST(:end AS DATE)");
    }
    if (Objects.nonNull(min)) {
      str.append(" and s.total >= :min");
    }
    return str;
  }

  @Override
  public ReportDto generateReportByDate(LocalDate start, LocalDate end) {
    validateStartDate(start);
    StringBuilder strQuery = new StringBuilder();
    strQuery.append(
        "SELECT COUNT(sp.id), SUM(sp.total), AVG(sp.total) FROM shopping.shop sp WHERE sp.date >= CAST(:start AS DATE) AND sp.date <= CAST(:end AS DATE)");
    Query query = entityManager.createNativeQuery(strQuery.toString());
    query.setParameter("start", start);
    query.setParameter("end", end);

    Object[] result = (Object[]) query.getSingleResult();

    return new ReportDto((Long) result[0], convertToDouble(result[1]), convertToDouble(result[2]));
  }

  private double convertToDouble(Object result) {
    try {
      BigDecimal convert = (BigDecimal) result;
      return convert.doubleValue();
    } catch (RuntimeException e) {
      return 0.0;
    }
  }

}
