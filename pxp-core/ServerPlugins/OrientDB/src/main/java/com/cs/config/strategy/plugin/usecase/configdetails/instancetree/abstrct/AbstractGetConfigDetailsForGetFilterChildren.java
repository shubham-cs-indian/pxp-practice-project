package com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public abstract class AbstractGetConfigDetailsForGetFilterChildren extends AbstractConfigDetailsForNewInstanceTree {

  public AbstractGetConfigDetailsForGetFilterChildren(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @SuppressWarnings("unchecked")
  protected void execute(Map<String, Object> requestMap, Map<String, Object> mapToReturn) throws Exception
  {
    String userId = (String) requestMap.get(IConfigDetailsForGetFilterChildrenRequestModel.USER_ID);
    
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    
    managePermission(roleNode, requestMap, mapToReturn);
    
    List<String> allowedEntities = (List<String>) mapToReturn.get(IConfigDetailsForGetNewInstanceTreeModel.ALLOWED_ENTITIES);
    
    List<String> searchableAttributes = getSearchableAttributes(allowedEntities);
    List<String> translatableAttributes = getTranslatableAttributes(allowedEntities);
    Map<String, String> ruleViolationsLabels = new HashedMap<String, String>();
    
    mapToReturn.put(IConfigDetailsForGetFilterChildrenResponseModel.SEARCHABLE_ATTRIBUTE_IDS, searchableAttributes);
    mapToReturn.put(IConfigDetailsForGetFilterChildrenResponseModel.TRANSLATABLE_ATTRIBUTE_IDS, translatableAttributes);
    mapToReturn.put(IConfigDetailsForGetFilterChildrenResponseModel.RULE_VIOLATIONS_LABELS, ruleViolationsLabels);
    
    String id = (String) requestMap.get(IConfigDetailsForGetFilterChildrenRequestModel.ID);
    String filterType = (String) requestMap.get(IConfigDetailsForGetFilterChildrenRequestModel.FILTER_TYPE);
    if(filterType.equals(CommonConstants.TAG) || filterType.equals(CommonConstants.ATTRIBUTE)) {
      String propertyVertexLabel = (filterType.equals(CommonConstants.TAG))?VertexLabelConstants.ENTITY_TAG:VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
      Vertex propertyNode = UtilClass.getVertexByIndexedId(id, propertyVertexLabel);
      mapToReturn.put(IConfigDetailsForGetFilterChildrenResponseModel.REFERENCED_PROPERTY, getReferencedPropertyMap(propertyNode, requestMap,filterType));
    }
    else if (filterType.equals(CommonConstants.COLOR_VOILATION_FILTER)) {
      List<String> colorCodes = Arrays.asList(CommonConstants.RED_VOILATION_FILTER, CommonConstants.ORANGE_VOILATION_FILTER,
          CommonConstants.YELLOW_VOILATION_FILTER, CommonConstants.GREEN_VOILATION_FILTER);
      
      Iterable<Vertex> colorVertices = UtilClass.getVerticesByIds(colorCodes, VertexLabelConstants.UI_TRANSLATIONS);
      
      for (Vertex color : colorVertices) {
        ruleViolationsLabels.put(color.getProperty(CommonConstants.CODE_PROPERTY),
            (String) UtilClass.getValueByLanguage(color, CommonConstants.LABEL_PROPERTY));
      }
    }
  }
 
}
