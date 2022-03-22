package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ExportTaskList extends AbstractOrientPlugin {
  
  public ExportTaskList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> responseMap = new HashMap<>();
    List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    
    List<String> taskCodes = (List<String>) requestMap.get("itemCodes");
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(taskCodes, ITask.CODE);
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_TASK + condition + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query)).execute();
    
    for (Vertex taskNode : resultIterable) {
      list.add(TasksUtil.getTaskMapFromNode(taskNode));
    }
    
    responseMap.put("list", list);
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportTaskList/*" };
  }
}
