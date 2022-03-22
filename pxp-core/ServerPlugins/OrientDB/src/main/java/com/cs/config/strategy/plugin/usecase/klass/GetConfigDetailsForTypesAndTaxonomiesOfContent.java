package com.cs.config.strategy.plugin.usecase.klass;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.klassinstance.IReferencedTypesAndTaxonomiesModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForTypesAndTaxonomiesOfContent extends AbstractOrientPlugin {
  
  public GetConfigDetailsForTypesAndTaxonomiesOfContent(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  { 
    return new String[] { "POST|GetConfigDetailsForTypesAndTaxonomiesOfContent/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> types = (List<String>) requestMap.get(IKlassInstance.TYPES);
    List<String> selectedTaxonomyIds = (List<String>) requestMap.get(IKlassInstance.TAXONOMY_IDS);
    
    Map<String, Object> returnMap = new HashMap<>();
    
    fillReferencedKlasses(types, returnMap);
    fillTaxonomyDetails(returnMap, selectedTaxonomyIds);
    
    return returnMap;
  }
  
  protected void fillReferencedKlasses(List<String> types, Map<String, Object> returnMap)
  {
    
    String typesString = EntityUtil.quoteIt(types);
    
    String query = "select from " + CommonConstants.ENTITY_KLASS + " where "
        + CommonConstants.CODE_PROPERTY + " in " + typesString;
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Map<String, Map<String, Object>> referencedKlasses = new HashMap<>();
    
    for (Vertex klass : resultIterable) {
      Map<String, Object> propertyMap = UtilClass
          .getMapFromVertex(Arrays.asList(IKlass.LABEL, IKlass.ID,
              IKlass.ICON, IKlass.TYPE, IKlass.CODE, IKlass.IS_NATURE), klass);
      referencedKlasses.put(UtilClass.getCode(klass), propertyMap);
    }
    
    returnMap.put(IReferencedTypesAndTaxonomiesModel.REFERENCED_KLASSES, referencedKlasses);
  }
  
  protected void fillTaxonomyDetails(Map<String, Object> mapToReturn, List<String> taxonomyIds)
      throws Exception
  {
    Map<String, Object> referencedTaxonomyMap = new HashMap<>();
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
      
      List<String> asList = Arrays.asList(IReferencedArticleTaxonomyModel.LABEL,
          IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
          IReferencedArticleTaxonomyModel.CODE, IReferencedArticleTaxonomyModel.BASETYPE);
      
      
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
          asList,
          taxonomyVertex);
      
      String taxonomyVertexId = fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      if (taxonomyVertexId == null) {
        continue;
      }
      referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
    }
    mapToReturn.put(IReferencedTypesAndTaxonomiesModel.REFERENCED_TAXONOMIES,
        referencedTaxonomyMap);
  }
  
  protected String fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId()
        .toString();
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    
    List<String> fieldsToFetch = Arrays.asList(ITaxonomy.LABEL,
        ITaxonomy.CODE, ITaxonomy.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
        ITaxonomy.BASE_TYPE);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where code <> '" + UtilClass.getCode(taxonomyVertex) + "'";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex childNode : resultIterable) {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, childNode));
    }
    
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    return fillParentsData(fieldsToFetch, taxonomiesParentMap, taxonomyVertex);
  }
  
  public static String fillParentsData(List<String> fieldsToFetch,
      Map<String, Object> taxonomiesParentMap, Vertex taxonomyVertex) throws Exception
  {
    Vertex parentNode = AttributionTaxonomyUtil.getParentTaxonomy(taxonomyVertex);
    if (parentNode == null) {
      taxonomiesParentMap.put(IReferencedTaxonomyParentModel.ID, "-1");
      return UtilClass.getCode(taxonomyVertex);
    }
    Map<String, Object> parentMap = new HashMap<>();
    taxonomiesParentMap.putAll(UtilClass.getMapFromVertex(fieldsToFetch, parentNode));
    if (CommonConstants.MINOR_TAXONOMY.equals(taxonomiesParentMap.get(ITaxonomy.TAXONOMY_TYPE))) {
      return null;
    }
    taxonomiesParentMap.put(IReferencedTaxonomyParentModel.PARENT, parentMap);
    return fillParentsData(fieldsToFetch, parentMap, parentNode);
  }
}