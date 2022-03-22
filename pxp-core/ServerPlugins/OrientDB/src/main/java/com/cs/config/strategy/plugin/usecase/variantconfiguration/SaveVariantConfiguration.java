package com.cs.config.strategy.plugin.usecase.variantconfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.variantconfiguration.IVariantConfiguration;
import com.cs.core.config.interactor.exception.variantconfiguration.VariantConfigurationNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class SaveVariantConfiguration extends AbstractOrientPlugin {
  
  public SaveVariantConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveVariantConfiguration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex variantConfigurationVertex = null;
    try {
    	variantConfigurationVertex = UtilClass.getVertexById((String) requestMap.get(IVariantConfiguration.ID),
    			VertexLabelConstants.VARIANT_CONFIGURATION);
    }
    catch (NotFoundException e) {
    	throw new VariantConfigurationNotFoundException(e);
    }
    
    UtilClass.saveNode(requestMap, variantConfigurationVertex, Arrays.asList(CommonConstants.CODE_PROPERTY));
    UtilClass.getGraph().commit();
    Map<String, Object> variantConfiguration = UtilClass.getMapFromVertex(new ArrayList<>(), variantConfigurationVertex);
    
    return variantConfiguration;
  }
  
}
