package com.cs.core.rdbms.postgres;

import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.function.RDBMSAbstractResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * POSTGRESQL implementation of a result set parser
 *
 * @author vallee
 */
public class RDBMSResultSetParser extends RDBMSAbstractResultSetParser implements IResultSetParser {

  RDBMSResultSetParser(ResultType resultType, CallableStatement statement) throws SQLException {
    super(statement.getResultSet(), statement, (resultType == ResultType.CURSOR));
  }

  RDBMSResultSetParser(ResultSet resultSet) {
    super(resultSet, null, true);
  }

  @Override
  protected Integer[] toInt(Array valueArray) throws SQLException, CSFormatException {
    if (valueArray == null) {
      return new Integer[0];
    }
    Object content = valueArray.getArray();
    if (content instanceof Integer[]) {
      return (Integer[]) content;
    }
    throw new CSFormatException(
            "Cannot convert to array of integers from content of type " + content.getClass());
  }

  @Override
  protected Long[] toLong(Array valueArray) throws SQLException, CSFormatException {
    if (valueArray == null) {
      return new Long[0];
    }
    Object content = valueArray.getArray();
    if (content instanceof Integer[]) {
      return (Long[]) content;
    }
    if (content instanceof Long[]) {
      return (Long[]) content;
    }
    throw new CSFormatException(
            "Cannot convert to array of longs from content of type " + content.getClass());
  }

  @Override
  public String getStringFromJSON(String key) throws SQLException {
    return getString(key);
  }

  @Override
  public String getStringFromJSON(int keyIndex) throws SQLException {
    return getString(keyIndex);
  }

  @Override
  public byte[] getBinaryBlob(String keyIndex) throws SQLException, CSFormatException {
    InputStream is = resultSet.getBinaryStream(keyIndex);
    if (is == null) {
      return null;
    }
    byte[] buffer = new byte[1];
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      while (is.read(buffer) > 0) {
        os.write(buffer);
      }
      os.flush();
      os.close();
      return os.toByteArray();
    } catch (IOException ex) {
      throw new CSFormatException("System error", ex);
    }
  }
}
