/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.db.util;

import org.assertj.db.exception.AssertJDBException;
import org.assertj.db.type.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.UUID;

/**
 * Utility methods related to values.
 *
 * @author Régis Pouiller
 * @author Otoniel Isidoro
 */
public class Values {

  /**
   * Private constructor.
   */
  private Values() {
    // Empty
  }

  /**
   * Returns if the value is equal to another value in parameter.
   *
   * @param value    The value.
   * @param expected The other value to compare.
   * @return {@code true} if the value is equal to the value in parameter, {@code false} otherwise.
   */
  public static boolean areEqual(Value value, Object expected) {
    ValueType valueType = value.getValueType();
    switch (valueType) {
    case BOOLEAN:
      if (expected instanceof Boolean) {
        return areEqual(value, (Boolean) expected);
      }
      break;
    case NUMBER:
      if (expected instanceof Number) {
        return areEqual(value, (Number) expected);
      } else if (expected instanceof String) {
        return areEqual(value, (String) expected);
      }
      break;
    case BYTES:
      if (expected instanceof byte[]) {
        return areEqual(value, (byte[]) expected);
      }
      break;
    case TEXT:
      if (expected instanceof String) {
        return areEqual(value, (String) expected);
      }
      break;
    case UUID:
      if (expected instanceof UUID) {
        return areEqual(value, (UUID) expected);
      } else if (expected instanceof String) {
        return areEqual(value, (String) expected);
      }
      break;
    case DATE:
      if (expected instanceof DateValue) {
        return areEqual(value, (DateValue) expected);
      } else if (expected instanceof String) {
        return areEqual(value, (String) expected);
      } else if (expected instanceof Date) {
        return areEqual(value, DateValue.from((Date) expected));
      }
      break;
    case TIME:
      if (expected instanceof TimeValue) {
        return areEqual(value, (TimeValue) expected);
      } else if (expected instanceof String) {
        return areEqual(value, (String) expected);
      } else if (expected instanceof Time) {
        return areEqual(value, TimeValue.from((Time) expected));
      }
      break;
    case DATE_TIME:
      if (expected instanceof DateTimeValue) {
        return areEqual(value, (DateTimeValue) expected);
      } else if (expected instanceof DateValue) {
        return areEqual(value, (DateValue) expected);
      } else if (expected instanceof String) {
        return areEqual(value, (String) expected);
      } else if (expected instanceof Timestamp) {
        return areEqual(value, DateTimeValue.from((Timestamp) expected));
      }
      break;
    default:
      Object object = value.getValue();
      if (expected == null && object == null) {
        return true;
      }
      if (object != null) {
        return object.equals(expected);
      }
    }
    return false;
  }

  /**
   * Returns if the value is equal to the {@code Boolean} in parameter.
   *
   * @param value    The value.
   * @param expected The {@code Boolean} to compare.
   * @return {@code true} if the value is equal to the {@code Boolean} parameter, {@code false} otherwise.
   */
  public static boolean areEqual(Value value, Boolean expected) {
    Object object = value.getValue();
    if (expected == null) {
      return object == null;
    }
    return expected.equals(object);
  }

  /**
   * Returns if the value is equal to the {@code Number} in parameter.
   *
   * @param value    The value.
   * @param expected The {@code Number} to compare.
   * @return {@code true} if the value is equal to the {@code Number} parameter, {@code false} otherwise.
   */
  public static boolean areEqual(Value value, Number expected) {
    Object object = value.getValue();
    if (expected == null) {
      return object == null;
    }

    // If parameter is a BigInteger,
    // change the actual in BigInteger to compare
    if (expected instanceof BigInteger) {
      BigInteger bi;

      if (object instanceof BigInteger) {
        bi = (BigInteger) object;
      } else {
        try {
          bi = new BigInteger("" + object);
        } catch (NumberFormatException e) {
          throw new AssertJDBException("Expected <%s> can not be compared to a BigInteger (<%s>)", expected, object);
        }
      }

      if (bi.compareTo((BigInteger) expected) == 0) {
        return true;
      }
    }
    // If parameter is a BigDecimal,
    // change the value in BigDecimal to compare
    else if (expected instanceof BigDecimal) {
      BigDecimal bd;

      if (object instanceof BigDecimal) {
        bd = (BigDecimal) object;
      } else {
        try {
          bd = new BigDecimal("" + object);
        } catch (NumberFormatException e) {
          throw new AssertJDBException("Expected <%s> can not be compared to a BigDecimal (<%s>)", expected, object);
        }
      }

      if (bd.compareTo((BigDecimal) expected) == 0) {
        return true;
      }
    }
    // Otherwise
    // If the value is Float, Double, BigInteger or BigDecimal
    // change the value to compare to make the comparison possible
    // else
    // get the value value in Long to compare
    else {
      Long actualValue = null;

      if (object instanceof Float) {
        if (((Float) object) == expected.floatValue()) {
          return true;
        }
      } else if (object instanceof Double) {
        if (((Double) object) == expected.doubleValue()) {
          return true;
        }
      } else if (object instanceof BigInteger) {
        BigInteger bi = new BigInteger("" + expected);
        if (((BigInteger) object).compareTo(bi) == 0) {
          return true;
        }
      } else if (object instanceof BigDecimal) {
        BigDecimal bd = new BigDecimal("" + expected);
        if (((BigDecimal) object).compareTo(bd) == 0) {
          return true;
        }
      } else if (object instanceof Byte) {
        actualValue = ((Byte) object).longValue();
      } else if (object instanceof Short) {
        actualValue = ((Short) object).longValue();
      } else if (object instanceof Integer) {
        actualValue = ((Integer) object).longValue();
      } else if (object instanceof Long) {
        actualValue = (Long) object;
      }

      if (actualValue != null) {
        if (expected instanceof Float) {
          if (actualValue == expected.floatValue()) {
            return true;
          }
        } else if (expected instanceof Double) {
          if (actualValue == expected.doubleValue()) {
            return true;
          }
        } else {
          if (actualValue == expected.longValue()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Returns if the value is equal to the array of {@code byte} in parameter.
   *
   * @param value    The value.
   * @param expected The array of {@code byte} to compare.
   * @return {@code true} if the value is equal to the array of {@code byte} parameter, {@code false} otherwise.
   */
  public static boolean areEqual(Value value, byte[] expected) {
    Object object = value.getValue();
    if (expected == null) {
      return object == null;
    }

    if (object instanceof byte[]) {
      byte[] bytes = (byte[]) object;
      if (bytes.length != expected.length) {
        return false;
      }
      for (int i = 0; i < bytes.length; i++) {
        if (bytes[i] != expected[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * Returns if the date is equal to the {@code String} representation in parameter.
   *
   * @param date     The date.
   * @param expected The {@code String} representation to compare.
   * @return {@code true} if the date is equal to the {@code String} representation parameter, {@code false} otherwise.
   * @throws AssertJDBException If it is not possible to compare {@code date} to {@code expected}.
   */
  private static boolean areEqual(Date date, String expected) {
    try {
      DateTimeValue dateTimeValue = DateTimeValue.of(DateValue.from(date));
      DateTimeValue expectedDateTimeValue = DateTimeValue.parse(expected);
      if (dateTimeValue.equals(expectedDateTimeValue)) {
        return true;
      }
    } catch (ParseException e) {
      throw new AssertJDBException("Expected <%s> is not correct to compare to <%s>", expected, date);
    }
    return false;
  }

  /**
   * Returns if the time is equal to the {@code String} representation in parameter.
   *
   * @param time     The time.
   * @param expected The {@code String} representation to compare.
   * @return {@code true} if the time is equal to the {@code String} representation parameter, {@code false} otherwise.
   * @throws AssertJDBException If it is not possible to compare {@code time} to {@code expected}.
   */
  private static boolean areEqual(Time time, String expected) {
    try {
      TimeValue timeValue = TimeValue.from(time);
      TimeValue expectedTimeValue = TimeValue.parse(expected);
      if (timeValue.equals(expectedTimeValue)) {
        return true;
      }
    } catch (ParseException e) {
      throw new AssertJDBException("Expected <%s> is not correct to compare to <%s>", expected, time);
    }
    return false;
  }

  /**
   * Returns if the timestamp is equal to the {@code String} representation in parameter.
   *
   * @param timestamp The timestamp.
   * @param expected  The {@code String} representation to compare.
   * @return {@code true} if the timestamp is equal to the {@code String} representation parameter, {@code false}
   * otherwise.
   * @throws AssertJDBException If it is not possible to compare {@code timestamp} to {@code expected}.
   */
  private static boolean areEqual(Timestamp timestamp, String expected) {
    try {
      DateTimeValue dateTimeValue = DateTimeValue.from(timestamp);
      DateTimeValue expectedDateTimeValue = DateTimeValue.parse(expected);
      if (dateTimeValue.equals(expectedDateTimeValue)) {
        return true;
      }
    } catch (ParseException e) {
      throw new AssertJDBException("Expected <%s> is not correct to compare to <%s>", expected, timestamp);
    }
    return false;
  }

  /**
   * Returns if the number is equal to the {@code String} representation in parameter.
   *
   * @param number   The number.
   * @param expected The {@code String} representation to compare.
   * @return {@code true} if the number is equal to the {@code String} representation parameter, {@code false}
   * otherwise.
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws AssertJDBException   If it is not possible to compare {@code number} to {@code expected}.
   */
  private static boolean areEqual(Number number, String expected) {
    try {
      if (number instanceof Float) {
        if (number.floatValue() == Float.parseFloat(expected)) {
          return true;
        }
      } else if (number instanceof Double) {
        if (number.doubleValue() == Double.parseDouble(expected)) {
          return true;
        }
      } else if (number instanceof BigInteger) {
        BigInteger bi = new BigInteger("" + expected);
        if (((BigInteger) number).compareTo(bi) == 0) {
          return true;
        }
      } else if (number instanceof BigDecimal) {
        BigDecimal bd = new BigDecimal("" + expected);
        if (((BigDecimal) number).compareTo(bd) == 0) {
          return true;
        }
      } else {
        Long actual = null;

        if (number instanceof Byte || number instanceof Short || number instanceof Integer) {
          actual = number.longValue();
        } else if (number instanceof Long) {
          actual = (Long) number;
        }

        if (actual != null && actual == Long.parseLong(expected)) {
          return true;
        }
      }
    } catch (NumberFormatException e) {
      throw new AssertJDBException("Expected <%s> is not correct to compare to <%s>", expected, number);
    }
    return false;
  }

  /**
   * Returns if the value is equal to the {@code String} in parameter.
   *
   * @param value    The value.
   * @param expected The {@code String} to compare.
   * @return {@code true} if the value is equal to the {@code String} parameter, {@code false} otherwise.
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws AssertJDBException   If {@code value} is a {@code Number} and it is not possible to compare to
   *                              {@code expected}.
   */
  public static boolean areEqual(Value value, String expected) {
    Object object = value.getValue();
    if (expected == null) {
      return object == null;
    }

    if (object instanceof Number) {
      return areEqual((Number) object, expected);
    } else if (object instanceof Date) {
      return areEqual((Date) object, expected);
    } else if (object instanceof Time) {
      return areEqual((Time) object, expected);
    } else if (object instanceof Timestamp) {
      return areEqual((Timestamp) object, expected);
    } else if (object instanceof UUID) {
      return areEqual((UUID) object, expected);
    }
    return expected.equals(object);
  }

  /**
   * Returns if the value is equal to the {@code UUID} in parameter.
   *
   * @param value    The value.
   * @param expected The {@code UUID} to compare.
   * @return {@code true} if the value is equal to the {@code UUID} parameter, {@code false} otherwise.
   * @since 1.1.0
   */
  public static boolean areEqual(Value value, UUID expected) {
    Object object = value.getValue();
    if (expected == null) {
      return object == null;
    }
    return expected.equals(object);
  }

  /**
   * Returns if the {@code UUID} value is equal to the {@code String} in parameter.
   *
   * @param value    The {@code UUID} value.
   * @param expected The {@code String} to compare.
   * @return {@code true} if the {@code UUID} value is equal to the {@code String} parameter, {@code false} otherwise.
   * @throws AssertJDBException If it is not possible to compare {@code UUID} to {@code expected}.
   * @since 1.1.0
   */
  public static boolean areEqual(UUID value, String expected) {
    if (expected == null) {
      return value == null;
    }
    try {
      return UUID.fromString(expected).equals(value);
    } catch (IllegalArgumentException e) {
      throw new AssertJDBException("Expected <%s> is not correct to compare to <%s>", expected, value);
    }

  }

  /**
   * Returns if the value is equal to the {@link DateValue} in parameter.
   *
   * @param value    The value.
   * @param expected The {@link DateValue} to compare.
   * @return {@code true} if the value is equal to the {@link DateValue} parameter, {@code false} otherwise.
   */
  public static boolean areEqual(Value value, DateValue expected) {
    Object object = value.getValue();
    if (expected == null) {
      return object == null;
    }

    if (object instanceof Date) {
      Date date = (Date) object;
      DateValue dateValue = DateValue.from(date);
      return dateValue.equals(expected);
    } else if (object instanceof Timestamp) {
      Timestamp timestamp = (Timestamp) object;
      DateTimeValue dateTimeValue = DateTimeValue.from(timestamp);
      return dateTimeValue.equals(DateTimeValue.of(expected));
    }
    return false;
  }

  /**
   * Returns if the value is equal to the {@link TimeValue} in parameter.
   *
   * @param value    The value.
   * @param expected The {@link TimeValue} to compare.
   * @return {@code true} if the value is equal to the {@link TimeValue} parameter, {@code false} otherwise.
   */
  public static boolean areEqual(Value value, TimeValue expected) {
    Object object = value.getValue();
    if (expected == null) {
      return object == null;
    }

    if (object instanceof Time) {
      Time time = (Time) object;
      TimeValue timeValue = TimeValue.from(time);
      return timeValue.equals(expected);
    }
    return false;
  }

  /**
   * Returns if the value is equal to the {@link DateTimeValue} in parameter.
   *
   * @param value    The value.
   * @param expected The {@link DateTimeValue} to compare.
   * @return {@code true} if the value is equal to the {@link DateTimeValue} parameter, {@code false} otherwise.
   */
  public static boolean areEqual(Value value, DateTimeValue expected) {
    Object object = value.getValue();
    if (expected == null) {
      return object == null;
    }

    if (object instanceof Date) {
      Date date = (Date) object;
      DateTimeValue dateTimeValue = DateTimeValue.of(DateValue.from(date));
      return dateTimeValue.equals(expected);
    }
    if (object instanceof Timestamp) {
      Timestamp timestamp = (Timestamp) object;
      DateTimeValue dateTimeValue = DateTimeValue.from(timestamp);
      return dateTimeValue.equals(expected);
    }
    return false;
  }

  /**
   * Returns the result of the comparison between the value and the {@code Number} in parameter.
   *
   * @param value    The value.
   * @param expected The {@code Number} to compare.
   * @return {@code 0} if the value is equal to the {@code Number} parameter, {@code -1} if value is less than the
   * {@code Number} parameter and {@code 1} if value is greater than the {@code Number} parameter.
   */
  public static int compare(Value value, Number expected) {
    Object object = value.getValue();
    // If parameter is a BigInteger,
    // change the actual in BigInteger to compare
    if (expected instanceof BigInteger) {
      BigInteger bi;

      if (object instanceof BigInteger) {
        bi = (BigInteger) object;
      } else {
        try {
          bi = new BigInteger("" + object);
        } catch (NumberFormatException e) {
          throw new AssertJDBException("Expected <%s> can not be compared to a BigInteger (<%s>)", expected, object);
        }
      }

      return bi.compareTo((BigInteger) expected);
    }
    // If parameter is a BigDecimal,
    // change the value in BigDecimal to compare
    else if (expected instanceof BigDecimal) {
      BigDecimal bd;

      if (object instanceof BigDecimal) {
        bd = (BigDecimal) object;
      } else {
        try {
          bd = new BigDecimal("" + object);
        } catch (NumberFormatException e) {
          throw new AssertJDBException("Expected <%s> can not be compared to a BigDecimal (<%s>)", expected, object);
        }
      }

      return bd.compareTo((BigDecimal) expected);
    }
    // Otherwise
    // If the value is Float, Double, BigInteger or BigDecimal
    // change the value to compare to make the comparison possible
    // else
    // get the value value in Long to compare
    else {
      Long actualValue = null;

      if (object instanceof Float) {
        float f = (Float) object;
        float expectedF = expected.floatValue();
        if (f > expectedF) {
          return 1;
        } else if (f < expectedF) {
          return -1;
        } else {
          return 0;
        }
      } else if (object instanceof Double) {
        double d = (Double) object;
        double expectedD = expected.doubleValue();
        if (d > expectedD) {
          return 1;
        } else if (d < expectedD) {
          return -1;
        } else {
          return 0;
        }
      } else if (object instanceof BigInteger) {
        BigInteger bi = new BigInteger("" + expected);
        return ((BigInteger) object).compareTo(bi);
      } else if (object instanceof BigDecimal) {
        BigDecimal bd = new BigDecimal("" + expected);
        return ((BigDecimal) object).compareTo(bd);
      } else if (object instanceof Byte) {
        actualValue = ((Byte) object).longValue();
      } else if (object instanceof Short) {
        actualValue = ((Short) object).longValue();
      } else if (object instanceof Integer) {
        actualValue = ((Integer) object).longValue();
      } else if (object instanceof Long) {
        actualValue = (Long) object;
      }

      if (actualValue != null) {
        if (expected instanceof Float) {
          float expectedF = expected.floatValue();
          if (actualValue > expectedF) {
            return 1;
          } else if (actualValue < expectedF) {
            return -1;
          } else {
            return 0;
          }
        } else if (expected instanceof Double) {
          double expectedD = expected.doubleValue();
          if (actualValue > expectedD) {
            return 1;
          } else if (actualValue < expectedD) {
            return -1;
          } else {
            return 0;
          }
        } else {
          double expectedL = expected.longValue();
          if (actualValue > expectedL) {
            return 1;
          } else if (actualValue < expectedL) {
            return -1;
          } else {
            return 0;
          }
        }
      }
    }

    throw new AssertJDBException("Expected <%s> can not be compared to a Number (<%s>)", expected, object);
  }

  /**
   * Returns a representation of the values (this representation is used for error message).
   *
   * @param values   Values in the database.
   * @param expected Expected values for comparison.
   * @return A representation of the values.
   * @throws org.assertj.db.exception.AssertJDBException If the length of the two arrays are different.
   */
  public static Object[] getRepresentationsFromValuesInFrontOfExpected(Value[] values, Object[] expected) {
    Object[] representationsValues = new Object[values.length];
    int i = 0;
    for (Value obj : values) {
      if (i >= expected.length) {
        representationsValues[i] = obj.getValue();
      } else {
        representationsValues[i] = Values.getRepresentationFromValueInFrontOfExpected(obj, expected[i]);
      }
      i++;
    }
    return representationsValues;
  }

  /**
   * Returns a representation of the value (this representation is used for error message).
   *
   * @param value    Value in the database.
   * @param expected Expected value for comparison.
   * @return A representation of the value.
   */
  public static Object getRepresentationFromValueInFrontOfExpected(Value value, Object expected) {
    Object object = value.getValue();
    switch (value.getValueType()) {
    case DATE:
      if (expected instanceof String) {
        if (((String) expected).contains("T")) {
          return DateTimeValue.of(DateValue.from((Date) object)).toString();
        } else {
          return DateValue.from((Date) object).toString();
        }
      }
    case TIME:
    case DATE_TIME:
    case NUMBER:
    case UUID:
    case BYTES:
    case TEXT:
    case BOOLEAN:
    default:
      return getRepresentationFromValueInFrontOfClass(value, expected == null ? null : expected.getClass());
    }
  }

  /**
   * Returns a representation of the value (this representation is used for error message).
   *
   * @param value    Value in the database.
   * @param clazz    Class for comparison.
   * @return A representation of the value.
   */
  public static Object getRepresentationFromValueInFrontOfClass(Value value, Class clazz) {
    Object object = value.getValue();
    switch (value.getValueType()) {
    case DATE:
      if (clazz == DateValue.class) {
        return DateValue.from((Date) object);
      } else if (clazz == DateTimeValue.class) {
        return DateTimeValue.of(DateValue.from((Date) object));
      } else if (clazz == String.class) {
        return DateTimeValue.of(DateValue.from((Date) object)).toString();
      }
      return object;
    case TIME:
      if (clazz == String.class) {
        return TimeValue.from((Time) object).toString();
      } else {
        return TimeValue.from((Time) object);
      }
    case DATE_TIME:
      if (clazz == String.class) {
        return DateTimeValue.from((Timestamp) object).toString();
      } else {
        return DateTimeValue.from((Timestamp) object);
      }
    case NUMBER:
    case UUID:
      if (clazz == String.class) {
        return object.toString();
      } else {
        return object;
      }

    case BYTES:
    case TEXT:
    case BOOLEAN:
    default:
      return object;
    }
  }
}
