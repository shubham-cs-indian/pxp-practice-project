package com.cs.config.strategy.plugin.usecase.entity;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IGetEntityIdsByEntityTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetConfigEntityIdsByEntityType extends AbstractOrientPlugin {
  
  public GetConfigEntityIdsByEntityType(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<String> configEntityIdsList = new ArrayList<>();
    String entityType = (String) requestMap.get(IGetEntityIdsByEntityTypeModel.ENTITY_TYPE);
    
    Iterable<Vertex> verticesOfEnity = null;
    Boolean isTaxonomy = false;
    if (entityType != null) {
      switch (entityType) {
        case VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE:
          getAttributeIds(configEntityIdsList);
          break;
        case VertexLabelConstants.ENTITY_TAG:
          getTagIds(configEntityIdsList);
          break;
        case VertexLabelConstants.ENTITY_TYPE_USER:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_USER);
          break;
        case VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
          break;
        case VertexLabelConstants.DATA_RULE:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.DATA_RULE);
          break;
        case VertexLabelConstants.ORGANIZATION:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.ORGANIZATION);
          break;
        case VertexLabelConstants.TAB:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.TAB);
          break;
        case VertexLabelConstants.PROPERTY_COLLECTION:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.PROPERTY_COLLECTION);
          break;
        case VertexLabelConstants.ENTITY_TYPE_TASK:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK);
          break;
        case VertexLabelConstants.GOVERNANCE_RULE_KPI:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.GOVERNANCE_RULE_KPI);
          break;
        case VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
          break;
        case VertexLabelConstants.TEMPLATE:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.TEMPLATE);
          break;
        case VertexLabelConstants.GOLDEN_RECORD_RULE:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.GOLDEN_RECORD_RULE);
          break;
        case VertexLabelConstants.RULE_LIST:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.RULE_LIST);
          break;
        case VertexLabelConstants.VARIANT_CONTEXT:
          verticesOfEnity = UtilClass.getGraph()
              .getVerticesOfClass(VertexLabelConstants.VARIANT_CONTEXT);
          break;
        case VertexLabelConstants.ATTRIBUTION_TAXONOMY:
          verticesOfEnity = UtilClass.getGraph()
              .command(new OCommandSQL("select from " + VertexLabelConstants.ATTRIBUTION_TAXONOMY
                  + " where isTaxonomy = true"))
              .execute();
          isTaxonomy = true;
          break;
        case VertexLabelConstants.ENTITY_TYPE_KLASS:
          verticesOfEnity = UtilClass.getGraph()
              .command(new OCommandSQL(
                  "Select from " + VertexLabelConstants.ENTITY_TYPE_KLASS + " ORDER BY @rid ASC"))
              .execute();
          break;
        case VertexLabelConstants.ENTITY_TYPE_ASSET:
          verticesOfEnity = UtilClass.getGraph()
              .command(new OCommandSQL(
                  "Select from " + VertexLabelConstants.ENTITY_TYPE_ASSET + " ORDER BY @rid ASC"))
              .execute();
          break;
        case VertexLabelConstants.ENTITY_TYPE_SUPPLIER:
          verticesOfEnity = UtilClass.getGraph()
              .command(new OCommandSQL("Select from " + VertexLabelConstants.ENTITY_TYPE_SUPPLIER
                  + " ORDER BY @rid ASC"))
              .execute();
          break;
        case VertexLabelConstants.ENTITY_TYPE_TARGET:
          verticesOfEnity = UtilClass.getGraph()
              .command(new OCommandSQL(
                  "Select from " + VertexLabelConstants.ENTITY_TYPE_TARGET + " ORDER BY @rid ASC"))
              .execute();
          break;
        case VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET:
          verticesOfEnity = UtilClass.getGraph()
              .command(new OCommandSQL("Select from " + VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET
                  + " ORDER BY @rid ASC"))
              .execute();
          break;
        case VertexLabelConstants.ENTITY_TYPE_LANGUAGE:
          verticesOfEnity = UtilClass.getGraph()
              .command(new OCommandSQL("Select from " + VertexLabelConstants.ENTITY_TYPE_LANGUAGE
                  + " ORDER BY @rid ASC"))
              .execute();
          break;
      }
    }
    
    if (!isTaxonomy) {
      if (verticesOfEnity != null) {
        for (Vertex vertex : verticesOfEnity) {
          configEntityIdsList.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
        }
      }
    }
    
    if (isTaxonomy) {
      if (verticesOfEnity != null) {
        List<String> rootTaxonomies = new ArrayList<>();
        List<String> childTaxonomies = new ArrayList<>();
        for (Vertex vertex : verticesOfEnity) {
          if (vertex.getPropertyKeys()
              .contains(CommonConstants.TAXONOMY_TYPE)
              && (vertex.getProperty(CommonConstants.TAXONOMY_TYPE)
                  .equals(CommonConstants.MAJOR_TAXONOMY)
                  || vertex.getProperty(CommonConstants.TAXONOMY_TYPE)
                      .equals(CommonConstants.MINOR_TAXONOMY))) {
            rootTaxonomies.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
          }
          else {
            childTaxonomies.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
          }
        }
        configEntityIdsList.addAll(rootTaxonomies);
        configEntityIdsList.addAll(childTaxonomies);
      }
    }
    
    returnMap.put(IIdsListParameterModel.IDS, configEntityIdsList);
    return returnMap;
  }
  
  private void getTagIds(List<String> configEntityIdsList)
  {
    String query = "Select from " + VertexLabelConstants.ENTITY_TAG
        + " where " + ITag.IS_ROOT + " = true";
    Iterable<Vertex> resultVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex vertex : resultVertices) {
      if (vertex.getProperty(ITag.TAG_TYPE) == null) {
        continue;
      }
      if (vertex.getProperty(ITag.TAG_TYPE)
          .equals(CommonConstants.BOOLEAN_TAG_TYPE_ID)) {
        configEntityIdsList.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
        continue;
      }
      configEntityIdsList.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
      Iterable<Vertex> vertices = vertex.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
      UtilClass.getListOfIds(vertices, configEntityIdsList);
    }
  }
  
  private void getAttributeIds(List<String> configEntityIdsList)
  {
    Iterable<Vertex> verticesOfAttribute = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    // Implementation done for retriving Calculated and Concatenated
    // attributes at the end of the list
    List<String> normalAttributes = new ArrayList<>();
    List<String> concatenated = new ArrayList<>();
    List<String> calculated = new ArrayList<>();
    
    for (Vertex vertex : verticesOfAttribute) {
      if (vertex.getProperty(CommonConstants.TYPE)
          .equals(CommonConstants.CALCULATED_ATTRIBUTE_TYPE)) {
        calculated.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
      }
      else if (vertex.getProperty(CommonConstants.TYPE)
          .equals(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE)) {
        concatenated.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
      }
      else {
        normalAttributes.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
      }
    }
    
    configEntityIdsList.addAll(normalAttributes);
    configEntityIdsList.addAll(calculated);
    configEntityIdsList.addAll(concatenated);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigEntityIdsByEntityType/*" };
  }
}
