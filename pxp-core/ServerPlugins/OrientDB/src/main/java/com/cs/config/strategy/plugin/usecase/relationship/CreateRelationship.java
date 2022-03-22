package com.cs.config.strategy.plugin.usecase.relationship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.relationship.util.GetRelationshipUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class CreateRelationship extends AbstractOrientPlugin {
  
  List<String> fieldsToExclude = Arrays.asList(IRelationshipModel.SECTIONS,
      IRelationshipModel.ADDED_ATTRIBUTES, IRelationshipModel.ADDED_TAGS,
      ICreateRelationshipModel.TAB);
  
  public CreateRelationship(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings({ "unchecked" })
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    HashMap<String, Object> relationshipMap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<>();
    
    relationshipMap = (HashMap<String, Object>) requestMap.get("relationship");
    
    Vertex vertex = RelationshipUtils.createRelationship(relationshipMap,
        fieldsToExclude);
    RelationshipUtils.addSectionElement(UtilClass.getGraph(), relationshipMap, vertex);
    RelationshipUtils.manageAddedAttributes(vertex, relationshipMap, new HashMap<>());
    RelationshipUtils.manageAddedTags(vertex, relationshipMap, new HashMap<>());
    
    Map<String, Object> tabMap = (Map<String, Object>) relationshipMap
        .get(ICreateRelationshipModel.TAB);
    TabUtils.linkAddedOrDefaultTab(vertex, tabMap, CommonConstants.RELATIONSHIP);
    UtilClass.getGraph()
        .commit();
    
    AuditLogUtils.fillAuditLoginfo(returnMap, vertex, Entities.RELATIONSHIPS,
        Elements.RELATIONSHIPS);
    
    Map<String, Object> entityMap = RelationshipUtils.getRelationshipEntityMap(vertex);
    returnMap.put(IGetRelationshipModel.ENTITY, entityMap);
    
    GetRelationshipUtils.fillConfigDetails(vertex, returnMap);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateRelationship/*" };
  }
}
