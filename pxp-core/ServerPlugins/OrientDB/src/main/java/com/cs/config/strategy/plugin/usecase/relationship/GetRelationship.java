package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.relationship.util.GetRelationshipUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class GetRelationship extends AbstractOrientPlugin {
  
  public GetRelationship(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex relationshipNode = null;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    try {
      relationshipNode = UtilClass.getVertexById((String) requestMap.get(IIdParameterModel.ID),
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    }
    catch (NotFoundException e) {
      throw new RelationshipNotFoundException();
    }
    
    Map<String, Object> relationshipEntityMap = RelationshipUtils
        .getRelationshipEntityMap(relationshipNode);
    returnMap.put(IGetRelationshipModel.ENTITY, relationshipEntityMap);
    
    GetRelationshipUtils.fillConfigDetails(relationshipNode, returnMap);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRelationship/*" };
  }
}
