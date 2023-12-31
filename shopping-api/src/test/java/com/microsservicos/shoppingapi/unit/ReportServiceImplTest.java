package com.microsservicos.shoppingapi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ReportDto;
import com.microsservicos.dto.ShopDto;
import com.microsservicos.shoppingapi.model.Item;
import com.microsservicos.shoppingapi.model.Shop;
import com.microsservicos.shoppingapi.repository.ShopRepository;
import com.microsservicos.shoppingapi.service.impl.ReportServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

  @Mock
  private ShopRepository shopRepository;

  @InjectMocks
  private ReportServiceImpl reportService;

  private static Shop ORDER_1 = new Shop();
  private static Item ITEM_1 = new Item();
  private static Item ITEM_2 = new Item();
  private static ItemDto ITEM_1_DTO = new ItemDto("XYZ000", 10.0f);
  private static ItemDto ITEM_2_DTO = new ItemDto("XYZ001", 5.5f);

    private static Date date;

  @BeforeEach
  private void setup() {
    date = new Date();

    ITEM_1.setProductIdentifier("XYZ000");
    ITEM_1.setPrice(10.0f);

    ITEM_2.setProductIdentifier("XYZ001");
    ITEM_2.setPrice(5.5f);

    ORDER_1.setId(1L);
    ORDER_1.setUserIdentifier("ABC123");
    ORDER_1.setTotal(10.0);
    ORDER_1.setDate(date);
    ORDER_1.setItens(List.of(ITEM_1, ITEM_2));
  }

  @Test
  public void getShopByFiltersWithAllArgsNotNull(){
    when(shopRepository.getShopByFilters(any(), any(), any())).thenReturn(List.of(ORDER_1));

    List<ShopDto> result = reportService.getShopByFilters(date, date, 100.0f);

    assertThat(result).extracting("userIdentifier", "total", "date", "itens")
    .contains(new Tuple("ABC123",10.0,date, List.of(ITEM_1_DTO, ITEM_2_DTO)));
  }

  @Test
  public void getShopByFiltersWithStartNull(){
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> reportService.getShopByFilters(null, date, 100.0f));
  }

  @Test
  public void getShopByFiltersWithEndNull(){
    when(shopRepository.getShopByFilters(any(), any(), any())).thenReturn(List.of(ORDER_1));

    List<ShopDto> result = reportService.getShopByFilters(date, null, 100.0f);

    assertThat(result).extracting("userIdentifier", "total", "date", "itens")
    .contains(new Tuple("ABC123",10.0,date, List.of(ITEM_1_DTO, ITEM_2_DTO)));
  }

  @Test
  public void getShopByFiltersWithMinNull(){
    when(shopRepository.getShopByFilters(any(), any(), any())).thenReturn(List.of(ORDER_1));

    List<ShopDto> result = reportService.getShopByFilters(date, date, null);

    assertThat(result).extracting("userIdentifier", "total", "date", "itens")
    .contains(new Tuple("ABC123",10.0,date, List.of(ITEM_1_DTO, ITEM_2_DTO)));
  }

  @Test
  public void getReportByDateWithAllArgsNotNull(){
    ReportDto expected = new ReportDto(10L, 100.0, 100.0);

    when(shopRepository.getReportByDate(any(), any())).thenReturn(expected);

    ReportDto result = reportService.getReportByDate(date, date);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void getReportByDateWithStartNull(){
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> reportService.getReportByDate(null, date));
  }

  @Test
  public void getReportByDateWithEndNull(){
    ReportDto expected = new ReportDto(10L, 100.0, 100.0);

    when(shopRepository.getReportByDate(any(), any())).thenReturn(expected);

    ReportDto result = reportService.getReportByDate(date, null);

    assertThat(result).isEqualTo(expected);
  }
}
