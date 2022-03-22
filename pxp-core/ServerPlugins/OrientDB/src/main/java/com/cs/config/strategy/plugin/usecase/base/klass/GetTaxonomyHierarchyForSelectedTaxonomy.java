package com.cs.config.strategy.plugin.usecase.base.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeResponseModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetTaxonomyHierarchyForSelectedTaxonomy extends AbstractOrientPlugin {
  
  public GetTaxonomyHierarchyForSelectedTaxonomy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTaxonomyHierarchyForSelectedTaxonomy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = getMapToReturn();
    List<String> selectedTaxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForSwitchTypeRequestModel.SELECTED_TAXONOMY_IDS);
    consolidateTaxonomyHierarchyIds(selectedTaxonomyIds, mapToReturn);
    fillTaxonomyDetails(mapToReturn, selectedTaxonomyIds);
    return mapToReturn;
  }
  
  protected void consolidateTaxonomyHierarchyIds(List<String> taxonomyIdsToGetDetailsFor,
      Map<String, Object> mapToReturn) throws Exception, KlassTaxonomyNotFoundException
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Collection<String>> taxonomyHierarchies = (Map<String, Collection<String>>) mapToReturn
        .get(IConfigDetailsForSwitchTypeResponseModel.TAXONOMY_HIERARCHIES);
    if (taxonomyIdsToGetDetailsFor != null) {
      for (String taxonomyId : taxonomyIdsToGetDetailsFor) {
        Vertex taxonomyVertex = null;
        try {
          taxonomyVertex = UtilClass.getVertexById(taxonomyId,
              VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        }
        catch (NotFoundException e) {
          throw new KlassTaxonomyNotFoundException();
        }
        String query = "select from(traverse out('"
            + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from "
            + taxonomyVertex.getId() + " strategy BREADTH_FIRST)";
        Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
            .execute();
        List<String> parentTaxonomyIds = new ArrayList<>();
        taxonomyHierarchies.put(taxonomyId, parentTaxonomyIds);
        for (Vertex taxonomyNode : resultIterable) {
          if (TagUtils.isTagNode(taxonomyNode)) {
            continue;
          }
          String taxnomyId = taxonomyNode.getProperty(CommonConstants.CODE_PROPERTY);
          parentTaxonomyIds.add(taxnomyId);
        }
      }
    }
  }
  
  protected void fillTaxonomyDetails(Map<String, Object> mapToReturn, List<String> taxonomyIds)
      throws Exception
  {
    Map<String, Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    Set<Vertex> taxonomyVertices = new HashSet<>();
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = null;
      try {
        taxonomyVertex = UtilClass.getVertexById(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        taxonomyVertices.add(taxonomyVertex);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
              IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
              IReferencedArticleTaxonomyModel.CODE, IReferencedArticleTaxonomyModel.BASETYPE),
          taxonomyVertex);
      
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
    }
  }
  
  protected String fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId()
        .toString();
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
        ITaxonomy.CODE, ITaxonomy.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
        ITaxonomy.BASE_TYPE);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where code <> '" + UtilClass.getCodeNew(taxonomyVertex) + "'";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex childNode : resultIterable) {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, childNode));
    }
    
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    return MultiClassificationUtils.fillParentsData(fieldsToFetch, taxonomiesParentMap,
        taxonomyVertex);
  }
  
  protected Map<String, Object> getMapToReturn()
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    Map<String, Collection<String>> taxonomyHierarchies = new HashMap<String, Collection<String>>();
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    mapToReturn.put(IGetConfigDetailsModel.TAXONOMY_HIERARCHIES, taxonomyHierarchies);
    return mapToReturn;
  }
}
