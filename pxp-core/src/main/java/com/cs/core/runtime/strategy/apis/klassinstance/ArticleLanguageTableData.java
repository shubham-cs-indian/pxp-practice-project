/*package com.cs.core.runtime.strategy.apis.klassinstance;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.constants.klassinstance.ArticleInstanceQueryConstants;
import com.cs.core.runtime.interactor.constants.klassinstance.PostgreConstants;

public class ArticleLanguageTableData extends TableData implements ITableData {

  public ArticleLanguageTableData()
  {
    columns = getColumns();
  }

  @Override
  public List<String> getColumns()
  {
    ArrayList<String> articleLanguageColumns = new ArrayList<>(
        Arrays.asList(PostgreConstants.COLUMN_NAME_ID, PostgreConstants.COLUMN_NAME_LANG_ID,
            PostgreConstants.COLUMN_NAME_NAME));
    return PostgreConstants.STANDARD_LANG_TABLE_COLUMNS;
  }

  @Override
  public String getCreateQuery()
  {
    String createArticleLanguageKey = ArticleInstanceQueryConstants.ARTICLE_LANG_CREATE_QUERY_KEY;
    String articleLanguageCreateQuery = getQuery(createArticleLanguageKey);
    return articleLanguageCreateQuery;
  }

  @Override
  public void createInsertStatement() throws SQLException
  {
    String articleLanguageCreateQuery = getCreateQuery();
    createUpdateStatementFor(articleLanguageCreateQuery);
  }

  @Override
  public PreparedStatement getCreateStatement()
  {
    return null;
  }

  @Override
  public void addData(Map<String, Object> columnsValue) throws Exception
  {
    createInsertStatement();
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
