/*package com.cs.core.runtime.strategy.apis.klassinstance;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.constants.klassinstance.ArticleInstanceQueryConstants;
import com.cs.core.runtime.interactor.constants.klassinstance.PostgreConstants;

public class ArticleAttributeTableData extends TableData implements ITableData {

  public ArticleAttributeTableData()
  {
    columns = getColumns();
  }

  @Override
  public List<String> getColumns()
  {
    ArrayList<String> articleAttributeColumns = new ArrayList<>(Arrays.asList("id",
        "klassinstanceid", "code", "language", "conflictingvalues", "isconflictresolved",
        "isunique", "basetype", "attributeid", "notification", "isshouldviolated", "context",
        "variantinstanceid", "value", "valueasexpression", "valueasnumber", "valueashtml",
        "ismatchandmerge", "originalinstanceid", "klassinstanceversion", "tags", "jobid",
        "ismandatoryviolated", "duplicatestatus", "versionid", "versiontimestamp", "createdby",
        "createdon", "lastmodifiedby", "lastmodified"));
    return PostgreConstants.STANDARD_ATTRIBUTE_TABLE_COLUMNS;
  }

  @Override
  public String getCreateQuery()
  {
    String createArticleAttributeKey = ArticleInstanceQueryConstants.ARTICLE_ATTRIBUTE_CREATE_QUERY_KEY;
    String articleAttributeCreateQuery = getQuery(createArticleAttributeKey);
    return articleAttributeCreateQuery;
  }

  @Override
  public void createInsertStatement() throws SQLException
  {
    String articleAttributeCreateQuery = getCreateQuery();
    createUpdateStatementFor(articleAttributeCreateQuery);
  }

  @Override
  public PreparedStatement getCreateStatement()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addData(Map<String, Object> columnsValue) throws Exception
  {
    setValuesToParameterizedQuery(columns, columnsValue);
  }

  @Override
  public void insertData() throws SQLException
  {
    executeCreateOrUpdate();

  }

  @Override
  public void addDataToBatch(Map<String, Object> columnsValue) throws Exception
  {
    addData(columnsValue);
    sendDataToBatch();

  }

  @Override
  public void insertBatchData() throws SQLException
  {
    executeCreateOrUpdateForBatch();
  }
}
*/
