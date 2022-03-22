package com.cs.core.rdbms.function;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.*;

/**
 * A helper class that facilitates conversions from a JDBC resultSet set
 *
 * @author vallee
 */
public abstract class RDBMSAbstractResultSetParser implements IResultSetParser {

  protected final CallableStatement statement;
  protected final ResultSet resultSet;
  private final boolean isCursorType;

  /**
   * Constructor from a result set
   *
   * @param resultSet
   * @param statement
   * @param isCursorType
   */
  protected RDBMSAbstractResultSetParser(ResultSet resultSet, CallableStatement statement, boolean isCursorType) {
    this.resultSet = resultSet;
    this.statement = statement;
    this.isCursorType = isCursorType;
  }

  @Override
  public boolean testKey(String key) {
    try {
      resultSet.findColumn(key);
    } catch (SQLException ex) {
      return false;
    }
    return true;
  }

  @Override
  public boolean isCursorType() {
    return isCursorType;
  }

  @Override
  public boolean next() throws SQLException, RDBMSException {
    if (isCursorType) {
      return resultSet.next();
    }
    throw new RDBMSException(100, "Design Error", "Inconsistent call to next() on a non-cursor result set");
  }

  @Override
  public long getDateTime() throws SQLException {
    return statement.getTimestamp(1).getTime();
  }

  @Override
  public String getString() throws SQLException {
    return statement.getString(1);
  }

  @Override
  public long getLong() throws SQLException {
    return statement.getLong(1);
  }

  @Override
  public Long[] getLongArray() throws SQLException, CSFormatException {
    return toLong(statement.getArray(1));
  }

  @Override
  public int getInt() throws SQLException {
    return statement.getInt(1);
  }

  @Override
  public String getString(String key) throws SQLException {
    String value = resultSet.getString(key);
    return resultSet.wasNull() ? "" : value;
  }

  @Override
  public String getString(int keyIndex) throws SQLException {
    String value = resultSet.getString(keyIndex);
    return resultSet.wasNull() ? "" : value;
  }

  @Override
  public long getLong(String key) throws SQLException {
    long value = resultSet.getLong(key);
    return resultSet.wasNull() ? 0 : value;
  }

  @Override
  public long getLong(int keyIndex) throws SQLException {
    long value = resultSet.getLong(keyIndex);
    return resultSet.wasNull() ? 0 : value;
  }

  @Override
  public int getInt(String key) throws SQLException {
    int value = resultSet.getInt(key);
    return resultSet.wasNull() ? 0 : value;
  }

  @Override
  public int getInt(int keyIndex) throws SQLException {
    int value = resultSet.getInt(keyIndex);
    return resultSet.wasNull() ? 0 : value;
  }

  @Override
  public double getDouble(String key) throws SQLException {
    double value = resultSet.getDouble(key);
    return resultSet.wasNull() ? 0 : value;
  }

  @Override
  public double getDouble(int keyIndex) throws SQLException {
    double value = resultSet.getDouble(keyIndex);
    return resultSet.wasNull() ? 0 : value;
  }

  @Override
  public long getDateTime(String key) throws SQLException {
    long value = resultSet.getLong(key);
    Date date = new Date(value * 1000);
    return resultSet.wasNull() ? 0 : date.getTime();
  }

  @Override
  public long getDateTime(int keyIndex) throws SQLException {
    long value = resultSet.getLong(keyIndex);
    Date date = new Date(value * 1000);
    return resultSet.wasNull() ? 0 : date.getTime();
  }

  @Override
  public boolean getBoolean(String key) throws SQLException {
    String value = resultSet.getString(key);
    return resultSet.wasNull() ? false
            : (value.toLowerCase()
                    .charAt(0) == 't');
  }

  @Override
  public boolean getBoolean(int keyIndex) throws SQLException {
    String value = resultSet.getString(keyIndex);
    return resultSet.wasNull() ? false
            : (value.toLowerCase()
                    .charAt(0) == 't');
  }

  /**
   * @param valueArray content returned by the database
   * @return the array converted as Integers
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  protected abstract Integer[] toInt(Array valueArray) throws SQLException, CSFormatException;

  /**
   * @param valueArray content returned by the database
   * @return the array converted as Long integers
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  protected abstract Long[] toLong(Array valueArray) throws SQLException, CSFormatException;

  @Override
  public Long[] getLongArray(String key) throws SQLException, CSFormatException {
    return toLong(resultSet.getArray(key));
  }

  @Override
  public Long[] getLongArray(int keyIndex) throws SQLException, CSFormatException {
    return toLong(resultSet.getArray(keyIndex));
  }

  @Override
  public Integer[] getIntArray(String key) throws SQLException, CSFormatException {
    return toInt(resultSet.getArray(key));
  }

  @Override
  public Integer[] getIntArray(int keyIndex) throws SQLException, CSFormatException {
    return toInt(resultSet.getArray(keyIndex));
  }

  @Override
  public String[] getStringArray(String key) throws SQLException {
    Array array = resultSet.getArray(key);
    if (array == null) {
      return new String[0];
    }
    return (String[]) array.getArray();
  }

  @Override
  public String[] getStringArray(int keyIndex) throws SQLException {
    Array array = resultSet.getArray(keyIndex);
    if (array == null) {
      return new String[0];
    }
    return (String[]) array.getArray();
  }
}
