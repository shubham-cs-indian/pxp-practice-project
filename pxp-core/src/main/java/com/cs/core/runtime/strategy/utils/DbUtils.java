package com.cs.core.runtime.strategy.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Selection;
import com.datastax.driver.core.querybuilder.Update;

@Component
public class DbUtils {
  
  @Autowired
  protected Connection postgresConnection;
  
  public final Boolean getTableExist(String tableName) throws Exception
  {
    DatabaseMetaData metaData = postgresConnection.getMetaData();
    ResultSet tables = metaData.getTables(null, null, tableName, null);
    if (tables.next()) {
      return true;
    }
    else {
      return false;
    }
  }
  
  public final Boolean getTrigerExist(String triggerName, String tableName) throws Exception
  {
    StringBuilder triggerQuery = new StringBuilder(
        "SELECT tgname FROM pg_trigger, pg_class WHERE tgrelid=pg_class.oid AND relname='");
    triggerQuery.append(tableName + "' AND tgname='" + triggerName + "'");
    List<Map<String, Object>> executeQueryWithResult = executeQueryWithResult(
        triggerQuery.toString());
    if (!executeQueryWithResult.isEmpty()) {
      return true;
    }
    return false;
  }
  
  private final void executeQueryWithoutResult(String query) throws Exception
  {
    Statement createStatement = getStatementFromConnection();
    createStatement.execute(query);
  }
  
  public final String quoteIt(String unquotedString)
  {
    return "\"" + unquotedString + "\"";
  }
  
  public static String quoteIt(Collection<String> listOfStrings)
  {
    StringBuilder classesArrayInString = new StringBuilder();
    classesArrayInString.append("(");
    int arraySize = listOfStrings.size();
    for (String listItem : listOfStrings) {
      arraySize--;
      classesArrayInString.append("'" + listItem + "'");
      if (arraySize != 0) {
        classesArrayInString.append(", ");
      }
    }
    classesArrayInString.append(")");
    
    return classesArrayInString.toString();
  }
  
  public final void insertQuery(String tableName, Map<String, Object> tableColumnnValues)
      throws Exception
  {
    Insert insertQuery = QueryBuilder.insertInto(tableName);
    Set<String> columnNames = tableColumnnValues.keySet();
    for (String columnName : columnNames) {
      insertQuery.value(quoteIt(columnName), tableColumnnValues.get(columnName));
    }
    
    executeQueryWithoutResult(insertQuery.toString());
  }
  
  public final void createTableQuery(String tableName, List<Map<String, Object>> listOfTableColumns)
      throws Exception
  {
    int size = listOfTableColumns.size();
    int loopCount = 1;
    StringBuilder createTable = new StringBuilder("CREATE TABLE " + tableName + " (");
    for (Map<String, Object> map : listOfTableColumns) {
      createTable.append(quoteIt((String) map.get(TransferConstants.COLUMN_NAME)) + " ");
      createTable.append(getDataTypeWithSize((String) map.get(TransferConstants.COLUMN_TYPE),
          (Integer) map.get(TransferConstants.COLUMN_SIZE)) + " ");
      String constraints = (String) map.get(TransferConstants.COLUMN_CONSTRAINTS);
      if (constraints != null && loopCount != size) {
        createTable.append(constraints + " ,");
      }
      else if (loopCount != size) {
        createTable.append(" ,");
      }
      
      loopCount++;
    }
    createTable.append(" )");
    executeQueryWithoutResult(createTable.toString());
  }
  
  private final String getDataTypeWithSize(String typeOfColumn, Integer size) throws Exception
  {
    if (size != null) {
      typeOfColumn += "(" + size + ")";
    }
    return typeOfColumn;
  }
  
  public final List<Map<String, Object>> selectQuery(String tableName,
      List<String> tableColumnToFetch, Map<String, Object> conditionalColumnnValues)
      throws Exception
  {
    Select selectQuery = processSelectQuery(tableName, tableColumnToFetch,
        conditionalColumnnValues);
    
    return executeQueryWithResult(selectQuery.toString());
  }
  
  public final Map<String, Object> selectProcessStatusQuery(String tableName,
      List<String> tableColumnToFetch, Map<String, Object> conditionalColumnnValues)
      throws Exception
  {
    Select selectQuery = processSelectQuery(tableName, tableColumnToFetch,
        conditionalColumnnValues);
    Map returnMap = new HashMap<>();
    returnMap.put(IListModel.LIST, executeQueryWithResult(selectQuery.toString()));
    return returnMap;
  }
  
  private Select processSelectQuery(String tableName, List<String> tableColumnToFetch,
      Map<String, Object> conditionalColumnnValues)
  {
    Selection selectForColumn = QueryBuilder.select();
    if (tableColumnToFetch.isEmpty()) {
      selectForColumn.all();
    }
    else {
      for (String columnName : tableColumnToFetch) {
        selectForColumn.column(quoteIt(columnName));
      }
    }
    
    Select selectQuery = selectForColumn.from(tableName);
    
    Set<String> columnNamesForCondition = conditionalColumnnValues.keySet();
    for (String columnName : columnNamesForCondition) {
      if (conditionalColumnnValues.get(columnName) instanceof List) {
        List<String> values = (List<String>) conditionalColumnnValues.get(columnName);
        selectQuery.where(QueryBuilder.in(quoteIt(columnName), values));
      }
      else {
        selectQuery
            .where(QueryBuilder.eq(quoteIt(columnName), conditionalColumnnValues.get(columnName)));
      }
    }
    return selectQuery;
  }
  
  public final List<Map<String, Object>> selectQueryWithNotNull(String tableName,
      List<String> tableColumnToFetch, Map<String, Object> conditionalColumnnValues,
      List<String> notNullColumns) throws Exception
  {
    Select selectQuery = processSelectQuery(tableName, tableColumnToFetch,
        conditionalColumnnValues);
    
    for (String notNullColumn : notNullColumns) {
      selectQuery.where(QueryBuilder.notNull(quoteIt(notNullColumn)));
    }
    
    return executeQueryWithResult(selectQuery.toString());
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, Object> getModelIntoMap(IModel model)
  {
    return (Map<String, Object>) ObjectMapperUtil.convertValue(model, Map.class);
  }
  
  // @Authour : Abhaypratap Singh
  // Note: Don't put null value column into conditional column
  public final void updateQuery(String tableName, Map<String, Object> columnValuesToSet,
      Map<String, Object> conditionalColumnValue) throws Exception
  {
    Update updateQuery = processUpdateQuery(tableName, columnValuesToSet, conditionalColumnValue);
    
    executeQueryWithoutResult(updateQuery.toString());
  }
  
  private Update processUpdateQuery(String tableName, Map<String, Object> columnValuesToSet,
      Map<String, Object> conditionalColumnValue)
  {
    Update updateQuery = QueryBuilder.update(tableName);
    
    Set<String> columnNames = columnValuesToSet.keySet();
    for (String columnName : columnNames) {
      updateQuery.with(QueryBuilder.set(quoteIt(columnName), columnValuesToSet.get(columnName)));
    }
    
    Set<String> condtionalColumns = conditionalColumnValue.keySet();
    for (String columnName : condtionalColumns) {
      updateQuery
          .where(QueryBuilder.eq(quoteIt(columnName), conditionalColumnValue.get(columnName)));
    }
    return updateQuery;
  }
  
  public final void updateQueryWithNotNull(String tableName, Map<String, Object> columnValuesToSet,
      Map<String, Object> conditionalColumnValue, List<String> nullColumnList) throws Exception
  {
    Update updateQuery = processUpdateQuery(tableName, columnValuesToSet, conditionalColumnValue);
    
    for (String columnName : nullColumnList) {
      updateQuery.where(QueryBuilder.notNull(columnName));
    }
    
    executeQueryWithoutResult(updateQuery.toString());
  }
  
  private Statement getStatementFromConnection() throws SQLException
  {
    Statement createStatement = postgresConnection.createStatement();
    return createStatement;
  }
  
  private final List<Map<String, Object>> executeQueryWithResult(String query) throws Exception
  {
    List<Map<String, Object>> listOfResult = new ArrayList<>();
    Statement createStatement = getStatementFromConnection();
    ResultSet resultSet = createStatement.executeQuery(query);
    postProcessOfTheResult(new ArrayList<>(), listOfResult, resultSet);
    return listOfResult;
  }
  
  private void postProcessOfTheResult(List<String> listOfColumnsToFetch,
      List<Map<String, Object>> listOfResult, ResultSet resultSet) throws SQLException
  {
    List<String> columnNameList = getColumnNameList(listOfColumnsToFetch, resultSet);
    getTheListOfData(listOfResult, resultSet, columnNameList);
  }
  
  private void getTheListOfData(List<Map<String, Object>> listOfResult, ResultSet resultSet,
      List<String> columnNameList) throws SQLException
  {
    while (resultSet.next()) {
      Map<String, Object> mapOfData = new HashMap<>();
      for (String columnName : columnNameList) {
        mapOfData.put(columnName, resultSet.getObject(columnName));
      }
      listOfResult.add(mapOfData);
    }
  }
  
  private List<String> getColumnNameList(List<String> listOfColumnsToFetch, ResultSet resultSet)
      throws SQLException
  {
    List<String> columnNameList = null;
    if (listOfColumnsToFetch.isEmpty()) {
      columnNameList = getListOfColumnName(resultSet);
    }
    else {
      columnNameList = listOfColumnsToFetch;
    }
    return columnNameList;
  }
  
  private List<String> getListOfColumnName(ResultSet resultSet) throws SQLException
  {
    ResultSetMetaData metaData = resultSet.getMetaData();
    int columnCount = metaData.getColumnCount();
    int i = 1;
    List<String> columnNameList = new ArrayList<>();
    while (i <= columnCount) {
      columnNameList.add(metaData.getColumnLabel(i));
      i++;
    }
    return columnNameList;
  }
  
  public String getValueInString(List<String> list)
  {
    StringBuilder strBuilder = new StringBuilder();
    int size = list.size();
    int j = 1;
    for (String value : list) {
      if (j == size) {
        strBuilder.append("\'" + value + "\'");
      }
      else {
        strBuilder.append("\'" + value + "\',");
      }
      j++;
    }
    return strBuilder.toString();
  }
  
  public final Map<String, Object> fillColumnValues(String columnName, String columnType,
      Integer columnSize, String columnConstraints)
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(TransferConstants.COLUMN_NAME, columnName);
    returnMap.put(TransferConstants.COLUMN_TYPE, columnType);
    returnMap.put(TransferConstants.COLUMN_SIZE, columnSize);
    returnMap.put(TransferConstants.COLUMN_CONSTRAINTS, columnConstraints);
    return returnMap;
  }
  
  public final void deleteQuery(String tableName, Map<String, Object> conditionalColumnnValues)
      throws Exception
  {
    Delete deleteQuery = processDeleteQuery(tableName, conditionalColumnnValues);
    
    executeQueryWithoutResult(deleteQuery.toString());
  }
  
  private Delete processDeleteQuery(String tableName, Map<String, Object> conditionalColumnnValues)
  {
    com.datastax.driver.core.querybuilder.Delete.Selection delete = QueryBuilder.delete();
    Delete deleteQuery = delete.from(tableName);
    
    Set<String> columnNamesForCondition = conditionalColumnnValues.keySet();
    for (String columnName : columnNamesForCondition) {
      if (conditionalColumnnValues.get(columnName) instanceof List) {
        List<String> values = (List<String>) conditionalColumnnValues.get(columnName);
        deleteQuery.where(QueryBuilder.in(quoteIt(columnName), values));
      }
      else {
        deleteQuery
            .where(QueryBuilder.eq(quoteIt(columnName), conditionalColumnnValues.get(columnName)));
      }
    }
    return deleteQuery;
  }
  
  public void functionCreation(String functionName, String ifQuery, String elseQuery,
      String condition) throws Exception
  {
    StringBuilder functionQuery = new StringBuilder("CREATE OR REPLACE FUNCTION ");
    functionQuery.append(functionName + "() RETURNS trigger AS $BODY$ BEGIN ");
    
    functionQuery.append(" IF " + condition + " THEN ");
    functionQuery.append(ifQuery);
    if (elseQuery != null) {
      functionQuery.append(" ELSE ");
      functionQuery.append(elseQuery);
    }
    functionQuery.append(" END IF; ");
    
    functionQuery.append(" RETURN NEW; END; $BODY$ LANGUAGE 'plpgsql'");
    
    executeQueryWithoutResult(functionQuery.toString());
  }
  
  public void createTriggerQuery(String functionName, String triggerName, String executionCondition,
      String tableName) throws Exception
  {
    StringBuffer triggerQuery = new StringBuffer(
        " CREATE TRIGGER " + triggerName + " " + executionCondition);
    triggerQuery.append(" ON " + tableName + " FOR EACH ROW EXECUTE PROCEDURE ");
    triggerQuery.append(functionName + "() ");
    executeQueryWithoutResult(triggerQuery.toString());
  }
}
