
package com.cs.config.strategy.plugin.usecase.mapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.model.mapping.IMappingHelperModel;
import com.cs.config.strategy.plugin.model.mapping.MappingHelperModel;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings({ "unchecked" })
public class CreateOutboundMapping extends AbstractOrientPlugin {
  
  List<String> fieldsToExclude = Arrays.asList(IMappingModel.ATTRIBUTE_MAPPINGS,
      IMappingModel.TAG_MAPPINGS, IMappingModel.CLASS_MAPPINGS, IMappingModel.TAXONOMY_MAPPINGS);
  
  public CreateOutboundMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mappingMap = new HashMap<String, Object>();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    mappingMap = (Map<String, Object>) requestMap.get(CommonConstants.MAPPING);
    UtilClass.checkDuplicateName((String) mappingMap.get(CommonConstants.LABEL_PROPERTY), VertexLabelConstants.PROPERTY_MAPPING);  
    OrientGraph graph = UtilClass.getGraph();
    OrientVertexType vertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.PROPERTY_MAPPING, CommonConstants.CID_PROPERTY);
    Vertex profileNode = UtilClass.createNode(mappingMap, vertexType, fieldsToExclude);
    IMappingHelperModel mappingHelperModel = new MappingHelperModel();
    returnMap.put(IMappingModel.CONFIG_DETAILS, mappingHelperModel.getConfigDetails());
    returnMap.putAll(UtilClass.getMapFromNode(profileNode));
    graph.commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateOutboundMapping/*" };
  }
}
