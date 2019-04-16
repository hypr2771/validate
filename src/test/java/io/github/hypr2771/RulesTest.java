package io.github.hypr2771;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RulesTest {

  private class TestRules extends Rules<String, String> {

    public TestRules() {
      super();
      this.append(() -> "Object is null", Objects::nonNull)
          .append(() -> "Object is too short", target -> Objects.nonNull(target) && target.length() > 2);
    }

  }

  private class TestRulesWithException extends Rules<String, Exception> {

    public TestRulesWithException() {
      super();
      this.append(() -> new NullPointerException("Object is null"), Objects::nonNull)
          .append(() -> new IllegalArgumentException("Object is too short"), target -> Objects.nonNull(target) && target.length() > 2);
    }

  }

  @Test
  @DisplayName("WHEN validate Strings with String errors")
  public void validateStringsWithStringErrors() {

    var cut = new TestRules();

    var shouldHaveTwoErrors = cut.validate(null);
    var shouldHaveOneError = cut.validate("");
    var shouldBeOk = cut.validate("blabla");

    assertTrue(shouldHaveTwoErrors.isError());
    assertFalse(shouldHaveTwoErrors.isValid());
    assertNotNull(shouldHaveTwoErrors.errors());
    assertEquals(2, shouldHaveTwoErrors.errors().size());
    assertNotNull(shouldHaveTwoErrors.errors().get(0));
    assertEquals("Object is null", shouldHaveTwoErrors.errors().get(0).get());
    assertNotNull(shouldHaveTwoErrors.errors().get(1));
    assertEquals("Object is too short", shouldHaveTwoErrors.errors().get(1).get());
    assertThrows(NullPointerException.class, shouldHaveTwoErrors::get);

    assertTrue(shouldHaveOneError.isError());
    assertFalse(shouldHaveOneError.isValid());
    assertNotNull(shouldHaveOneError.errors());
    assertEquals(1, shouldHaveOneError.errors().size());
    assertNotNull(shouldHaveOneError.errors().get(0));
    assertEquals("Object is too short", shouldHaveOneError.errors().get(0).get());
    assertThrows(NullPointerException.class, shouldHaveOneError::get);

    assertFalse(shouldBeOk.isError());
    assertTrue(shouldBeOk.isValid());
    assertNotNull(shouldBeOk.get());
    assertEquals("blabla", shouldBeOk.get());
    assertThrows(NullPointerException.class, shouldBeOk::errors);

  }

  @Test
  @DisplayName("WHEN validate Strings with Exception errors")
  public void validateStringWithExceptionErrors() {

    var cut = new TestRulesWithException();

    var shouldHaveTwoErrors = cut.validate(null);
    var shouldHaveOneError = cut.validate("");
    var shouldBeOk = cut.validate("blabla");

    assertTrue(shouldHaveTwoErrors.isError());
    assertFalse(shouldHaveTwoErrors.isValid());
    assertNotNull(shouldHaveTwoErrors.errors());
    assertEquals(2, shouldHaveTwoErrors.errors().size());
    assertNotNull(shouldHaveTwoErrors.errors().get(0));
    assertNotNull(shouldHaveTwoErrors.errors().get(0).get());
    assertEquals(new NullPointerException("Object is null").getMessage(), shouldHaveTwoErrors.errors().get(0).get().getMessage());
    assertNotNull(shouldHaveTwoErrors.errors().get(1));
    assertNotNull(shouldHaveTwoErrors.errors().get(1).get());
    assertEquals(new IllegalArgumentException("Object is too short").getMessage(), shouldHaveTwoErrors.errors().get(1).get().getMessage());
    assertThrows(NullPointerException.class, shouldHaveTwoErrors::get);

    assertTrue(shouldHaveOneError.isError());
    assertFalse(shouldHaveOneError.isValid());
    assertNotNull(shouldHaveOneError.errors());
    assertEquals(1, shouldHaveOneError.errors().size());
    assertNotNull(shouldHaveOneError.errors().get(0));
    assertNotNull(shouldHaveOneError.errors().get(0).get());
    assertEquals(new IllegalArgumentException("Object is too short").getMessage(), shouldHaveOneError.errors().get(0).get().getMessage());
    assertThrows(NullPointerException.class, shouldHaveOneError::get);

    assertFalse(shouldBeOk.isError());
    assertTrue(shouldBeOk.isValid());
    assertNotNull(shouldBeOk.get());
    assertEquals("blabla", shouldBeOk.get());
    assertThrows(NullPointerException.class, shouldBeOk::errors);

  }
}