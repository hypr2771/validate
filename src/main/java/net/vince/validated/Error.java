package net.vince.validated;

@FunctionalInterface
public interface Error<T> {

  T message();

}
