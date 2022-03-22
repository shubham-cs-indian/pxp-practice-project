package com.cs.config.strategy.plugin.usecase.entity.hidden;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.hidden.IPropertyModificationInputModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SaveEntityProperty extends AbstractOrientPlugin {
  
  public SaveEntityProperty(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, String> returnMap = new HashMap<>();
    Map<String, Object> data = (Map<String, Object>) requestMap.get(CommonConstants.DATA_PROPERTY);
    String id = (String) data.get(IPropertyModificationInputModel.ID);
    String entityType = (String) data.get(IPropertyModificationInputModel.ENTITY_TYPE);
    Map<String, String> properties = (Map<String, String>) data
        .get(IPropertyModificationInputModel.PROPERTIES);
    
    Vertex entityNode = UtilClass.getVertexById(id, entityType);
    savePropertiesToNode(properties, entityNode);
    returnMap.put(IIdParameterModel.ID, id);
    return returnMap;
  }
  
  private void savePropertiesToNode(Map<String, String> properties, Vertex entityNode) throws Exception
  {
    Set<String> propertiesToUpdate = properties.keySet();
    Set<String> nodeAvailableProperties = entityNode.getPropertyKeys();
    Map<String, Object> mapFromNode = UtilClass.getMapFromNode(entityNode);
    for (String property : propertiesToUpdate) {
      /*if (nodeAvailableProperties.contains(property) && !nodeAvailableProperties.equals(CommonConstants.ID_PROPERTY)) {
        mapFromNode.put(property, properties.get(property));
      }*/
      /**
       * @Autour: Abhaypratap Singh For Now only the icon property is going to
       *          modify
       */
      if (property.equals(CommonConstants.ICON_PROPERTY)
          && nodeAvailableProperties.contains(property)) {
        mapFromNode.put(property, properties.get(property));
      }
    }
    UtilClass.saveNode(mapFromNode, entityNode, new ArrayList<>());
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveEntityProperty/*" };
  }
}
