package com.cs.config.strategy.plugin.usecase.variantcontext;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.strategy.model.context.IConfigDetailsForLinkedVariantDuplicateCheckRequestModel;
import com.cs.core.config.strategy.model.context.IConfigDetailsForLinkedVariantDuplicateCheckResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetConfigtDetailsForDuplicateLinkedVariant extends AbstractOrientPlugin {
  
  public GetConfigtDetailsForDuplicateLinkedVariant(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigtDetailsForDuplicateLinkedVariant/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String contextId = (String) requestMap.get(IConfigDetailsForLinkedVariantDuplicateCheckRequestModel.CONTEXT_ID);
    String relationshipId = (String) requestMap.get(IConfigDetailsForLinkedVariantDuplicateCheckRequestModel.RELATIONSHIP_ID);
    
    Vertex contextNode = UtilClass.getVertexByIndexedId(contextId, VertexLabelConstants.VARIANT_CONTEXT);
    Boolean isAllowedDuplicate = contextNode.getProperty(IVariantContext.IS_DUPLICATE_VARIANT_ALLOWED);
    
    Vertex natureRelNode = UtilClass.getVertexByIndexedId(relationshipId, VertexLabelConstants.NATURE_RELATIONSHIP);
    Long propertyIId = natureRelNode.getProperty(IRelationship.PROPERTY_IID);
    
    returnMap.put(IConfigDetailsForLinkedVariantDuplicateCheckResponseModel.IS_DUPLICATION_ALLOWED, isAllowedDuplicate);
    returnMap.put(IConfigDetailsForLinkedVariantDuplicateCheckResponseModel.RELATIONSHIP_IID, propertyIId);
    return returnMap;
  }

}
