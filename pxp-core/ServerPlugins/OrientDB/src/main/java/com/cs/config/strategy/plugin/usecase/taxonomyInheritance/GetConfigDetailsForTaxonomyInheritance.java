package com.cs.config.strategy.plugin.usecase.taxonomyInheritance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IGetInheritanceTaxonomyIdsResponseModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForTaxonomyInheritance extends AbstractOrientPlugin{

  public GetConfigDetailsForTaxonomyInheritance(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForTaxonomyInheritance/*" };
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> taxonomyIds  = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    Map<String , Object> mapToReturn = new HashMap<String, Object> ();
    fillTaxonomyDetails(mapToReturn, taxonomyIds);
    return mapToReturn;
  }
  
  protected void fillTaxonomyDetails(Map<String, Object> mapToReturn, List<String> taxonomyIds)
      throws Exception
  {
    Map<String,Object> referencedTaxonomyMap = new HashMap<>();
    mapToReturn.put(IGetInheritanceTaxonomyIdsResponseModel.REFERENCED_TAXONOMIES, referencedTaxonomyMap);
    Set<Vertex> taxonomyVertices = new HashSet<>();
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = null;
      try {
        taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        taxonomyVertices.add(taxonomyVertex);
      } catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(Arrays.asList(IReferencedArticleTaxonomyModel.LABEL,
              IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE, IReferencedArticleTaxonomyModel.CODE, IReferencedArticleTaxonomyModel.BASETYPE),
          taxonomyVertex);
      if(taxonomyMap.get( IReferencedArticleTaxonomyModel.TAXONOMY_TYPE).equals(CommonConstants.MAJOR_TAXONOMY)){
        fillTaxonomiesChildrenAndParentData(taxonomyMap,taxonomyVertex);
        referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
      }
    }
  }
  
  protected String  fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    Map<String,Object> taxonomiesParentMap = new HashMap<>();
   
    List<String> fieldsToFetch = Arrays.asList(ITaxonomy.LABEL,ITaxonomy.CODE,
        ITaxonomy.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE, ITaxonomy.BASE_TYPE);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    return MultiClassificationUtils.fillParentsData(fieldsToFetch,taxonomiesParentMap,taxonomyVertex);
  }
  
}

