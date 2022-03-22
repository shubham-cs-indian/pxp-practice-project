package com.cs.core.rdbms.postgres;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.technical.irdbms.IRDBMSDriver;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Entry point for PostgreSQL implementation Standard RDBMS Implementation
 *
 * @author vallee @modified Pankaj Gajjar
 */
@Configuration
@EnableTransactionManagement
public class RDBMSDriver extends RDBMSAbstractDriver implements IRDBMSDriver {
  
  private static final String            DRIVER_CLASS_NAME = "org.postgresql.Driver";
  
  public RDBMSDriver() throws RDBMSException
  {
    initializeDriver(RDBMSAbstractDriver.DBVendor.POSTGRES);
  }
  
  @Override
  public RDBMSAbstractFunction getFunction(RDBMSConnection connection, RDBMSAbstractFunction.ResultType resultType, String callDeclaration)
  {
    return new RDBMSFunction(connection, resultType, callDeclaration);
  }
  
  @Override
  public IResultSetParser getResultSetParser(ResultSet resultSet) throws SQLException
  {
    return new RDBMSResultSetParser(resultSet);
  }
  
  @Override
  public IResultSetParser getResultSetParser(RDBMSAbstractFunction.ResultType resultType, CallableStatement statement) throws SQLException
  {
    return new RDBMSResultSetParser(resultType, statement);
  }
  
  @Override
  public String getSystemDateExpression()
  {
    return "Now()";
  }
  
  @Override
  public RDBMSAbstractDriver.DBVendor getDBVendor()
  {
    return RDBMSAbstractDriver.DBVendor.POSTGRES;
  }
  
  @Override
  public String getDriverClassName()
  {
    return DRIVER_CLASS_NAME;
  }
  
  @Override
  public String getCreateTemporaryTableExpression(String tableName, String query)
  {
    return String.format("create temporary table %s on commit delete rows as (%s)", tableName, query);
  }
  
  @Override
  public String getOffsetExpression(long nbRows)
  {
    return String.format("offset %d", nbRows);
  }
  
  @Override
  public String getOffsetExpression(long nbRows, long limit)
  {
    return String.format("limit %d offset %d", nbRows, limit);
  }
  
  @Override
  public String getListAggregateExpression(String paramName)
  {
    return "string_agg( " + paramName + "::varchar, ',')";
  }
  
  @Override
  public String getSimilarPathPattern(String colName, String keyElt)
  {
    return colName + " similar to '(%,)?" + keyElt + "(,%)?'";
  }
  
  @Override
  public String getExcludeOperator()
  {
    return "except";
  }
}
