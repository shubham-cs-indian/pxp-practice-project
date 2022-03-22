package com.cs.config.strategy.plugin.usecase.variantcontext;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class GetVariantContext extends AbstractOrientPlugin {
  
  public GetVariantContext(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetVariantContext/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    String contextId = (String) requestMap.get(IIdParameterModel.ID);
    
    Vertex contextNode = null;
    try {
      contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
    }
    catch (NotFoundException e) {
      throw new ContextNotFoundException();
    }
    
    Map<String, Object> returnMap = VariantContextUtils.getContextMapToReturn(contextNode);
    
    return returnMap;
  }
}
