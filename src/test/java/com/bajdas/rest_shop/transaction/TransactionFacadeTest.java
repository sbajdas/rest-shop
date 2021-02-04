package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientTransaction;
import com.bajdas.rest_shop.model.ClientTransactionDto;
import com.bajdas.rest_shop.model.Product;
import com.bajdas.rest_shop.notification.Observer;
import com.bajdas.rest_shop.notification.Status;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TransactionFacadeTest {

  private static final long SAMPLE_TRANSACTION_ID = 1L;
  @Mock
  private TransactionRepository transactionRepositoryMock;
  @Mock
  private ProductFinder productFinderMock;
  @Mock
  private TransactionManipulator manipulatorMock;
  @Mock
  private TotalPriceCalculator calculatorMock;
  @Mock
  private Observer observerMock;
  @Mock
  private List<Observer> observers;

  @InjectMocks
  private TransactionFacade transactionFacade;
  @Captor
  private ArgumentCaptor<ClientTransaction> transactionCaptor;

  private static final long SAMPLE_PRODUCT_ID = 1L;
  private static final Product SAMPLE_PRODUCT = Product.builder()
      .id(SAMPLE_PRODUCT_ID)
      .name("test name")
      .price(BigDecimal.ONE)
      .build();
  private static final BigDecimal QUANTITY = BigDecimal.valueOf(10.0);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    transactionFacade.registerObserver(observerMock);
    when(productFinderMock.findProduct(eq(SAMPLE_PRODUCT_ID))).thenReturn(SAMPLE_PRODUCT);
  }

  @Test
  void shouldStartNewTransactionWhenIdNotProvided() {
    //given
    var transaction = prepareSampleTransaction();
    when(manipulatorMock.startNew(any(), any(Product.class)))
        .thenReturn(transaction);
    when(transactionRepositoryMock.save(any(ClientTransaction.class))).thenReturn(transaction);

    //when
    transactionFacade.createTransaction(SAMPLE_PRODUCT_ID, QUANTITY);

    //then
    verify(transactionRepositoryMock, never()).findById(anyLong());
    verify(transactionRepositoryMock).save(any(ClientTransaction.class));
  }


  @Test
  void shouldAddItemToTransaction() {
    //given
    var expectedQuantity = BigDecimal.valueOf(20);
    var transaction = prepareSampleTransaction();
    when(transactionRepositoryMock.findById(eq(SAMPLE_TRANSACTION_ID))).thenReturn(Optional.of(transaction));
    when(manipulatorMock.addItem(eq(transaction), any(Product.class), any(BigDecimal.class)))
        .thenCallRealMethod();

    when(transactionRepositoryMock.save(transactionCaptor.capture())).thenReturn(transaction);

    //when
    transactionFacade.addToTransaction(SAMPLE_TRANSACTION_ID, SAMPLE_PRODUCT_ID, QUANTITY);

    //then
    var actualTransaction = transactionCaptor.getValue();
    var actualQuantity = actualTransaction.getItems().get(0).getQuantity();
    assertEquals(1, actualTransaction.getItems().size());
    assertEquals(SAMPLE_PRODUCT, actualTransaction.getItems().get(0).getItem());
    assertEquals(0, expectedQuantity.compareTo(actualQuantity));
  }

  @Test
  void shouldThrowExceptionWhenAddingToNonExistingTransaction() {
    //given
    when(transactionRepositoryMock.findById(eq(180L))).thenReturn(Optional.empty());

    //then
    assertThrows(TransactionNotFoundException.class,
        () -> transactionFacade.addToTransaction(180L, SAMPLE_PRODUCT_ID, QUANTITY));
    verify(transactionRepositoryMock).findById(eq(180L));
    verifyNoMoreInteractions(transactionRepositoryMock);
  }

  @Test
  void shouldSaveNewTransactionWithProductAmount() {
    //given
    var transaction = prepareSampleTransaction();
    when(manipulatorMock.startNew(any(), any(Product.class)))
        .thenReturn(transaction);

    when(transactionRepositoryMock.save(transactionCaptor.capture())).thenReturn(transaction);

    //when
    transactionFacade.createTransaction(SAMPLE_PRODUCT_ID, QUANTITY);

    //then
    verify(transactionRepositoryMock, never()).findById(anyLong());
    var actual = transactionCaptor.getValue();
    assertEquals(1, actual.getItems().size());
    assertEquals(SAMPLE_PRODUCT, actual.getItems().get(0).getItem());
    assertEquals(QUANTITY, actual.getItems().get(0).getQuantity());
  }

  @Test
  void shouldThrowExceptionWhenTransactionIsAlreadyCompleted() {
    //given
    var transaction = prepareSampleTransaction();
    transaction.setCompleted(true);
    when(transactionRepositoryMock.findById(eq(SAMPLE_TRANSACTION_ID))).thenReturn(Optional.of(transaction));

    //then
    assertThrows(TransactionCompletedException.class,
        () -> transactionFacade.addToTransaction(1L, SAMPLE_PRODUCT_ID, QUANTITY));
    verify(transactionRepositoryMock, never()).save(any(ClientTransaction.class));
  }

  @Test
  void shouldFinishTransactionAndNotifyObservers() {
    //given
    var transaction = prepareSampleTransaction();
    when(transactionRepositoryMock.findById(eq(SAMPLE_TRANSACTION_ID))).thenReturn(Optional.of(transaction));
    when(transactionRepositoryMock.save(transactionCaptor.capture())).thenReturn(transaction);

    //when
    transactionFacade.finishTransaction(SAMPLE_TRANSACTION_ID);

    //then
    var actual = transactionCaptor.getValue();
    assertTrue(actual.isCompleted());
    verify(observerMock, times(1)).update(eq(Status.TRANSACTION_COMPLETED), any(ClientTransactionDto.class));
  }

  @Test
  void shouldNotFinishAlreadyCompletedTransaction() {
    //given
    var transaction = prepareSampleTransaction();
    transaction.setCompleted(true);
    when(transactionRepositoryMock.findById(eq(SAMPLE_TRANSACTION_ID))).thenReturn(Optional.of(transaction));

    //when
    assertThrows(TransactionCompletedException.class, () -> transactionFacade.finishTransaction(SAMPLE_TRANSACTION_ID));
    verify(transactionRepositoryMock, never()).save(any(ClientTransaction.class));
    verify(observerMock, never()).update(any(Status.class), any(ClientTransactionDto.class));
  }

  private ClientTransaction prepareSampleTransaction() {
    var transaction = new ClientTransaction();
    transaction.setTransactionId(1L);
    transaction.addItem(SAMPLE_PRODUCT, QUANTITY);
    return transaction;

  }

}