package com.cs.config.strategy.plugin.usecase.template.util;

public class GetTemplateUtils {
  
  /**
   * Description Read template node and all its connected nodes and return the
   * template entity map to be sent to UI..
   *
   * @author Ajit
   * @param templateNode
   * @return
   * @throws Exception
   */
  /*public static Map<String,Object> getTemplateData(Vertex templateNode) throws Exception{
  
      Map<String, Object> templateMapToReturn = UtilClass.getMapFromVertex(
          new ArrayList<>(), templateNode);
      populateHeaderData(templateNode, templateMapToReturn);
  
      List<Map<String,Object>> tabsList = new ArrayList<>();
      Iterable<Vertex> tabs = templateNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE_TAB);
  
      for (Vertex tabNode : tabs) {
        String baseType = tabNode.getProperty(ITemplateTab.BASE_TYPE);
        Map<String,Object> tabMap = UtilClass.getMapFromVertex(new ArrayList<>(), tabNode);
  
        switch(baseType){
          case CommonConstants.TEMPLATE_HOME_TAB_BASETYPE:
            populateHomeTabData(templateNode, tabMap);
            break;
          case CommonConstants.TEMPLATE_RELATIONSHIP_TAB_BASETYPE:
            populateRelationshipTabData(templateNode, tabMap);
            break;
        }
        populateNatureRelationships(templateNode, tabMap);
        tabsList.add(tabMap);
      }
  
      templateMapToReturn.put(ITemplate.TABS, tabsList);
      return templateMapToReturn;
    }
  */
  /**
   * Description : traverse all nature relatioships linked to template node and
   * populate respective tabs data. Promo version relationship will goto Promo
   * version tab and bundle/Prod variant will goto home tab..
   *
   * @author Ajit
   * @param templateNode
   * @param tabMap
   */
  /*
  private static void populateNatureRelationships(Vertex templateNode, Map<String, Object> tabMap)
  {
    Iterable<Vertex> natureRelationshipIterable = templateNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE_NATURE_RELATIONSHIP);
    for (Vertex natureRelationshipNode : natureRelationshipIterable) {
      String relationshipType = natureRelationshipNode.getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE);
      String natureRelationshipId = UtilClass.getCode(natureRelationshipNode);
  
      String baseType = (String) tabMap.get(ITemplateTab.BASE_TYPE);
      List<String> natureRelationshipIds = new ArrayList<>();
      natureRelationshipIds.add(natureRelationshipId);
      if(baseType.equals(CommonConstants.TEMPLATE_PROMO_VERSION_TAB_BASETYPE)){
        if(relationshipType.equals(CommonConstants.PROMOTIONAL_VERSION_RELATIONSHIP)){
          tabMap.put(ITemplatePromoVersionTab.NATURE_RELATIONSHIPS, natureRelationshipIds);
        }
      } else if(baseType.equals(CommonConstants.TEMPLATE_HOME_TAB_BASETYPE)){
        if(!relationshipType.equals(CommonConstants.PROMOTIONAL_VERSION_RELATIONSHIP)){
          tabMap.put(ITemplatePromoVersionTab.NATURE_RELATIONSHIPS, natureRelationshipIds);
        }
      }
    }
  }*/
  /**
   * Description read header node and return the info.
   *
   * @author CS33
   * @param templateNode
   * @param templateMapToReturn
   * @throws Exception
   */
  /*
    public static void populateHeaderData(Vertex templateNode,
        Map<String, Object> templateMapToReturn) throws Exception
    {
      Vertex headerNode = TemplateUtils.getHeaderNodeFromTemplate(templateNode);
      Map<String,Object> headerMap = UtilClass.getMapFromVertex(new ArrayList<>(), headerNode);
      templateMapToReturn.put(ITemplate.HEADER_VISIBILITY,headerMap);
    }
  */
  /**
   * Description Read the relationship node and return the info..
   *
   * @author Ajit
   * @param templateNode
   * @param tabMap
   * @throws Exception
   */
  /*
  public static void populateRelationshipTabData(Vertex templateNode,
      Map<String, Object> tabMap) throws Exception
  {
    Vertex sequenceNode = TemplateUtils.getSequenceFromTemplate(templateNode, RelationshipLabelConstants.HAS_RELATIONSHIP_SEQUENCE);
    List<String> sortedRelationshipIds =  sequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    tabMap.put(ITemplateRelationshipTab.RELATIONSHIPS, sortedRelationshipIds);
  }
  
  /**
   * Description Read the home tab node and return the data..
   * @author Ajit
   * @param templateNode
   * @param tabMap
   * @throws Exception
   */
  /*
  public static void populateHomeTabData(Vertex templateNode, Map<String, Object> tabMap) throws Exception
  {
    Vertex sequenceNode = TemplateUtils.getSequenceFromTemplate(templateNode, RelationshipLabelConstants.HAS_PROPERTYCOLLECTION_SEQUENCE);
    List<String> sortedPropertyCollectionIds = sequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    tabMap.put(ITemplateHomeTab.PROPERTY_COLLECTIONS, sortedPropertyCollectionIds);
  }*/
}
