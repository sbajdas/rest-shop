package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientTransaction;
import com.bajdas.rest_shop.model.Product;
import com.bajdas.rest_shop.product.ProductFinder;
import com.bajdas.rest_shop.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

  @Mock
  private TransactionRepository transactionRepositoryMock;

  @Mock
  private ProductFinder productFinderMock;

  @Mock
  private TransactionManipulator manipulatorMock;

  @InjectMocks
  private TransactionService transactionService;
  @Captor
  private ArgumentCaptor<ClientTransaction> transactionCaptor;

  private final Product expectedProduct = Product.builder()
      .name("test name")
      .price(BigDecimal.ONE)
      .build();
  private static final BigDecimal QUANTITY = BigDecimal.valueOf(10.0);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(productFinderMock.findProduct(eq(1L))).thenReturn(expectedProduct);
  }

  @Test
  void shouldStartNewTransactionWhenIdNotProvided() {
    //given
    when(manipulatorMock.startNew(any(), any(Product.class)))
        .thenReturn(createTransaction(expectedProduct, QUANTITY));

    //when
    transactionService.createTransaction(1L, QUANTITY);

    //then
    verify(transactionRepositoryMock, never()).findById(anyLong());
    verify(transactionRepositoryMock).save(any(ClientTransaction.class));
  }

  @Test
  void shouldThrowExceptionWhenTransactionIdNotFound() {
    //given
    when(transactionRepositoryMock.findById(eq(180L))).thenReturn(Optional.empty());

    //when
    assertThrows(TransactionNotFoundException.class,
        () -> transactionService.addToTransaction(180L, 1L, QUANTITY));

    //then
    verify(transactionRepositoryMock).findById(eq(180L));
    verifyNoMoreInteractions(transactionRepositoryMock);
  }

  @Test
  void shouldSaveTransactionWithProductAmount() {
    //given
    when(manipulatorMock.startNew(any(), any(Product.class)))
        .thenReturn(createTransaction(expectedProduct, QUANTITY));

    //when
    transactionService.createTransaction(1L, QUANTITY);

    //then
    verify(transactionRepositoryMock, never()).findById(anyLong());
    verify(transactionRepositoryMock).save(transactionCaptor.capture());
    var actual = transactionCaptor.getValue();
    assertEquals(1, actual.getItems().size());
    assertEquals(expectedProduct, actual.getItems().get(0).getItem());
    assertEquals(QUANTITY, actual.getItems().get(0).getQuantity());
  }

  private ClientTransaction createTransaction(Product product, BigDecimal quantity) {
    ClientTransaction transaction = new ClientTransaction();
    transaction.addItem(product, quantity);
    return transaction;

  }

}