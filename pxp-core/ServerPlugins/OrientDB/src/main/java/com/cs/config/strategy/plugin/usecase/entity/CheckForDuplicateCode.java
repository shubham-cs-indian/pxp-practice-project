package com.cs.config.strategy.plugin.usecase.entity;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeModel;
import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeReturnModel;
import com.cs.core.config.interactor.exception.duplicatecode.DuplicateCodeException;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckForDuplicateCode extends AbstractOrientPlugin {
  
  public CheckForDuplicateCode(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Boolean> returnMap = new HashMap<>();
    String codeValue = (String) requestMap.get(ICheckForDuplicateCodeModel.ID);
    String entityType = (String) requestMap.get(ICheckForDuplicateCodeModel.ENTITYTYPE);
    
    if (codeValue != null && entityType != null) {
      OrientGraph graph = UtilClass.getGraph();
      List<String> entityToCheckForAttributionTaxonomy = new ArrayList<>();
      if (entityType.equals(CommonConstants.ATTRIBUTION_TAXONOMY)
          || entityType.equals(CommonConstants.TAXONOMY_MASTER_LIST)) {
        entityToCheckForAttributionTaxonomy = Arrays.asList(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, VertexLabelConstants.ENTITY_TAG);
      }
      else {
        entityToCheckForAttributionTaxonomy.add(EntityUtil.getVertexLabelByEntityType(entityType));
      }
      
      for (String entityName : entityToCheckForAttributionTaxonomy) {
        Iterable<Vertex> result = graph
            .command(new OCommandSQL("select from " + entityName + " where "
                + CommonConstants.CODE_PROPERTY + " = \'" + codeValue + "\'"))
            .execute();
        if (result.iterator()
            .hasNext())
          throw new DuplicateCodeException();
      }
      
    }
    else {
      throw new PluginException("Null Value Passed");
    }
    
    returnMap.put(ICheckForDuplicateCodeReturnModel.FOUND, true);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CheckForDuplicateCode/*" };
  }
}
