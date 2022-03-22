package com.cs.core.runtime.klassinstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.Constants;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.duplicatecode.AttributeConflictingValuesModel;
import com.cs.core.config.interactor.model.duplicatecode.TagConflictingValuesModel;
import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionForMultipleNatureTypesRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesResponseModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionTagModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInstanceModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetAllNatureRelationshipsIdsStrategy;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.ConflictingPropertyDTO;
import com.cs.core.rdbms.config.dto.RecordConflictDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IConflictingPropertyDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.coupling.dto.DataTransferForClonedEntitiesDTO;
import com.cs.core.rdbms.coupling.idto.IDataTransferForClonedEntitiesDTO;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.builder.BuilderFactory;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveClonePermission;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.clone.IDataForInstanceCloneModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForOverviewTabStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.utils.BaseEntityUtils;

@Component
public abstract class AbstractCreateInstanceSingleClone<P extends ICreateKlassInstanceSingleCloneModel, R extends IGetKlassInstanceModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected IGetConfigDetailsForOverviewTabStrategy            getConfigDetailsForOverviewTabStrategy;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy        getConfigDetailsWithoutPermissionsStrategy;
  
  @Autowired
  protected ConfigUtil                                         configUtil;
  
  @Autowired
  protected RDBMSComponentUtils                                rdbmsComponentUtils;
  
  @Autowired
  protected IGetAllNatureRelationshipsIdsStrategy              getAllNatureRelationshipsIdsStrategy;
  
  @Autowired
  protected IGetGlobalPermissionForMultipleNatureTypesStrategy getGlobalPermissionForMultipleNatureTypesStrategy;
  
  @Autowired
  protected ISessionContext                                    context;
  
  @Autowired
  protected PermissionUtils                                    permissionUtils;
  
  @Autowired
  protected GoldenRecordUtils                                  goldenRecordUtils;
  
  private static final String                                  SERVICE = "HANDLE_DATA_TRANSFER_FOR_CLONED_ENTITIES";
  
  protected abstract Exception getUserNotHaveCreatePermissionException();
  
  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P dataModel) throws Exception
  {
    checkPermissions(dataModel);
    long baseEntityIID = Long.parseLong(dataModel.getKlassInstanceIdToClone());
    GetKlassInstanceForCustomTabModel responseModel = new GetKlassInstanceForCustomTabModel();
    Collection<IPropertyDTO> propertiesToClone = dataModel.getShouldCloneAllProperties()
        ? rdbmsComponentUtils.getLocaleCatlogDAO().getAllEntityProperties(baseEntityIID)
        : getPropertiesToClone(dataModel);
    
    if (dataModel.getShouldCloneAllProperties()) {
      
      IIdsListParameterModel natureRelationshipIdsList = getAllNatureRelationshipsIdsStrategy.execute(new VoidModel());
      propertiesToClone = removeNatureRelationships(propertiesToClone, natureRelationshipIdsList.getIds());
    }
    
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    IBaseEntityDTO cloneEntityByIID = createClone(baseEntityIID, propertiesToClone, localeCatalogDAO, dataModel);
    IBaseEntityDAO baseEntityDAOByBaseEntityDTO = rdbmsComponentUtils.getBaseEntityDAO(cloneEntityByIID.getBaseEntityIID());
    IMulticlassificationRequestModel multiClassificationModel = this.configUtil
        .getConfigRequestModelForCreateInstanceForSingleClone(baseEntityDAOByBaseEntityDTO);
    IGetConfigDetailsForCustomTabModel configDetailForCloneArticle = getConfigDetailsForOverviewTabStrategy
        .execute(multiClassificationModel);

    // BGP call to handle coupling for all cloned Entities (variants also)
    List<Long> allClonedEntitiesIIDs = localeCatalogDAO.getAllChildrenIIDs(cloneEntityByIID.getBaseEntityIID());
    allClonedEntitiesIIDs.add(cloneEntityByIID.getBaseEntityIID());
    if(!allClonedEntitiesIIDs.isEmpty()) {
      prepareDataTransferDTOForClonedEntities(localeCatalogDAO, allClonedEntitiesIIDs);
    }
    
    prepareResponse(responseModel, baseEntityDAOByBaseEntityDTO, configDetailForCloneArticle);
    rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(cloneEntityByIID.getBaseEntityIID(), EventType.ELASTIC_UPDATE);
   
    goldenRecordUtils.initiateEvaluateGoldenRecordBucket(cloneEntityByIID);
    
    return (R) responseModel;
    
  }
  
  public void prepareDataTransferDTOForClonedEntities(ILocaleCatalogDAO localeCatlogDAO, List<Long> allClonedEntitiesIIDs) throws Exception
  {
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    
    IDataTransferForClonedEntitiesDTO dataTransferForClonedEntitiesDTO = new DataTransferForClonedEntitiesDTO();
    ILocaleCatalogDTO localeCatalogDTO = localeCatlogDAO.getLocaleCatalogDTO();
    dataTransferForClonedEntitiesDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    dataTransferForClonedEntitiesDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    dataTransferForClonedEntitiesDTO.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    dataTransferForClonedEntitiesDTO.setUserId(context.getUserId());
    dataTransferForClonedEntitiesDTO.setClonedBaseEntityIIDs(allClonedEntitiesIIDs);
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE, "", userPriority,
        new JSONContent(dataTransferForClonedEntitiesDTO.toJSON()));
  }
  
  
  private void checkPermissions(P dataModel) throws Exception
  {
    long baseEntityIID = Long.parseLong(dataModel.getKlassInstanceIdToClone());
    IGetGlobalPermissionForMultipleNatureTypesRequestModel requestModel = new GetGlobalPermissionForMultipleNatureTypesRequestModel();
    requestModel.setUserId(context.getUserId());
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
    Set<String> classifierCodes = new HashSet<String>();
    if (!dataModel.getShouldCloneAllProperties()) {
      classifierCodes.addAll(dataModel.getCloneData().getTypes());
    }
    else {
      classifierCodes.add((String) baseEntityDAO.getBaseEntityDTO().getNatureClassifier().getClassifierCode());
    }
    requestModel.setKlassIds(classifierCodes);
    IGetGlobalPermissionForMultipleNatureTypesResponseModel permissionResponseModel = getGlobalPermissionForMultipleNatureTypesStrategy
        .execute(requestModel);
    IFunctionPermissionModel functionPermissionMap = permissionResponseModel.getFunctionPermission();
    if (shouldCheckClonePermission() && !functionPermissionMap.getCanClone()) {
      throw new UserNotHaveClonePermission();
    }
    Map<String, IGlobalPermission> globalPermissionMap = permissionResponseModel.getGlobalPermission();
    Entry<String, IGlobalPermission> permission = globalPermissionMap.entrySet().iterator().next();
    if (!permission.getValue().getCanCreate()) {
      throw getUserNotHaveCreatePermissionException();
    }
  }
  
  private List<IConflictingPropertyDTO> fillConflictingValuesForProperties(IGetConfigDetailsForCustomTabModel configDetailsForClone,
      IBaseEntityDAO baseEntityDAO, Collection<IPropertyDTO> propertiesToClone)
  {
    List<IConflictingPropertyDTO> properties = new ArrayList<>();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetailsForClone.getReferencedElements();
    for (IPropertyDTO property : propertiesToClone) {
      String propertyCode = property.getCode();
      IConflictingPropertyDTO conflictingProperty = new ConflictingPropertyDTO(property);
      properties.add(conflictingProperty);
      if (property.isStandardTag() || property.isRelational()) {
        continue;
      }
      
      IReferencedSectionElementModel refElement = referencedElements.get(propertyCode);
      if (refElement == null) {
        continue;
      }
      List<IElementConflictingValuesModel> conflicts = refElement.getConflictingSources();
      Integer givenCouplingPrecedence = Integer.MAX_VALUE;
      Map<ICSECoupling.CouplingType, List<String>> tuples = new HashMap<>();
      if (conflicts == null) {
        continue;
      }
      for (IElementConflictingValuesModel conflict : conflicts) {
        ICSECoupling.CouplingType couplingType = getCouplingType(conflict);
        List<String> conflictingProperties = tuples.get(couplingType);
        if (conflictingProperties == null) {
          conflictingProperties = new ArrayList<>();
        }
        conflictingProperties.add(conflict.getId());
        tuples.put(couplingType, conflictingProperties);
        if (givenCouplingPrecedence > couplingType.getPrecedence()) {
          givenCouplingPrecedence = couplingType.getPrecedence();
        }
      }
      ICSECoupling.CouplingType typeByPrecedence = ICSECoupling.CouplingType.getTypeByPrecedence(givenCouplingPrecedence);
      List<String> conflictingSourceCodes = tuples.get(typeByPrecedence);
      for (String code : conflictingSourceCodes) {
        RecordConflictDTO conflict = new RecordConflictDTO(code, typeByPrecedence);
        if (property.getPropertyType().equals(IPropertyDTO.SuperType.ATTRIBUTE)) {
          ReferencedSectionAttributeModel attributeElement = (ReferencedSectionAttributeModel) refElement;
          conflict.setValue(attributeElement.getDefaultValue());
        }
        else if (property.getPropertyType().equals(IPropertyDTO.SuperType.TAGS)) {
          ReferencedSectionTagModel tagElement = (ReferencedSectionTagModel) refElement;
          List<ITagDTO> tags = tagElement.getDefaultValue().stream().map(x -> new TagDTO(x.getCode(), x.getRelevance()))
              .collect(Collectors.toList());
          conflict.setTagValues(tags);
        }
        conflictingProperty.getConflicts().add(conflict);
      }
    }
    return properties;
  }
  
  private ICSECoupling.CouplingType getCouplingType(IElementConflictingValuesModel conflict)
  {
    String couplingType = "";
    
    if (conflict.getType().equals(CommonConstants.ATTRIBUTE)) {
      couplingType = ((AttributeConflictingValuesModel) conflict).getCouplingType();
    }
    else if (conflict.getType().equals(CommonConstants.TAG)) {
      couplingType = ((TagConflictingValuesModel) conflict).getCouplingType();
    }
    switch (couplingType) {
      case CommonConstants.TIGHTLY_COUPLED:
        switch (conflict.getSourceType()) {
          case CommonConstants.RELATIONSHIP_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.TIGHT_RELATIONSHIP;
          case CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE:
          case CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.TIGHT_CLASSIFICATION;
          case CommonConstants.CONTEXT_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.TIGHT_INHERITANCE;
        }
      case CommonConstants.DYNAMIC_COUPLED:
        switch (conflict.getSourceType()) {
          case CommonConstants.RELATIONSHIP_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.DYN_RELATIONSHIP;
          case CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE:
          case CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.DYN_CLASSIFICATION;
          case CommonConstants.CONTEXT_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.DYN_INHERITANCE;
        }
      default:
        return ICSECoupling.CouplingType.UNDEFINED;
    }
  }
  
  private IGetConfigDetailsForCustomTabModel getConfigDetailsForClone(P dataModel, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    List<String> typesToClone = new ArrayList<>();
    List<String> taxonomiesToClone = new ArrayList<>();
    if (dataModel.getShouldCloneAllProperties()) {
      List<IClassifierDTO> classifiers = baseEntityDAO.getClassifiers();
      typesToClone.addAll(BaseEntityUtils.getReferenceTypeFromClassifierDTO(classifiers));
      typesToClone.add(baseEntityDAO.getBaseEntityDTO().getNatureClassifier().getClassifierCode());
      taxonomiesToClone.addAll(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(classifiers));
    }
    else {
      typesToClone.addAll(dataModel.getCloneData().getTypes());
      taxonomiesToClone.addAll(dataModel.getCloneData().getTaxonomies());
    }
    
    IMulticlassificationRequestModel configDetailsRequestModel = configUtil.getConfigRequestModelForGivenTypesTaxonomies(typesToClone,
        taxonomiesToClone);
    
    return getConfigDetailsWithoutPermissionsStrategy.execute(configDetailsRequestModel);
  }
  
  protected Set<IClassifierDTO> fillClassifierData(P dataModel) throws RDBMSException
  {
    List<String> types = new ArrayList<String>();
    Set<IClassifierDTO> classifiersToBeRemoved = new HashSet<IClassifierDTO>();
    if (!dataModel.getShouldCloneAllProperties()) {
      types = dataModel.getCloneData().getTypes();
      types.addAll(dataModel.getCloneData().getTaxonomies());
      for (String type : types) {
        classifiersToBeRemoved.add(ConfigurationDAO.instance().getClassifierByCode(type));
      }
    }
    return classifiersToBeRemoved;
  }
  
  protected IBaseEntityDTO createClone(long baseEntityIID, Collection<IPropertyDTO> propertiesToClone, ILocaleCatalogDAO localeCatlogDAO,
      P dataModel) throws Exception
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
    IGetConfigDetailsForCustomTabModel configDetailsForClone = getConfigDetailsForClone(dataModel, baseEntityDAO);
    List<IConflictingPropertyDTO> conflictingProperties = fillConflictingValuesForProperties(configDetailsForClone, baseEntityDAO,
        propertiesToClone);
    
    Set<IClassifierDTO> classifiersToBeRemoved = fillClassifierData(dataModel);
    IBaseEntityDTO cloneEntityByIID = localeCatlogDAO.cloneEntityByIID(baseEntityIID, classifiersToBeRemoved,
        conflictingProperties.toArray(new IConflictingPropertyDTO[0]));

    // load propertyRecords so that the pxon(created on revision creation) will have information related to properties.
    Set<IPropertyDTO> properties = (Set<IPropertyDTO>) rdbmsComponentUtils.getLocaleCatlogDAO().getAllEntityProperties(cloneEntityByIID.getBaseEntityIID());
    IBaseEntityDAO cloneDAO = rdbmsComponentUtils.getBaseEntityDAO(cloneEntityByIID.getBaseEntityIID());
    cloneEntityByIID = cloneDAO.loadPropertyRecords(properties.toArray(new IPropertyDTO[0]));
    KlassInstanceUtils.handleDefaultImage(cloneDAO);
    rdbmsComponentUtils.createNewRevision(cloneEntityByIID, configDetailsForClone.getNumberOfVersionsToMaintain());

    RuleHandler ruleHandler = new RuleHandler();
    ruleHandler.initiateRuleHandling(rdbmsComponentUtils.getBaseEntityDAO(cloneEntityByIID), rdbmsComponentUtils.getLocaleCatlogDAO(),
        false, configDetailsForClone.getReferencedElements(), configDetailsForClone.getReferencedAttributes(),
        configDetailsForClone.getReferencedTags());
    return cloneEntityByIID;
  }
  
  private Collection<IPropertyDTO> getPropertiesToClone(P dataModel) throws Exception
  {
    IDataForInstanceCloneModel cloneData = dataModel.getCloneData();
    Collection<IPropertyDTO> propertiesToClone = new ArrayList<>();
    
    for (String attribute : cloneData.getAttributes()) {
      propertiesToClone.add(RDBMSUtils.getPropertyByCode(attribute));
    }
    
    for (String tag : cloneData.getTags()) {
      propertiesToClone.add(RDBMSUtils.getPropertyByCode(tag));
    }
    
    for (String relationship : cloneData.getRelationships()) {
      IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByCode(relationship);
      propertyDTO.setRelationSide(RelationSide.SIDE_1);
      propertiesToClone.add(propertyDTO);
    }
    
    IPropertyDTO nameAttribute = RDBMSUtils.getPropertyByCode(CommonConstants.NAME_ATTRIBUTE);
    if (!propertiesToClone.contains(nameAttribute)) {
      propertiesToClone.add(nameAttribute);
    }
    propertiesToClone.add(RDBMSUtils.getPropertyByCode(Constants.LISTING_STATUS_TAG));
    propertiesToClone.add(RDBMSUtils.getPropertyByCode(Constants.LIFE_SATUS_TAG));
    return propertiesToClone;
  }
  
  private IGetKlassInstanceModel prepareResponse(GetKlassInstanceForCustomTabModel responseModel, IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, rdbmsComponentUtils);
    
    responseModel.setKlassInstance((IContentInstance) klassInstanceBuilder.getKlassInstance());
    responseModel.setConfigDetails(configDetails);
    
    responseModel.setBranchOfLabel(KlassInstanceUtils.getBranchOfLabel(baseEntityDAO.getBaseEntityDTO(), rdbmsComponentUtils));
    
    if (configDetails.getReferencedNatureRelationships() != null && configDetails.getReferencedNatureRelationships().size() > 0) {
      IRelationshipInstanceModel relationshipInstanceModel = BuilderFactory
          .newRelationshipInstanceBuilder(rdbmsComponentUtils, configDetails).baseEntityDAO(baseEntityDAO).build();
      
      responseModel.setNatureRelationships(relationshipInstanceModel.getNatureRelationships());
      responseModel
          .setReferenceNatureRelationshipInstanceElements(relationshipInstanceModel.getReferenceNatureRelationshipInstanceElements());
    }
    permissionUtils.manageKlassInstancePermissions(responseModel);
    return responseModel;
  }
  
  private Collection<IPropertyDTO> removeNatureRelationships(Collection<IPropertyDTO> propertiesToClone, List<String> natureRelationshipIds)
  {
    Collection<IPropertyDTO> updatedProperties = new ArrayList<>();
    IPropertyDTO[] properties = propertiesToClone.toArray(new IPropertyDTO[0]);
    for (IPropertyDTO property : properties) {
      if (!natureRelationshipIds.contains(property.getCode())) {
        
        updatedProperties.add(property);
      }
    }
    
    return updatedProperties;
  }
  
  protected Boolean shouldCheckClonePermission()
  {
    return true;
  }
  
}
