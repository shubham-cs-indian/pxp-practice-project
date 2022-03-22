package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IMigrationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractOrientMigration extends AbstractOrientPlugin {
  
  public AbstractOrientMigration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected abstract Object executeMigration(Map<String, Object> requestMap) throws Exception;
  
  protected void saveMigration(Map<String, Object> requestMap) throws Exception
  {
    Long date = System.currentTimeMillis();
    requestMap.put(IMigrationModel.APPLIED_ON, date);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.MIGRATION);
    UtilClass.createNode(requestMap, vertexType, new ArrayList<>());
    UtilClass.getGraph()
        .commit();
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Object executeMigration = executeMigration(requestMap);
    saveMigration(requestMap);
    return executeMigration;
  }
}
