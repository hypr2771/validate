package io.github.hypr2771;

/**
 * Represents an error when {@link Rules#validate(Object)} fails.
 *
 * @param <T> the type of the error
 */
@FunctionalInterface
public interface Error<T> {

  /**
   * @return the supplied error
   */
  T get();

}
