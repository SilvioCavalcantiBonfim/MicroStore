package com.microsservicos.shoppingapi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import java.util.Date;
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
import com.microsservicos.dto.ShopDto;
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
  private ShopDto SHOPDTO;
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

    ITEMDTO = new ItemDto(ITEM.getProductIdentifier(), ITEM.getPrice());

    SHOP.setId(null);
    SHOP.setUserIdentifier("RST789");
    SHOP.setTotal(10.0);
    SHOP.setDate(new Date());
    SHOP.setItens(List.of(ITEM));

    SHOPDTO = new ShopDto(SHOP.getUserIdentifier(), SHOP.getTotal(), SHOP.getDate(), List.of(ITEMDTO));
  }

  @Test
  public void itemToItemDtoNonNull() {
    ItemDto result = Mapper.itemToItemDto(ITEM);

    assertThat(result).isEqualTo(ITEMDTO);
  }

  @Test
  public void itemToItemDtoWithProductIdentifierNull() {

    ITEM.setProductIdentifier(null);

    ItemDto result = Mapper.itemToItemDto(ITEM);

    ItemDto expected = new ItemDto(null, ITEM.getPrice());

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void itemToItemDtoWithPriceNull() {

    ITEM.setPrice(null);
    ItemDto result = Mapper.itemToItemDto(ITEM);

    ItemDto expected = new ItemDto(ITEM.getProductIdentifier(), null);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void itemToItemDtoWithProductIdentifierBlank() {

    ITEM.setProductIdentifier("");

    ItemDto result = Mapper.itemToItemDto(ITEM);

    ItemDto expected = new ItemDto("", ITEM.getPrice());

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void itemToItemDtoNull() {
    assertThatThrownBy(() -> Mapper.itemToItemDto(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid argument provided.");
  }

  @Test
  public void itemDtoToItemNonNull() {
    Item result = Mapper.itemDtoToItem(ITEMDTO);

    violation(ITEMDTO).isEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(ITEM);
  }

  @Test
  public void itemDtoToItemWithProductIdentifierNull() {

    ItemDto nullDto = new ItemDto(null, ITEM.getPrice());

    Item result = Mapper.itemDtoToItem(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(nullDto);
  }

  @Test
  public void itemDtoToItemWithProductIdentifierBlank() {

    ItemDto nullDto = new ItemDto("", ITEM.getPrice());

    Item result = Mapper.itemDtoToItem(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(nullDto);
  }

  @Test
  public void itemDtoToItemWithPriceNull() {

    ItemDto nullDto = new ItemDto(ITEM.getProductIdentifier(), null);

    Item result = Mapper.itemDtoToItem(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(nullDto);
  }

  @Test
  public void itemDtoToItemNull() {
    assertThatThrownBy(() -> Mapper.itemDtoToItem(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid argument provided.");
  }

  @Test
  public void shopToShopDtoNonNull() {
    ShopDto result = Mapper.shopToShopDto(SHOP);

    assertThat(result).isEqualTo(SHOPDTO);
  }

  @Test
  public void shopToShopDtoNull() {
    assertThatThrownBy(() -> Mapper.shopToShopDto(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid argument provided.");
  }

  @Test
  public void shopToShopDtoUserIdentifierNull() {

    SHOP.setUserIdentifier(null);

    ShopDto result = Mapper.shopToShopDto(SHOP);

    ShopDto expected = new ShopDto(null, SHOP.getTotal(), SHOP.getDate(), List.of(ITEMDTO));

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void shopToShopDtoTotalNull() {
    SHOP.setTotal(null);

    ShopDto result = Mapper.shopToShopDto(SHOP);

    ShopDto expected = new ShopDto(SHOP.getUserIdentifier(), null, SHOP.getDate(), List.of(ITEMDTO));

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void shopToShopDtoDateNull() {
    SHOP.setDate(null);

    ShopDto result = Mapper.shopToShopDto(SHOP);

    ShopDto expected = new ShopDto(SHOP.getUserIdentifier(), SHOP.getTotal(), null, List.of(ITEMDTO));

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void shopToShopDtoItemsNull() {
    SHOP.setItens(null);

    ShopDto result = Mapper.shopToShopDto(SHOP);

    ShopDto expected = new ShopDto(SHOP.getUserIdentifier(), SHOP.getTotal(), SHOP.getDate(), List.of());

    assertThat(result).isEqualTo(expected);
  }
  @Test
  public void shopToShopDtoItemsEmpty() {
    SHOP.setItens(List.of());

    ShopDto result = Mapper.shopToShopDto(SHOP);

    ShopDto expected = new ShopDto(SHOP.getUserIdentifier(), SHOP.getTotal(), SHOP.getDate(), List.of());

    assertThat(result).isEqualTo(expected);
  }

  // 

  @Test
  public void shopDtoToShopNonNull() {
    Shop result = Mapper.shopDtoToShop(SHOPDTO);

    violation(SHOPDTO).isEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(SHOP);
  }

  @Test
  public void shopDtoToShopWithProductIdentifierNull() {

    ShopDto nullDto = new ShopDto(null, SHOP.getTotal(), SHOP.getDate(), List.of(ITEMDTO));

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of(ITEM));

    Shop result = Mapper.shopDtoToShop(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void shopDtoToShopWithProductIdentifierBlank() {

    ShopDto nullDto = new ShopDto("", SHOP.getTotal(), SHOP.getDate(), List.of(ITEMDTO));

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of(ITEM));

    Shop result = Mapper.shopDtoToShop(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

   @Test
  public void shopDtoToShopWithTotalNull() {

    ShopDto nullDto = new ShopDto(SHOP.getUserIdentifier(), null, SHOP.getDate(), List.of(ITEMDTO));

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of(ITEM));

    Shop result = Mapper.shopDtoToShop(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

   @Test
  public void shopDtoToShopWithDateNull() {

    ShopDto nullDto = new ShopDto(SHOP.getUserIdentifier(), SHOP.getTotal(), null, List.of(ITEMDTO));

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of(ITEM));

    Shop result = Mapper.shopDtoToShop(nullDto);

    violation(nullDto).isNotEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void shopDtoToShopWithItensNull() {

    ShopDto nullDto = new ShopDto(SHOP.getUserIdentifier(), SHOP.getTotal(), SHOP.getDate(), null);

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of());

    Shop result = Mapper.shopDtoToShop(nullDto);

    violation(nullDto).isEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void shopDtoToShopWithItensEmpty() {

    ShopDto nullDto = new ShopDto(SHOP.getUserIdentifier(), SHOP.getTotal(), SHOP.getDate(), List.of());

    Shop expected = new Shop();
    expected.setUserIdentifier(nullDto.userIdentifier());
    expected.setDate(nullDto.date());
    expected.setTotal(nullDto.total());
    expected.setItens(List.of());

    Shop result = Mapper.shopDtoToShop(nullDto);

    violation(nullDto).isEmpty();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void shopDtoToShopNull() {
    assertThatThrownBy(() -> Mapper.shopDtoToShop(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid argument provided.");
  }


  private <T> AbstractCollectionAssert<?, Collection<? extends ConstraintViolation<T>>, ConstraintViolation<T>, ObjectAssert<ConstraintViolation<T>>> violation(
      T dto) {
    Set<ConstraintViolation<T>> violations = validator.validate(dto);
    return assertThat(violations);
  }
}
