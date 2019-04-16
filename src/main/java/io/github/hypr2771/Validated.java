package io.github.hypr2771;

import java.util.List;
import java.util.Optional;

/**
 * Represents a object which has been validated through {@link Rules#validate(Object)}.
 * <br>
 *
 * <br>
 * <hr>
 * <br>
 *
 * A common implementation could be as :
 *
 * <pre>
 * public class AdminRules extends {@link Rules}&lt;{@link String}, {@link String}&gt; {
 *
 *   public AdminRules() {
 *     this.{@link Rules#append}(() -&gt; "Please, at least try something smart...", target -&gt; null != target &amp;&amp; !target.isBlank())
 *         .{@link Rules#append}(() -&gt; "Not an admin", target -&gt; target.equals("มะแว้ง"));
 *   }
 *
 * }
 * </pre>
 *
 * <br>
 *   Then you could use it as follows :
 *
 * <pre>
 * private final AdminRules adminRules;
 *
 * public String adminPanel(String name){
 *
 *   var adminValidation = adminRules.{@link #validate}(name);
 *   if(adminValidation.isValid()){
 *     return String.format("Welcome, master %s", adminValidation.get());
 *   }
 *
 *   return computeErrors(adminValidation.errors());
 * }
 * </pre>
 *
 * @param <T> the type of the object to validate
 * @param <E> the type of every errors
 */
public class Validated<T, E> {

  private Validated(T validate) {
    this.validate = validate;
    this.errors   = null;
  }

  private Validated(List<Error<E>> errors) {
    this.validate = null;
    this.errors   = errors;
  }

  private T              validate;
  private List<Error<E>> errors;

  /**
   * @return whether the {@link Rules#validate(Object)} succeeded and the supplied object was valid according to all set of supplied {@link io.github.hypr2771.Rules.Rule}s
   */
  public boolean isValid() {
    return validate != null;
  }

  /**
   * @return whether the {@link Rules#validate(Object)} failed and the supplied object was invalid for at least one of the supplied set of {@link io.github.hypr2771.Rules.Rule}
   */
  public boolean isError() {
    return errors != null;
  }

  /**
   * @return the valid object
   * @throws NullPointerException if the object was invalid
   */
  public T get() {
    return Optional.of(validate).get();
  }

  /**
   * @return the list of error which failed the validation, <em>ordered</em> as provided in {@link Rules}
   * @throws NullPointerException if the object was valid
   */
  public List<Error<E>> errors() {
    return Optional.of(errors).get();
  }

  static <T, E> Validated<T, E> valid(final T candidate) {
    return new Validated<>(candidate);
  }

  static <T, E> Validated<T, E> error(final List<Error<E>> errors) {
    return new Validated<>(errors);
  }
}
