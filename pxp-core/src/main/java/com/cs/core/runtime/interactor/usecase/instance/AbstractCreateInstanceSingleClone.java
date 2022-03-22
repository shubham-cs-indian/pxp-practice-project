package com.cs.core.runtime.interactor.usecase.instance;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
public abstract class AbstractCreateInstanceSingleClone<P extends ICreateKlassInstanceSingleCloneModel, R extends IGetKlassInstanceModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  /*<<<<<<< HEAD
  @Autowired
  protected IGetConfigDetailsForOverviewTabStrategy getConfigDetailsForOverviewTabStrategy;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;
  
  @Autowired
  protected ConfigUtil                              configUtil;
  
  @Autowired
  protected RDBMSComponentUtils                     rdbmsComponentUtils;
  
  @Autowired
  protected IGetAllNatureRelationshipsIdsStrategy getAllNatureRelationshipsIdsStrategy;
  
  @Autowired
  protected IGetGlobalPermissionForMultipleNatureTypesStrategy getGlobalPermissionForMultipleNatureTypesStrategy;
  
  @Autowired
  protected ISessionContext                                 context;
  
  protected abstract Exception getUserNotHaveCreatePermissionException();
  
  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P dataModel) throws Exception
  {
    checkPermissions(dataModel);
    long baseEntityIID = Long.parseLong(dataModel.getKlassInstanceIdToClone());
    GetKlassInstanceForCustomTabModel responseModel = new GetKlassInstanceForCustomTabModel();
    Collection<IPropertyDTO> propertiesToClone = dataModel.getShouldCloneAllProperties()
        ? rdbmsComponentUtils.getLocaleCatlogDAO().getAllEntityProperties(baseEntityIID) : getPropertiesToClone(dataModel);
            
     if (dataModel.getShouldCloneAllProperties()) {  
    
      IIdsListParameterModel natureRelationshipIdsList = getAllNatureRelationshipsIdsStrategy.execute(new VoidModel());
     // propertiesToClone = removeNatureRelationships(propertiesToClone, natureRelationshipIdsList.getIds());
    }
    
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    IBaseEntityDTO cloneEntityByIID = createClone(baseEntityIID, propertiesToClone, localeCatalogDAO, dataModel);
      
    IBaseEntityDAO baseEntityDAOByBaseEntityDTO = rdbmsComponentUtils.getBaseEntityDAO(cloneEntityByIID.getBaseEntityIID());
    IMulticlassificationRequestModel multiClassificationModel = this.configUtil.getConfigRequestModelForCreateInstanceForSingleClone(baseEntityDAOByBaseEntityDTO);
    IGetConfigDetailsForCustomTabModel configDetailForCloneArticle = getConfigDetailsForOverviewTabStrategy.execute(multiClassificationModel);
    prepareResponse(responseModel, baseEntityDAOByBaseEntityDTO, configDetailForCloneArticle);
    return (R) responseModel;
    
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
    } else {
      classifierCodes.add((String) baseEntityDAO.getBaseEntityDTO().getNatureClassifier().getClassifierCode());
    }
    requestModel.setKlassIds(classifierCodes);
    IGetGlobalPermissionForMultipleNatureTypesResponseModel permissionResponseModel = getGlobalPermissionForMultipleNatureTypesStrategy.execute(requestModel);
    IFunctionPermissionModel functionPermissionMap = permissionResponseModel.getFunctionPermission();
    if(!functionPermissionMap.getCanClone()) {
      throw new UserNotHaveClonePermission();
    }
    Map<String, IGlobalPermission> globalPermissionMap = permissionResponseModel.getGlobalPermission();
    Entry<String, IGlobalPermission> permission = globalPermissionMap.entrySet().iterator().next();
    if (!permission.getValue().getCanCreate()) {
      throw getUserNotHaveCreatePermissionException();
    }
  }
  
  private List<IConflictingPropertyDTO> fillConflictingValuesForProperties(
      IGetConfigDetailsForCustomTabModel configDetailsForClone, IBaseEntityDAO baseEntityDAO,
      Collection<IPropertyDTO> propertiesToClone)
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
      if(refElement == null){
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
        if(property.getPropertyType().equals(IPropertyDTO.SuperType.ATTRIBUTE)){
          ReferencedSectionAttributeModel attributeElement = (ReferencedSectionAttributeModel) refElement;
          conflict.setValue(attributeElement.getDefaultValue());
        }
        else if(property.getPropertyType().equals(IPropertyDTO.SuperType.TAGS)){
          ReferencedSectionTagModel tagElement = (ReferencedSectionTagModel) refElement;
          List<ITagDTO> tags = tagElement.getDefaultValue()
              .stream()
              .map(x -> new TagDTO(x.getCode(), x.getRelevance()))
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
    else if
      (conflict.getType().equals(CommonConstants.TAG)) {
    couplingType = ((TagConflictingValuesModel) conflict).getCouplingType();
  }
    switch (couplingType) {
      case CommonConstants.TIGHTLY_COUPLED:
        switch (conflict.getSourceType()) {
          case CommonConstants.RELATIONSHIP_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.TIGHT_RELATIONSHIP;
          case CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE:
          case CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.TIGHT_DEFAULT;
          case CommonConstants.CONTEXT_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.TIGHT_INHERITANCE;
        }
      case CommonConstants.DYNAMIC_COUPLED:
        switch (conflict.getSourceType()) {
          case CommonConstants.RELATIONSHIP_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.DYN_RELATIONSHIP;
          case CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE:
          case CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE:
            return ICSECoupling.CouplingType.DYN_DEFAULT;
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
  
    IMulticlassificationRequestModel configDetailsRequestModel = configUtil.getConfigRequestModelForGivenTypesTaxonomies(
        typesToClone, taxonomiesToClone);
  
    return getConfigDetailsWithoutPermissionsStrategy.execute(configDetailsRequestModel);
  }
  
  protected Set<IClassifierDTO> fillClassifierData(P dataModel)
      throws RDBMSException
  {
    List<String> types = new ArrayList<String>();
    Set<IClassifierDTO> classifiersToBeRemoved = new HashSet<IClassifierDTO>();
    if(!dataModel.getShouldCloneAllProperties()) {
      types = dataModel.getCloneData().getTypes();
      types.addAll(dataModel.getCloneData().getTaxonomies());
      for(String type: types) {
        classifiersToBeRemoved.add(ConfigurationDAO.instance().getClassifierByCode(type));
      }
    }
    return classifiersToBeRemoved;
  }
  
  protected IBaseEntityDTO createClone(long baseEntityIID, Collection<IPropertyDTO> propertiesToClone,
      ILocaleCatalogDAO localeCatlogDAO, P dataModel)
      throws Exception
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
    IGetConfigDetailsForCustomTabModel configDetailsForClone = getConfigDetailsForClone(dataModel, baseEntityDAO);
    List<IConflictingPropertyDTO> conflictingProperties = fillConflictingValuesForProperties(configDetailsForClone, baseEntityDAO,propertiesToClone);
  
    Set<IClassifierDTO> classifiersToBeRemoved = fillClassifierData(dataModel);
    return localeCatlogDAO.cloneEntityByIID( baseEntityIID, classifiersToBeRemoved, conflictingProperties.toArray(new IConflictingPropertyDTO[0]));
  }
  
  private Collection<IPropertyDTO> getPropertiesToClone(P dataModel) throws Exception
  {
    IDataForInstanceCloneModel cloneData = dataModel.getCloneData();
    Collection<IPropertyDTO> propertiesToClone = new ArrayList<>();
    
    for(String attribute : cloneData.getAttributes()) {
      propertiesToClone.add(RDBMSUtils.getPropertyByCode(attribute));
    }
    
    for(String tag : cloneData.getTags()) {
      propertiesToClone.add(RDBMSUtils.getPropertyByCode(tag));
    }
    
    for(String relationship: cloneData.getRelationships()) {
      IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByCode(relationship);
      propertyDTO.setRelationSide(RelationSide.SIDE_1);
      propertiesToClone.add(propertyDTO);
    }

    IPropertyDTO nameAttribute = RDBMSUtils.getPropertyByCode(CommonConstants.NAME_ATTRIBUTE);
    if(!propertiesToClone.contains(nameAttribute)) {
      propertiesToClone.add(nameAttribute);
    }
    propertiesToClone.add(RDBMSUtils.getPropertyByCode(Constants.LISTING_STATUS_TAG));
    propertiesToClone.add(RDBMSUtils.getPropertyByCode(Constants.LIFE_SATUS_TAG));
    return propertiesToClone;
  }
  =======
  >>>>>>> dev/20.6*/
  
}
