package com.cs.config.strategy.plugin.usecase.relationshipInheritance;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetEligibleRelationshipsForRelationshipInheritance extends AbstractOrientPlugin {
  
  public GetEligibleRelationshipsForRelationshipInheritance(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetEligibleRelationshipsForRelationshipInheritance/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> fieldsToFetchForRelationshipInheritance = Arrays.asList(
        IConfigEntityInformationModel.ID, IConfigEntityInformationModel.LABEL,
        IConfigEntityInformationModel.ICON, IConfigEntityInformationModel.CODE);
    
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    
    Iterable<Vertex> relationshipNodes = RelationshipRepository
        .getRelationshipNodesForRelationshipInheritance(requestMap);
    Long totalCount = RelationshipRepository
        .getRelationshipNodesCountForRelationshipInheritance(requestMap);
    
    List<Map<String, Object>> list = new ArrayList<>();
    for (Vertex relationshipNode : relationshipNodes) {
      Map<String, Object> configEntityInformation = UtilClass
          .getMapFromVertex(fieldsToFetchForRelationshipInheritance, relationshipNode);
      list.add(configEntityInformation);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetConfigDataEntityResponseModel.FROM, from);
    returnMap.put(IGetConfigDataEntityResponseModel.SIZE, size);
    returnMap.put(IGetConfigDataEntityResponseModel.LIST, list);
    returnMap.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT, totalCount);
    return returnMap;
  }
}
