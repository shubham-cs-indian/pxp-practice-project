 package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForRuntimeCleanupOnKlassOrTaxonomySave extends AbstractOrientPlugin {

  public GetConfigDetailsForRuntimeCleanupOnKlassOrTaxonomySave(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForRuntimeCleanupOnKlassOrTaxonomySave/*" };
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<String> klassCodes = (List<String>) requestMap.get("klassCodes");
    List<String> taxonomyCodes = (List<String>) requestMap.get("taxonomyCodes");
    handleContextClassifiersToBeRemoved(requestMap, returnMap, klassCodes, taxonomyCodes);
    handleAttributeVariantsToBeRemoved(requestMap, returnMap, klassCodes, taxonomyCodes);
    return returnMap;
  }

  private void handleContextClassifiersToBeRemoved(Map<String, Object> requestMap,
      Map<String, Object> returnMap, List<String> klassCodes, List<String> taxonomyCodes)
      throws Exception
  {
    List<Integer> classifierIIDs = (List<Integer>) requestMap.get("removedContextClassifierIIDs");
    if(classifierIIDs == null || classifierIIDs.isEmpty()) {
      return;
    }
    Set<Long> applicableContextClassifierIIDs = new HashSet<>(); 
    List<Long> classifierIIDsToRemove = new ArrayList<>();
    //TODO: check bellow code - arshad
    for (Integer classifierIID : classifierIIDs) {
      classifierIIDsToRemove.add(classifierIID.longValue());
    }
    fillApplicableDetails(applicableContextClassifierIIDs, klassCodes, taxonomyCodes);
    classifierIIDsToRemove.removeAll(applicableContextClassifierIIDs);
    returnMap.put("classifierIIDsToRemove", classifierIIDsToRemove);
  }
  
  private void handleAttributeVariantsToBeRemoved(Map<String, Object> requestMap,
      Map<String, Object> returnMap, List<String> klassCodes, List<String> taxonomyCodes)
  {
    Map<String, List<String>> removedAttributeIdVsContextIds = (Map<String, List<String>>) requestMap.get("removedAttributeIdVsContextIds");
    if(removedAttributeIdVsContextIds == null || removedAttributeIdVsContextIds.isEmpty()) {
      return;
    }
    Map<String, List<String>> applicablePropertyIIDVsContextIds = new HashMap<>(); 
    fillAttributeContextIds(klassCodes, taxonomyCodes, applicablePropertyIIDVsContextIds);
    if(applicablePropertyIIDVsContextIds.isEmpty()) {
      returnMap.put("removedAttributeIdVsContextIds", removedAttributeIdVsContextIds);
    }else {
      returnMap.put("removedAttributeIdVsContextIds",
          removedAttributeIdVsContextIds.entrySet().stream()
          .filter(entry -> removeApplicableContextIds(entry.getValue(),applicablePropertyIIDVsContextIds.get(entry.getKey())))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
  }

  private boolean removeApplicableContextIds(List<String> removedContextIds, List<String> applicableContextIds)
  {
    removedContextIds = removedContextIds.stream().filter(removedContextId -> !applicableContextIds.contains(removedContextId)).collect(Collectors.toList());
    return (removedContextIds.size()> 0) ? true : false;
  }

  public static void fillApplicableDetails(
      Set<Long> apllicableContextClassifierIIDs, List<String> klassCodes,
      List<String> taxonomyCodes) throws Exception
  {
    
    for (String klassCode : klassCodes) {
      Vertex klassVertex = UtilClass.getVertexByCode(klassCode, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      fillApplicableContextClassifierIIDs(klassVertex, apllicableContextClassifierIIDs);
    }
    
    for (String taxonomyCode : taxonomyCodes) {
      Vertex taxonomyVertex = UtilClass.getVertexByCode(taxonomyCode, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      fillApplicableContextClassifierIIDs(taxonomyVertex, apllicableContextClassifierIIDs);
    }
  }
  
  private static void fillApplicableContextClassifierIIDs(Vertex klassVertex, Set<Long> applicableContextClassifierIIDs)
  {
    Iterable<Vertex> contextKlassIterable = klassVertex.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Vertex contextKlassNode : contextKlassIterable) {
      long classifierIID = contextKlassNode.getProperty(ICategoryInformationModel.CLASSIFIER_IID);
      applicableContextClassifierIIDs.add(classifierIID);
    }
  }
  
  private static void fillAttributeContextIds(List<String> klassIds,List<String> taxonomyIds,
      Map<String, List<String>> attributeIdVsContextIds)
  {
    String klassQuery = "select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where code in " + EntityUtil.quoteIt(klassIds);
    String taxonomyQuery = "select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY + " where code in "  +  EntityUtil.quoteIt(taxonomyIds);
    
    StringBuilder query = new StringBuilder("select expand(out('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')["
        + ISectionElement.TYPE + "='" + CommonConstants.ATTRIBUTE + "']) from (");
    
    if(!klassIds.isEmpty() && !taxonomyIds.isEmpty()) {
      query.append("Select expand($c) let $a = (" + klassQuery + "), $b = (" + taxonomyQuery + "), $c = unionALL($a, $b)");
    }else if(!klassIds.isEmpty()) {
      query.append(klassQuery);
    }else {
      query.append(taxonomyQuery);
    }
    query.append(")");

    Iterable<Vertex> verticesFromQuery = UtilClass.getVerticesFromQuery(query.toString());
    for (Vertex klassAttributeVertex : verticesFromQuery) {
      Iterable<Vertex> variantContextOfVertices = klassAttributeVertex.getVertices(Direction.IN,
          RelationshipLabelConstants.VARIANT_CONTEXT_OF);
      List<String> variantContextIds = new ArrayList<String>();
      for (Vertex attributeVariantNode : variantContextOfVertices) {
        variantContextIds.add((String) attributeVariantNode.getProperty(CommonConstants.CODE_PROPERTY));
      }
      
      if (!variantContextIds.isEmpty()) {
        String attributeId = klassAttributeVertex.getProperty(CommonConstants.PROPERTY_ID);
        List<String> attributeVariantIds = attributeIdVsContextIds.get(attributeId);
        if (attributeVariantIds != null) {
          attributeVariantIds.addAll(variantContextIds);
        }
        else {
          attributeIdVsContextIds.put(attributeId, variantContextIds);
        }
      }
    }
    
  }
}
