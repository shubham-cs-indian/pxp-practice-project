/*package com.cs.core.runtime.strategy.apis.klassinstance;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.constants.klassinstance.ArticleInstanceQueryConstants;
import com.cs.core.runtime.interactor.constants.klassinstance.PostgreConstants;

public class ArticleTableData extends TableData implements ITableData {

  public ArticleTableData()
  {
    columns = getColumns();
  }

  @Override
  public List<String> getColumns()
  {
    ArrayList<String> articleColumns = new ArrayList<>(Arrays.asList("id", "owner", "jobid",
        "basetype", "componentid", "roles", "types", "taxonomyids", "selectedtaxonomyids",
        "organizationid", "physicalcatalogid", "logicalcatalogid", "systemid", "endpointid",
        "originalinstanceid", "defaultassetinstanceid", "branchof", "versionof", "klassinstanceid",
        "parentid", "isskipped", "isfromexternalsource", "path", "variants", "attributevariants",
        "languageinstances", "languagecodes", "creationlanguage", "ruleviolation", "messages",
        "context", "summary", "partnersources", "relationships", "naturerelationships",
        "isembedded", "savecomment", "versionid", "versiontimestamp", "createdby", "createdon",
        "lastmodifiedby", "lastmodified"));
    return PostgreConstants.STANDARD_KLASS_TABLE_COLUMNS;

  }

  @Override
  public String getCreateQuery()
  {
    String createArticleKey = ArticleInstanceQueryConstants.ARTICLE_CREATE_QUERY_KEY;
    String articleCreateQuery = getQuery(createArticleKey);
    return articleCreateQuery;
  }

  @Override
  public void createInsertStatement() throws SQLException
  {
    String articleCreateQuery = getCreateQuery();
    createUpdateStatementFor(articleCreateQuery);
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
