package net.vince.validated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.Test;

public class RulesTest {

  private class TestRules extends Rules<String> {

    public TestRules() {
      super();
      this.append(() -> "Object is null", Objects::nonNull)
          .append(() -> "Object is too short", target -> Objects.nonNull(target) && target.length() > 2);
    }

  }

  @Test
  public void validate() {

    var cut = new TestRules();

    var shouldHaveTwoErrors = cut.validate(null);
    var shouldHaveOneError = cut.validate("");
    var shouldBeOk = cut.validate("blabla");

    assertTrue(shouldHaveTwoErrors.isError());
    assertFalse(shouldHaveTwoErrors.isValid());
    assertNotNull(shouldHaveTwoErrors.errors());
    assertEquals(2, shouldHaveTwoErrors.errors().size());
    assertNotNull(shouldHaveTwoErrors.errors().get(0));
    assertEquals("Object is null", shouldHaveTwoErrors.errors().get(0).message());
    assertNotNull(shouldHaveTwoErrors.errors().get(1));
    assertEquals("Object is too short", shouldHaveTwoErrors.errors().get(1).message());
    assertThrows(NullPointerException.class, shouldHaveTwoErrors::get);

    assertTrue(shouldHaveOneError.isError());
    assertFalse(shouldHaveOneError.isValid());
    assertNotNull(shouldHaveOneError.errors());
    assertEquals(1, shouldHaveOneError.errors().size());
    assertNotNull(shouldHaveOneError.errors().get(0));
    assertEquals("Object is too short", shouldHaveOneError.errors().get(0).message());
    assertThrows(NullPointerException.class, shouldHaveOneError::get);

    assertFalse(shouldBeOk.isError());
    assertTrue(shouldBeOk.isValid());
    assertNotNull(shouldBeOk.get());
    assertEquals("blabla", shouldBeOk.get());
    assertThrows(NullPointerException.class, shouldBeOk::errors);

  }
}