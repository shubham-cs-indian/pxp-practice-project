/*package com.cs.core.runtime.strategy.apis.klassinstance;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.constants.klassinstance.ArticleInstanceQueryConstants;
import com.cs.core.runtime.interactor.constants.klassinstance.PostgreConstants;

public class ArticleTagTableData extends TableData implements ITableData {

  public ArticleTagTableData()
  {
    columns = getColumns();
  }

  @Override
  public List<String> getColumns()
  {
    ArrayList<String> articleTagColumns = new ArrayList<>(
        Arrays.asList("id", "klassinstanceid", "conflictingvalues", "ismatchandmerge", "tagid",
            "variantof", "basetype", "klassinstanceversion", "tags", "jobid", "notification",
            "isconflictresolved", "isshouldviolated", "ismandatoryviolated", "variantinstanceid",
            "contextinstanceid", "tagvalues", "versionid", "versiontimestamp", "createdby",
            "createdon", "lastmodifiedby", "lastmodified"));
    return PostgreConstants.STANDARD_TAG_TABLE_COLUMNS;
  }

  @Override
  public String getCreateQuery()
  {
    String createArticleTagKey = ArticleInstanceQueryConstants.ARTICLE_TAG_CREATE_QUERY_KEY;
    String articleTagCreateQuery = getQuery(createArticleTagKey);
    return articleTagCreateQuery;
  }

  @Override
  public void createInsertStatement() throws SQLException
  {
    String articleTagCreateQuery = getCreateQuery();
    createUpdateStatementFor(articleTagCreateQuery);
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
