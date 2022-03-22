package com.cs.core.rdbms.oracle;

import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.technical.irdbms.IRDBMSDriver;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Entry point for ORACLE implementation Standard RDBMS Implementation
 *
 * @author vallee @modified Pankaj Gajjar
 */
public final class RDBMSDriver extends RDBMSAbstractDriver implements IRDBMSDriver {

  private static final String DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";

  public RDBMSDriver() throws RDBMSException {
    initializeDriver(RDBMSAbstractDriver.DBVendor.ORACLE);
  }

  @Override
  public RDBMSAbstractFunction getFunction(RDBMSConnection connection,
          RDBMSAbstractFunction.ResultType resultType, String callDeclaration) {
    return new RDBMSFunction(connection, resultType, callDeclaration);
  }

  @Override
  public IResultSetParser getResultSetParser(ResultSet resultSet) throws SQLException {
    return new RDBMSResultSetParser(resultSet);
  }

  @Override
  public IResultSetParser getResultSetParser(RDBMSAbstractFunction.ResultType resultType,
          CallableStatement statement) throws SQLException {
    return new RDBMSResultSetParser(resultType, statement);
  }

  @Override
  public String getSystemDateExpression() {
    return "SYSTIMESTAMP";
  }

  @Override
  public RDBMSAbstractDriver.DBVendor getDBVendor() {
    return RDBMSAbstractDriver.DBVendor.ORACLE;
  }

  @Override
  public String getDriverClassName() {
    return DRIVER_CLASS_NAME;
  }

  @Override
  public String getCreateTemporaryTableExpression(String tableName, String query) {
    return String.format(
            "create global temporary table %s on commit delete rows as (%s)", tableName, query);
  }

  @Override
  public String getOffsetExpression(long nbRows) {
    return String.format("offset %d rows", nbRows);
  }

  @Override
  public String getOffsetExpression(long nbRows, long limit) {
    return String.format("offset %d rows fetch next %d rows only", nbRows, limit);
  }

  @Override
  public String getListAggregateExpression(String paramName) {
    return "listagg( " + paramName + ", ',')";
  }

  @Override
  public String getSimilarPathPattern(String colName, String keyElt) {
    return "regexp_like( " + colName + ", '(^|,)" + keyElt + "(,|$))'";
  }

  @Override
  public String getExcludeOperator() {
    return "minus";
  }
}
