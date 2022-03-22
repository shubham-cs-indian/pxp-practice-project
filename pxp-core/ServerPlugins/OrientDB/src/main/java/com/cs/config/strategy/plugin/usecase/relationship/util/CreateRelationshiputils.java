package com.cs.config.strategy.plugin.usecase.relationship.util;

public class CreateRelationshiputils {
  
  /**
   * Description : Get the Klass relationship nodes of relationship node and
   * then get the klassnodes connected to it. Link this relationship node to
   * template of those klasses and update the sequence..
   *
   * @author Ajit
   * @param relationshipNode
   * @throws Exception
   */
  /*
  public static void manageRelationshiplinkToTemplates(Vertex relationshipNode) throws Exception
  {
    Iterable<Vertex> klassPropertyNodes = relationshipNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex klassPropertyNode : klassPropertyNodes) {
      Vertex parentTemplateNode = null;
      List<Vertex> childKlassNodes = new ArrayList<Vertex>();
      Iterable<Edge> hasKlassProperties = klassPropertyNode.getEdges(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Edge hasKlassProperty : hasKlassProperties) {
        Vertex klassNode = hasKlassProperty.getVertex(Direction.OUT);
        Boolean isInherited = hasKlassProperty.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
        Vertex templateNode = null;
        try {
        templateNode = TemplateUtils.getTemplateFromKlass(klassNode);
        }
        catch (Exception e) {
          Map<String, Object> templateMap = new HashMap<String, Object>();
          templateMap.put(ITemplate.LABEL, UtilClass.getValueByLanguage(klassNode, IAttributionTaxonomy.LABEL));
          templateMap.put(ITemplate.TYPE, CommonConstants.TAXONOMY_TEMPLATE);
          Boolean shouldCreateSequence = true;
          templateNode = TemplateUtils.createTemplateNode(templateMap, shouldCreateSequence);
          Edge hasTemplateEdge = klassNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE, templateNode);
          hasTemplateEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, isInherited);
  
        }
        Iterable<Vertex> templateTabNodes = templateNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_TEMPLATE_TAB);
        for (Vertex templateTabNode : templateTabNodes) {
          if (((String) templateTabNode.getProperty(ITemplateTab.BASE_TYPE))
              .equals(CommonConstants.TEMPLATE_RELATIONSHIP_TAB_BASETYPE)
              && !(boolean) templateTabNode.getProperty(ITemplateTab.IS_VISIBLE)) {
            templateTabNode.setProperty(ITemplateTab.IS_VISIBLE, true);
            break;
          }
        }
        Edge hasTemplate = templateNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE_RELATIONSHIP, relationshipNode);
        hasTemplate.setProperty(CommonConstants.IS_INHERITED_PROPERTY, isInherited);
        if(isInherited){
          childKlassNodes.add(klassNode);
        } else {
          parentTemplateNode = templateNode;
        }
      }
        KlassUtils.updateTemplateSequenceNode(parentTemplateNode,
            UtilClass.getCode(relationshipNode), childKlassNodes, -1,
            RelationshipLabelConstants.HAS_RELATIONSHIP_SEQUENCE);
    }
  }
  */
  /**
   * Description : get KNR node of Nature relationship and then get connecting
   * klass node. Link this relationship to template node of that klass..
   *
   * @author Ajit
   * @param relationshipNode
   * @throws Exception
   */
  /*
  public static void manageNatureRelationshiplinkToTemplates(Vertex relationshipNode) throws Exception
  {
    Iterable<Vertex> klassPropertyNodes = relationshipNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_RELATIONSHIP);
    for (Vertex klassPropertyNode : klassPropertyNodes) {
      Iterable<Edge> hasKlassProperties = klassPropertyNode.getEdges(Direction.IN, RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
      for (Edge hasKlassProperty : hasKlassProperties) {
        Vertex klassNode = hasKlassProperty.getVertex(Direction.OUT);
        Vertex templateNode = TemplateUtils.getTemplateFromKlass(klassNode);
        templateNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE_NATURE_RELATIONSHIP, relationshipNode);
      }
    }
  }*/
}
