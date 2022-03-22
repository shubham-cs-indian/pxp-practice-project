package com.cs.config.strategy.plugin.usecase.component;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRuleIdsFromComponent extends AbstractOrientPlugin {
  
  public GetRuleIdsFromComponent(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> responseList = new ArrayList<>();
    String componentId = (String) requestMap.get(IIdParameterModel.ID);
    // TODO - Fix Me
    if (componentId != null && !componentId.equals("")) {
      Vertex componentVertex = UtilClass.getVertexById(componentId, VertexLabelConstants.COMPONENT);
      if (componentVertex != null) {
        Iterable<Vertex> dataRules = componentVertex.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_DATARULE);
        for (Vertex dataRule : dataRules) {
          responseList.add(dataRule.getProperty(CommonConstants.CODE_PROPERTY));
        }
      }
    }
    Map<String, Object> listModel = new HashMap<>();
    listModel.put(IIdsListParameterModel.IDS, responseList);
    
    return listModel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRuleIdsFromComponent/*" };
  }
}
