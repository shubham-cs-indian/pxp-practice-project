package com.cs.core.rdbms.oracle;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Collection;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.apache.commons.dbcp2.DelegatingConnection;

/**
 * @author vallee
 */
public class RDBMSFunction extends RDBMSAbstractFunction {

  private OracleConnection oraConnection;
  private int indexOffset;

  /**
   * @param connection
   * @param resultType
   * @param callDeclaration the callable statement declaration
   */
  public RDBMSFunction(RDBMSConnection connection, ResultType resultType, String callDeclaration) {
    super(connection, resultType, callDeclaration);
    DelegatingConnection delConnection = new DelegatingConnection(connection.getConnection());
    oraConnection = (oracle.jdbc.OracleConnection) delConnection.getInnermostDelegate();
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
  protected CallableStatement getCallableStatement() throws SQLException, RDBMSException {
    StringBuffer jdbcStatement = initializeJDBCStatement();
    for (int i = 0; i < parameterTypes.size(); i++) {
      jdbcStatement.append("?,");
    }
    jdbcStatement.deleteCharAt(jdbcStatement.length() - 1)
            .append(")}");
    OracleCallableStatement statement = (OracleCallableStatement) oraConnection
            .prepareCall(jdbcStatement.toString());

    indexOffset = (hasOutPutParameter ? 2 : 1);
    if (hasOutPutParameter) {
      switch (resultType) {
        case STRING:
          statement.registerOutParameter(1, OracleTypes.VARCHAR);
          break;
        case IID:
          statement.registerOutParameter(1, OracleTypes.BIGINT);
          break;
        case LONG:
        case INT:
          statement.registerOutParameter(1, OracleTypes.INTEGER);
          break;
        case CURSOR:
          statement.registerOutParameter(1, OracleTypes.CURSOR);
          break;
        case DATE_TIME:
          statement.registerOutParameter(1, OracleTypes.TIMESTAMP);
      }
    }
    return statement;
  }

  @Override
  protected void prepareCallableStatement(CallableStatement statement)
          throws SQLException, RDBMSException {

    for (int i = 0; i < parameterTypes.size(); i++) {
      switch (parameterTypes.get(i)) {
        case STRING:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, OracleTypes.VARCHAR);
          } else {
            statement.setString(indexOffset + i, (String) parameters.get(i));
          }
          break;
        case TEXT:
        case JSON:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, OracleTypes.CLOB);
          } else {
            statement.setString(indexOffset + i, (String) parameters.get(i));
          }
          break;
        case BOOLEAN:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, OracleTypes.CHAR);
          } else {
            statement.setString(indexOffset + i, ((boolean) parameters.get(i) ? "t" : "f"));
          }
          break;
        case LONG:
        case IID:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, OracleTypes.NUMBER);
          } else {
            statement.setLong(indexOffset + i,
                    (parameters.get(i) instanceof Long ? (long) parameters.get(i)
                    : (int) parameters.get(i)));
          }
          break;
        case FLOAT:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, OracleTypes.NUMERIC);
          } else {
            statement.setDouble(indexOffset + i, (double) parameters.get(i));
          }
          break;
        case INT:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, OracleTypes.NUMBER);
          } else {
            statement.setInt(indexOffset + i, (int) parameters.get(i));
          }
          break;
        case BLOB:
          if (parameters.get(i) == null) {
            statement.setNull(indexOffset + i, OracleTypes.BLOB);
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
            statement.setNull(indexOffset + i, OracleTypes.ARRAY);
          } else {
            statement.setArray(indexOffset + i, (Array) parameters.get(i));
          }
          break;
      }
    }
  }

  @Override
  public Array newStringArray(Collection<String> array) throws SQLException {
    return oraConnection.createOracleArray("VARCHARARRAY", array.toArray());
  }

  @Override
  public Array newIIDArray(Collection<Long> array) throws SQLException {
    return oraConnection.createOracleArray("IIDARRAY", array.toArray());
  }

  @Override
  public Array newIntArray(Collection<Integer> array) throws SQLException {
    return oraConnection.createOracleArray("IIDARRAY", array.toArray());
  }
}
