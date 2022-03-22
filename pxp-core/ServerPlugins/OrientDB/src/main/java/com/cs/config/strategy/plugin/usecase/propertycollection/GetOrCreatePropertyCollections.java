package com.cs.config.strategy.plugin.usecase.propertycollection;

import com.cs.config.strategy.plugin.usecase.propertycollection.util.CreatePropertyCollectionUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetOrCreatePropertyCollections extends AbstractOrientPlugin {
  
  public GetOrCreatePropertyCollections(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreatePropertyCollections/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<Map<String, Object>> propertyCollectionsList = (List<Map<String, Object>>) requestMap
        .get("propertyCollections");
    Map<String, Object> responseMap = new HashMap<>();
    List<Map<String, Object>> createdPropertyCollections = new ArrayList<>();
    responseMap.put(IListModel.LIST, createdPropertyCollections);
    for (Map<String, Object> propertyCollection : propertyCollectionsList) {
      String id = (String) propertyCollection.get(IPropertyCollection.ID);
      Vertex existingPropertyCollection = UtilClass.getVertexByIdWithoutException(id,
          VertexLabelConstants.PROPERTY_COLLECTION);
      if (existingPropertyCollection == null) {
        Vertex createdPropertyCollection = CreatePropertyCollectionUtil
            .createPropertyCollection(propertyCollection);
        Map<String, Object> propertyCollectionMap = UtilClass
            .getMapFromNode(createdPropertyCollection);
        createdPropertyCollections.add(propertyCollectionMap);
      }
    }
    
    return responseMap;
  }
}
