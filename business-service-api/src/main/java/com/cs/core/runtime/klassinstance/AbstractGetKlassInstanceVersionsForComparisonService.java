package com.cs.core.runtime.klassinstance;

import com.cs.config.standard.IConfigMap;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.historyversions.IKlassesAndTaxonomiesForVersionsInfoRequestModel;
import com.cs.core.config.interactor.model.historyversions.KlassesAndTaxonomiesForVersionsInfoRequestModel;
import com.cs.core.config.strategy.usecase.historyversions.IGetKlassAndTaxonomyInfoForVersionsStrategy;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityMerger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.revision.dto.RevisionTimelineBuilder;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.revision.idto.IObjectRevisionDTO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.tracking.dto.ObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndVersionId;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndVersionId;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.*;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.version.*;
import com.cs.core.runtime.interactor.utils.klassinstance.ConvertRelationSetToRelationInstance;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractGetKlassInstanceVersionsForComparisonService<P extends IKlassInstanceVersionsComparisonModel, R extends IGetKlassInstanceVersionsForComparisonModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  PermissionUtils                             permissionUtils;
  
  @Autowired
  protected TransactionThreadData             controllerThread;
  
  @Autowired
  protected RDBMSComponentUtils               rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                        configUtil;
  
  @Autowired
  IGetKlassAndTaxonomyInfoForVersionsStrategy getKlassAndTaxonomyInfoForVersionsStrategy;
  
  protected abstract IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel idParameterModel) throws Exception;
  
  protected abstract String getBaseType();
  
  protected abstract IContentInstance getContentInstance();
  
  @Override
  protected R executeInternal(P instanceRequestModel) throws Exception
  {
    long baseEntityIID = Long.parseLong(instanceRequestModel.getId());
    String dataLang = controllerThread.getTransactionData().getDataLanguage();
    IBaseEntityDAO baseEntityDAO = this.rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
    
    IMulticlassificationRequestModel multiclassificationRequestModel = configUtil.getConfigRequestModelForTimeline(instanceRequestModel, baseEntityDAO);
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetails(multiclassificationRequestModel);
    
    IGetKlassInstanceVersionsForComparisonModel returnModel = new GetKlassInstanceVersionsForComparisonModel();
    List<String> versionIds = instanceRequestModel.getVersionIds();
    Set<Integer> versionIdsToFetch = new HashSet<>();
    boolean shouldFetchLiveCopy = false;
    for (String versionId : versionIds) {
      if (versionId.equals("-1"))
        shouldFetchLiveCopy = true;
      else 
        versionIdsToFetch.add(Integer.parseInt(versionId));
    }
    
    IRevisionDAO revisionDAO = rdbmsComponentUtils.getRevisionDAO();
    IObjectRevisionDTO lastObjectRevision = null;
    Map<ChangeCategory, ICSEList> liveCopyChanges = new HashMap<ChangeCategory, ICSEList>();
    IBaseEntityDTO liveCopyEntityDTO = null;
    
    Set<String> changedPropertyCodes = new HashSet<String>();
    changedPropertyCodes.add(CommonConstants.NAME_ATTRIBUTE);
    
    if (shouldFetchLiveCopy) {
      lastObjectRevision = revisionDAO.getLastObjectRevision(baseEntityIID);
      versionIdsToFetch.add(lastObjectRevision.getRevisionNo());
      List<IObjectTrackingDTO> lastChanges = revisionDAO.getLastObjectTrackings(baseEntityIID, lastObjectRevision);
      
      IBaseEntityDTO baseEntityDTOArchive = lastObjectRevision.getBaseEntityDTOArchive();
      
      BaseEntityMerger entityMerger = new BaseEntityMerger(baseEntityDTOArchive);
      RevisionTimelineBuilder rtBuilder = new RevisionTimelineBuilder();
      for (IObjectTrackingDTO change : lastChanges) {
        BaseEntityDTO changedEntity = new BaseEntityDTO();
        changedEntity.fromPXON(change.getJSONExtract().toString());
        entityMerger.synchronizeChange(change.getJSONTimelineData(), changedEntity);
        rtBuilder.addTrackingTimeline((ObjectTrackingDTO) change);
      }
      liveCopyChanges = rtBuilder.getDelta();
      liveCopyEntityDTO = entityMerger.getEntity();
      fillChangedPropertyCode(liveCopyChanges, changedPropertyCodes);
    }
    
    List<IObjectRevisionDTO> objectRevisionDTOs = revisionDAO.getRevisions(baseEntityIID, versionIdsToFetch);
    
    // fetch only changed properties from timeline summary
    for(IObjectRevisionDTO objectRevisionDTO : objectRevisionDTOs)
    {
      fillChangedPropertyCode(objectRevisionDTO.getTimelines(), changedPropertyCodes);
    }
    
    Set<Long> defaultAssetInstanceIIDs = new HashSet<Long>();
    Set<String> versionComparisionLanguages = new HashSet<String>(); 
    List<IKlassInstance> versions = returnModel.getVersions();
    for (IObjectRevisionDTO objectRevisionDTO : objectRevisionDTOs) {
      if (versionIds.contains(String.valueOf(objectRevisionDTO.getRevisionNo()))) {
       
        IBaseEntityDTO baseEntityDTO = objectRevisionDTO.getBaseEntityDTOArchive();
        IContentInstance klassInstance = fillContentInstance(
            objectRevisionDTO.getRevisionNo(), dataLang, baseEntityDTO, changedPropertyCodes, returnModel, configDetails);
        klassInstance.setVersionTimestamp(objectRevisionDTO.getCreated());
        klassInstance.setLastModifiedBy(Long.toString(objectRevisionDTO.getAuthor().getUserIID()));
        klassInstance.setLastModified(objectRevisionDTO.getCreated());
        defaultAssetInstanceIIDs.add(baseEntityDTO.getDefaultImageIID());
        versionComparisionLanguages.addAll(baseEntityDTO.getLocaleIds());
        versions.add(klassInstance);
      }
    }
    // add live copy in version if versionIds contain -1   
    if (lastObjectRevision != null) {
//      IBaseEntityDTO latestBaseEntity = lastObjectRevision.getBaseEntityDTOArchive();
      IContentInstance klassInstance = fillContentInstance(-1, dataLang, liveCopyEntityDTO, changedPropertyCodes, returnModel, configDetails);
      klassInstance.setVersionTimestamp(lastObjectRevision.getCreated());
      klassInstance.setLastModifiedBy(Long.toString(lastObjectRevision.getAuthor().getUserIID()));
      klassInstance.setLastModified(lastObjectRevision.getCreated());
 
      defaultAssetInstanceIIDs.add(liveCopyEntityDTO.getDefaultImageIID());
      versionComparisionLanguages.addAll(liveCopyEntityDTO.getLocaleIds());
      versions.add(klassInstance);
    }
    Map<String, IAssetAttributeInstanceInformationModel> referencedAssets = (Map<String, IAssetAttributeInstanceInformationModel>) returnModel.getReferencedAssets();
    for (long defaultAssetInstanceIID : defaultAssetInstanceIIDs) {
      if (defaultAssetInstanceIID != 0) {
        IBaseEntityDTO assetBaseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(defaultAssetInstanceIID);
        IAssetAttributeInstanceInformationModel assetInformationModel = BaseEntityUtils.fillAssetInformationModel(assetBaseEntityDTO);
        referencedAssets.put(String.valueOf(assetBaseEntityDTO.getBaseEntityIID()), assetInformationModel);
      }
      
    }    
    
    fillTaskInfo(baseEntityIID, returnModel);
    returnModel.setConfigDetails(configDetails);
    permissionUtils.manageKlassInstancePermissions(returnModel);
    // fill klass instance
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstanceForTimelineTab();
    
    returnModel.setKlassInstance((IContentInstance) klassInstance);
    returnModel.setVersionComparisionLanguages(new ArrayList<String>(versionComparisionLanguages));
    klassInstance.setVersionId(-1l);
    
    fillReferencedKlassAndTaxonomyDataForVersions(returnModel);

    return (R) returnModel;
  }
  
  
  protected void fillReferencedKlassAndTaxonomyDataForVersions(
      IGetKlassInstanceVersionsForComparisonModel model) throws Exception
  {
    List<IKlassInstance> versions = model.getVersions();
    Set<String> klassIdSet = new HashSet<>();
    Set<String> taxonomyIdSet = new HashSet<>();
    IKlassesAndTaxonomiesForVersionsInfoRequestModel requestModel = new KlassesAndTaxonomiesForVersionsInfoRequestModel();
    
    for (IKlassInstance version : versions) {
      klassIdSet.addAll(version.getTypes());
      taxonomyIdSet.addAll(version.getSelectedTaxonomyIds());
    }
    
    List<String> klassIdList = new ArrayList<>(klassIdSet);
    List<String> taxonomyIdList = new ArrayList<>(taxonomyIdSet);
    
    requestModel.setKlassIds(klassIdList);
    requestModel.setTaxonomyIds(taxonomyIdList);
    
    IGetKlassInstanceVersionsForComparisonModel responseModel = getKlassAndTaxonomyInfoForVersionsStrategy
        .execute(requestModel);
    
    model.setReferencedKlassesInfo(responseModel.getReferencedKlassesInfo());
    model.setReferencedTaxonomiesInfo(responseModel.getReferencedTaxonomiesInfo());
  }
  
  private void fillChangedPropertyCode(Map<ChangeCategory, ICSEList> timelines, Set<String> chnagedPropertyCodes)
      throws CSFormatException
  {
    for(ChangeCategory category : timelines.keySet())
    {
      ICSEList elements = timelines.get(category);
      List<ICSEElement> subElements = elements.getSubElements();
     for(ICSEElement subElement : subElements)
      {
        switch (category) {
          case CreatedRecord:
          case UpdatedRecord:
          case AddedRelation:
          case RemovedRelation:
            String propertyCode = ((CSEObject) subElement).getCode();
            chnagedPropertyCodes.add(propertyCode);
            break;
        }
       }
     }
  }

  private IContentInstance fillContentInstance(int revisionNo, String dataLang, 
      IBaseEntityDTO baseEntityDTO, Set<String> changedPropertyCodes,
      IGetKlassInstanceVersionsForComparisonModel returnModel, IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    IContentInstance klassInstance = getContentInstance();
    klassInstance.setId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    klassInstance.setOriginalInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    klassInstance.setName(baseEntityDTO.getBaseEntityName());
    klassInstance.setBaseType(getBaseType());
    klassInstance.setVersionId((long) revisionNo);
    klassInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getTopParentIID()));
    klassInstance.setDefaultAssetInstanceId(String.valueOf(baseEntityDTO.getDefaultImageIID()));
    klassInstance.setCreatedBy(baseEntityDTO.getCreatedTrack().getWho());
    klassInstance.setCreatedOn(baseEntityDTO.getCreatedTrack().getWhen());
    if(baseEntityDTO.getTopParentIID() == 0l) {
      klassInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    }
    klassInstance.setParentId(String.valueOf(baseEntityDTO.getParentIID()));
    if(baseEntityDTO.getParentIID() == 0l) {
      klassInstance.setParentId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    }
    klassInstance.setCreationLanguage(baseEntityDTO.getBaseLocaleID());
    List<String> languageCodes = new ArrayList<String>();
    languageCodes.add(rdbmsComponentUtils.getDataLanguage());
    klassInstance.setLanguageCodes(languageCodes);
    
    
    IContextualDataDTO contextualObject = baseEntityDTO.getContextualObject();
    if(!contextualObject.isNull()) {
      IContextInstance contextInstance = new ContextInstance();
      contextInstance.setContextId(contextualObject.getContextCode());
      contextInstance.setId(Long.toString(contextualObject.getContextualObjectIID()));
      klassInstance.setContext(contextInstance);  
     }

    for (IPropertyRecordDTO propertyRecordDTO : baseEntityDTO.getPropertyRecords()) {
      if (changedPropertyCodes.contains(propertyRecordDTO.getProperty().getCode())) {
        SuperType superType = propertyRecordDTO.getProperty().getSuperType();
        if (superType.equals(SuperType.ATTRIBUTE))
          fillAttributesAndBooleanTag(dataLang, baseEntityDTO, (IValueRecordDTO) propertyRecordDTO, klassInstance);
        else if (superType.equals(SuperType.TAGS))
          fillTags(baseEntityDTO, (ITagsRecordDTO) propertyRecordDTO, klassInstance);
      }
    }
    
    fillTypesANdTaxonomies(baseEntityDTO, klassInstance);
    if (baseEntityDTO.getBaseType().equals(BaseType.ASSET)) {
      List<IContentAttributeInstance> attributes = (List<IContentAttributeInstance>) klassInstance.getAttributes();
      KlassInstanceBuilder.fillEntityExtensionInAssetCoverFlowAttribute(attributes, baseEntityDTO);
    }
    fillRelationship(revisionNo, baseEntityDTO, returnModel, configDetails, changedPropertyCodes);
    
    return klassInstance;
  }

  private void fillRelationship(int revisionNo, IBaseEntityDTO baseEntityDTO,
      IGetKlassInstanceVersionsForComparisonModel returnModel,
      IGetConfigDetailsForCustomTabModel configDetails, Set<String> propertyCodesToFetch) throws Exception
  {
    ConvertRelationSetToRelationInstance setToRelationInstance = new ConvertRelationSetToRelationInstance(baseEntityDTO, configDetails, rdbmsComponentUtils, propertyCodesToFetch);  
    Map<Long, IContentRelationshipInformationModel> contentRelationships = returnModel.getContentRelationships();
    IContentRelationshipInformationModel contentRelationshipInformationModel  = new ContentRelationshipsInformationModel();
    contentRelationshipInformationModel.setRelationships(setToRelationInstance.getContentRelationships());
    contentRelationships.put((long)revisionNo, contentRelationshipInformationModel);

      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElements = returnModel.getReferenceRelationshipInstanceElements();
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElementsForVersion = setToRelationInstance.getReferenceRelationshipInstanceElements();
      for (String relationID : referenceRelationshipInstanceElementsForVersion.keySet()) {
        List<IKlassInstanceInformationModel> list = referenceRelationshipInstanceElements.get(relationID);
        if (list == null) {
          referenceRelationshipInstanceElements.put(relationID, referenceRelationshipInstanceElementsForVersion.get(relationID));
        }
        else {
          list.addAll(referenceRelationshipInstanceElementsForVersion.get(relationID));
        }
      }    
    
    IContentRelationshipInformationModel natureRelationshipInformationModel = new ContentRelationshipsInformationModel();
    natureRelationshipInformationModel.setRelationships(setToRelationInstance.getNatureRelationships());
    Map<Long, IContentRelationshipInformationModel> natureRelationships = returnModel.getNatureRelationships();
    natureRelationships.put((long)revisionNo, natureRelationshipInformationModel);
      Map<String, List<IKlassInstanceInformationModel>> referenceNatureRelationshipInstanceElements = returnModel.getReferenceNatureRelationshipInstanceElements();
      Map<String, List<IKlassInstanceInformationModel>> referenceNatureRelationshipInstanceElementsForVersion = setToRelationInstance.getReferenceNatureRelationshipInstanceElements();
      for (String relationID : referenceNatureRelationshipInstanceElementsForVersion.keySet()) {
        List<IKlassInstanceInformationModel> list = referenceNatureRelationshipInstanceElements.get(relationID);
        if (list == null) {
          referenceNatureRelationshipInstanceElements.put(relationID, referenceNatureRelationshipInstanceElementsForVersion.get(relationID));
        }
        else {
          list.addAll(referenceNatureRelationshipInstanceElementsForVersion.get(relationID));
        }
      }    
    } 
  
  private void fillTags(IBaseEntityDTO baseEntityDTO, ITagsRecordDTO tagsRecordDTO, IKlassInstance klassInstance)
  {
    ITagInstance tagInstance = new TagInstance();
    tagInstance.setId(tagsRecordDTO.getNodeID());
    tagInstance.setKlassInstanceId(baseEntityDTO.getNatureClassifier()
        .getClassifierCode());
    tagInstance.setBaseType(TagInstance.class.getName());
    tagInstance.setTagId(tagsRecordDTO.getProperty()
        .getPropertyCode());
    
    // Tag values
    List<ITagInstanceValue> tagInstanceValues = new ArrayList<ITagInstanceValue>();
    tagsRecordDTO.getTags()
        .forEach(tagRecordDTO -> {
          ITagInstanceValue tagInstanceValue = new TagInstanceValue();
          tagInstanceValue.setTagId(tagRecordDTO.getTagValueCode());
          tagInstanceValue.setId(tagRecordDTO.getTagValueCode());
          tagInstanceValue.setCode(tagRecordDTO.getTagValueCode());
          tagInstanceValue.setRelevance(tagRecordDTO.getRange());
          tagInstanceValues.add(tagInstanceValue);
        });
    tagInstance.setTagValues(tagInstanceValues);
    ((List<IContentTagInstance>) klassInstance.getTags()).add(tagInstance);
  }
  

  private void fillAttributesAndBooleanTag(String dataLang, IBaseEntityDTO baseEntityDTO, IValueRecordDTO valueRecord, IKlassInstance klassInstance) throws Exception
  {
    switch(valueRecord.getProperty().getPropertyType()) {
      case BOOLEAN:
        ITagInstance tagInstance = new TagInstance();
        tagInstance.setId(Long.toString(valueRecord.getValueIID()));
        tagInstance.setKlassInstanceId(baseEntityDTO.getNatureClassifier().getClassifierCode());
        tagInstance.setBaseType(TagInstance.class.getName());
        tagInstance.setTagId(valueRecord.getProperty().getPropertyCode());
        
        // Tag values
        List<ITagInstanceValue> tagInstanceValues = new ArrayList<ITagInstanceValue>();
        ITagInstanceValue tagInstanceValue = new TagInstanceValue();
        tagInstanceValue.setId(Long.toString(valueRecord.getValueIID()));
        tagInstanceValue.setCode(Long.toString(valueRecord.getValueIID()));
        
        int relevance = 0;
        if (valueRecord.getValue().equals(IStandardConfig.TRUE) && valueRecord.getAsNumber() == 1) {
          relevance = 100;
        }
        tagInstanceValue.setRelevance(relevance);
        tagInstanceValues.add(tagInstanceValue);
        tagInstance.setTagValues(tagInstanceValues);
        ((List<IContentTagInstance>) klassInstance.getTags()).add(tagInstance);
        
      default:
        // Attribute variant
        if (valueRecord.getContextualObject() != null && valueRecord.getContextualObject()
            .getContextualObjectIID() != 0) {
          IIdAndVersionId attributeContext = new IdAndVersionId();
          attributeContext.setId(KlassInstanceBuilder.getAttributeID(valueRecord));
          if (klassInstance != null && klassInstance instanceof IContentInstance) {
            ((IContentInstance) klassInstance).getAttributeVariants().add(attributeContext);
          }
        }
        // Normal attributes
        else {
          // fill all language in-dependent attributes and dependent attributes with current data language
          if (dataLang.equals(valueRecord.getLocaleID()) || valueRecord.getLocaleID().isEmpty()) {
            IAttributeInstance attributeInstance = new AttributeInstance();
            attributeInstance.setId(KlassInstanceBuilder.getAttributeID(valueRecord));
            attributeInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
            attributeInstance.setBaseType(AttributeInstance.class.getName());
            attributeInstance.setLanguage(valueRecord.getLocaleID());
            attributeInstance.setValue(valueRecord.getValue());
            attributeInstance.setOriginalInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
            attributeInstance.setAttributeId(valueRecord.getProperty().getPropertyCode());
            attributeInstance.setValueAsNumber(valueRecord.getAsNumber());
            attributeInstance.setValueAsHtml(valueRecord.getAsHTML());
            ((List<IContentAttributeInstance>) klassInstance.getAttributes()).add(attributeInstance);
          }
        }
    }
  }
  
  
  private void fillTypesANdTaxonomies(IBaseEntityDTO baseEntityDTO, IContentInstance klassInstance)
  {
    List<String> types = klassInstance.getTypes();
    types.add(baseEntityDTO.getNatureClassifier().getClassifierCode());
    List<String> taxonomyIds = klassInstance.getTaxonomyIds();
    List<String> selectedTaxonomyIds = klassInstance.getSelectedTaxonomyIds();
    for (IClassifierDTO classifierDTO : baseEntityDTO.getOtherClassifiers()) {
      ClassifierType classifierType = classifierDTO.getClassifierType();
      if (classifierType.equals(ClassifierType.CLASS)) {
        types.add(classifierDTO.getClassifierCode());
      }
      else if (classifierType.equals(ClassifierType.TAXONOMY)
          || classifierType.equals(ClassifierType.MINOR_TAXONOMY)) {
        taxonomyIds.add(classifierDTO.getClassifierCode());
        selectedTaxonomyIds.add(classifierDTO.getClassifierCode());
      }
    }
  }
   
   
  
  private void fillTaskInfo(long baseEntityIID,
      IGetKlassInstanceVersionsForComparisonModel returnModel) throws Exception, RDBMSException
  {
    // Task Count
    ITaskRecordDAO taskRecordDAO = rdbmsComponentUtils.openTaskDAO();
    int taskCountOnBaseEntity = taskRecordDAO.getTaskCountOnBaseEntity(baseEntityIID, rdbmsComponentUtils.getUserID());
    returnModel.setTasksCount(taskCountOnBaseEntity);
  }
  
  protected void fillMulticlassificationRequestModel(P instanceRequestModel,
      IKlassInstanceTypeModel klassInstanceTypeModel,
      IMulticlassificationRequestModel multiclassificationRequestModel)
  {
    multiclassificationRequestModel.setTemplateId(instanceRequestModel.getTemplateId());
    multiclassificationRequestModel.getKlassIds()
        .addAll(klassInstanceTypeModel.getTypes());
    multiclassificationRequestModel.getSelectedTaxonomyIds()
        .addAll(klassInstanceTypeModel.getSelectedTaxonomyIds());
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    TransactionData transactionData = controllerThread.getTransactionData();
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setLanguageCodes(klassInstanceTypeModel.getLanguageCodes());
  }
  
}
