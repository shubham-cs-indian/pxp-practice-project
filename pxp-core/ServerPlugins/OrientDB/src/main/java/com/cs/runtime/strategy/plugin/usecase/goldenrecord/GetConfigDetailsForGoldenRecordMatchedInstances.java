package com.cs.runtime.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForMatchedInstancesRequestModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetBucketInstancesResponseModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetailsForGoldenRecord;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForGoldenRecordMatchedInstances
    extends AbstractConfigDetailsForGoldenRecord {
  
  public GetConfigDetailsForGoldenRecordMatchedInstances(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGoldenRecordMatchedInstances/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IGetConfigDetailsForMatchedInstancesRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IGetConfigDetailsForMatchedInstancesRequestModel.SELECTED_TAXONOMY_IDS);
    List<String> organizationIds = (List<String>) requestMap
        .get(IGetConfigDetailsForMatchedInstancesRequestModel.ORGANIZATION_IDS);
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> referencedKlasses = new HashMap<>();
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    Map<String, Object> referencedOrganizations = new HashMap<>();
    if (!klassIds.isEmpty()) {
      fillReferencedKlasses(referencedKlasses, klassIds);
    }
    if (!taxonomyIds.isEmpty()) {
      fillReferencedTaxonomies(referencedTaxonomies, taxonomyIds);
    }
    if (!organizationIds.isEmpty()) {
      fillReferencedOrganizations(referencedOrganizations, organizationIds);
    }
    returnMap.put(IGetBucketInstancesResponseModel.REFERENCED_KLASSES, referencedKlasses);
    returnMap.put(IGetBucketInstancesResponseModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    returnMap.put(IGetBucketInstancesResponseModel.REFERENCED_ORGANIZATIONS,
        referencedOrganizations);
    return returnMap;
  }
  
  protected void fillReferencedKlasses(Map<String, Object> referencedKlasses, List<String> klassIds)
      throws Exception
  {
    for (String klassId : klassIds) {
      List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
          IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
          IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.CODE);
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassNode);
      referencedKlasses.put(klassId, klassMap);
    }
  }
  
  protected void fillReferencedTaxonomies(Map<String, Object> referencedTaxonomies,
      List<String> taxonomyIds) throws Exception
  {
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      Map<String, Object> taxonomyMap = UtilClass
          .getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY,
              IReferencedArticleTaxonomyModel.LABEL, IReferencedArticleTaxonomyModel.ICON,
              IReferencedArticleTaxonomyModel.TAXONOMY_TYPE, IKlass.CODE), taxonomyVertex);
      referencedTaxonomies.put(taxonomyId, taxonomyMap);
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
    }
  }
  
  protected void fillReferencedOrganizations(Map<String, Object> referencedOrganizations,
      List<String> organizationIds) throws Exception
  {
    Iterable<Vertex> organizationVertices = UtilClass.getVerticesByIndexedIds(organizationIds,
        VertexLabelConstants.ORGANIZATION);
    for (Vertex organization : organizationVertices) {
      String organizationId = UtilClass.getCodeNew(organization);
      Map<String, Object> idLabelMap = new HashMap<>();
      idLabelMap.put(IIdLabelCodeModel.ID, organizationId);
      idLabelMap.put(IIdLabelCodeModel.LABEL,
          (String) UtilClass.getValueByLanguage(organization, IIdLabelModel.LABEL));
      idLabelMap.put(IIdLabelCodeModel.CODE, UtilClass.getCode(organization));
      referencedOrganizations.put(organizationId, idLabelMap);
    }
  }
}
