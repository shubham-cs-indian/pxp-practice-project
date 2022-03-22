/*package com.cs.core.runtime.strategy.db;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.postgresql.util.PGobject;

import com.cs.core.runtime.interactor.constants.klassinstance.PostgreConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@SuppressWarnings("unchecked")
public class PostgreDBUtil {


  public static List<String> getColumnNames(String tableName) throws Exception
  {
    List<String> columnNames = new ArrayList<>();
    String query = "SELECT column_name\r\n" + "FROM information_schema.columns\r\n" + "WHERE table_name   = '"
        + tableName + "'\r\n" + " AND  table_schema = 'public'";

    try(Connection connection =PostgreDBConnectionUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);) {

      while (result.next()) {
        columnNames.add((String) result.getObject(1));
      }
    } catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }

    return columnNames;
  }

	
	 * public static String getMasterTableName(PostgreConstants.ItemType itemType) {
	 * String tableName = null; if
	 * (itemType.equals(PostgreConstants.ItemType.ARTICLE)) { tableName =
	 * getSchema(PostgreConstants.ItemType.ARTICLE) +
	 * PostgreConstants.ARTICLE_TABLE_NAME; }
	 *
	 * return tableName; }
	
  public static String getAttributeTableName(PostgreConstants.ItemType itemType)
  {
    String tableName = null;
    if (itemType.equals(PostgreConstants.ItemType.ARTICLE)) {
      tableName = getSchema(PostgreConstants.ItemType.ARTICLE) + PostgreConstants.ATTRIBUTE_TABLE_NAME;
    }

    return tableName;
  }

	
	 * public static String getLanguageTableName(PostgreConstants.ItemType itemType)
	 * { String tableName = null; if
	 * (itemType.equals(PostgreConstants.ItemType.ARTICLE)) { tableName =
	 * getSchema(PostgreConstants.ItemType.ARTICLE) +
	 * PostgreConstants.LANGUAGE_TABLE_NAME; }
	 *
	 * return tableName; }
	
	
	 * public static String getTagTableName(PostgreConstants.ItemType itemType) {
	 * String tableName = null; if
	 * (itemType.equals(PostgreConstants.ItemType.ARTICLE)) { tableName =
	 * getSchema(PostgreConstants.ItemType.ARTICLE) +
	 * PostgreConstants.TAG_TABLE_NAME; }
	 *
	 * return tableName; }
	
	
	 * public static String getAttributesByItemIDQuery(PostgreConstants.ItemType
	 * itemType, String itemId) { String tableName =
	 * PostgreDBUtil.getAttributeTableName(itemType); String query = " SELECT *" +
	 * " FROM " + tableName + " WHERE " + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.KLASS_INSTANCE_ID +PostgreConstants. COLUMN_QUOTE + " = '" +
	 * itemId + "'";
	 *
	 * return query; }
	

	
	 * public static String getTagsByItemIDQuery(PostgreConstants.ItemType itemType,
	 * String itemId) { String tableName = PostgreDBUtil.getTagTableName(itemType);
	 * String query = " SELECT *" + " FROM " + tableName + " WHERE " +
	 * PostgreConstants.COLUMN_QUOTE + PostgreConstants.KLASS_INSTANCE_ID +
	 * PostgreConstants.COLUMN_QUOTE + " = '" + itemId + "'";
	 *
	 * return query; }
	
	
	 * public static String getAllItemsQuery(ItemType itemType,
	 * IGetKlassInstanceTreeStrategyModel klassInstanceTreeModel) { String tableName
	 * = PostgreDBUtil.getMasterTableName(itemType); String query = " SELECT *" +
	 * " FROM " + tableName + " ORDER BY " + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.ID + PostgreConstants.COLUMN_QUOTE + " LIMIT " +
	 * klassInstanceTreeModel.getSize() + " OFFSET " +
	 * klassInstanceTreeModel.getFrom();
	 *
	 * return query; }
	
	
	 * public static String getItemByIdQuery(PostgreConstants.ItemType itemType,
	 * String itemId) { String tableName =
	 * PostgreDBUtil.getMasterTableName(itemType); String query = " SELECT *" +
	 * " FROM " + tableName + " WHERE " + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.ID + PostgreConstants.COLUMN_QUOTE + " = '" + itemId + "'";
	 *
	 * return query; }
	
	
	 * public static String getItemByNameQuery(PostgreConstants.ItemType itemType,
	 * String name) { String tableName = PostgreDBUtil.getMasterTableName(itemType);
	 * String languageTableName = getLanguageTableName(itemType); String query =
	 * " SELECT ar.*" + " FROM " + tableName + " ar" + " INNER JOIN " +
	 * languageTableName + " lan" + " ON lan.id = ar.id" + " WHERE lan." +
	 * PostgreConstants.COLUMN_QUOTE + PostgreConstants.NAME +
	 * PostgreConstants.COLUMN_QUOTE + " LIKE '%" + name + "%'";
	 *
	 * return query; }
	
  public static String getAttributesByValueExactQuery(PostgreConstants.ItemType itemType, String value, String attributeId)
  {
    String attributeIdClause = getAttributeIdClause(attributeId);
    String tableName = PostgreDBUtil.getAttributeTableName(itemType);
    String query = " SELECT *"
        + " FROM " + tableName
        + " WHERE " + PostgreConstants.COLUMN_QUOTE + PostgreConstants.VALUE  + PostgreConstants.COLUMN_QUOTE + " = '" + value + "'"
        + attributeIdClause;

    return query;
  }

  public static String getAttributesByValueNotEqualsQuery(PostgreConstants.ItemType itemType, String value)
  {
    String tableName = PostgreDBUtil.getAttributeTableName(itemType);
    String query = " SELECT *"
        + " FROM " + tableName
        + " WHERE " + PostgreConstants.COLUMN_QUOTE + PostgreConstants.VALUE  + PostgreConstants.COLUMN_QUOTE + " <> '" + value + "'";

    return query;
  }

  public static String getAttributesByValueContainsQuery(PostgreConstants.ItemType itemType, String value, String attributeId)
  {
    String attributeIdClause = getAttributeIdClause(attributeId);
    String tableName = PostgreDBUtil.getAttributeTableName(itemType);
    String query = " SELECT *"
        + " FROM " + tableName
        + " WHERE " + PostgreConstants.COLUMN_QUOTE + PostgreConstants.VALUE  + PostgreConstants.COLUMN_QUOTE + " LIKE '" + PostgreConstants.STRING_LIKE_CONSTANT  + value + PostgreConstants.STRING_LIKE_CONSTANT  + "'"
        + attributeIdClause;

    return query;
  }

  public static String getAttributesByValueNotContainsQuery(PostgreConstants.ItemType itemType, String value, String attributeId)
  {
    String attributeIdClause = getAttributeIdClause(attributeId);
    String tableName = PostgreDBUtil.getAttributeTableName(itemType);
    String query = " SELECT *"
        + " FROM " + tableName
        + " WHERE " + PostgreConstants.COLUMN_QUOTE + PostgreConstants.VALUE  + PostgreConstants.COLUMN_QUOTE + " NOT LIKE '" + PostgreConstants.STRING_LIKE_CONSTANT  + value + PostgreConstants.STRING_LIKE_CONSTANT + "'"
        + attributeIdClause;

    return query;
  }

  public static String getAttributesByValueStartsWithQuery(PostgreConstants.ItemType itemType, String value, String attributeId)
  {
    String attributeIdClause = getAttributeIdClause(attributeId);
    String tableName = PostgreDBUtil.getAttributeTableName(itemType);
    String query = " SELECT *"
        + " FROM " + tableName
        + " WHERE " + PostgreConstants.COLUMN_QUOTE + PostgreConstants.VALUE  + PostgreConstants.COLUMN_QUOTE + " LIKE '" + value + PostgreConstants.STRING_LIKE_CONSTANT + "'"
        + attributeIdClause;

    return query;
  }

  public static String getAttributesByValueEndsWithQuery(PostgreConstants.ItemType itemType, String value, String attributeId)
  {
    String attributeIdClause = getAttributeIdClause(attributeId);
    String tableName = PostgreDBUtil.getAttributeTableName(itemType);
    String query = " SELECT *"
        + " FROM " + tableName
        + " WHERE " + PostgreConstants.COLUMN_QUOTE + PostgreConstants.VALUE  + PostgreConstants.COLUMN_QUOTE + " LIKE "+ PostgreConstants.STRING_LIKE_CONSTANT + value + "'"
        + attributeIdClause;

    return query;
  }

  public static String getInClause(Set<String> dataSet)
  {
    String inCluase = "(";
    for (String data : dataSet) {
      inCluase += "'" + data + "',";
    }
    if(!dataSet.isEmpty()) {
      inCluase = inCluase.substring(0, inCluase.length() - 1);
    }
    else {
      inCluase += "''";
    }
    inCluase += ")";

    return inCluase;
  }

	
	 * public static String getItemsByIdsQuery(PostgreConstants.ItemType itemType,
	 * Set<String> ids) { String tableName =
	 * PostgreDBUtil.getMasterTableName(itemType); String query = " SELECT *" +
	 * " FROM " + tableName + " WHERE " + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.ID + PostgreConstants.COLUMN_QUOTE + " IN " +
	 * PostgreDBUtil.getInClause(ids);
	 *
	 * return query; }
	
	
	 * public static String getAttributesByItemIdsQuery(PostgreConstants.ItemType
	 * itemType, Set<String> ids) { String tableName =
	 * PostgreDBUtil.getAttributeTableName(itemType); String query = " SELECT *" +
	 * " FROM " + tableName + " WHERE " + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.KLASS_INSTANCE_ID + PostgreConstants.COLUMN_QUOTE + " IN " +
	 * PostgreDBUtil.getInClause(ids);
	 *
	 * return query; }
	

	
	 * public static String getTagsByItemIdsQuery(PostgreConstants.ItemType
	 * itemType, Set<String> ids) { String tableName =
	 * PostgreDBUtil.getTagTableName(itemType); String query = " SELECT *" +
	 * " FROM " + tableName + " WHERE " + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.KLASS_INSTANCE_ID + PostgreConstants.COLUMN_QUOTE + " IN " +
	 * PostgreDBUtil.getInClause(ids);
	 *
	 * return query; }
	

  private static String getAttributeIdClause(String attributeId)
  {
    return attributeId != null ? "AND \"" + PostgreConstants.COLUMN_NAME_ATTRIBUTE_ID +"\" = '" + attributeId + "'" : "";
  }

  public static List<String> getAllIgnoredCoumns()
  {
    //componentId data type mismatch
    return Arrays.asList(PostgreConstants.COLUMN_NAME_OWNER,
        PostgreConstants.COLUMN_NAME_COMPONENT_ID, PostgreConstants.COLUMN_NAME_IS_SKIPPED,
        PostgreConstants.COLUMN_NAME_IS_FROM_EXTERNAL_SOURCE);
  }

	
	 * public static String getTagsByTagIdsQuery(PostgreConstants.ItemType itemType,
	 * Set<String> ids) { String tableName =
	 * PostgreDBUtil.getTagTableName(itemType); String query = " SELECT *" +
	 * " FROM " + tableName + " WHERE " + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.ID + PostgreConstants.COLUMN_QUOTE + " IN " +
	 * PostgreDBUtil.getInClause(ids);
	 *
	 * return query; }
	

  public static Map<String,Object> buildQuery(Map<String, Object> values, List<String> fields, String table) {
    StringBuilder columns = new StringBuilder();
    StringBuilder vals = new StringBuilder();
    int i = 1;
    Map<String, Integer> queryIndexValue = new HashMap<>();
    for (Object col : values.keySet()) {
      if(fields.contains((String)col)) {
        columns.append("\"").append((String)col).append("\"").append(",");
        vals.append('?').append(",");
        queryIndexValue.put((String)col, i);
        i++;
      }

    }
    columns.setLength(columns.length()-1);
    vals.setLength(vals.length()-1);
    // TODO: Please check for the schema. Currently it is working with default schema only

    String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table,
        columns.toString(), vals.toString());
    Map<String,Object> queryWithColumnIndex = new HashMap<String, Object>();
    queryWithColumnIndex.put("query", query);
    queryWithColumnIndex.put("columnIndex", queryIndexValue);
    return queryWithColumnIndex;
  }

  public static void setQueryParameter(Map<String, ArrayList<String>> fields,
      Map<String, Object> values, Map<String, Integer> queryIndexValue, PreparedStatement statement)
      throws Exception
  {
    for (Object col : values.keySet()) {

      Object value = values.get(col);
      String key = (String) col;

      if (fields.get("stringFields")
          .contains(key)) {

        if (value == null) {
          statement.setNull(queryIndexValue.get(key), java.sql.Types.CHAR);
        }
        else {
          statement.setString(queryIndexValue.get(key), (String) value);
        }

      }
      else if (fields.get("timeStampFields")
          .contains(key)) {
        if (value == null) {
          statement.setNull(queryIndexValue.get(key), java.sql.Types.TIMESTAMP);
        }
        else {
          statement.setTimestamp(queryIndexValue.get(key), new Timestamp((Long) values.get(col)));

        }
      }
      else if (fields.get("longFields")
          .contains(key)) {
        if (value == null) {
          statement.setNull(queryIndexValue.get(key), java.sql.Types.INTEGER);
        }
        else {
          statement.setLong(queryIndexValue.get(key), (long) values.get(col));
        }
      }
      else if (fields.get("intFields")
          .contains(key)) {
        if (value == null) {
          statement.setNull(queryIndexValue.get(key), java.sql.Types.INTEGER);
        }
        else {
          statement.setInt(queryIndexValue.get(key), (int) values.get(col));
        }
      }
      else if (fields.get("arrayFields")
          .contains(key)) {
        if (value == null) {
          statement.setNull(queryIndexValue.get(key), java.sql.Types.ARRAY);
        }
        else {
          String[] columnValueInJavaArray = ((List<String>) value)
              .toArray(new String[((List<String>) value).size()]);
          Array columnValueInSqlArray;
          try {
            columnValueInSqlArray = DatabaseConnection.getConnection()
                .createArrayOf("text", columnValueInJavaArray);
            statement.setArray(queryIndexValue.get((String) col), columnValueInSqlArray);
          }
          catch (Exception e) {
            RDBMSLogger.instance().exception(e);
          }
        }

      }
      else if (fields.get("jsonFields")
          .contains(key)) {
        if (value == null) {
          statement.setNull(queryIndexValue.get(key), java.sql.Types.OTHER);
        }
        else {
          PGobject jsonObject = convertPGJsonObject(values.get(col));
          statement.setObject(queryIndexValue.get(key), jsonObject, java.sql.Types.OTHER);
        }

      }
      else if (fields.get("booleanFields")
          .contains(key)) {
        // if(value == null) {
        // statement.setNull(queryIndexValue.get((String)col),
        // java.sql.Types.BOOLEAN);
        // }else {
        statement.setBoolean(queryIndexValue.get(key), false);
        // }
      }
    }
  }


	
	 * public static String getSaveAttributeValueQuery(ItemType itemType) { String
	 * tableName = PostgreDBUtil.getAttributeTableName(itemType); String query =
	 * " UPDATE " + tableName + " SET " + PostgreConstants.COLUMN_QUOTE+
	 * PostgreConstants.VALUE + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.PARAMETER_MARKER+ "," + PostgreConstants.COLUMN_QUOTE +
	 * "lastModified" + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.PARAMETER_MARKER + " Where" + PostgreConstants.COLUMN_QUOTE+
	 * PostgreConstants.ID + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.PARAMETER_MARKER +" AND " + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.KLASS_INSTANCE_ID+ PostgreConstants.COLUMN_QUOTE
	 * +PostgreConstants.PARAMETER_MARKER; return query; }
	

	
	 * public static String getSaveArticleNameQuery(ItemType itemType) { String
	 * tableName = PostgreDBUtil.getLanguageTableName(itemType); String query =
	 * " UPDATE " + tableName + " SET " + PostgreConstants.COLUMN_QUOTE+
	 * PostgreConstants.NAME + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.PARAMETER_MARKER + " Where" + PostgreConstants.COLUMN_QUOTE+
	 * PostgreConstants.ID + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.PARAMETER_MARKER +" AND " + PostgreConstants.COLUMN_QUOTE +
	 * "langid"+ PostgreConstants.COLUMN_QUOTE +PostgreConstants.PARAMETER_MARKER;
	 * return query; }
	

	
	 * public static String getSaveTagValuesQuery(ItemType itemType) { String
	 * tableName = PostgreDBUtil.getTagTableName(itemType); String query =
	 * " UPDATE " + tableName + " SET " + PostgreConstants.COLUMN_QUOTE+ "tagValues"
	 * + PostgreConstants.COLUMN_QUOTE + PostgreConstants.PARAMETER_MARKER+ "," +
	 * PostgreConstants.COLUMN_QUOTE + "lastModified" +
	 * PostgreConstants.COLUMN_QUOTE + PostgreConstants.PARAMETER_MARKER + " Where"
	 * + PostgreConstants.COLUMN_QUOTE+ PostgreConstants.ID +
	 * PostgreConstants.COLUMN_QUOTE + PostgreConstants.PARAMETER_MARKER +" AND " +
	 * PostgreConstants.COLUMN_QUOTE + PostgreConstants.KLASS_INSTANCE_ID+
	 * PostgreConstants.COLUMN_QUOTE +PostgreConstants.PARAMETER_MARKER; return
	 * query; }
	
	
	 * public static String getUpdateLastModiFiedQuery(ItemType itemType) { String
	 * tableName = PostgreDBUtil.getMasterTableName(itemType); String query =
	 * " UPDATE " + tableName + " SET " + PostgreConstants.COLUMN_QUOTE+
	 * "lastModified" + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.PARAMETER_MARKER + " Where" + PostgreConstants.COLUMN_QUOTE+
	 * PostgreConstants.ID + PostgreConstants.COLUMN_QUOTE +
	 * PostgreConstants.PARAMETER_MARKER; return query; }
	

  private static String getSchema(PostgreConstants.ItemType usecase)
  {
    String string = usecase.toString();
    String property = PostgreConnectionDetails.SCHEMA_AND_QUERIES.getProperty(string);
    return ((property != null) ? property + "." : "public.");
  }

	
	 * public static String getAllArticleCountQuery(ItemType itemType) { String
	 * tableName = PostgreDBUtil.getMasterTableName(itemType); String query =
	 * "SELECT COUNT("+ PostgreConstants.COLUMN_QUOTE+ PostgreConstants.ID +
	 * PostgreConstants.COLUMN_QUOTE+") FROM " + tableName; return query; }
	

  private static String getSchemaName(PostgreConstants.ItemType usecase)
  {
    String string = usecase.toString();
    String property = PostgreConnectionDetails.SCHEMA_AND_QUERIES.getProperty(string);
    return ((property != null) ? property + "." : "public");
  }
   public static PGobject convertPGJsonObject(Object object) throws JsonProcessingException, SQLException {
	    String json;

	    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	    json = ow.writeValueAsString(object);


	    PGobject jsonObject = new PGobject();
	    jsonObject.setType("json");
	    jsonObject.setValue(json);
	    return jsonObject;
	  }
}*/
