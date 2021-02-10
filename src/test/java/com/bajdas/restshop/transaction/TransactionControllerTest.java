package com.bajdas.restshop.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TransactionControllerTest {

  @Mock
  private TransactionService transactionServiceMock;
  @InjectMocks
  private TransactionController controller;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldStartNewTransactionWhenIdNotProvided() {
    //when
    controller.addToBasket(null, Collections.emptyList());

    //then
    verify(transactionServiceMock).createTransaction(any());
    verifyNoMoreInteractions(transactionServiceMock);
  }

  @Test
  void shouldAddToBasketWhenIdProvided() {
    //when
    controller.addToBasket(1L, Collections.emptyList());

    //then
    verify(transactionServiceMock).addToTransaction(eq(1L), any());
    verifyNoMoreInteractions(transactionServiceMock);
  }

  @Test
  void shouldCallFinishTransaction() {
    //when
    controller.finishTransaction(1L);

    //then
    verify(transactionServiceMock).finishTransaction(eq(1L));
    verifyNoMoreInteractions(transactionServiceMock);
  }


  @Test
  void shouldPassException() {
    //given
    when(transactionServiceMock.addToTransaction(any(), anyList())).thenThrow(TransactionCompletedException.class);

    //then
    assertThrows(TransactionCompletedException.class, () -> controller.addToBasket(1L, Collections.emptyList()));
  }

}