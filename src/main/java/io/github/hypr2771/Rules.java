package io.github.hypr2771;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Rules are a set of rule used to validate a component. They will combine before returning the
 * whole set of violated rules.
 * <br>
 *
 * Even tho you can create a simple set of rules for your whole application doing
 * {@link Rules}&lt;?, ?&gt;, we encourage you to create a set of rules for each items.
 * This will enhance generics behavior of your rules while still keeping the type safe nature
 * offered by {@link Rules}.
 * <br>
 *
 * <br>
 * <hr>
 * <br>
 *
 * The only visible method present is {@link Rules#validate(Object)}, used to validate a <code>T</code>
 * type. In case {@link Rules#validate(Object)} fails, you will instead have a
 * {@link List}<code>&lt;E&gt;</code>.
 * <br>
 *
 * To implement your own set of {@link Rules}, you can simply inherit {@link Rules}, then use the
 * {@link #append(Error, Predicate)} to populate your rules in constructor.
 *
 * <br> An example of a simple {@link Rules} implementation could be as follow :
 *
 * <pre>
 * public class AdminRules extends {@link Rules}&lt;{@link String}, {@link String}&gt; {
 *
 *   public AdminRules() {
 *     this.{@link #append}(() -&gt; "Please, at least try something smart...", target -&gt; null != target &amp;&amp; !target.isBlank())
 *         .{@link #append}(() -&gt; "Not an admin", target -&gt; target.equals("มะแว้ง"));
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
 *     return "goSu";
 *   }
 *
 *   return "lambda";
 * }
 * </pre>
 *
 * @param <T> the class to validate for the given set of rules
 * @param <E> the type of the error
 */
public abstract class Rules<T, E> {

  private List<Rule<T, E>> ruleList = new ArrayList<>();

  /**
   * Computes all rules in order and returns <em>all</em> failed ones.
   *
   * @param candidate the object to test conformity on
   * @return either an instance of <code>T</code> on the left if it passes all tests, or {@link
   * List} of failed {@link Error} on right
   */
  public Validated<T, E> validate(T candidate) {
    var errors = ruleList.stream().filter(rule -> !rule.apply(candidate)).map(rule -> rule.error).collect(
        Collectors.toList());

    return errors.isEmpty() ? Validated.valid(candidate) : Validated.error(errors);
  }

  /**
   * Add a rule to the existing set of rules
   *
   * @param error the error to add to the {@link List} of {@link Error} if validator predicate
   * fails
   * @param validator the predicate used to test the <code>T</code> against
   * @return the new set of rules
   */
  protected Rules<T, E> append(Error<E> error, Predicate<T> validator) {
    ruleList.add(new Rule<>(error, validator));
    return this;
  }

  /**
   * Represents an unique {@link Rule} in a whole {@link Rules}.
   *
   * @param <U> the type of the object to validate
   * @param <O> the type of the error if validation fails
   */
  private class Rule<U, O> {

    private final Error<O>     error;
    private final Predicate<U> validator;

    private Rule(final Error<O> error, final Predicate<U> validator) {
      this.error     = error;
      this.validator = validator;
    }

    private boolean apply(U candidate) {
      return validator.test(candidate);
    }
  }

}
