package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

/**
 * Orient plugin for migrating default translations. 
 * @author rahul.sehrawat
 *
 */
public class OrientMigrationForDefaultTranslations extends AbstractOrientMigration {
  
  private String LABEL_SEPERATOR = "label" + Seperators.FIELD_LANG_SEPERATOR;
  
  public OrientMigrationForDefaultTranslations(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|OrientMigrationForDefaultTranslations/*" };
  }
  
  @Override
  public Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    String defaultLanguageWithoutSeprator = (String) requestMap.get(IIdParameterModel.ID);
    String defaultLanguage = LABEL_SEPERATOR + defaultLanguageWithoutSeprator;
    List<String> verticesName = getAllVeticesName();
    
    for (String vertexLabel : verticesName) {
      Iterable<Vertex> entityVertices = UtilClass.getGraph()
          .command(new OCommandSQL("Select from " + vertexLabel))
          .execute();
      for (Vertex entityVertex : entityVertices) {
        String defaultValue = entityVertex.getProperty(defaultLanguage);
        
        if (defaultValue != null && !defaultValue.isEmpty()) {
          entityVertex.setProperty(CommonConstants.DEFAULT_LABEL, defaultValue);
        }
        else {
          entityVertex.setProperty(CommonConstants.DEFAULT_LABEL, "");
        }
        if (vertexLabel == VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP) {
          Map<String, Object> side1Map = entityVertex.getProperty(CommonConstants.RELATIONSHIP_SIDE_1);
          Map<String, Object> side2Map = entityVertex.getProperty(CommonConstants.RELATIONSHIP_SIDE_2);
          String defaultValueForSide1 = (String) side1Map.get(defaultLanguage);
          String defaultValueForSide2 = (String) side2Map.get(defaultLanguage);
          insertDefaultLabelPropertyToNodeForRelationship(defaultValueForSide1, side1Map);
          insertDefaultLabelPropertyToNodeForRelationship(defaultValueForSide2, side2Map);
          entityVertex.setProperty(CommonConstants.RELATIONSHIP_SIDE_1, side1Map);
          entityVertex.setProperty(CommonConstants.RELATIONSHIP_SIDE_2, side2Map);
        }
      }
      UtilClass.getGraph().commit();
    }
    
    return null;
  }
  
  protected void insertDefaultLabelPropertyToNodeForRelationship(String defaultValue, Map<String, Object> sideMap) {
    
    if (defaultValue != null && !defaultValue.isEmpty()) {
      sideMap.put(CommonConstants.DEFAULT_LABEL, defaultValue);
    }
    else {
      sideMap.put(CommonConstants.DEFAULT_LABEL, "");
    }
  }
  
  protected List<String> getAllVeticesName() {
    List<String> VerticesName = Arrays.asList(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,VertexLabelConstants.ENTITY_TYPE_KLASS, VertexLabelConstants.ENTITY_TAG,
        VertexLabelConstants.ENTITY_TYPE_ASSET,VertexLabelConstants.ENTITY_TYPE_USER,VertexLabelConstants.ATTRIBUTION_TAXONOMY,
        VertexLabelConstants.ATTRIBUTION_TAXONOMY_LEVEL,VertexLabelConstants.AUTHORIZATION_MAPPING,VertexLabelConstants.DASHBOARD_TAB,
        VertexLabelConstants.GOLDEN_RECORD_RULE,VertexLabelConstants.GOVERNANCE_RULE_KPI,VertexLabelConstants.ENTITY_TYPE_LANGUAGE,VertexLabelConstants.NATURE_RELATIONSHIP,
        VertexLabelConstants.ORGANIZATION,VertexLabelConstants.PROCESS_EVENT,VertexLabelConstants.PROPERTY_COLLECTION,VertexLabelConstants.PROPERTY_MAPPING,
        VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP,VertexLabelConstants.ENTITY_TYPE_ROLE,VertexLabelConstants.ROOT_RELATIONSHIP,VertexLabelConstants.SMART_DOCUMENT_PRESET,
        VertexLabelConstants.ENTITY_STANDARD_ROLE,VertexLabelConstants.ENTITY_TYPE_SUPPLIER,VertexLabelConstants.SYSTEM,VertexLabelConstants.ENTITY_TYPE_TASK,
        VertexLabelConstants.TEMPLATE,VertexLabelConstants.ENTITY_TYPE_TARGET,VertexLabelConstants.TAB,VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET,VertexLabelConstants.VARIANT_CONTEXT,
        VertexLabelConstants.UI_TRANSLATIONS,VertexLabelConstants.DATA_RULE,VertexLabelConstants.ENTITY_TYPE_ICON,
        VertexLabelConstants.RULE_LIST,VertexLabelConstants.ENDPOINT);
    
    return VerticesName;
  }

}
