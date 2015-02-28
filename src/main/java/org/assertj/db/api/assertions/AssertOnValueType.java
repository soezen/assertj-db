package org.assertj.db.api.assertions;

import org.assertj.db.type.ValueType;

/**
 * Interface containing assertion methods on type of value.
 *
 * @param <T> The "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *            for more details.
 * @author Régis Pouiller
 */
public interface AssertOnValueType<T extends AssertOnValueType<T>> {

  /**
   * Verifies that the type of the value is equal to the type in parameter.
   * <p>
   * Example where the assertion verifies that the value in the {@code Column} called "title" of the second {@code Row}
   * of the {@code Table} is of type {@code TEXT} :
   * </p>
   *
   * <pre>
   * <code class='java'>
   * assertThat(table).row(1).value(&quot;title&quot;).isOfType(ValueType.TEXT);
   * </code>
   * </pre>
   * <p>
   * Example where the assertion verifies that the value in the {@code Column} called "title" of the {@code Row} at end point
   * of the first {@code Change} is of type {@code TEXT} :
   * </p>
   *
   * <pre>
   * <code class='java'>
   * assertThat(changes).change().rowAtEndPoint().value(&quot;title&quot;).isOfType(ValueType.TEXT);
   * </code>
   * </pre>
   *
   * @param expected The expected type to compare to.
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is different to the type in parameter.
   * @see org.assertj.db.api.AbstractValueAssert#isOfType(org.assertj.db.type.ValueType)
   * @see org.assertj.db.api.AbstractAssertWithValues#isOfType(org.assertj.db.type.ValueType)
   */
  public T isOfType(ValueType expected);

  /**
   * Verifies that the type of the value is equal to one of the types in parameters.
   * <p>
   * Example where the assertion verifies that the value in the {@code Column} called "title" of the {@code Row} at end point
   * of the first {@code Change} is of type {@code TEXT} or of type {@code NUMBER} :
   * </p>
   *
   * <pre>
   * <code class='java'>
   * assertThat(changes).change().rowAtEndPoint().value(&quot;title&quot;).isOfType(ValueType.TEXT, ValueType.NUMBER);
   * </code>
   * </pre>
   *
   * @param expected The expected types to compare to.
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is different to all the types in parameters.
   */
  public T isOfAnyOfTypes(ValueType... expected);

  /**
   * Verifies that the value is a number.
   * <p>
   * Example where the assertion verifies that the value in the first {@code Column} of the {@code Row} at end point
   * of the first {@code Change} is a number :
   * </p>
   *
   * <pre>
   * <code class='java'>
   * assertThat(changes).change().rowAtEndPoint().value().isNumber();
   * </code>
   * </pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a number.
   */
  public T isNumber();

  /**
   * Verifies that the value is a boolean.
   * <p>
   * Example where the assertion verifies that the value in the first {@code Column} of the {@code Row} at end point
   * of the first {@code Change} is a boolean :
   * </p>
   *
   * <pre>
   * <code class='java'>
   * assertThat(changes).change().rowAtEndPoint().value().isBoolean();
   * </code>
   * </pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a boolean.
   */
  public T isBoolean();

  /**
   * Verifies that the value is a date.
   * <p>
   * Example where the assertion verifies that the value in the first {@code Column} of the {@code Row} at end point
   * of the first {@code Change} is a date :
   * </p>
   *
   * <pre>
   * <code class='java'>
   * assertThat(changes).change().rowAtEndPoint().value().isDate();
   * </code>
   * </pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a date.
   */
  public T isDate();

  /**
   * Verifies that the value is a time.
   * <p>
   * Example where the assertion verifies that the value in the first {@code Column} of the {@code Row} at end point
   * of the first {@code Change} is a time :
   * </p>
   *
   * <pre>
   * <code class='java'>
   * assertThat(changes).change().rowAtEndPoint().value().isTime();
   * </code>
   * </pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a time.
   */
  public T isTime();

  /**
   * Verifies that the value is a date/time.
   * <p>
   * Example where the assertion verifies that the value in the first {@code Column} of the {@code Row} at end point
   * of the first {@code Change} is a date/time :
   * </p>
   *
   * <pre>
   * <code class='java'>
   * assertThat(changes).change().rowAtEndPoint().value().isDateTime();
   * </code>
   * </pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a date/time.
   */
  public T isDateTime();

  /**
   * Verifies that the value is a array of bytes.
   * <p>
   * Example where the assertion verifies that the value in the first {@code Column} of the {@code Row} at end point
   * of the first {@code Change} is a array of bytes :
   * </p>
   *
   * <pre>
   * <code class='java'>
   * assertThat(changes).change().rowAtEndPoint().value().isBytes();
   * </code>
   * </pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a array of bytes.
   */
  public T isBytes();

  /**
   * Verifies that the value is a text.
   * <p>
   * Example where the assertion verifies that the value in the first {@code Column} of the {@code Row} at end point
   * of the first {@code Change} is a text :
   * </p>
   *
   * <pre>
   * <code class='java'>
   * assertThat(changes).change().rowAtEndPoint().value().isText();
   * </code>
   * </pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a text.
   */
  public T isText();
}