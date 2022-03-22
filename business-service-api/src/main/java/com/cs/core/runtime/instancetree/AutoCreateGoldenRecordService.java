package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.cs.core.config.interactor.exception.goldenrecord.InvalidBucketForAutoMergeException;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.goldenrecord.IMatchPropertiesModel;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.goldenrecord.bucket.IAutoCreateGoldenRecordService;
import com.cs.core.runtime.goldenrecord.bucket.IGetGoldenRecordMergeView;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.model.goldenrecord.GoldenRecordRuleKlassInstancesMergeViewRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.GoldenRecordRuleKlassInstancesMergeViewResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ICreateGoldenRecordRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRelationshipIdSourceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.pim.runtime.goldenrecord.GetRecommendedPropertyDataForMergeView;
import com.cs.utils.BaseEntityUtils;

@Service
public class AutoCreateGoldenRecordService extends
    AbstractCreateGoldenRecordService<ICreateGoldenRecordRequestModel, IGetKlassInstanceModel> implements IAutoCreateGoldenRecordService {
  
  @Autowired
  protected Long goldenArticleKlassCounter;
  
  @Autowired
  GetRecommendedPropertyDataForMergeView getRecommendedPropertyDataForMergeView;
  
  @Autowired
  IGetGoldenRecordMergeView getGoldenRecordRelationshipMergeView;
  
  @Override
  public IGetKlassInstanceModel executeInternal(ICreateGoldenRecordRequestModel model) throws Exception
  {
    GoldenRecordBucketDAO goldenRecordBucketDAO = new GoldenRecordBucketDAO();
    IGoldenRecordDTO goldenRecordDTO = goldenRecordBucketDAO
        .getGoldenRecordRuleAndBaseEntityIIDs(model.getBucketId(), model.getGoldenRecordId());
    
    String ruleId = goldenRecordDTO.getRuleId();
    
    Set<String> taxonomyIds = new HashSet<>();
    Set<String> klassIds = new HashSet<>();
    Set<String> languageCodes = new HashSet<>();
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    
    List<String> baseEntityIIDs = goldenRecordDTO.getLinkedBaseEntities()
                                                 .stream()
                                                 .map(String::valueOf)
                                                 .collect(Collectors.toList());
    
    List<IBaseEntityDTO> baseEntityDTOs = localeCatlogDAO.getBaseEntitiesByIIDs(baseEntityIIDs);
    
    for (IBaseEntityDTO baseEntityDTO : baseEntityDTOs) {
      languageCodes.addAll(localeCatlogDAO.loadLocaleIds(baseEntityDTO).getLocaleIds());
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
      klassIds.addAll(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO));
      taxonomyIds.addAll(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers()));
    }
    
    IConfigDetailsForGetKlassInstancesToMergeModel configDetails = goldenRecordUtils.getConfigDetails(ruleId, new ArrayList<>(taxonomyIds), new ArrayList<>(klassIds),
        new ArrayList<>(languageCodes));
    
    IGoldenRecordRule goldenRecord = configDetails.getGoldenRecordRule();
    IMergeEffect mergeEffect = goldenRecord.getMergeEffect();
    
    if (goldenRecord.getIsAutoCreate() == false) {
      throw new InvalidBucketForAutoMergeException();
    }
    
    IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel = getRecommendationDataForAutoCreate(
        mergeEffect, model, configDetails, model.getGoldenRecordId(), baseEntityDTOs, localeCatlogDAO, languageCodes);
    
    Map<String, IRecommendationModel> recommendations = responseModel.getRecommendation();
    Map<String, Map<String, IRecommendationModel>>  recommendedDependentAttributes = responseModel.getDependentAttributeRecommendation();
    IMatchPropertiesModel matchProperties = responseModel.getMatchPropertiesModel();
    
    Map<String, List<IConflictingValue>> attributes = new HashMap<>();
    Map<String, List<IConflictingValue>> tags = new HashMap<>();
    Map<String,Map<String,List<IConflictingValue>>>  dependentAttributes = new HashMap<>();
    List<IRelationshipIdSourceModel> relationshipIdSourceModels = new ArrayList<>();
    
    goldenRecordUtils.fillMatchedAttributesAndTagsConflictingMap(attributes, tags, matchProperties);
    
    if(recommendations != null) {
      recommendations.forEach((id, recommendation) -> 
        goldenRecordUtils.fillRecommendedAttributesAndTagsConflictingMap(attributes, tags, relationshipIdSourceModels, recommendation, id));
    }
    
    goldenRecordUtils.fillRecommendedLanguageDependentAttributes(dependentAttributes, recommendedDependentAttributes);
    
    model.setAttributes(attributes);
    model.setDependentAttributes(dependentAttributes);
    model.setTags(tags);
    model.setKlassIds(new ArrayList<>(klassIds));
    model.setTaxonomyIds(new ArrayList<>(taxonomyIds));
    model.setRelationships(relationshipIdSourceModels);    
    model.setSelectedLanguageCodes(new ArrayList<>(languageCodes));
    model.setCreationLanguage(languageCodes.iterator().next());
    model.setSourceIds(baseEntityIIDs);
    
    return super.executeInternal(model);
  }
  
  private IGoldenRecordRuleKlassInstancesMergeViewResponseModel getRecommendationDataForAutoCreate(IMergeEffect mergeEffect,
      ICreateGoldenRecordRequestModel dataModel, IConfigDetailsForGetKlassInstancesToMergeModel configDetails, String goldenRecordId, List<IBaseEntityDTO> baseEntityDTOs, ILocaleCatalogDAO localeCatlogDAO, Set<String> languageCodes) throws Exception
  {
    IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel = new GoldenRecordRuleKlassInstancesMergeViewResponseModel();
    
    IGoldenRecordRuleKlassInstancesMergeViewRequestModel requestModel = new GoldenRecordRuleKlassInstancesMergeViewRequestModel();
    requestModel.setMergeEffect(mergeEffect);
    requestModel.setDependentAttributeIds(configDetails.getDependendAttributeIds());
    requestModel.setBucketInstanceId(dataModel.getBucketId());
    requestModel.setReferencedRelationshipProperties(configDetails.getReferencedRelationshipProperties());
    requestModel.setIsAutoCreate(true);
    requestModel.setAllLocales(new ArrayList<>(languageCodes));
    
    try {
      getRecommendedPropertyDataForMergeView.execute(requestModel, configDetails, responseModel, baseEntityDTOs, localeCatlogDAO);
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
    
    getGoldenRecordRelationshipMergeView.getRecommendedPropertyDataForMergeView(requestModel, configDetails, responseModel, baseEntityDTOs, localeCatlogDAO);
    
    return responseModel;
  }
  
  @Override
  protected Long getCounter()
  {
    return goldenArticleKlassCounter++;
  }
}
