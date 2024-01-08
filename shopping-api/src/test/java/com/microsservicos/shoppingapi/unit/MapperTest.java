package com.microsservicos.shoppingapi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.AbstractCollectionAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.shoppingapi.model.Item;
import com.microsservicos.shoppingapi.model.Shop;
import com.microsservicos.shoppingapi.util.Mapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
public class MapperTest {

  private Item ITEM = new Item();
  private ItemDto ITEMDTO;
  private Shop SHOP = new Shop();
  private ShopOutputDto SHOPDTO;
  private static Validator validator;

  @BeforeAll
  private static void setupAll() {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();

  }

  @BeforeEach
  private void setup() {
    ITEM.setProductIdentifier("ABC123");
    ITEM.setPrice(10.0f);
    ITEM.setAmount(1);

    ITEMDTO = new ItemDto(ITEM.getProductIdentifier(), ITEM.getPrice(), ITEM.getAmount());

    SHOP.setId(null);
    SHOP.setUserIdentifier("RST789");
    SHOP.setTotal(10.0);
    SHOP.setDate(LocalDateTime.now());
    SHOP.setItens(List.of(ITEM));

    SHOPDTO = new ShopOutputDto(SHOP.getUserIdentifier(), SHOP.getTotal(), SHOP.getDate(), List.of(ITEMDTO));
  }

  @Test
  public void itemToItemDtoNonNull() {
    ItemDto result = Mapper.convertItemToDto(ITEM);

    assertThat(result).isEqualTo(ITEMDTO);
  }

  @Test
  public void itemToItemDtoWithProductIdentifierNull() {

    ITEM.setProductIdentifier(null);

    ItemDto result = Mapper.convertItemToDto(ITEM);

    ItemDto expected = new ItemDto(null, ITEM.getPrice(), ITEM.getAmount());

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void itemToItemDtoWithPriceNull() {

    ITEM.setPrice(null);
    ItemDto result = Mapper.convertItemToDto(ITEM);

    ItemDto expected = new ItemDto(ITEM.getProductIdentifier(), null, ITEM.getAmount());

    assertThat(result).isEqualTo(expected);
  }

    @Test
  public void itemToItemDtoWithAmountNull() {

    ITEM.setAmount(null);
    ItemDto result = Mapper.convertItemToDto(ITEM);

    ItemDto expected = new ItemDto(ITEM.getProductIdentifier(), ITEM.getPrice(), null);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void itemToItemDtoWithProductIdentifierBlank() {

    ITEM.setProductIdentifier("");

    ItemDto result = Mapper.convertItemToDto(ITEM);

    ItemDto expected = new ItemDto("", ITEM.getPrice(), ITEM.getAmount());

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void itemToItemDtoNull() {
    assertThatThrownBy(() -> Mapper.convertItemToDto(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid argument provided.");
  }

  @Test
  public void itemDtoToItemNonNull() {
    Item result = Mapper.convertDtoToItem(ITEMDTO);

    violation(ITEMDTO).isEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(ITEM);
  }

  @Test
  public void itemDtoToItemWithProductIdentifierNull() {

    ItemDto nullDto = new ItemDto(null, ITEM.getPrice(), ITEM.getAmount());

    Item result = Mapper.convertDtoToItem(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(nullDto);
  }

  @Test
  public void itemDtoToItemWithProductIdentifierBlank() {

    ItemDto nullDto = new ItemDto("", ITEM.getPrice(), ITEM.getAmount());

    Item result = Mapper.convertDtoToItem(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(nullDto);
  }

  @Test
  public void itemDtoToItemWithPriceNull() {

    ItemDto nullDto = new ItemDto(ITEM.getProductIdentifier(), null, ITEM.getAmount());

    Item result = Mapper.convertDtoToItem(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(nullDto);
  }

  @Test
  public void itemDtoToItemNull() {
    assertThatThrownBy(() -> Mapper.convertDtoToItem(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid argument provided.");
  }

  @Test
  public void shopToShopDtoNonNull() {
    ShopOutputDto result = Mapper.convertShopToDto(SHOP);

    assertThat(result).isEqualTo(SHOPDTO);
  }

  @Test
  public void shopToShopDtoNull() {
    assertThatThrownBy(() -> Mapper.convertShopToDto(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid argument provided.");
  }

  @Test
  public void shopToShopDtoUserIdentifierNull() {

    SHOP.setUserIdentifier(null);

    ShopOutputDto result = Mapper.convertShopToDto(SHOP);

    ShopOutputDto expected = new ShopOutputDto(null, SHOP.getTotal(), SHOP.getDate(), List.of(ITEMDTO));

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void shopToShopDtoTotalNull() {
    SHOP.setTotal(null);

    ShopOutputDto result = Mapper.convertShopToDto(SHOP);

    ShopOutputDto expected = new ShopOutputDto(SHOP.getUserIdentifier(), null, SHOP.getDate(), List.of(ITEMDTO));

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void shopToShopDtoDateNull() {
    SHOP.setDate(null);

    ShopOutputDto result = Mapper.convertShopToDto(SHOP);

    ShopOutputDto expected = new ShopOutputDto(SHOP.getUserIdentifier(), SHOP.getTotal(), null, List.of(ITEMDTO));

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void shopToShopDtoItemsNull() {
    SHOP.setItens(null);

    ShopOutputDto result = Mapper.convertShopToDto(SHOP);

    ShopOutputDto expected = new ShopOutputDto(SHOP.getUserIdentifier(), SHOP.getTotal(), SHOP.getDate(), List.of());

    assertThat(result).isEqualTo(expected);
  }
  @Test
  public void shopToShopDtoItemsEmpty() {
    SHOP.setItens(List.of());

    ShopOutputDto result = Mapper.convertShopToDto(SHOP);

    ShopOutputDto expected = new ShopOutputDto(SHOP.getUserIdentifier(), SHOP.getTotal(), SHOP.getDate(), List.of());

    assertThat(result).isEqualTo(expected);
  }

  // 

  @Test
  public void shopDtoToShopNonNull() {
    Shop result = Mapper.convertDtoToShop(SHOPDTO);

    violation(SHOPDTO).isEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(SHOP);
  }

  @Test
  public void shopDtoToShopWithProductIdentifierNull() {

    ShopOutputDto nullDto = new ShopOutputDto(null, SHOP.getTotal(), SHOP.getDate(), List.of(ITEMDTO));

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of(ITEM));

    Shop result = Mapper.convertDtoToShop(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void shopDtoToShopWithProductIdentifierBlank() {

    ShopOutputDto nullDto = new ShopOutputDto("", SHOP.getTotal(), SHOP.getDate(), List.of(ITEMDTO));

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of(ITEM));

    Shop result = Mapper.convertDtoToShop(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

   @Test
  public void shopDtoToShopWithTotalNull() {

    ShopOutputDto nullDto = new ShopOutputDto(SHOP.getUserIdentifier(), null, SHOP.getDate(), List.of(ITEMDTO));

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of(ITEM));

    Shop result = Mapper.convertDtoToShop(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

   @Test
  public void shopDtoToShopWithDateNull() {

    ShopOutputDto nullDto = new ShopOutputDto(SHOP.getUserIdentifier(), SHOP.getTotal(), null, List.of(ITEMDTO));

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of(ITEM));

    Shop result = Mapper.convertDtoToShop(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void shopDtoToShopWithItensNull() {

    ShopOutputDto nullDto = new ShopOutputDto(SHOP.getUserIdentifier(), SHOP.getTotal(), SHOP.getDate(), null);

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of());

    Shop result = Mapper.convertDtoToShop(nullDto);

    violation(nullDto).isEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void shopDtoToShopWithItensEmpty() {

    ShopOutputDto nullDto = new ShopOutputDto(SHOP.getUserIdentifier(), SHOP.getTotal(), SHOP.getDate(), List.of());

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of());

    Shop result = Mapper.convertDtoToShop(nullDto);

    violation(nullDto).isEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void shopDtoToShopNull() {
    assertThatThrownBy(() -> Mapper.convertDtoToShop(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid argument provided.");
  }


  private <T> AbstractCollectionAssert<?, Collection<? extends ConstraintViolation<T>>, ConstraintViolation<T>, ObjectAssert<ConstraintViolation<T>>> violation(
      T dto) {
    Set<ConstraintViolation<T>> violations = validator.validate(dto);
    return assertThat(violations);
  }
}
