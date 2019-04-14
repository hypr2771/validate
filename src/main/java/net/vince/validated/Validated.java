package net.vince.validated;

import io.vavr.collection.List;
import io.vavr.control.Option;

public class Validated<T> {

  private Validated(T validate) {
    this.validate = Option.of(validate);
    this.errors   = Option.none();
  }

  private Validated(List<Error> errors) {
    this.validate = Option.none();
    this.errors   = Option.of(errors);
  }

  private Option<T>           validate;
  private Option<List<Error>> errors;

  public boolean isValid(){
    return validate.isDefined();
  }

  public boolean isError(){
    return errors.isDefined();
  }

  public T get(){
    return validate.getOrElseThrow(NullPointerException::new);
  }

  public List<Error> errors(){
    return errors.getOrElseThrow(NullPointerException::new);
  }

  static <T> Validated<T> valid(final T candidate) {
    return new Validated<>(candidate);
  }

  static <T> Validated<T> error(final List<Error> errors) {
    return new Validated<>(errors);
  }
}
