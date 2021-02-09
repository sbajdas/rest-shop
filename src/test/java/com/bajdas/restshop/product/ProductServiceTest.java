package com.bajdas.restshop.product;

import com.bajdas.restshop.model.Product;
import com.bajdas.restshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ProductServiceTest {

  private static final String EXPECTED_PRODUCT_NAME = "sample name";
  @Mock
  private ProductRepository productRepositoryMock;
  @InjectMocks
  private ProductService finder;
  private final Product expectedProduct = Product.builder()
      .id(1L)
      .name(EXPECTED_PRODUCT_NAME)
      .price(BigDecimal.ONE)
      .build();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldFindProductById() {
    //given
    when(productRepositoryMock.findById(eq(1L))).thenReturn(Optional.of(expectedProduct));

    //when
    var actual = finder.findProduct(1L);

    //then
    assertEquals(expectedProduct, actual);
  }

  @Test
  void shouldThrowExceptionWhenProductNotFoundById() {
    //given
    when(productRepositoryMock.findById(eq(1L))).thenReturn(Optional.empty());

    //when
    assertThrows(ProductNotFoundException.class, () -> finder.findProduct(1L));
  }

  @Test
  void shouldFindProductUsingSearchByIdWhenProvided() {
    //given
    when(productRepositoryMock.findById(eq(1L))).thenReturn(Optional.of(expectedProduct));

    //when
    finder.findProduct(1L, null);

    //then
    verify(productRepositoryMock).findById(anyLong());
    verifyNoMoreInteractions(productRepositoryMock);
  }

  @Test
  void shouldFindProductUsingSearchByNameWhenProvided() {
    //given
    when(productRepositoryMock.findByName(eq(EXPECTED_PRODUCT_NAME))).thenReturn(Optional.of(expectedProduct));

    //when
    finder.findProduct(null, EXPECTED_PRODUCT_NAME);

    //then
    verify(productRepositoryMock).findByName(anyString());
    verifyNoMoreInteractions(productRepositoryMock);
  }

  @Test
  void shouldThrowExceptionWhenProductNotFoundByIdOrName() {

    //when
    assertThrows(ProductNotFoundException.class, () -> finder.findProduct(8L, null));
  }


}