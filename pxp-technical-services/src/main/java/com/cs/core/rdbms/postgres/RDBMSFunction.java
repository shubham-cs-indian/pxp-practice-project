package com.cs.core.rdbms.postgres;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;

/**
 * @author vallee
 */
public class RDBMSFunction extends RDBMSAbstractFunction {

  private int indexOffset;

  /**
   * @param connection
   * @param resultType
   * @param callDeclaration the callable statement declaration
   */
  public RDBMSFunction(RDBMSConnection connection, ResultType resultType, String callDeclaration) {
    super(connection, resultType, callDeclaration);
  }

  @Override
  protected CallableStatement getCallableStatement() throws SQLException, RDBMSException {
    StringBuffer jdbcStatement = initializeJDBCStatement();
    for (ParameterType paramterType : parameterTypes) {
      switch (paramterType) {
        case TEXT:
          jdbcStatement.append("?::text,");
          break;
        case JSON:
          jdbcStatement.append("?::json,");
          break;
        case FLOAT:
          jdbcStatement.append("?::numeric,");
          break;
        case BLOB:
          jdbcStatement.append("?::bytea,");
          break;
        default:
          jdbcStatement.append("?,");
      }
    }
    
    if (!parameterTypes.isEmpty()) {
      jdbcStatement.deleteCharAt(jdbcStatement.length() - 1);
    }
    jdbcStatement.append(resultType == ResultType.VOID ? ")" : ")}");

    CallableStatement statement = connection.prepareCall(jdbcStatement.toString());
    indexOffset = (hasOutPutParameter ? 2 : 1);
    if (hasOutPutParameter) {
      switch (resultType) {
        case STRING:
          statement.registerOutParameter(1, Types.VARCHAR);
          break;
        case SYSTIME:
        case IID:
          statement.registerOutParameter(1, Types.BIGINT);
          break;
        case LONG:
        case INT:
          statement.registerOutParameter(1, Types.INTEGER);
          break;
        case DATE_TIME:
          statement.registerOutParameter(1, Types.TIMESTAMP);
          break;
        case IID_ARRAY:
          statement.registerOutParameter(1, Types.ARRAY);
          break;
      }
    }
    return statement;
  }

  /**
   * Initialize the JDBC callable statement string according to the result type
   *
   * @return the statement as expected by PGSQL JDBC driver
   */
  private StringBuffer initializeJDBCStatement() {
    StringBuffer jdbcStatement = new StringBuffer();
    switch (resultType) {
      case VOID:
        jdbcStatement.append("call ");
        break;
      case CURSOR:
        jdbcStatement.append("{call ");
        break;
      default:
        jdbcStatement.append("{? = call ");
        hasOutPutParameter = true;
    }
    return jdbcStatement.append(functionName)
            .append("(");
  }

  @Override
  protected void prepareCallableStatement(CallableStatement statement)
          throws SQLException, RDBMSException {

    for (int i = 0; i < parameterTypes.size(); i++) {
      switch (parameterTypes.get(i)) {
        case STRING:
        case JSON:
        case TEXT:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, Types.VARCHAR);
          } else {
            statement.setString(indexOffset + i, (String) parameters.get(i));
          }
          break;
        case BOOLEAN:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, Types.BOOLEAN);
          } else {
            statement.setBoolean(indexOffset + i, (boolean) parameters.get(i));
          }
          break;
        case LONG:
        case IID:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, Types.BIGINT);
          } else {
            statement.setLong(indexOffset + i,
                    (parameters.get(i) instanceof Long ? (long) parameters.get(i)
                    : (int) parameters.get(i)));
          }
          break;
        case INT:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, Types.INTEGER);
          } else {
            statement.setInt(indexOffset + i, (int) parameters.get(i));
          }
          break;
        case FLOAT:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, Types.DOUBLE);
          } else {
            statement.setDouble(indexOffset + i, (double) parameters.get(i));
          }
          break;
        case BLOB:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, Types.BLOB);
          } else {
            byte[] content = (byte[]) parameters.get(i);
            InputStream blob = new ByteArrayInputStream(content);
            statement.setBinaryStream(indexOffset + i, blob, content.length);
          }
          break;
        case STRING_ARRAY:
        case IID_ARRAY:
        case INT_ARRAY:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, Types.ARRAY);
          } else {
            statement.setArray(indexOffset + i, (Array) parameters.get(i));
          }
          break;
      }
    }
  }

  @Override
  public Array newStringArray(Collection<String> array) throws SQLException {
    return connection.getConnection()
            .createArrayOf("varchar", array.toArray());
  }

  @Override
  public Array newIIDArray(Collection<Long> array) throws SQLException {
    return connection.getConnection()
            .createArrayOf("BIGINT", array.toArray());
  }

  @Override
  public Array newIntArray(Collection<Integer> array) throws SQLException {
    return connection.getConnection()
            .createArrayOf("integer", array.toArray());
  }
}
