package com.microsservicos.shoppingapi.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
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
  public List<Shop> getShopByFilters(Date start, Date end, Float min) {
    validateStartDate(start);
    StringBuilder str = createStringQuery(end, min);
    Query query = createQueryWithParameters(start, end, min, str);
    List<?> result = query.getResultList();
    return result.stream().map(Shop.class::cast).toList();
  }

  private void validateStartDate(Date start) {
    if (Objects.isNull(start)) {
      throw new IllegalArgumentException("Start date cannot be null.");
    }
  }

  private Query createQueryWithParameters(Date start, Date end, Float min, StringBuilder str) {
    Query query = entityManager.createQuery(str.toString());
    query.setParameter("start", start);
    if (end != null) {
      query.setParameter("end", end);
    }
    if (min != null) {
      query.setParameter("min", min);
    }
    return query;
  }

  private StringBuilder createStringQuery(Date end, Float min) {
    StringBuilder str = new StringBuilder();
    str.append("SELECT s FROM shop s WHERE s.date >= CAST(:start AS DATE)");
    if (end != null) {
      str.append(" and s.date <= CAST(:end AS DATE)");
    }
    if (min != null) {
      str.append(" and s.total >= :min");
    }
    return str;
  }

  @Override
  public ReportDto getReportByDate(Date start, Date end) {
    validateStartDate(start);
    StringBuilder strQuery = new StringBuilder();
    strQuery.append(
        "SELECT COUNT(sp.id), SUM(sp.total), AVG(sp.total) FROM shopping.shop sp WHERE sp.date >= CAST(:start AS DATE) AND sp.date <= CAST(:end AS DATE)");
    Query query = entityManager.createNativeQuery(strQuery.toString());
    query.setParameter("start", start);
    query.setParameter("end", end);

    Object[] result = (Object[]) query.getSingleResult();
    
    return new ReportDto((Long) result[0], getValue(result[1]), getValue(result[2]));
  }

  private double getValue(Object result) {
    double total = 0.0;
    if (Objects.nonNull(result)) {
      total = ((BigDecimal) result).doubleValue();
    }
    return total;
  }

}
