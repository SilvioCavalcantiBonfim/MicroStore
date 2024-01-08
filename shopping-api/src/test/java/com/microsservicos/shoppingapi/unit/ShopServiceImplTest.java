package com.microsservicos.shoppingapi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microsservicos.dto.CategoryDto;
import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ItemInputDto;
import com.microsservicos.dto.ProductDto;
import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.dto.ShopInputDto;
import com.microsservicos.dto.UserOutputDto;
import com.microsservicos.exception.IllegalProductException;
import com.microsservicos.exception.ShopNotFoundException;
import com.microsservicos.exception.UserNotFoundException;
import com.microsservicos.shoppingapi.model.Item;
import com.microsservicos.shoppingapi.model.Shop;
import com.microsservicos.shoppingapi.repository.ShopRepository;
import com.microsservicos.shoppingapi.service.ProductService;
import com.microsservicos.shoppingapi.service.UserService;
import com.microsservicos.shoppingapi.service.impl.ShopServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ShopServiceImplTest {

  private static ItemDto ITEM_1_DTO = new ItemDto("XYZ000", 10.0f, 1);
  private static Item ITEM_1 = new Item();
  private static ItemDto ITEM_2_DTO = new ItemDto("XYZ001", 5.5f, 2);
  private static Item ITEM_2 = new Item();

  @Mock
  private ShopRepository shopRepository;

  @Mock
  private UserService userService;

  @Mock
  private ProductService productService;

  @InjectMocks
  private ShopServiceImpl shopService;

  private static ShopOutputDto ORDER_1_DTO;
  private static Shop ORDER_1 = new Shop();
  private static ShopOutputDto ORDER_2_DTO;
  private static Shop ORDER_2 = new Shop();

  private static ShopInputDto ORDER_INPUT;

  private static LocalDateTime date = LocalDateTime.now();

  private static UserOutputDto user;

  private static ProductDto product;

  @BeforeEach
  private void setup() {

    product = new ProductDto("XYZ001", "product test", "test", 5.5f, new CategoryDto(1L, "test"));

    user = new UserOutputDto("User Test", "ABC123", "street test", "test@email.com", "000000000", LocalDateTime.now(),
        "0000");

    ITEM_1.setProductIdentifier("XYZ000");
    ITEM_1.setPrice(10.0f);
    ITEM_1.setAmount(1);

    ITEM_2.setProductIdentifier("XYZ001");
    ITEM_2.setPrice(5.5f);
    ITEM_2.setAmount(2);

    ORDER_1_DTO = new ShopOutputDto("ABC123", 10.0, date, List.of(ITEM_1_DTO, ITEM_2_DTO));

    ORDER_1.setId(1L);
    ORDER_1.setUserIdentifier("ABC123");
    ORDER_1.setTotal(10.0);
    ORDER_1.setDate(date);
    ORDER_1.setItens(List.of(ITEM_1, ITEM_2));

    ORDER_2_DTO = new ShopOutputDto("DEF456", 0.0, date, List.of(ITEM_2_DTO));

    ORDER_2.setId(2L);
    ORDER_2.setUserIdentifier("DEF456");
    ORDER_2.setTotal(0.0);
    ORDER_2.setDate(date);
    ORDER_2.setItens(List.of(ITEM_2));

    ORDER_INPUT = new ShopInputDto("DEF456", List.of(new ItemInputDto("XYZ001", 2)));
  }

  @Test
  public void getAllShopSuccessTest() {
    when(shopRepository.findAll()).thenReturn(List.of(ORDER_1, ORDER_2));

    List<ShopOutputDto> result = shopService.retrieveAllShops();
    assertThat(result)
        .extracting("userIdentifier", "total", "date", "itens")
        .contains(
            new Tuple("ABC*23", 10.0, date, List.of(ITEM_1_DTO, ITEM_2_DTO)),
            new Tuple("DEF*56", 0.0, date, List.of(ITEM_2_DTO)));

  }

  @Test
  public void getByUserSuccessTest() {
    when(shopRepository.findAllByUserIdentifier(any())).thenReturn(List.of(ORDER_1));
    when(userService.retrieveUserByCpf(anyString(), anyString())).thenReturn(user);

    List<ShopOutputDto> result = shopService.retrieveShopsByUser("ABC123", "0000");
    assertThat(result)
        .extracting("userIdentifier", "total", "date", "itens")
        .contains(
            new Tuple("ABC123", 10.0, date, List.of(ITEM_1_DTO, ITEM_2_DTO)));

  }

  @Test
  public void getByDateSuccessTest() {
    when(shopRepository.findAllByDateAfter(any())).thenReturn(List.of(ORDER_1, ORDER_2));

    List<ShopOutputDto> result = shopService.retrieveShopsByDate(date.toLocalDate());
    assertThat(result)
        .extracting("userIdentifier", "total", "date", "itens")
        .contains(
            new Tuple("ABC*23", 10.0, date, List.of(ITEM_1_DTO, ITEM_2_DTO)),
            new Tuple("DEF*56", 0.0, date, List.of(ITEM_2_DTO)));

  }

  @Test
  public void findShopByIdSuccessTest() {
    when(shopRepository.findById(any())).thenReturn(Optional.of(ORDER_1));

    ShopOutputDto result = shopService.retrieveShopById(1L);
    assertThat(result).isEqualTo(ORDER_1_DTO.obfuscate());
  }

  @Test
  public void findShopByIdNotFoundTest() {
    when(shopRepository.findById(any())).thenReturn(Optional.empty());

    assertThatExceptionOfType(ShopNotFoundException.class).isThrownBy(() -> shopService.retrieveShopById(1L));
  }

  @Test
  public void findShopByIdWithNullIdTest() {
    when(shopRepository.findById(any())).thenReturn(Optional.empty());

    assertThatExceptionOfType(ShopNotFoundException.class).isThrownBy(() -> shopService.retrieveShopById(null));
  }

  @Test
  public void saveUserNotFoundExceptionTest() {
    when(userService.retrieveUserByCpf(anyString(), anyString())).thenReturn(null);

    assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> shopService.addShop(ORDER_INPUT, "0000"));
  }

  @Test
  public void saveIllegalProductExceptionWithProductServiceTest() {
    when(userService.retrieveUserByCpf(anyString(), anyString())).thenReturn(user);
    when(productService.retrieveProductByIdentifier(anyString())).thenReturn(null);

    assertThatExceptionOfType(IllegalProductException.class).isThrownBy(() -> shopService.addShop(ORDER_INPUT, "0000"));
  }

  @Test
  public void saveSuccessTest() {
    when(userService.retrieveUserByCpf(anyString(), anyString())).thenReturn(user);
    when(shopRepository.save(any())).thenReturn(ORDER_2);
    when(productService.retrieveProductByIdentifier(anyString())).thenReturn(product);

    ShopOutputDto result = shopService.addShop(ORDER_INPUT, "0000");

    assertThat(result).isEqualTo(ORDER_2_DTO);

  }

  @Test
  public void saveWithUserIdenfierNull() {
    when(userService.retrieveUserByCpf(any(), anyString())).thenReturn(null);

    ORDER_INPUT = new ShopInputDto(null, List.of(new ItemInputDto("XYZ001", 2)));

    assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> shopService.addShop(ORDER_INPUT, "0000"));
  }

  @Test
  public void saveWithListItemsNull() {
    when(userService.retrieveUserByCpf(anyString(), anyString())).thenReturn(user);
   
    ORDER_INPUT = new ShopInputDto("DEF456", null);

    assertThatExceptionOfType(IllegalProductException.class).isThrownBy(() -> shopService.addShop(ORDER_INPUT, "0000"));

  }
  @Test
  public void saveWithListItemsEmpty() {
    when(userService.retrieveUserByCpf(anyString(), anyString())).thenReturn(user);
   
    ORDER_INPUT = new ShopInputDto("DEF456", List.of());

    assertThatExceptionOfType(IllegalProductException.class).isThrownBy(() -> shopService.addShop(ORDER_INPUT, "0000"));

  }
}
