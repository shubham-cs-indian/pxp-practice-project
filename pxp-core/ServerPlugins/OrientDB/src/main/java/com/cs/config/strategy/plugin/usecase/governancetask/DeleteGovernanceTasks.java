package com.cs.config.strategy.plugin.usecase.governancetask;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteGovernanceTasks extends AbstractOrientPlugin {
  
  public DeleteGovernanceTasks(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    IExceptionModel failure = new ExceptionModel();
    List<String> taskIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    List<String> deletedIds = new ArrayList<>();
    
    for (String id : taskIds) {
      Vertex taskNode = null;
      try {
        taskNode = UtilClass.getVertexById(id, VertexLabelConstants.GOVERNANCE_RULE_TASK);
        taskNode.remove();
        deletedIds.add(id);
      }
      catch (NotFoundException e) {
        deletedIds.add(id);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkDeleteReturnModel.SUCCESS, deletedIds);
    responseMap.put(IBulkDeleteReturnModel.FAILURE, failure);
    UtilClass.getGraph()
        .commit();
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteGovernanceTasks/*" };
  }
}
