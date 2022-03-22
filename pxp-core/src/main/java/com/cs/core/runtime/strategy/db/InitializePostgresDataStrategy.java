/*
package com.cs.core.runtime.strategy.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.processdetails.IProcessKlassInstanceStatusModel;
import com.cs.core.config.interactor.model.processdetails.IProcessRelationshipDetailsModel;
import com.cs.core.config.interactor.model.processdetails.IProcessSourceDestinationDetailsModel;
import com.cs.core.config.interactor.model.processdetails.IProcessStatusDetailsModel;
import com.cs.core.config.interactor.model.processdetails.IProcessVariantStatusModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.IInitializeDataStrategy;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.utils.DbUtils;

@Component("initializePostgresDataStrategy")
public class InitializePostgresDataStrategy extends BasePostgresStrategy
    implements IInitializeDataStrategy {

  @Autowired
  protected DbUtils dbUtils;

  @Override
  public IModel execute(IModel model) throws Exception
  {
    createTableSchema();
    return null;
  }

  private void createTableSchema() throws Exception
  {
    if (!dbUtils.getTableExist(TransferConstants.PROCESS_STATUS_TABLE)) {
      dbUtils.createTableQuery(TransferConstants.PROCESS_STATUS_TABLE,
          getProcessStatusTableColumns());
    }

    if (!dbUtils.getTableExist(TransferConstants.KLASS_INSTANCE_STATUS_TABLE)) {
      dbUtils.createTableQuery(TransferConstants.KLASS_INSTANCE_STATUS_TABLE,
          getKlassInstanceTableColumns());
    }

    if (!dbUtils.getTableExist(TransferConstants.VARIANT_STATUS_TABLE)) {
      dbUtils.createTableQuery(TransferConstants.VARIANT_STATUS_TABLE,
          getVariantTableColumns());
    }

    if (!dbUtils.getTableExist(TransferConstants.ATTRIBUTE_VARIANT_STATUS_TABLE)) {
      dbUtils.createTableQuery(TransferConstants.ATTRIBUTE_VARIANT_STATUS_TABLE,
          getVariantTableColumns());
    }

    if (!dbUtils.getTableExist(TransferConstants.RELATIONSHIP_STATUS_TABLE)) {
      dbUtils.createTableQuery(TransferConstants.RELATIONSHIP_STATUS_TABLE,
          getRelationshipDetailTableColumn());
    }

    if (!dbUtils.getTableExist(TransferConstants.NATURE_RELATIONSHIP_STATUS_TABLE)) {
      dbUtils.createTableQuery(TransferConstants.NATURE_RELATIONSHIP_STATUS_TABLE,
          getRelationshipDetailTableColumn());
    }

    if (!dbUtils.getTableExist(TransferConstants.SOURCE_DESTINATION_TABLE)) {
      dbUtils.createTableQuery(TransferConstants.SOURCE_DESTINATION_TABLE,
          getSourceDestinationDetailsTableColumn());
    }

    createTrigger();
  }

  private void createTrigger() throws Exception
  {
    if (!dbUtils.getTrigerExist(TransferConstants.KLASS_INSTANCE_TRIGGER_NAME,
        TransferConstants.KLASS_INSTANCE_STATUS_TABLE)) {
      dbUtils.functionCreation(TransferConstants.KLASS_INSTANCE_TRIGGER_FUNCTION,
          getIfConditionForTriggerFunction(), getElseConditionForTriggerFunction(),
          " New." + dbUtils.quoteIt(IProcessStatusDetailsModel.STATUS) + " = true ");

      dbUtils.createTriggerQuery(TransferConstants.KLASS_INSTANCE_TRIGGER_FUNCTION,
          TransferConstants.KLASS_INSTANCE_TRIGGER_NAME, " AFTER UPDATE ",
          TransferConstants.KLASS_INSTANCE_STATUS_TABLE);
    }

    if (!dbUtils.getTrigerExist(TransferConstants.VARIANT_INSTANCE_TRIGGER_NAME,
        TransferConstants.VARIANT_STATUS_TABLE)) {
      dbUtils.functionCreation(TransferConstants.VARIANT_INSTANCE_TRIGGER_FUNCTION,
          getIfConditionForTriggerFunction(), getElseConditionForTriggerFunction(),
          " New." + dbUtils.quoteIt(IProcessStatusDetailsModel.STATUS) + " = true ");

      dbUtils.createTriggerQuery(TransferConstants.VARIANT_INSTANCE_TRIGGER_FUNCTION,
          TransferConstants.VARIANT_INSTANCE_TRIGGER_NAME, " AFTER UPDATE ",
          TransferConstants.VARIANT_STATUS_TABLE);
    }

    if (!dbUtils.getTrigerExist(TransferConstants.ATTRIBUTE_VARIANT_INSTANCE_TRIGGER_NAME,
        TransferConstants.ATTRIBUTE_VARIANT_STATUS_TABLE)) {
      dbUtils.functionCreation(TransferConstants.ATTRIBUTE_VARIANT_INSTANCE_TRIGGER_FUNCTION,
          getIfConditionForTriggerFunction(), getElseConditionForTriggerFunction(),
          " New." + dbUtils.quoteIt(IProcessStatusDetailsModel.STATUS) + " = true ");

      dbUtils.createTriggerQuery(TransferConstants.ATTRIBUTE_VARIANT_INSTANCE_TRIGGER_FUNCTION,
          TransferConstants.ATTRIBUTE_VARIANT_INSTANCE_TRIGGER_NAME, " AFTER UPDATE ",
          TransferConstants.ATTRIBUTE_VARIANT_STATUS_TABLE);
    }

    if (!dbUtils.getTrigerExist(TransferConstants.RELATIONSHIP_INSTANCE_TRIGGER_NAME,
        TransferConstants.RELATIONSHIP_STATUS_TABLE)) {
      dbUtils.functionCreation(TransferConstants.RELATIONSHIP_INSTANCE_TRIGGER_FUNCTION,
          getIfConditionForTriggerFunction(), getElseConditionForTriggerFunction(),
          " New." + dbUtils.quoteIt(IProcessStatusDetailsModel.STATUS) + " = true ");

      dbUtils.createTriggerQuery(TransferConstants.RELATIONSHIP_INSTANCE_TRIGGER_FUNCTION,
          TransferConstants.RELATIONSHIP_INSTANCE_TRIGGER_NAME, " AFTER UPDATE ",
          TransferConstants.RELATIONSHIP_STATUS_TABLE);
    }

    if (!dbUtils.getTrigerExist(TransferConstants.NATURE_RELATIONSHIP_INSTANCE_TRIGGER_NAME,
        TransferConstants.NATURE_RELATIONSHIP_STATUS_TABLE)) {
      dbUtils.functionCreation(TransferConstants.NATURE_RELATIONSHIP_INSTANCE_TRIGGER_FUNCTION,
          getIfConditionForTriggerFunction(), getElseConditionForTriggerFunction(),
          " New." + dbUtils.quoteIt(IProcessStatusDetailsModel.STATUS) + " = true ");

      dbUtils.createTriggerQuery(TransferConstants.NATURE_RELATIONSHIP_INSTANCE_TRIGGER_FUNCTION,
          TransferConstants.NATURE_RELATIONSHIP_INSTANCE_TRIGGER_NAME, " AFTER UPDATE ",
          TransferConstants.NATURE_RELATIONSHIP_STATUS_TABLE);
    }
  }

  private String getIfConditionForTriggerFunction() throws Exception
  {
    StringBuffer ifQuery = new StringBuffer(
        "UPDATE " + TransferConstants.PROCESS_STATUS_TABLE + " SET ");
    ifQuery.append(dbUtils.quoteIt(IProcessStatusDetailsModel.INPROGRESS_COUNT) + "="
        + dbUtils.quoteIt(IProcessStatusDetailsModel.INPROGRESS_COUNT) + " - 1 ,");
    ifQuery.append(dbUtils.quoteIt(IProcessStatusDetailsModel.SUCCESS_COUNT) + "="
        + dbUtils.quoteIt(IProcessStatusDetailsModel.SUCCESS_COUNT) + " + 1 ");
    ifQuery.append(" where  " + dbUtils.quoteIt(IProcessStatusDetailsModel.PROCESS_INSTANCE_ID) + " = New."
        + dbUtils.quoteIt(IProcessStatusDetailsModel.PROCESS_INSTANCE_ID));
    ifQuery.append(" AND " + dbUtils.quoteIt(IProcessStatusDetailsModel.COMPONENT_ID) + " = New."
        + dbUtils.quoteIt(IProcessStatusDetailsModel.COMPONENT_ID) + " ;");

    return ifQuery.toString();
  }

  private String getElseConditionForTriggerFunction() throws Exception
  {
    StringBuffer ifQuery = new StringBuffer(
        "UPDATE " + TransferConstants.PROCESS_STATUS_TABLE + " SET ");
    ifQuery.append(dbUtils.quoteIt(IProcessStatusDetailsModel.INPROGRESS_COUNT) + "="
        + dbUtils.quoteIt(IProcessStatusDetailsModel.INPROGRESS_COUNT) + " - 1 ,");
    ifQuery.append(dbUtils.quoteIt(IProcessStatusDetailsModel.FAILED_COUNT) + "="
        + dbUtils.quoteIt(IProcessStatusDetailsModel.FAILED_COUNT) + " + 1 ");
    ifQuery.append(" where  " + dbUtils.quoteIt(IProcessStatusDetailsModel.PROCESS_INSTANCE_ID) + " = New."
        + dbUtils.quoteIt(IProcessStatusDetailsModel.PROCESS_INSTANCE_ID));
    ifQuery.append(" AND " + dbUtils.quoteIt(IProcessStatusDetailsModel.COMPONENT_ID) + " = New."
        + dbUtils.quoteIt(IProcessStatusDetailsModel.COMPONENT_ID) + " ;");

    return ifQuery.toString();
  }

  private List<Map<String, Object>> getProcessStatusTableColumns()
  {
    List<Map<String, Object>> tableColumns = new ArrayList<>();
    tableColumns.add(dbUtils.fillColumnValues(IProcessStatusDetailsModel.ID,
        TransferConstants.VARCHAR, 50, TransferConstants.PRIMARY_KEY));
    tableColumns.add(dbUtils.fillColumnValues(IProcessStatusDetailsModel.PROCESS_INSTANCE_ID,
        TransferConstants.VARCHAR, 50, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessStatusDetailsModel.COMPONENT_ID,
        TransferConstants.VARCHAR, 50, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessStatusDetailsModel.STATUS, TransferConstants.BOOLEAN,
        null, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessStatusDetailsModel.TOTAL_COUNT,
        TransferConstants.INTEGER, null, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessStatusDetailsModel.FAILED_COUNT,
        TransferConstants.INTEGER, null, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessStatusDetailsModel.SUCCESS_COUNT,
        TransferConstants.INTEGER, null, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessStatusDetailsModel.INPROGRESS_COUNT,
        TransferConstants.INTEGER, null, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessStatusDetailsModel.START_TIME,
        TransferConstants.VARCHAR, 50, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessStatusDetailsModel.END_TIME,
        TransferConstants.VARCHAR, 50, null));

    return tableColumns;
  }

  private List<Map<String, Object>> getRelationshipDetailTableColumn()
  {
    List<Map<String, Object>> tableColumns = new ArrayList<>();
    tableColumns.add(dbUtils.fillColumnValues(IProcessRelationshipDetailsModel.ID,
        TransferConstants.VARCHAR, 50, TransferConstants.PRIMARY_KEY));
    tableColumns.add(dbUtils.fillColumnValues(IProcessRelationshipDetailsModel.PROCESS_INSTANCE_ID,
        TransferConstants.VARCHAR, 50, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessRelationshipDetailsModel.COMPONENT_ID,
        TransferConstants.VARCHAR, 50, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessRelationshipDetailsModel.ENTITY_ID,
    		TransferConstants.VARCHAR, 250, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessRelationshipDetailsModel.STATUS,
        TransferConstants.BOOLEAN, null, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessRelationshipDetailsModel.FAILURE_MESSAGE,
        TransferConstants.VARCHAR, 2000, null));

    return tableColumns;
  }

  private List<Map<String, Object>> getSourceDestinationDetailsTableColumn()
  {
    List<Map<String, Object>> tableColumns = new ArrayList<>();
    tableColumns.add(dbUtils.fillColumnValues(IProcessSourceDestinationDetailsModel.ID,
        TransferConstants.VARCHAR, 50, TransferConstants.PRIMARY_KEY));
    tableColumns.add(dbUtils.fillColumnValues(IProcessSourceDestinationDetailsModel.PROCESS_INSTANCE_ID,
        TransferConstants.VARCHAR, 50, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessSourceDestinationDetailsModel.COMPONENT_ID,
        TransferConstants.VARCHAR, 50, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessSourceDestinationDetailsModel.SOURCE_ID,
        TransferConstants.VARCHAR, 250, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessSourceDestinationDetailsModel.DESTINATION_ID,
        TransferConstants.VARCHAR, 250, null));

    return tableColumns;
  }

  private List<Map<String, Object>> getKlassInstanceTableColumns()
  {
    List<Map<String, Object>> tableColumns = new ArrayList<>();
    tableColumns.add(dbUtils.fillColumnValues(IProcessKlassInstanceStatusModel.ID,
        TransferConstants.VARCHAR, 50, TransferConstants.PRIMARY_KEY));
    tableColumns.add(dbUtils.fillColumnValues(IProcessKlassInstanceStatusModel.PROCESS_INSTANCE_ID,
        TransferConstants.VARCHAR, 50, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessKlassInstanceStatusModel.COMPONENT_ID,
        TransferConstants.VARCHAR, 50, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessKlassInstanceStatusModel.ENTITY_ID,
        TransferConstants.VARCHAR, 250, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessKlassInstanceStatusModel.ENTITY_TYPE,
        TransferConstants.VARCHAR, 200, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessKlassInstanceStatusModel.STATUS,
        TransferConstants.BOOLEAN, null, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessKlassInstanceStatusModel.FAILURE_MESSAGE,
        TransferConstants.VARCHAR, 2000, null));

    return tableColumns;
  }

  private List<Map<String, Object>> getVariantTableColumns()
  {
    List<Map<String, Object>> tableColumns = new ArrayList<>();
    tableColumns.add(dbUtils.fillColumnValues(IProcessVariantStatusModel.ID,
        TransferConstants.VARCHAR, 50, TransferConstants.PRIMARY_KEY));
    tableColumns.add(dbUtils.fillColumnValues(IProcessVariantStatusModel.PROCESS_INSTANCE_ID,
        TransferConstants.VARCHAR, 50, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessVariantStatusModel.COMPONENT_ID,
        TransferConstants.VARCHAR, 50, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessVariantStatusModel.ENTITY_ID,
        TransferConstants.VARCHAR, 250, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessVariantStatusModel.ENTITY_TYPE,
        TransferConstants.VARCHAR, 50, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessVariantStatusModel.STATUS, TransferConstants.BOOLEAN,
        null, TransferConstants.NOT_NULL));
    tableColumns.add(dbUtils.fillColumnValues(IProcessVariantStatusModel.FAILURE_MESSAGE,
        TransferConstants.VARCHAR, 2000, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessVariantStatusModel.KLASS_INSTANCE_ID,
        TransferConstants.VARCHAR, 250, null));
    tableColumns.add(dbUtils.fillColumnValues(IProcessVariantStatusModel.PARENT_ID,
        TransferConstants.VARCHAR, 250, null));

    return tableColumns;
  }

}
*/
