package com.cs.config.strategy.plugin.migration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.entity.migration.IMigration;
import com.cs.core.runtime.interactor.model.configuration.IMigrationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SaveMigration extends AbstractOrientPlugin {
  
  public SaveMigration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveMigration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> migrations = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.MIGRATION);
    for (Map<String, Object> migration : migrations) {
      long date = System.currentTimeMillis();
      migration.put(IMigrationModel.APPLIED_ON, date);
      Map<String, Object> migrationEntity = (Map<String, Object>) migration
          .get(IMigrationModel.ENTITY);
      migrationEntity.put(IMigration.APPLIED_ON, date);
      UtilClass.createNode(migrationEntity, vertexType, new ArrayList<>());
    }
    UtilClass.getGraph()
        .commit();
    return new HashMap<>();
  }
}
