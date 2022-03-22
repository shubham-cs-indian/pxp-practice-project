package com.cs.core.rdbms.oracle;

import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.function.RDBMSAbstractResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleClob;
import oracle.jdbc.OracleResultSet;
import org.apache.commons.dbcp2.DelegatingResultSet;

/**
 * @author vallee
 */
public class RDBMSResultSetParser extends RDBMSAbstractResultSetParser implements IResultSetParser {

  private final OracleCallableStatement oraStatement;
  private final OracleResultSet oraRs;

  RDBMSResultSetParser(ResultType resultType, CallableStatement statement) throws SQLException {
    super(statement.getResultSet(), statement, resultType == ResultType.CURSOR);
    oraStatement = (OracleCallableStatement) statement;
    oraRs = (OracleResultSet) resultSet;
  }

  RDBMSResultSetParser(ResultSet resultSet) throws SQLException {
    super(resultSet, null, true);
    oraStatement = null;
    oraRs = (OracleResultSet) ((DelegatingResultSet) resultSet).getInnermostDelegate();
  }

  @Override
  protected Integer[] toInt(Array valueArray) throws SQLException, CSFormatException {
    if (valueArray == null) {
      return new Integer[0];
    }
    Object content = valueArray.getArray();
    if (content instanceof BigDecimal[]) {
      Integer[] values = new Integer[((BigDecimal[]) content).length];
      for (int i = 0; i < values.length; i++) {
        values[i] = ((BigDecimal[]) content)[i].intValue();
      }
      return values;
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
    if (content instanceof BigDecimal[]) {
      Long[] values = new Long[((BigDecimal[]) content).length];
      for (int i = 0; i < values.length; i++) {
        values[i] = ((BigDecimal[]) content)[i].longValue();
      }
      return values;
    }
    throw new CSFormatException(
            "Cannot convert to array of integers from content of type " + content.getClass());
  }

  @Override
  public String getStringFromJSON(String key) throws SQLException {
    OracleClob clob = oraRs.getCLOB(key);
    return resultSet.wasNull() ? ""
            : clob.getSubString(1, new Long(((OracleClob) clob).length()).intValue());
  }

  @Override
  public String getStringFromJSON(int keyIndex) throws SQLException {
    OracleClob clob = oraRs.getCLOB(keyIndex);
    return resultSet.wasNull() ? ""
            : clob.getSubString(1, new Long(((OracleClob) clob).length()).intValue());
  }

  @Override
  public byte[] getBinaryBlob(String keyIndex) throws SQLException, CSFormatException {
    InputStream is = oraStatement.getBinaryStream(keyIndex);
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
