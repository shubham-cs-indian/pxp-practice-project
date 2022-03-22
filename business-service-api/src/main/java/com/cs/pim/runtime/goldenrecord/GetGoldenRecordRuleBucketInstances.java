package com.cs.pim.runtime.goldenrecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.postgresql.util.HStoreConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForGoldenRecordRuleResponseModel;
import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.elastic.idto.ISearchResultDTO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.idto.IGRBucketAttributeDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketTagDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordBucketDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.instancetree.AbstractNewInstanceTree;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.goldenrecord.GoldenRecordRuleBucketInstanceModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleBucketInstanceModel;
import com.cs.core.runtime.interactor.model.instancetree.GetInstanceTreeForGoldenRecordBucketResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IAppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetInstanceTreeForGoldenRecordBucketResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForGetGoldenRecordBucketInstancesStrategy;
import com.cs.core.runtime.strategy.usecase.goldenrecord.IGetConfigDetailsForGoldenRecordBucketsStrategy;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class GetGoldenRecordRuleBucketInstances extends
    AbstractNewInstanceTree<IGetInstanceTreeRequestModel, IGetInstanceTreeForGoldenRecordBucketResponseModel>
    implements IGetGoldenRecordRuleBucketInstances {
  
  @Autowired
  protected IGetConfigDetailsForGetGoldenRecordBucketInstancesStrategy getConfigDetailsForGetGoldenRecordBucketInstancesStrategy;

  @Autowired
  protected IGetConfigDetailsForGoldenRecordBucketsStrategy            getConfigDetailsForGoldenRecordBucketsStrategy;
  
  @Override
  protected IGetConfigDetailsForGetNewInstanceTreeRequestModel getConfigDetailsRequestModel()
  {
    return new GetConfigDetailsForGetNewInstanceTreeRequestModel();
  }

  @Override
  protected IGetNewInstanceTreeResponseModel executeRuntimeStrategy(
      IGetInstanceTreeRequestModel model, IConfigDetailsForNewInstanceTreeModel configDetails, Map<String, List<Map<String, Object>>> idVsHighlightsMap)
      throws Exception
  { 
    IGetInstanceTreeForGoldenRecordBucketResponseModel responseModel = new GetInstanceTreeForGoldenRecordBucketResponseModel();
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();

    ISearchDTOBuilder searchBuilder = prepareSearchDTOAndSearch(model, configDetails, localeCatalogDao);
    ISearchDTO searchDTO = searchBuilder.build();
    ISearchDAO searchDAO = openSearchDAO(localeCatalogDao, searchDTO);
    ISearchResultDTO search = searchDAO.search();
    idVsHighlightsMap.putAll(search.getHighlightsMap());
    
    List<String> goldenRecordBucketIds = search.getBaseEntityIIDs();
    List<IGoldenRecordRuleBucketInstanceModel> bucketInfo = fetchGoldenRecordBucketInfo(goldenRecordBucketIds, responseModel);
    responseModel.setBucketInstances(bucketInfo);
    responseModel.setTotalContents(search.getTotalCount());
    prepareResponseModel(model, responseModel, new ArrayList<>());
    if (model.getIsFilterDataRequired()) {
      List<INewApplicableFilterModel> filterData = getAllUtils.fillApplicableFilters(
          model.getFilterData(), configDetails.getTranslatableAttributeIds(), searchDAO);
      responseModel.setFilterData(filterData);
    }
    //fillPostConfigDetails(configDetails, responseModel);
    return responseModel;
  }

  private List<IGoldenRecordRuleBucketInstanceModel> fetchGoldenRecordBucketInfo(
      List<String> goldenRecordBucketIds, IGetInstanceTreeForGoldenRecordBucketResponseModel responseModel)
  {
    GoldenRecordBucketDAO grbDAO = new GoldenRecordBucketDAO();
    List<IGoldenRecordBucketDTO> goldenRecordBucketDTOs = new ArrayList<>();
    goldenRecordBucketIds.forEach(goldenRecordBucketId -> {
      try {
        goldenRecordBucketDTOs.add(grbDAO.getGoldenRecordBucketById(Long.parseLong(goldenRecordBucketId)));
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    });
    
    List<IGoldenRecordRuleBucketInstanceModel> bucketInstances = new ArrayList<>();
    goldenRecordBucketDTOs.stream().forEach(goldenRecordBucketDTO -> {
      List<Long> bucketBaseEntityIIDs = goldenRecordBucketDTO.getLinkedBaseEntities();
      String bucketId = goldenRecordBucketDTO.getBucketId().toString();
     
      Boolean goldenRecordExistInBucket = null;
      try 
      {
        goldenRecordExistInBucket = grbDAO.goldenRecordExistInBucket(bucketBaseEntityIIDs);
      }
      catch (RDBMSException e) 
      {
        
      }
      
      if(goldenRecordExistInBucket != null && goldenRecordExistInBucket) {
        responseModel.getBucketIdsContainingGoldenRecord().add(bucketId);
      }
      
      Set<IGRBucketAttributeDTO> bucketAttributes = goldenRecordBucketDTO.getBucketAttributes();
      Set<IGRBucketTagDTO> bucketTags = goldenRecordBucketDTO.getBucketTags();
      
      List<IAttributeInstance> attributeInstances = bucketAttributes.stream().map(bucketAttribute->{
        IAttributeInstance attributeInstance = new AttributeInstance();
        IPropertyDTO propertyByCode = new PropertyDTO();
        try {
           propertyByCode = ConfigurationDAO.instance().getPropertyByCode(bucketAttribute.getAttributeId());
        }
        catch (RDBMSException e) {
          e.printStackTrace();
        }
        attributeInstance.setAttributeId(bucketAttribute.getAttributeId());
        attributeInstance.setValue(bucketAttribute.getValue());
        if((PropertyType.HTML).equals(propertyByCode.getPropertyType())) {
          attributeInstance.setValueAsHtml(bucketAttribute.getValue());
        }
        return attributeInstance;
      }).collect(Collectors.toList());
      
      attributeInstances.add(
          createAttributeModel(StandardProperty.lastmodifiedattribute.toString(), goldenRecordBucketDTO.getLastModifiedTime().toString()));
      attributeInstances
          .add(createAttributeModel(StandardProperty.createdonattribute.toString(), goldenRecordBucketDTO.getcreatedOnTime().toString()));
      
      List<ITagInstance> tagInstances = bucketTags.stream().map(bucketTag->{
        Map<String, String> tagValues = HStoreConverter.fromString(bucketTag.getHStoreFormat());
        ITagInstance tagInstance = new TagInstance();
        tagInstance.setTagId(bucketTag.getTagId());
        tagInstance.setId(bucketTag.getTagId());
        tagInstance.setTagValues(tagValues.entrySet().stream().map(tagValue-> {
          ITagInstanceValue tagValueInstance = new TagInstanceValue();
          tagValueInstance.setId(tagValue.getKey());
          tagValueInstance.setTagId(tagValue.getKey());
          tagValueInstance.setCode(tagValue.getKey());
          tagValueInstance.setRelevance(Integer.parseInt(tagValue.getValue()));
          return tagValueInstance;
        }).collect(Collectors.toList()));
        return tagInstance;
      }).collect(Collectors.toList());
      
      IGoldenRecordRuleBucketInstanceModel bucketInstance = new GoldenRecordRuleBucketInstanceModel();
      bucketInstance.setEndpointId(goldenRecordBucketDTO.getEndpointCode());
      bucketInstance.setOrganizationId(goldenRecordBucketDTO.getOrganisationCode());
      bucketInstance.setPhysicalCatalogId(goldenRecordBucketDTO.getCatalogCode());
      bucketInstance.setCreatedOn(goldenRecordBucketDTO.getcreatedOnTime());
      bucketInstance.setLastModified(goldenRecordBucketDTO.getLastModifiedTime());
      bucketInstance.setId(bucketId);
      bucketInstance.setIsSearchable(goldenRecordBucketDTO.getIsSearchable());
      bucketInstance.setRuleId(goldenRecordBucketDTO.getRuleId());
      bucketInstance.setIsSearchable(goldenRecordBucketDTO.getIsSearchable());
      bucketInstance.setAttributes(attributeInstances);
      bucketInstance.setTags(tagInstances);
      bucketInstance.setKlassInstanceIds(bucketBaseEntityIIDs.stream().map(bucketBaseEntityIID-> String.valueOf(bucketBaseEntityIID)).collect(Collectors.toList()));
      bucketInstance.setKlassInstanceCount(Long.valueOf(String.valueOf(bucketBaseEntityIIDs.size())));
      bucketInstances.add(bucketInstance);
    });
    return bucketInstances;
  }

  @Override
  protected IConfigDetailsForNewInstanceTreeModel executeConfigDetailsStrategy(
      IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailsForGetGoldenRecordBucketInstancesStrategy.execute(configRequestModel);
  }

  @Override
  protected List getModuleEntities(IGetInstanceTreeRequestModel model) throws Exception
  {
    return Arrays.asList(CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY);
  }
  
  @Override
  protected ISearchDAO openSearchDAO(ILocaleCatalogDAO localeCatalogDao,
      ISearchDTO searchDTO) throws Exception
  {
    return localeCatalogDao.openGoldenRecordBucketSearchDAO(searchDTO);
  }

  @Override
  protected void fillPostConfigDetails(IConfigDetailsForNewInstanceTreeModel configDetails,
      IGetNewInstanceTreeResponseModel returnModel, Map<String, List<Map<String, Object>>> idVsHighlightsMap) throws Exception
  {
    List<IGoldenRecordRuleBucketInstanceModel> listOfBuckets = ((IGetInstanceTreeForGoldenRecordBucketResponseModel) returnModel).getBucketInstances();
    List<String> ruleIds = new ArrayList<>();
    for (IGoldenRecordRuleBucketInstanceModel buckets : listOfBuckets) {
      ruleIds.add(buckets.getRuleId());
    }
    
    IIdsListParameterModel listModel = new IdsListParameterModel();
    listModel.setIds(ruleIds);
    IGetConfigDetailsForGoldenRecordRuleResponseModel postConfigDetails = getConfigDetailsForGoldenRecordBucketsStrategy.execute(listModel);
    
    returnModel.setReferencedKlasses(postConfigDetails.getReferencedKlasses());
    returnModel.setReferencedAttributes(postConfigDetails.getReferencedAttributes());
    returnModel.setReferencedTags(postConfigDetails.getReferencedTags());
    ((IGetInstanceTreeForGoldenRecordBucketResponseModel) returnModel).setReferencedTaxonomies(postConfigDetails.getReferencedTaxonomies());
    ((IGetInstanceTreeForGoldenRecordBucketResponseModel) returnModel).setReferencedGoldenRecordRules(postConfigDetails.getReferencedGoldenRecordRules());
    
    for(IAppliedSortModel sortModel:returnModel.getAppliedSortData()) {
      IAttribute iAttribute = postConfigDetails.getReferencedAttributes().get(sortModel.getSortField());
      if(iAttribute != null) {
        sortModel.setLabel(iAttribute.getLabel());
      }
    }
  }
  
  private IAttributeInstance createAttributeModel(String attributeId, String value)
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    attributeInstance.setAttributeId(attributeId);
    attributeInstance.setValue(value);
    return attributeInstance;
  }
}
