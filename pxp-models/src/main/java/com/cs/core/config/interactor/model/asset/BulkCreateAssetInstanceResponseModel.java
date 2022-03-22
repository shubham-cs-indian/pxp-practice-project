package com.cs.core.config.interactor.model.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;


public class BulkCreateAssetInstanceResponseModel extends KlassInstanceInformationModel implements IBulkCreateAssetInstanceResponseModel {
  
  private static final long serialVersionUID = 1L;
  private List<String>      success                      = new ArrayList<>();
  private List<Long>        successInstanceIIds          = new ArrayList<>();
  private List<String>      failure                      = new ArrayList<>();
  private Map<Long, Object> tivDuplicateDetectionInfoMap = new HashMap<>();
  private List<String>      tivFailure                   = new ArrayList<>();
  private List<String>      tivSuccess                   = new ArrayList<>();
  private List<String>      tivWarning                   = new ArrayList<>();
  private Set<Long>         duplicateIIdSet              = new HashSet<>();
  
  public void fillKlassInstanceInformationModel(IKlassInstanceInformationModel klassInstanceInformationModel)
  {
    this.entity = (IContentInstance) klassInstanceInformationModel.getEntity();
    this.id = klassInstanceInformationModel.getId();
    this.originalInstanceId = klassInstanceInformationModel.getOriginalInstanceId();
    this.name = klassInstanceInformationModel.getName();
    this.attributes = klassInstanceInformationModel.getAttributes();
    this.tags = klassInstanceInformationModel.getTags();
    this.types = klassInstanceInformationModel.getTypes();
//    this.owner = klassInstanceInformationModel.getOriginalInstanceId();
    this.branchOf = klassInstanceInformationModel.getBranchOf();
    this.versionId = klassInstanceInformationModel.getVersionId();
    this.lastModifiedBy = klassInstanceInformationModel.getLastModifiedBy();
    this.lastModified = klassInstanceInformationModel.getLastModified();
    this.globalPermission = klassInstanceInformationModel.getGlobalPermission();
    this.baseType = klassInstanceInformationModel.getBaseType();
    this.roles = klassInstanceInformationModel.getRoles();
    this.relevance = klassInstanceInformationModel.getRelevance();
    this.ruleViolation = klassInstanceInformationModel.getRuleViolation();
    this.messages = klassInstanceInformationModel.getMessages();
    this.hits = klassInstanceInformationModel.getHits();
    this.count = klassInstanceInformationModel.getCount();
    this.branchOfLabel = klassInstanceInformationModel.getBranchOfLabel();
    this.versionOfLabel = klassInstanceInformationModel.getVersionOfLabel();
    this.mandatoryViolationCount = klassInstanceInformationModel.getMandatoryViolationCount();
    this.shouldViolationCount = klassInstanceInformationModel.getShouldViolationCount();
    this.applyEffect = klassInstanceInformationModel.getApplyEffect();
    this.dataForUniquenessStatEvaluation = klassInstanceInformationModel.getDataForUniquenessStatEvaluation();
    this.isUniqueViolationCount = klassInstanceInformationModel.getIsUniqueViolationCount();
    this.createdOn = klassInstanceInformationModel.getCreatedOn();
    this.languageCodes = klassInstanceInformationModel.getLanguageCodes();
    this.creationLanguage = klassInstanceInformationModel.getCreationLanguage();
    this.eventSchedule = klassInstanceInformationModel.getEventSchedule();
    this.selectedTaxonomyIds = klassInstanceInformationModel.getSelectedTaxonomyIds();
    this.defaultAssetInstanceId = klassInstanceInformationModel.getDefaultAssetInstanceId();
    this.assetInformation = klassInstanceInformationModel.getAssetInformation();
    this.isAssetExpired = klassInstanceInformationModel.getIsAssetExpired();
    this.isDuplicate = klassInstanceInformationModel.getIsDuplicate();
    this.variantOfLabel = klassInstanceInformationModel.getVariantOfLabel();
    this.variantOf = klassInstanceInformationModel.getVariantOf();
    this.referencedAssets = klassInstanceInformationModel.getReferencedAssets();
  }
  
  @Override
  public List<String> getSuccess()
  {
    return success;
  }
  
  @Override
  public void setSuccess(List<String> success)
  {
    this.success = success;
  }
  
  @Override
  public List<Long> getSuccessInstanceIIds()
  {
    return successInstanceIIds;
  }
  
  @Override
  public void setSuccessInstanceIIds(List<Long> successInstanceIIds)
  {
    this.successInstanceIIds = successInstanceIIds;
  }
  
  @Override
  public List<String> getFailure()
  {
    return failure;
  }
  
  @Override
  public void setFailure(List<String> failure)
  {
    this.failure = failure;
  }
  
  @Override
  public Map<Long, Object> getTivDuplicateDetectionInfoMap()
  {
    return tivDuplicateDetectionInfoMap;
  }
  
  @Override
  public void setTivDuplicateDetectionInfoMap(Map<Long, Object> tivDuplicateDetectionInfoMap)
  {
    this.tivDuplicateDetectionInfoMap = tivDuplicateDetectionInfoMap;
  }
  
  @Override
  public List<String> getTivFailure()
  {
    return tivFailure;
  }
  
  @Override
  public void setTivFailure(List<String> tivFailure)
  {
    this.tivFailure = tivFailure;
  }
  
  @Override
  public List<String> getTivSuccess()
  {
    return tivSuccess;
  }
  
  @Override
  public void setTivSuccess(List<String> tivSuccess)
  {
    this.tivSuccess = tivSuccess;
  }
  
  @Override
  public List<String> getTivWarning()
  {
    return tivWarning;
  }
  
  @Override
  public void setTivWarning(List<String> tivWarning)
  {
    this.tivWarning = tivWarning;
  }
  
  @Override
  public Set<Long> getDuplicateIIdSet()
  {
    return duplicateIIdSet;
  }
  
  @Override
  public void setDuplicateIIdSet(Set<Long> duplicateIIdSet)
  {
    this.duplicateIIdSet = duplicateIIdSet;
  }
  
}
