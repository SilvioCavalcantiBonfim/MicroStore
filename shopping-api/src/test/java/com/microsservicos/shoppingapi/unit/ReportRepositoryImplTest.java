package com.microsservicos.shoppingapi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microsservicos.dto.ReportDto;
import com.microsservicos.shoppingapi.model.Shop;
import com.microsservicos.shoppingapi.repository.impl.ReportRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@ExtendWith(MockitoExtension.class)
public class ReportRepositoryImplTest {

  @Mock
  private EntityManager entityManager;

  @InjectMocks
  private ReportRepositoryImpl reportRepository;

  private Query query = mock(Query.class);

  @Test
  public void getShopByFiltersSuccessAllArgsNoNull() {

    when(entityManager.createQuery(anyString())).thenReturn(query);

    when(query.getResultList()).thenReturn(new ArrayList<>());
    List<Shop> result = reportRepository.getShopByFilters(LocalDate.now(), LocalDate.now(), 0.0f);

    assertThat(result).isEmpty();
  }

  @Test
  public void getShopByFiltersSuccessWithEndNull() {

    when(entityManager.createQuery(anyString())).thenReturn(query);

    when(query.getResultList()).thenReturn(new ArrayList<>());
    List<Shop> result = reportRepository.getShopByFilters(LocalDate.now(), null, 0.0f);

    assertThat(result).isEmpty();
  }

  @Test
  public void getShopByFiltersSuccessWithMinNull() {

    when(entityManager.createQuery(anyString())).thenReturn(query);

    when(query.getResultList()).thenReturn(new ArrayList<>());
    List<Shop> result = reportRepository.getShopByFilters(LocalDate.now(), LocalDate.now(), null);

    assertThat(result).isEmpty();
  }

  @Test
  public void getShopByFiltersWithStartNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reportRepository.getShopByFilters(null, LocalDate.now(), 0.0f));
  }

  @Test
  public void getReportByDateSuccessWithEndNoNull() {

    when(entityManager.createNativeQuery(anyString())).thenReturn(query);

    when(query.getSingleResult()).thenReturn(new Object[] { 1L, BigDecimal.valueOf(100.0), BigDecimal.valueOf(100.0) });

    ReportDto result = reportRepository.getReportByDate(LocalDate.now(), LocalDate.now());

    ReportDto expected = new ReportDto(1L, 100.0, 100.0);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void getReportByDateWithStartNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reportRepository.getReportByDate(null, LocalDate.now()));
  }

    @Test
  public void getReportByDateSuccessWithEndNull() {

    when(entityManager.createNativeQuery(anyString())).thenReturn(query);

    when(query.getSingleResult()).thenReturn(new Object[] { 1L, BigDecimal.valueOf(100.0), BigDecimal.valueOf(100.0)});

    ReportDto result = reportRepository.getReportByDate(LocalDate.now(), null);

    ReportDto expected = new ReportDto(1L, 100.0, 100.0);

    assertThat(result).isEqualTo(expected);
  }
}
