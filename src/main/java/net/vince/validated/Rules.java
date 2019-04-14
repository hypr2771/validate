package net.vince.validated;

import io.vavr.collection.List;
import java.util.function.Predicate;

/**
 * Rules are a set of rule used to validate a component. They will combine before returning the
 * whole set of violated rules.<br/>
 *
 * Even tho you can create a simple set of rules for your whole application doing
 * <code>Rules<?></code>, we encourage you to create a set of rules for each items.
 * This will enhance generics behavior of your rules while still keeping the type safe nature
 * offered by <code>Rules</code>.<br/>
 *
 * <br/>
 * <hr>
 * <br/>
 *
 * The only visible method present is {@link Rules#validate(T)}, used to validate a <code>T</code>
 * type.<br/>
 *
 * To implement your own set of {@link Rules}, you can simply inherit {@link Rules}, then use the
 * {@link #append(Error, Predicate)} to populate your rules in constructor.
 *
 * <br/> An example of a simple {@link Rules} implementation could be as follow :
 *
 * <pre>
 * public class AdminRules extends {@link Rules}&lt;{@link String}&gt; {
 *
 *   public AdminRules() {
 *     this.{@link #append}(() -> "Please, at least try something smart...", target -> null != target && !target.isBlank())
 *         .{@link #append}(() -> "Not an admin", target -> target.equals("มะแว้ง"));
 *   }
 *
 * }
 * </pre>
 *
 * <br/> Then you could use it as follows :
 *
 * <pre>
 *   private final AdminRules adminRules;
 *
 *   public String adminPanel(String name){
 *
 *     var adminValidation = adminRules.{@link #validate}(name);
 *     if(adminValidation.isLeft()){
 *       return "goSu";
 *     }
 *
 *     return "lambda";
 *   }
 * </pre>
 *
 * @param <T> the class to validate for the given set of rules
 */
public abstract class Rules<T> {

  private List<Rule<T>> ruleList = List.empty();

  /**
   * Computes all rules in order and returns <em>all</em> failed ones.
   *
   * @param candidate the object to test conformity on
   * @return either an instance of <code>T</code> on the left if it passes all tests, or {@link
   * List} of failed {@link Error} on right
   */
  public Validated<T> validate(T candidate) {
    var errors = ruleList.filter(rule -> !rule.apply(candidate)).map(rule -> rule.error).toList();

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
  protected Rules<T> append(Error error, Predicate<T> validator) {
    ruleList = ruleList.append(new Rule<>(error, validator));
    return this;
  }

  private class Rule<U> {

    private final Error        error;
    private final Predicate<U> validator;

    Rule(final Error error, final Predicate<U> validator) {
      this.error     = error;
      this.validator = validator;
    }

    private boolean apply(U candidate) {
      return validator.test(candidate);
    }
  }

}
