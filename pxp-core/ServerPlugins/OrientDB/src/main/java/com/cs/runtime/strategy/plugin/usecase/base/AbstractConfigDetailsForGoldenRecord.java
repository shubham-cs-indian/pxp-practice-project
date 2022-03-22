package com.cs.runtime.strategy.plugin.usecase.base;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForGoldenRecordRuleResponseModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class AbstractConfigDetailsForGoldenRecord extends AbstractConfigDetails {
  
  public AbstractConfigDetailsForGoldenRecord(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected void fillReferencedTaxonomies(Map<String, Object> conigDetails, Vertex vertex,
      Map<String, Object> ruleMap) throws Exception
  {
    Map<String, Object> referencedTaxonoies = (Map<String, Object>) conigDetails
        .get(IGetConfigDetailsForGoldenRecordRuleResponseModel.REFERENCED_TAXONOMIES);
    
    Iterable<Vertex> taxonomyVertices = vertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAXONOMY_LINK);
    
    List<String> taxonomies = new ArrayList<>();
    ruleMap.put(IGoldenRecordRule.TAXONOMY_IDS, taxonomies);
    
    for (Vertex taxonomyVertex : taxonomyVertices) {
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
              IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
              IReferencedArticleTaxonomyModel.CODE),
          taxonomyVertex);
      String taxonomyId = (String) taxonomyMap.get(IMasterTaxonomy.ID);
      taxonomies.add(taxonomyId);
      referencedTaxonoies.put(taxonomyId, taxonomyMap);
      
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
    }
  }
  
  protected void fillReferencedKlasses(Map<String, Object> configDetails, Vertex vertex,
      Map<String, Object> ruleMap) throws Exception
  {
    Map<String, Object> referencedKlasses = (Map<String, Object>) configDetails
        .get(IGetConfigDetailsForGoldenRecordRuleResponseModel.REFRENCED_KLASSES);
    
    Iterable<Vertex> klassVertices = vertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_KLASS_LINK);
    
    List<String> klasses = new ArrayList<>();
    ruleMap.put(IGoldenRecordRule.KLASS_IDS, klasses);
    
    Map<String, Object> referencedTagMap = (Map<String, Object>) configDetails
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    for (Vertex klassVertex : klassVertices) {
      List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
          IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
          IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.CODE);
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
      String klassId = (String) klassMap.get(IKlass.ID);
      klasses.add(klassId);
      referencedKlasses.put(klassId, klassMap);
      Iterable<Vertex> linkedLifeCycleStatusTags = klassVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK);
      for (Vertex linkedLifeCycleStatusTag : linkedLifeCycleStatusTags) {
        String id = linkedLifeCycleStatusTag.getProperty(CommonConstants.CODE_PROPERTY);
        Vertex linkedTagNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(linkedTagNode, true);
        String tagId = (String) referencedTag.get(ITag.ID);
        referencedTagMap.put(tagId, referencedTag);
      }
    }
  }
}
