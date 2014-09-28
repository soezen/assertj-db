package org.assertj.db.api;

import static org.assertj.db.error.ShouldBeType.shouldBeType;
import static org.assertj.db.error.ShouldHaveRowsSize.shouldHaveRowsSize;

import java.util.List;

import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.Failures;
import org.assertj.db.type.AbstractDbData;
import org.assertj.db.type.Column;

/**
 * Assertion methods about the data in a <code>{@link Column}</code>.
 * 
 * @author Régis Pouiller
 * 
 * @param <E> The class of the actual value (an sub-class of {@link AbstractDbData}).
 * @param <D> The class of the original assert (an sub-class of {@link AbstractDbAssert}).
 * @param <C> The class of this assert (an sub-class of {@link AbstractColumnAssert}).
 * @param <CV> The class of this assertion on the value (an sub-class of {@link AbstractColumnValueAssert}).
 * @param <R> The class of the equivalent row assert (an sub-class of {@link AbstractRowAssert}).
 * @param <RV> The class of the equivalent row assertion on the value (an sub-class of {@link AbstractRowValueAssert}).
 */
public abstract class AbstractColumnAssert<E extends AbstractDbData<E>, D extends AbstractDbAssert<E, D, C, CV, R, RV>, C extends AbstractColumnAssert<E, D, C, CV, R, RV>, CV extends AbstractColumnValueAssert<E, D, C, CV, R, RV>, R extends AbstractRowAssert<E, D, C, CV, R, RV>, RV extends AbstractRowValueAssert<E, D, C, CV, R, RV>>
    extends AbstractSubAssert<E, D, C, CV, C, CV, R, RV> {

  /**
   * Column on which do the assertion.
   */
  private Column column;

  /**
   * To notice failures in the assertion.
   */
  private static Failures failures = Failures.instance();

  /**
   * Constructor.
   * 
   * @param originalDbAssert The original assert. That could be a {@link RequestAssert} or a {@link TableAssert}.
   * @param selfType Class of this assert (the sub assert) : a sub-class of {@code AbstractSubAssert}.
   * @param valueType Class of the assert on the value : a sub-class of {@code AbstractValueAssert}.
   */
  AbstractColumnAssert(D originalDbAssert, Class<C> selfType, Class<CV> valueType, Column column) {
    super(originalDbAssert, selfType, valueType);
    this.column = column;
  }

  /** {@inheritDoc} */
  @Override
  protected List<Object> getValuesList() {
    return column.getValuesList();
  }

  /** {@inheritDoc} */
  @Override
  protected void assertHasSize(WritableAssertionInfo info, int expected) {
    List<Object> valuesList = column.getValuesList();
    int size = valuesList.size();
    if (size != expected) {
      throw failures.failure(info, shouldHaveRowsSize(size, expected));
    }
  }

  /**
   * Verifies that the type of the values of the column is equal to the type in parameter.
   * <p>
   * Example where the assertion verifies that all the values in the {@code Column} called "title" of the {@code Table} is of
   * type {@code TEXT} :
   * </p>
   * 
   * <pre>
   * assertThat(table).column(&quot;title&quot;).isOfType(ValueType.TEXT, false);
   * </pre>
   * <p>
   * Example where the assertion verifies that all the values in the {@code Column} called "title" of the {@code Table} is of
   * type {@code TEXT} or not identified (for example {@code null}) :
   * </p>
   * 
   * <pre>
   * assertThat(table).column(&quot;title&quot;).isOfType(ValueType.TEXT, true);
   * </pre>
   * 
   * @param expected The expected type to compare to.
   * @param lenient {@code true} if the test is lenient : if the type of a value is not identified (for example when the
   *          value is {@code null}), it consider that it is ok.
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is different to the type in parameter.
   */
  public C isOfType(ValueType expected, boolean lenient) {
    for (Object value : getValuesList()) {
      ValueType type = ValueType.getType(value);
      if (type != expected && (!lenient || type != ValueType.NOT_IDENTIFIED)) {
        throw failures.failure(info, shouldBeType(value, expected, type));
      }
    }
    return myself;
  }

  /**
   * Verifies that the type of the values of the column is a number.
   * <p>
   * Example where the assertion verifies that all the values in the {@code Column} called "year" of the first {@code Row} of
   * the {@code Table} is a number :
   * </p>
   * 
   * <pre>
   * assertThat(table).column(&quot;year&quot;).isNumber(true);
   * </pre>
   * 
   * @param lenient {@code true} if the test is lenient : if the type of a value is not identified (for example when the
   *          value is {@code null}), it consider that it is ok.
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a number.
   */
  public C isNumber(boolean lenient) {
    return isOfType(ValueType.NUMBER, lenient);
  }

  /**
   * Verifies that the type of the values of the column is a boolean.
   * <p>
   * Example where the assertion verifies that all the values in the first {@code Column} of the first {@code Row} of the
   * {@code Table} is a boolean :
   * </p>
   * 
   * <pre>
   * assertThat(table).column().isBoolean(false);
   * </pre>
   * 
   * @param lenient {@code true} if the test is lenient : if the type of a value is not identified (for example when the
   *          value is {@code null}), it consider that it is ok.
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a number.
   */
  public C isBoolean(boolean lenient) {
    return isOfType(ValueType.BOOLEAN, lenient);
  }

  /**
   * Verifies that the type of the values of the column is a date.
   * <p>
   * Example where the assertion verifies that all the values in the {@code Column} called "birth" of the first {@code Row}
   * of the {@code Table} is a date :
   * </p>
   * 
   * <pre>
   * assertThat(table).column(&quot;birth&quot;).isDate(false);
   * </pre>
   * 
   * @param lenient {@code true} if the test is lenient : if the type of a value is not identified (for example when the
   *          value is {@code null}), it consider that it is ok.
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a number.
   */
  public C isDate(boolean lenient) {
    return isOfType(ValueType.DATE, lenient);
  }

  /**
   * Verifies that the type of the values of the column is a time.
   * <p>
   * Example where the assertion verifies that all the values in the first {@code Column} of the first {@code Row} of the
   * {@code Table} is a time :
   * </p>
   * 
   * <pre>
   * assertThat(table).column().isTime(false);
   * </pre>
   * 
   * @param lenient {@code true} if the test is lenient : if the type of a value is not identified (for example when the
   *          value is {@code null}), it consider that it is ok.
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a number.
   */
  public C isTime(boolean lenient) {
    return isOfType(ValueType.TIME, lenient);
  }

  /**
   * Verifies that the type of the values of the column is a date/time.
   * <p>
   * Example where the assertion verifies that all the values in the first {@code Column} of the first {@code Row} of the
   * {@code Table} is a date/time :
   * </p>
   * 
   * <pre>
   * assertThat(table).column().isDateTime(false);
   * </pre>
   * 
   * @param lenient {@code true} if the test is lenient : if the type of a value is not identified (for example when the
   *          value is {@code null}), it consider that it is ok.
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a number.
   */
  public C isDateTime(boolean lenient) {
    return isOfType(ValueType.DATE_TIME, lenient);
  }

  /**
   * Verifies that the type of the values of the column is a array of bytes.
   * <p>
   * Example where the assertion verifies that all the values in the first {@code Column} of the first {@code Row} of the
   * {@code Table} is a array of bytes :
   * </p>
   * 
   * <pre>
   * assertThat(table).column().isBytes(false);
   * </pre>
   * 
   * @param lenient {@code true} if the test is lenient : if the type of a value is not identified (for example when the
   *          value is {@code null}), it consider that it is ok.
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a number.
   */
  public C isBytes(boolean lenient) {
    return isOfType(ValueType.BYTES, lenient);
  }

  /**
   * Verifies that the type of the values of the column is a text.
   * <p>
   * Example where the assertion verifies that all the values in the {@code Column} called "title" of the first {@code Row}
   * of the {@code Table} is a text :
   * </p>
   * 
   * <pre>
   * assertThat(table).column(&quot;title&quot;).isText(false);
   * </pre>
   * 
   * @param lenient {@code true} if the test is lenient : if the type of a value is not identified (for example when the
   *          value is {@code null}), it consider that it is ok.
   * @return {@code this} assertion object.
   * @throws AssertionError If the type is not a number.
   */
  public C isText(boolean lenient) {
    return isOfType(ValueType.TEXT, lenient);
  }
}
