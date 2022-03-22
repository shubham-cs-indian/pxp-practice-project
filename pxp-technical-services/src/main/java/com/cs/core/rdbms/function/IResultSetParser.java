package com.cs.core.rdbms.function;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;

/**
 * Manage results returned by functions or queries by handling correctly null cases
 *
 * @author vallee
 */
public interface IResultSetParser {

  /**
   * Test if a column name or key is existing in the current result set
   *
   * @param key column name
   * @return true if existing
   */
  public boolean testKey(String key);

  /**
   * @return true when the internal result set has more results
   * @throws SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public boolean next() throws SQLException, RDBMSException;

  /**
   * @return true if this result set is a cursor on which next() can be used
   */
  public boolean isCursorType();

  /**
   * @return the returned result as a single Long
   * @throws java.sql.SQLException
   */
  public long getDateTime() throws SQLException;

  /**
   * @return the returned result as a single String
   * @throws java.sql.SQLException
   */
  public String getString() throws SQLException;

  /**
   * @return the returned result as a single Long
   * @throws java.sql.SQLException
   */
  public long getLong() throws SQLException;

  /**
   * @return the returned result as a internal ID
   * @throws java.sql.SQLException
   */
  public default long getIID() throws SQLException {
    return getLong();
  }

  /**
   * @return the returned array of long integer
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Long[] getLongArray() throws SQLException, CSFormatException;

  /**
   * @return the returned array of internal ID
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public default Long[] getIIDArray() throws SQLException, CSFormatException {
    return getLongArray();
  }

  /**
   * @return the returned result as a single int
   * @throws java.sql.SQLException
   */
  int getInt() throws SQLException;

  /**
   * @param key result set field identifier
   * @return the corresponding content as String
   * @throws java.sql.SQLException
   */
  String getString(String key) throws SQLException;

  /**
   * @param keyIndex result set field identifier
   * @return the corresponding content as String
   * @throws java.sql.SQLException
   */
  public String getString(int keyIndex) throws SQLException;

  /**
   * @param key result set field identifier when a JSON field is involved (clob conversion)
   * @return the corresponding content as String
   * @throws java.sql.SQLException
   */
  public String getStringFromJSON(String key) throws SQLException;

  /**
   * @param keyIndex result set field identifier when a JSON field is involved (clob conversion)
   * @return the corresponding content as String
   * @throws java.sql.SQLException
   */
  public String getStringFromJSON(int keyIndex) throws SQLException;

  /**
   * @param key RDBMS field identifier
   * @return the corresponding content as Long
   * @throws java.sql.SQLException
   */
  public long getLong(String key) throws SQLException;

  /**
   * @param keyIndex RDBMS field identifier
   * @return the corresponding content as Long
   * @throws java.sql.SQLException
   */
  public long getLong(int keyIndex) throws SQLException;

  /**
   * @param key RDBMS field identifier
   * @return the corresponding content as integer
   * @throws java.sql.SQLException
   */
  public int getInt(String key) throws SQLException;

  /**
   * @param keyIndex RDBMS field identifier
   * @return the corresponding content as integer
   * @throws java.sql.SQLException
   */
  public int getInt(int keyIndex) throws SQLException;

  /**
   * @param key RDBMS field identifier
   * @return the corresponding content as double
   * @throws java.sql.SQLException
   */
  public double getDouble(String key) throws SQLException;

  /**
   * @param keyIndex RDBMS field identifier
   * @return the corresponding content as double
   * @throws java.sql.SQLException
   */
  public double getDouble(int keyIndex) throws SQLException;

  /**
   * @param key RDBMS field identifier
   * @return the corresponding content as Long time ms
   * @throws java.sql.SQLException
   */
  public long getDateTime(String key) throws SQLException;

  /**
   * @param keyIndex RDBMS field identifier
   * @return the corresponding content as Long time ms
   * @throws java.sql.SQLException
   */
  public long getDateTime(int keyIndex) throws SQLException;

  /**
   * @param key RDBMS field identifier
   * @return the corresponding content as boolean
   * @throws java.sql.SQLException
   */
  public boolean getBoolean(String key) throws SQLException;

  /**
   * @param keyIndex RDBMS field identifier
   * @return the corresponding content as boolean
   * @throws java.sql.SQLException
   */
  public boolean getBoolean(int keyIndex) throws SQLException;

  /**
   * @param keyIndex RDBMS field identifier
   * @return the corresponding content as byte array
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public byte[] getBinaryBlob(String keyIndex) throws SQLException, CSFormatException;

  /**
   * @param key RDBMS field identifier
   * @return an array of long from an RDBMS array
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Long[] getLongArray(String key) throws SQLException, CSFormatException;

  /**
   * @param keyIndex RDBMS field identifier by result order
   * @return an array of long from an RDBMS array
   * @throws SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Long[] getLongArray(int keyIndex) throws SQLException, CSFormatException;

  /**
   * @param key RDBMS field identifier
   * @return an array of int from an RDBMS array
   * @throws SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Integer[] getIntArray(String key) throws SQLException, CSFormatException;

  /**
   * @param keyIndex RDBMS field identifier by result order
   * @return an array of int from an RDBMS array
   * @throws SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Integer[] getIntArray(int keyIndex) throws SQLException, CSFormatException;

  /**
   * @param key RDBMS field identifier
   * @return an array of Strings from an RDBMS array
   * @throws SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public String[] getStringArray(String key) throws SQLException, CSFormatException;

  /**
   * @param keyIndex RDBMS field identifier by result order
   * @return an array of Strings from an RDBMS array
   * @throws SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public String[] getStringArray(int keyIndex) throws SQLException, CSFormatException;
}
