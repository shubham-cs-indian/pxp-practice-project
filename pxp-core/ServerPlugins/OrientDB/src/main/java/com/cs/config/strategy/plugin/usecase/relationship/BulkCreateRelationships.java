package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.relationship.IRelationshipModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkCreateRelationships extends AbstractOrientPlugin {
  
  public BulkCreateRelationships(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<HashMap<String, Object>> relationshipList = new ArrayList<>();
    relationshipList = (List<HashMap<String, Object>>) requestMap.get("list");
    List<Map<String, Object>> createdRelationshipList = new ArrayList<>();
    List<Map<String, Object>> failedRelationshipList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    for (HashMap<String, Object> relationship : relationshipList) {
      
      String relationshipId = (String) relationship.get(CommonConstants.ID_PROPERTY);
      try {
        UtilClass.getVertexByIndexedId(relationshipId,
            VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
      }
      catch (NotFoundException e) {
        Map<String, Object> relationshipMap = RelationshipUtils
            .bulkCreateRelationships(relationship);
        Map<String, Object> returnRelationshipMap = new HashMap<>();
        returnRelationshipMap.put(ISummaryInformationModel.ID,
            relationshipMap.get(IRelationshipModel.ID));
        returnRelationshipMap.put(ISummaryInformationModel.LABEL,
            relationshipMap.get(IRelationshipModel.LABEL));
        createdRelationshipList.add(returnRelationshipMap);
      }
    }
    
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, createdRelationshipList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedRelationshipList);
    return result;
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedRelationshipList,
      Map<String, Object> relationship)
  {
    Map<String, Object> failedRelationshipMap = new HashMap<>();
    failedRelationshipMap.put(ISummaryInformationModel.ID, relationship.get(IRelationshipModel.ID));
    failedRelationshipMap.put(ISummaryInformationModel.LABEL,
        relationship.get(IRelationshipModel.LABEL));
    failedRelationshipList.add(failedRelationshipMap);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkCreateRelationships/*" };
  }
}
