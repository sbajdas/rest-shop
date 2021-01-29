package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientTransaction;
import com.bajdas.rest_shop.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionManipulatorTest {

  private final TransactionManipulator manipulator = new TransactionManipulator();
  private static final Product TEST_PRODUCT = Product.builder()
      .name("sample name")
      .price(BigDecimal.ONE)
      .build();
  private static final Product ANOTHER_TEST_PRODUCT = Product.builder()
      .name("different test name")
      .price(BigDecimal.TEN)
      .build();

  @Test
  void shouldStartNewTransactionWithProductQuantity() {
    //given
    var expected = getTransactionWithItem();

    //when
    var actual = manipulator.startNew(BigDecimal.ONE, TEST_PRODUCT);

    //then
    assertEquals(expected, actual);
  }

  @Test
  void shouldAddNewItemToTransaction() {
    //given
    var transaction = new ClientTransaction();

    //when
    var actual = manipulator.addItem(transaction, TEST_PRODUCT, BigDecimal.ONE);

    //then
    assertEquals(1, actual.getItems().size());
    assertEquals(TEST_PRODUCT, actual.getItems().get(0).getItem());
    assertEquals(BigDecimal.ONE, actual.getItems().get(0).getQuantity());
  }

  @Test
  void shouldAddQuantityToExistingItemInTransaction() {
    //given
    var transaction = getTransactionWithItem();

    //when
    var actual = manipulator.addItem(transaction, TEST_PRODUCT, BigDecimal.ONE);

    //then
    assertEquals(1, actual.getItems().size());
    assertEquals(TEST_PRODUCT, actual.getItems().get(0).getItem());
    assertEquals(0, BigDecimal.valueOf(2.0).compareTo(actual.getItems().get(0).getQuantity()));
  }


  @Test
  void shouldAddNewProductToItemsInTransaction() {
    //given
    var transaction = getTransactionWithItem();

    //when
    var actual = manipulator.addItem(transaction, ANOTHER_TEST_PRODUCT, BigDecimal.ONE);

    //then
    assertEquals(2, actual.getItems().size());
    assertTrue(actual.getItems().stream().anyMatch(pq -> pq.containsProduct(ANOTHER_TEST_PRODUCT)));
    assertTrue(actual.getItems().stream().anyMatch(pq -> pq.containsProduct(TEST_PRODUCT)));
  }

  private ClientTransaction getTransactionWithItem() {
    var expected = new ClientTransaction();
    expected.addItem(TEST_PRODUCT, BigDecimal.ONE);
    return expected;
  }

}