# Validate

## Why

I was bored doing much validation before entering a method and I wanted
a simple way to validate ***any*** object before treating it.

## How

### 1. Dependency

#### Maven
```
<dependency>
  <groupId>io.github.hypr2771</groupId>
  <artifactId>validated</artifactId>
  <version>1.3</version>
</dependency>
```

#### Gradle
```
compile "io.github.hypr2771:validated:1.3"
```

### 2. Create your own set of `Rules`

```java
  private class TestRules extends Rules<String, String> {
    public TestRules() {
      super();
      this.append(() -> "Object is null", Objects::nonNull)
          .append(() -> "Object is too short", target -> Objects.nonNull(target) && target.length() > 2);
    }
  }
```

### 3. Instantiate it the way you want

As it is simply constructed in a conventional way, you can either use your
constructor, or autowiring, or any dark magic you wish.

```java
  var rules = new TestRules();
```

### 4. Start validating your objects

```java
    var shouldHaveTwoErrors = rules.validate(null);
    var shouldHaveOneError = rules.validate("");
    var shouldBeOk = rules.validate("blabla");

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
```

### 5. Go further

You don't have to define the `Rules` only for `String`. `Rules` is generic for both the object to validate and the type of the error to be returned.

If a behavior you expect is not available, code is really simple, so don't hesitate to contribute too.
