package com.cs.config.strategy.plugin.usecase.historyversions;

import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.historyversions.IKlassesAndTaxonomiesForVersionsInfoRequestModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetKlassAndTaxonomyInfoForVersions extends AbstractOrientPlugin {
  
  public GetKlassAndTaxonomyInfoForVersions(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassAndTaxonomyInfoForVersions/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IKlassesAndTaxonomiesForVersionsInfoRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IKlassesAndTaxonomiesForVersionsInfoRequestModel.TAXONOMY_IDS);
    
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> klassInfoMap = new HashMap<>();
    Map<String, Object> taxonomyInfoMap = new HashMap<>();
    
    fillKlassesInfoMap(klassIds, klassInfoMap);
    fillTaxonomiesInfoMap(taxonomyIds, taxonomyInfoMap);
    
    mapToReturn.put(IGetKlassInstanceVersionsForComparisonModel.REFERENCED_KLASSES_INFO,
        klassInfoMap);
    mapToReturn.put(IGetKlassInstanceVersionsForComparisonModel.REFERENCED_TAXONOMIES_INFO,
        taxonomyInfoMap);
    
    return mapToReturn;
  }
  
  /**
   * Fill Ids & Labels into map against respective Ids for klasses
   *
   * @author Krish
   */
  private void fillKlassesInfoMap(List<String> klassIds, Map<String, Object> klassInfoMap)
      throws Exception
  {
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexById(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IKlass.LABEL, IKlass.CODE), klassNode);
      klassInfoMap.put(klassId, klassMap);
    }
  }
  
  /**
   * Fill Ids, Labels, Childrens, Parents into map against respective Ids for
   * taxonomies
   *
   * @author Krish
   */
  protected void fillTaxonomiesInfoMap(List<String> taxonomyIds,
      Map<String, Object> taxonomyInfoMap) throws Exception
  {
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = null;
      try {
        taxonomyVertex = UtilClass.getVertexById(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
      Map<String, Object> taxonomyMap = UtilClass
          .getMapFromVertex(
              Arrays.asList(CommonConstants.CODE_PROPERTY,
                  IReferencedArticleTaxonomyModel.LABEL, IReferencedArticleTaxonomyModel.CODE),
              taxonomyVertex);
      
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      taxonomyInfoMap.put(taxonomyId, taxonomyMap);
    }
  }
  
  // TODO: duplicate method, remove this and use method from
  // AbstractGetConfigDetails
  protected void fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId()
        .toString();
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
        ITaxonomy.ICON, ITaxonomy.CODE);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where code <> '" + UtilClass.getCodeNew(taxonomyVertex) + "'";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex childNode : resultIterable) {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, childNode));
    }
    
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    MultiClassificationUtils.fillParentsData(fieldsToFetch, taxonomiesParentMap, taxonomyVertex);
  }
  /*
    //TODO: duplicate method, remove this and use method from AbstractGetConfigDetails
    @Deprecated
    protected void fillParentsData(List<String> fieldsToFetch, Map<String, Object> taxonomiesParentMap, Vertex taxonomyVertex) throws Exception
    {
      Vertex parentNode = AttributionTaxonomyUtil.getParentTaxonomy(taxonomyVertex);
      if(parentNode == null){
        taxonomiesParentMap.put(IReferencedTaxonomyParentModel.ID, "-1");
        return;
      }
  
        Map<String,Object> parentMap = new HashMap<>();
        taxonomiesParentMap.putAll(UtilClass.getMapFromVertex(fieldsToFetch, parentNode));
        taxonomiesParentMap.put(IReferencedTaxonomyParentModel.PARENT,parentMap);
        fillParentsData(fieldsToFetch,parentMap,parentNode);
    }
  */
}
