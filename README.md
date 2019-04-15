# Validate

## Why

I was bored doing much validation before entering a method and I wanted
a simple way to validate ***any*** object before treating it.

## How

You can use it doing :

### 1. Create your validator from `Rules`

```java
  private class TestRules extends Rules<String> {
    public TestRules() {
      super();
      this.append(() -> "Object is null", Objects::nonNull)
          .append(() -> "Object is too short", target -> Objects.nonNull(target) && target.length() > 2);
    }
  }
```

### 2. Instantiate it the way you want

As it is simply constructed in a conventional way, you can either use your
constructor, or autowiring, or any dark magic you wish.

```java
  var rules = new TestRules();
```

### 3. Start validating your objects

```java
    var shouldHaveTwoErrors = rules.validate(null);
    var shouldHaveOneError = rules.validate("");
    var shouldBeOk = rules.validate("blabla");

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
```
