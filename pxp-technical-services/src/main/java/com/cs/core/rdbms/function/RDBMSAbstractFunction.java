package com.cs.core.rdbms.function;

import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A handler that call stored procedure and stored function indifferently according to PGORA vendor. for internal IDs (use IID type and not
 * LONG)
 *
 * @author vallee
 */
public abstract class RDBMSAbstractFunction {
  
  /**
   * possible return types for the abstract function (use VOID for procedures)
   */
  public static enum ResultType {
    VOID, STRING, LONG, INT, IID, DATE_TIME, SYSTIME, // simple types
    IID_ARRAY, // array types
    CURSOR // row types
  }

  /**
   * possible parameter types for the abstract function
   */
  public static enum ParameterType {
    STRING, LONG, INT, IID, FLOAT, SYSTIME, BOOLEAN, TEXT, BLOB, // simple types
    STRING_ARRAY, IID_ARRAY, INT_ARRAY, // array types
    HSTORE, JSON, LONG_RANGE // compound types
  }

  private static RDBMSAbstractDriver driver;
  protected final ResultType resultType;
  protected final String functionName;
  protected final RDBMSConnection connection;
  protected final List<Object> parameters = new ArrayList<>();
  protected final List<ParameterType> parameterTypes = new ArrayList<>();
  private final List<Array> allocatedArrays = new ArrayList<>();
  protected boolean hasOutPutParameter = false;
  protected ResultSet result;
  private CallableStatement callableStatement;

  /**
   * @param connection current connection
   * @param resultType the result type of the called function or procedure
   * @param functionName the
   */
  protected RDBMSAbstractFunction(RDBMSConnection connection, ResultType resultType,
          String functionName) {
    driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    this.connection = connection;
    this.resultType = resultType;
    this.functionName = functionName;
  }

  ;
  
  /**
   * Clear any input in order to prepare another call or batch
   */
  private void clearInput() {
    parameters.clear();
    parameterTypes.clear();
    allocatedArrays.clear();
  }

  ;
  
  /**
   * Define an input parameter type and value value
   *
   * @param type the type of parameter
   * @param value the assigned value
   * @return the current abstract function
   * @throws SQLException
   */
  public RDBMSAbstractFunction setInput(ParameterType type, Object value) throws SQLException {
    switch (type) {
      case STRING_ARRAY: {
        Array array = newStringArray((Collection<String>) value);
        allocatedArrays.add(array);
        parameters.add(array);
        break;
      }
      case INT_ARRAY: {
        Array array = newIntArray((Collection<Integer>) value);
        allocatedArrays.add(array);
        parameters.add(array);
        break;
      }
      case IID_ARRAY: {
        Array array = newIIDArray((Collection<Long>) value);
        allocatedArrays.add(array);
        parameters.add(array);
        break;
      }
      default:
        parameters.add(value);
        break;
    }
    parameterTypes.add(type);
    return this;
  }

  /**
   * Adapt the call declaration to vendor technology
   *
   * @param cs the prepared callable statement
   * @throws SQLException
   * @throws RDBMSException
   */
  protected abstract void prepareCallableStatement(CallableStatement cs)
          throws SQLException, RDBMSException;

  /**
   * Get a new callable statement according to vendor technology
   *
   * @return the vendor callable statement
   * @throws SQLException
   * @throws RDBMSException
   */
  protected abstract CallableStatement getCallableStatement() throws SQLException, RDBMSException;

  /**
   * @param array contains array values
   * @return a JDBC allocated array
   * @throws SQLException
   */
  public abstract Array newStringArray(Collection<String> array) throws SQLException;

  /**
   * @param array contains IIDs
   * @return a JDBC allocated array
   * @throws SQLException
   */
  public abstract Array newIIDArray(Collection<Long> array) throws SQLException;

  /**
   * @param array contains integers
   * @return a JDBC allocated array
   * @throws SQLException
   */
  public abstract Array newIntArray(Collection<Integer> array) throws SQLException;

  /**
   * Execute the call
   *
   * @return a result set in case of function or null in case of procedure
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IResultSetParser execute() throws SQLException, RDBMSException {
    callableStatement = getCallableStatement();
    prepareCallableStatement(callableStatement);
    callableStatement.execute();
    IResultSetParser result = null;
    if (resultType != ResultType.VOID) {
      result = driver.getResultSetParser(resultType, callableStatement);
    }
    clearInput();
    callableStatement = null; // clear callable statement for another call
    return result;
  }

  /**
   * Add the call to a batch
   *
   * @throws SQLException
   * @throws RDBMSException
   */
  public void addBatch() throws SQLException, RDBMSException {
    if (resultType != ResultType.VOID) {
      throw new RDBMSException(0, "Error", "A function cannot be called in batch mode");
    }
    if (callableStatement == null) {
      callableStatement = getCallableStatement();
    }
    prepareCallableStatement(callableStatement);
    callableStatement.addBatch();
    clearInput();
  }

  /**
   * Execute the call as batch
   *
   * @return an error list that contains the command numbers which failed in the batch
   * @throws SQLException
   * @throws RDBMSException
   */
  public List<Integer> executeBatch() throws SQLException, RDBMSException {
    int[] executions = callableStatement.executeBatch();
    List<Integer> errorNbs = new ArrayList<>();
    for (int i = 0; i < executions.length; i++) {
      if (executions[i] == Statement.EXECUTE_FAILED) {
        errorNbs.add(i);
      }
    }
    clearInput();
    return errorNbs;
  }

  /**
   * Free any JDBC resource when the object is released
   *
   * @throws Throwable
   */
  @Override
  protected void finalize() throws Throwable {
    try {
      // free the string arrays when declared
      for (Array array : allocatedArrays) {
        array.free();
      }
    } catch (SQLException ex) {
      // no other choice to do nothing in that case...
    } finally {
      super.finalize();
    }
  }
}
