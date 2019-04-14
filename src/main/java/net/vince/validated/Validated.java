package net.vince.validated;

import java.util.List;
import java.util.Optional;

public class Validated<T> {

  private Validated(T validate) {
    this.validate = validate;
    this.errors   = null;
  }

  private Validated(List<Error<T>> errors) {
    this.validate = null;
    this.errors   = errors;
  }

  private T              validate;
  private List<Error<T>> errors;

  public boolean isValid() {
    return validate != null;
  }

  public boolean isError() {
    return errors != null;
  }

  public T get() {
    return Optional.of(validate).get();
  }

  public List<Error<T>> errors() {
    return Optional.of(errors).get();
  }

  static <T> Validated<T> valid(final T candidate) {
    return new Validated<>(candidate);
  }

  static <T> Validated<T> error(final List<Error<T>> errors) {
    return new Validated<>(errors);
  }
}
