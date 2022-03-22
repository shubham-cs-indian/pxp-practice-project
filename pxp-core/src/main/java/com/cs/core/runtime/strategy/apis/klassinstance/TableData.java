/*package com.cs.core.runtime.strategy.apis.klassinstance;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.postgresql.util.PGobject;

import com.cs.core.runtime.strategy.db.PostgreConnectionDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TableData {

  public PreparedStatement statement;
  public Connection        connection;
  public List<String> columns;

  public void useConnection(Connection connection)
  {
    this.connection = connection;
  }

  public void createUpdateStatementFor(String query) throws SQLException
  {
    statement = connection.prepareStatement(query);
  }

  public PreparedStatement getStatement()
  {
    return statement;
  }

  public void setColumns(ArrayList<String> columns)
  {
    this.columns = columns;
  }

  public void executeCreateOrUpdate() throws SQLException
  {
    statement.executeUpdate();
  }

  public void sendDataToBatch() throws SQLException
  {
    statement.addBatch();
    if(statement == null) {
      statement = connection.prepareStatement(temporaryStatement.toString());
    } else {
      statement.addBatch(temporaryStatement.toString());
    }
  }

  public void executeCreateOrUpdateForBatch() throws SQLException
  {
    statement.executeBatch();
  }

  public String getQuery(String type)
  {
    String query = getAllQueries().getProperty(type);
    return query;
  }

  private Properties getAllQueries()
  {
    return PostgreConnectionDetails.SCHEMA_AND_QUERIES;
  }

  private Integer getColumnIndexForQuery(String column)
  {
    // statement index start with one
    Integer columnsIndex = columns.indexOf(column) + 1;
    return columnsIndex;

  }

  public Boolean isTimeStampColumn(String column)
  {
    ArrayList<String> timeStampColumns = getTimeStampColumns();
    Boolean isTimeStampColumn = timeStampColumns.contains(column);
    return isTimeStampColumn;
  }

  private ArrayList<String> getTimeStampColumns()
  {
    ArrayList<String> timeStampColumn = new ArrayList<>(
        Arrays.asList("versiontimestamp", "createdon", "lastmodified"));
    return timeStampColumn;
  }

  public void setValuesToParameterizedQuery(List<String> columns,
      Map<String, Object> columnsValue) throws Exception
  {
    Map<String, Object> columnsValuesWithLowerCaseKey = convertMapKeyToLowerCase(columnsValue);
    for (String column : columns) {
      Object columnValue = columnsValuesWithLowerCaseKey.get(column);
      int columnIndex = getColumnIndexForQuery(column);
      checkColumnTypeAndAddValueToParameterizedQuery(column, columnValue, columnIndex);
    }
  }
  private Map<String, Object> convertMapKeyToLowerCase(Map<String, Object> map){
    Map<String, Object> lowerCaseMap = new HashMap<>(map.size());
    for (Map.Entry<String, Object> entry : map.entrySet()) {
       lowerCaseMap.put(entry.getKey().toLowerCase(), entry.getValue());
    }
    return lowerCaseMap;
  }
  private void checkColumnTypeAndAddValueToParameterizedQuery(String columnName, Object columnValue, Integer columnIndex) throws Exception
  {

    if (columnValue instanceof String) {

      setStringValue(columnValue, columnIndex);

    }
    else if (columnValue instanceof Long) {

      Boolean isLongUsedForTimeStampColumn = isTimeStampColumn(columnName);

      if (isLongUsedForTimeStampColumn)
        setTimeStampValue(columnValue, columnIndex);
      else
        setLongValue(columnValue, columnIndex);

    }
    else if (columnValue instanceof Integer) {

      setIntegerValue(columnValue, columnIndex);

    }
    //TODO tested with list of string only - columnValue
    else if (columnValue instanceof List) {
		try {
			setStringArrayValue(columnValue, columnIndex);
		} catch (ArrayStoreException e) {// Temp fixed for List of Object
			setJsonValue(columnValue, columnIndex);
		}
    }
    else if (columnValue instanceof Boolean) {
      setBooleanValue(columnValue, columnIndex);

    } else if(columnValue instanceof Double){
      setDoubleValue(columnValue, columnIndex);
    }
    else {

      setJsonValue(columnValue, columnIndex);

    }

  }

  private void setDoubleValue(Object value, Integer index) throws SQLException
  {
    Integer type = java.sql.Types.INTEGER;
    if (Objects.isNull(value)) {
      setNullValue(index, type);
    }
    else {
      statement.setDouble(index, (Double) value);
    }
  }

  private Boolean isStringList(List values)
  {

  }

  private void setStringValue(Object value, Integer index) throws SQLException
  {
    Integer type = java.sql.Types.CHAR;
    if (Objects.isNull(value)) {
      setNullValue(index, type);
    }
    else {
      statement.setString(index, (String) value);
    }
  }

  private void setLongValue(Object value, Integer index) throws SQLException
  {
    Integer type = java.sql.Types.INTEGER;
    if (Objects.isNull(value)) {
      setNullValue(index, type);
    }
    else {
      statement.setLong(index, (long) value);
    }
  }

  private void setTimeStampValue(Object value, Integer index) throws SQLException
  {
    Integer type = java.sql.Types.TIMESTAMP;
    if (Objects.isNull(value)) {
      setNullValue(index, type);
    }
    else {
      statement.setTimestamp(index, new Timestamp((Long) value));
    }
  }

  private void setIntegerValue(Object value, Integer index) throws SQLException
  {
    Integer type = java.sql.Types.INTEGER;
    if (Objects.isNull(value)) {
      setNullValue(index, type);

    }
    else {
      statement.setInt(index, (Integer)value);
    }
  }

  private void setBooleanValue(Object value, Integer index) throws SQLException
  {
    Integer type = java.sql.Types.BOOLEAN;
    if (Objects.isNull(value)) {
      setNullValue(index, type);
    }
    else {
      statement.setBoolean(index, false);
    }
  }

  private void setStringArrayValue(Object value, Integer index) throws Exception
  {
    Integer type = java.sql.Types.ARRAY;
    if (Objects.isNull(value) || ((List<String>) value).isEmpty()) {
      setNullValue(index, type);
    }
    else {
      String[] columnValueInJavaStringArray = convertArrayListToStringArray((List<String>) value);
      Array columnValueInSqlArray = convertJavaStringArrayToSqlStringArray(
          columnValueInJavaStringArray);
      statement.setArray(index, columnValueInSqlArray);

    }
  }

  private String[] convertArrayListToStringArray(List<String> value)
  {
    Integer sizeOfList = value.size();
    String[] stringArrayOfSizeOfList = new String[sizeOfList];
    return putFromListToStringArray(value, stringArrayOfSizeOfList);
  }

  private String[] putFromListToStringArray(List<String> from, String[] to)
  {
    return from.toArray(to);
  }

  private Array convertJavaStringArrayToSqlStringArray(String[] javaStringArray) throws SQLException
  {
    Array postGreSqlArray = connection.createArrayOf("text", javaStringArray);
    return postGreSqlArray;
  }

  private void setJsonValue(Object value, Integer index)
      throws SQLException, JsonProcessingException
  {
    Integer type = java.sql.Types.OTHER;
    if (Objects.isNull(value)) {
      setNullValue(index, type);
    }
    else {
      PGobject jsonObject = converObjectToPGJsonObject(value);
      statement.setObject(index, jsonObject, java.sql.Types.OTHER);
    }
  }

  private void setNullValue(Integer index, Integer type) throws SQLException
  {
    statement.setNull(index, type);
  }

  public PGobject converObjectToPGJsonObject(Object object)
      throws JsonProcessingException, SQLException
  {
    String json;
    json = getJsonFromObject(object);
    PGobject pgJsonObject = getPGobjectFromJsonString(json);
    return pgJsonObject;
  }

  private String getJsonFromObject(Object object) throws JsonProcessingException
  {
    String json;
    ObjectWriter ow = new ObjectMapper().writer()
        .withDefaultPrettyPrinter();
    json = ow.writeValueAsString(object);
    return json;
  }

  private PGobject getPGobjectFromJsonString(String json) throws SQLException
  {
    PGobject jsonObject = new PGobject();
    jsonObject.setType("json");
    jsonObject.setValue(json);
    return jsonObject;

  }

}
*/
