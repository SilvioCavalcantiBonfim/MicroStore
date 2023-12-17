package com.microsservicos.shoppingapi.repository.impl;

import java.util.Date;
import java.util.List;

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
    StringBuilder str = createStringQuery(end, min);
    Query query = createQuery(start, end, min, str);
    List<?> result = query.getResultList();
    return result.stream().map(e -> (Shop) e).toList();
  }

  private Query createQuery(Date start, Date end, Float min, StringBuilder str) {
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
    str.append("select s from shop s where s.date >= :start");
    if (end != null) {
      str.append(" and s.date <= :end");
    }
    if (min != null) {
      str.append(" and s.total <= :min");
    }
    return str;
  }

  @Override
  public ReportDto getReportByDate(Date start, Date end) {
    StringBuilder strQuery = new StringBuilder();
    strQuery.append(
        "select count(sp.id), sum(sp.total), avg(sp.total) from shopping.shop sp where sp.date >= :start and sp.date <= :end");
    Query query = entityManager.createNativeQuery(strQuery.toString());
    query.setParameter("start", start);
    query.setParameter("end", end);

    Object[] result = (Object[]) query.getSingleResult();

    return new ReportDto((Long) result[0], (Double) result[1],  (Double) result[2]);
  }

}
