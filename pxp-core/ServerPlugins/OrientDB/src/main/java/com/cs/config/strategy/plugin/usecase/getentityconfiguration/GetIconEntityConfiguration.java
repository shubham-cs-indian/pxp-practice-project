package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.icon.IconNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IUsageSummary;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.EntityConfigurationConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

/**
 * This is  icon entityUsage  Plugin.
 * @author jamil.ahmad
 *
 */
public class GetIconEntityConfiguration extends AbstractOrientPlugin {
  
  public GetIconEntityConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetIconEntityConfiguration/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> ids = (List<String>) requestMap.get(IGetEntityConfigurationRequestModel.IDS);
    Map<String, Object> responseMap = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Map<String, Object>> usageSummary = new ArrayList<Map<String, Object>>();
    for (String id : ids) {
      Map<String, Object> details = getIconEntityUsageSummary(id);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData, usageSummary);
    }
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
  }
  
  /**
   * Description: get usage summary of icon type entity
   *
   * @param id
   * @return
   * @throws Exception
   */
  protected Map<String, Object> getIconEntityUsageSummary(String id) throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Vertex iconNode = null;
    try {
      iconNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ENTITY_TYPE_ICON);
    }
    catch (NotFoundException e) {
      throw new IconNotFoundException(e);
    }
    Map<String, Object> usageSummary = UtilClass
        .getMapFromVertex(Arrays.asList(IUsageSummary.CODE, IUsageSummary.LABEL, CommonConstants.CODE_PROPERTY), iconNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    List<String> entityTypeList = Arrays.asList(EntityConfigurationConstants.TAG, EntityConfigurationConstants.KLASS,
        EntityConfigurationConstants.STANDARD_ATTRIBUTE, EntityConfigurationConstants.TAB, EntityConfigurationConstants.ATTRIBUTION_TAXONOMY,
        EntityConfigurationConstants.CONTEXT, EntityConfigurationConstants.ORGANISATION, EntityConfigurationConstants.PROPERTY_COLLECTION,
        EntityConfigurationConstants.RELATIONSHIP, EntityConfigurationConstants.ATTRIBUTE,
        EntityConfigurationConstants.EMBEDDED, EntityConfigurationConstants.LANGUAGE,
        EntityConfigurationConstants.DATA_GOVERNANCE_TASK, EntityConfigurationConstants.TECHINICAL_IMAGE, EntityConfigurationConstants.TASK,
        EntityConfigurationConstants.ASSET, EntityConfigurationConstants.TARGET, EntityConfigurationConstants.SUPPLIER, 
        EntityConfigurationConstants.KLASS_TEXT_ASSET, EntityConfigurationConstants.ENDPOINT);
    
    for (String entityType : entityTypeList) {
      GetEntityConfigurationUtils.setByGraphAPIForIcon(iconNode, Direction.IN, RelationshipLabelConstants.HAS_ICON, referenceData, usedBy,
          usageSummary, entityType);
    }
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}
