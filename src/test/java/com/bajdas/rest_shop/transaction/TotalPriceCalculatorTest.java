package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.Product;
import com.bajdas.rest_shop.model.ProductQuantity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TotalPriceCalculatorTest {

  private final TotalPriceCalculator priceCalculator = new TotalPriceCalculator();
  private static final Product SAMPLE_PRODUCT = Product.builder()
      .name("sample product")
      .price(BigDecimal.TEN)
      .build();

  @Test
  void shouldReturn0ForEmptyItemsList() {
    //given
    List<ProductQuantity> items = new ArrayList<>();

    //when
    var totalPrice = priceCalculator.calculate(items);

    //then
    assertEquals(0, BigDecimal.ZERO.compareTo(totalPrice));
  }

  @Test
  void shouldReturnPriceForOneItemInList() {
    //given
    List<ProductQuantity> items = new ArrayList<>();
    items.add(new ProductQuantity(SAMPLE_PRODUCT, BigDecimal.ONE));

    //when
    var totalPrice = priceCalculator.calculate(items);

    //then
    assertEquals(0, BigDecimal.TEN.compareTo(totalPrice));
  }

  @Test
  void shouldReturnPriceForOneItemWithMoreQuantity() {
    //given
    List<ProductQuantity> items = new ArrayList<>();
    items.add(new ProductQuantity(SAMPLE_PRODUCT, BigDecimal.TEN));

    //when
    var totalPrice = priceCalculator.calculate(items);

    //then
    assertEquals(0, BigDecimal.valueOf(100).compareTo(totalPrice));
  }


  @Test
  void shouldReturnPriceForManyItemsWithMoreQuantity() {
    //given
    var secondProduct = Product.builder()
        .name("sample product")
        .price(BigDecimal.ONE)
        .build();

    var thirdProduct = Product.builder()
        .name("sample product")
        .price(BigDecimal.valueOf(13))
        .build();

    List<ProductQuantity> items = new ArrayList<>();
    items.add(new ProductQuantity(SAMPLE_PRODUCT, BigDecimal.TEN));
    items.add(new ProductQuantity(secondProduct, BigDecimal.valueOf(13)));
    items.add(new ProductQuantity(thirdProduct, BigDecimal.valueOf(2)));

    //when
    var totalPrice = priceCalculator.calculate(items);

    //then
    assertEquals(0, BigDecimal.valueOf(139).compareTo(totalPrice));
  }

}