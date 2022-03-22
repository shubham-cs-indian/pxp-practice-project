package com.cs.core.runtime.interactor.utils.klassinstance;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.dto.EntityRelationshipInfoDTO;
import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.RelationsSetDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.relationship.dto.RelationCoupleRecordDTO.RelationCoupleRecordDTOBuilder;
import com.cs.core.rdbms.relationship.idao.IRelationCoupleRecordDAO;
import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTO;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.exception.configuration.CardinalityException;
import com.cs.core.runtime.interactor.exception.relationshipinstance.InstanceAlreadyPresentInAnotherLinkedVariantRelationshipException;
import com.cs.core.runtime.interactor.exception.relationshipinstance.InvalidEntityUsedForRelationshipException;
import com.cs.core.runtime.interactor.exception.variants.DuplicateVariantExistsException;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipDataTransferInfoModel;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class RelationshipRecordBuilder {
  
  private final IBaseEntityDAO                                      entityDAO;
  private final Map<String, IReferencedRelationshipModel>           referencedRelationship;
  private final Map<String, IGetReferencedNatureRelationshipModel>  referencedNatureRelationships;
  private final Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties;
  private final Map<String, IReferencedSectionElementModel>         referencedElements;
  private final Map<String, IReferencedVariantContextModel>         relationshipVariantContexts;
  private final Map<String, IReferencedVariantContextModel>         productVariantContexts;
  private final Map<String, ITag>                                   referencedTags;
  private List<Long>                                                entitiesWithoutDefaultImage = new ArrayList<>();
  private final RDBMSComponentUtils                                 rdbmsComponentUtils;
  private List<Long>                                                linkedVariantPropertyIids; 
  
  public RelationshipRecordBuilder(IBaseEntityDAO entityDAO,
      IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails,
      RDBMSComponentUtils rdbmsComponentUtils)
  {
    this.entityDAO = entityDAO;
    this.referencedRelationship = configDetails.getReferencedRelationships();
    this.referencedNatureRelationships = configDetails.getReferencedNatureRelationships();
    this.referencedRelationshipProperties = configDetails.getReferencedRelationshipProperties();
    this.referencedElements = configDetails.getReferencedElements();
    this.relationshipVariantContexts = configDetails.getReferencedVariantContexts()
        .getRelationshipVariantContexts();
    this.productVariantContexts = configDetails.getReferencedVariantContexts()
        .getProductVariantContexts();
    this.referencedTags = configDetails.getReferencedTags();
    this.rdbmsComponentUtils = rdbmsComponentUtils;
    this.linkedVariantPropertyIids = configDetails.getLinkedVariantPropertyIids();
  }
  
  public List<Long> getEntitiesWithoutDefaultImage()
  {
    return entitiesWithoutDefaultImage;
  }
  
  private IPropertyDTO newRelationshipProperty(IRelationship relationshipConfig,
      IPropertyDTO.PropertyType propertyType, RelationSide relationSide) throws Exception
  {
    IPropertyDTO propertyDTO = this.entityDAO.newPropertyDTO(
        relationshipConfig.getPropertyIID(), relationshipConfig.getCode(), propertyType);
    propertyDTO.setRelationSide(relationSide);
    return propertyDTO;
  }
  
  private IRelationsSetDTO newEntityRelationsSetDTO(IRelationship relationshipConfig,
      IPropertyDTO.PropertyType propertyType, RelationSide relationSide) throws Exception
  {
    IPropertyDTO propertyDTO = this.newRelationshipProperty(relationshipConfig, propertyType,
        relationSide);
    IRelationsSetDTO relationsSetDTO = this.entityDAO
        .newEntityRelationsSetDTOBuilder(propertyDTO, relationSide)
        .build();
    return relationsSetDTO;
  }
  
  public IPropertyRecordDTO updateRelationshipRecord(
      IContentRelationshipInstanceModel modifiedRelationships,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel) throws Exception
  {
    String relationshipId = modifiedRelationships.getRelationshipId();
    IReferencedRelationshipModel relationshipConfig = this.referencedRelationship
        .get(relationshipId);
    if (relationshipConfig == null) {
      relationshipConfig = this.referencedNatureRelationships.get(relationshipId);
    }
    IReferencedRelationshipPropertiesModel relationshipProperty = this.referencedRelationshipProperties
        .get(relationshipId);
    IReferencedSectionElementModel relationshipElement = this.referencedElements
        .get(modifiedRelationships.getSideId());
    RelationSide relationSide = RelationshipRecordBuilder
               .getRelationshipSide(modifiedRelationships.getSideId(), relationshipConfig);

    List<String> sourceKlassIds = relationSide.equals(RelationSide.SIDE_1) ? relationshipProperty.getSide1().getKlassIds()
        : relationshipProperty.getSide2().getKlassIds();
    
    if (modifiedRelationships.getAddedElements() != null
        && !modifiedRelationships.getAddedElements().isEmpty()) {
      Boolean isSourceClassPresent = validateRelationSide(this.entityDAO, sourceKlassIds);
      if(!isSourceClassPresent) {
        throw new InvalidEntityUsedForRelationshipException();
      }
    }
    
    return this.buildRelationshipRecord(relationshipConfig, relationshipProperty,
        relationshipElement, modifiedRelationships, relationSide,relationshipDataTransferModel);
  }
  
  public static RelationSide getRelationshipSide(String sideId, IRelationship relationshipConfig)
  {
    IRelationshipSide side1 = relationshipConfig.getSide1();
    IRelationshipSide side2 = relationshipConfig.getSide2();
    if (side1.getElementId()
        .equals(sideId)) {
      return RelationSide.SIDE_1;
    }
    else if (side2.getElementId()
        .equals(sideId)) {
      return RelationSide.SIDE_2;
    }
    return RelationSide.UNDEFINED;
  }
  
  private IPropertyRecordDTO buildRelationshipRecord(
      IReferencedRelationshipModel relationshipConfig,
      IReferencedRelationshipPropertiesModel relationshipProperty,
      IReferencedSectionElementModel relationshipElement,
      IContentRelationshipInstanceModel modifiedRelationships, RelationSide relationSide,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel)
      throws Exception
  {
    PropertyType propertyType = relationshipConfig.getIsNature() ? PropertyType.NATURE_RELATIONSHIP :  PropertyType.RELATIONSHIP;
    IPropertyRecordDTO propertyRecordDTO = null;
    propertyRecordDTO = updateRelationshipRecord(relationshipConfig, relationshipProperty,
        relationshipElement, modifiedRelationships, propertyType, relationSide, this.linkedVariantPropertyIids, relationshipDataTransferModel);
    return propertyRecordDTO;
  }
  
  private IRelationsSetDTO updateRelationshipRecord(IReferencedRelationshipModel relationshipConfig,
      IReferencedRelationshipPropertiesModel relationshipProperty,
      IReferencedSectionElementModel relationshipElement,
      IContentRelationshipInstanceModel modifiedRelationships, PropertyType propertyType,
      RelationSide relationSide, List<Long> linkedVariantPropertyIids,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel)
      throws Exception
  {
    IRelationsSetDTO relationsSetDTO = this.loadRelationsSetDTO(relationshipConfig, propertyType,
        relationSide);
    Set<Long> updatedEntityElements = new TreeSet<>(
        Arrays.asList(relationsSetDTO.getReferencedBaseEntityIIDs()));
    if (relationsSetDTO != null) {
      relationsSetDTO = this.deletedElement(relationsSetDTO, relationshipConfig,
          relationshipProperty, relationshipElement, modifiedRelationships.getDeletedElements(),
          updatedEntityElements, modifiedRelationships.getBaseType(), relationshipDataTransferModel);
      relationsSetDTO = this.addedElement(relationsSetDTO, relationshipConfig, relationshipProperty,
          relationshipElement, modifiedRelationships.getAddedElements(), updatedEntityElements, modifiedRelationships.getBaseType(),
          linkedVariantPropertyIids, relationshipDataTransferModel);
      relationsSetDTO = this.modifiedElement(relationsSetDTO, relationshipConfig,
          relationshipProperty, relationshipElement, modifiedRelationships.getModifiedElements(),
          updatedEntityElements,relationshipDataTransferModel);

      IRelationCoupleRecordDAO relationCoupleRecordDao = rdbmsComponentUtils.getLocaleCatlogDAO().openRelationCoupleRecordDAO();
      IPropertyDTO relationProperty = ConfigurationDAO.instance().getPropertyByCode(relationsSetDTO.getProperty().getPropertyCode());

      clearConflictIfExistOnTargetEntity(relationsSetDTO.getEntityIID(), relationCoupleRecordDao, relationProperty);
      for(IEntityRelationDTO relation : relationsSetDTO.getRelations()) {
        clearConflictIfExistOnTargetEntity(relation.getOtherSideEntityIID(), relationCoupleRecordDao, relationProperty);
      }
    }
    return relationsSetDTO;
  }


  private void clearConflictIfExistOnTargetEntity(long entityIID, IRelationCoupleRecordDAO relationCoupleRecordDao,
      IPropertyDTO relationProperty) throws Exception, RDBMSException
  {
    if (isConflictExistOnTargetEntity(entityIID, relationProperty, relationCoupleRecordDao)) {
      StringBuilder filterQuery = new StringBuilder();
      IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().targetEntityId(entityIID)
          .propagableRelationshipId(relationProperty.getPropertyIID()).build();
      filterQuery = relationCoupleRecordDao.getFilterQuery(relationCoupleRecord);
      relationCoupleRecordDao.deleteRelationCoupleRecord(filterQuery);
    }
  }

  private IRelationsSetDTO addedElement(IRelationsSetDTO relationsSetDTO,
      IReferencedRelationshipModel relationshipConfig,
      IReferencedRelationshipPropertiesModel relationshipProperty,
      IReferencedSectionElementModel relationshipElement, List<IRelationshipVersion> addedElements,
      Set<Long> updatedEntityElements, String baseType,
      List<Long> linkedVariantPropertyIids,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel)
      throws Exception
  {
    if (addedElements == null || addedElements.isEmpty())
      return relationsSetDTO;
    
    if(SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID.equals(relationshipConfig.getCode())) {
      relationsSetDTO.getRelations().clear();
    }
    else {
      if (checkCardinality(addedElements.size(), updatedEntityElements.size(), relationshipConfig, relationshipElement)) {
        return relationsSetDTO;
      }
    }
    
    String side = ((ReferencedSectionRelationshipModel) relationshipElement).getSide();
    Boolean shouldCheckTagetCardinality = shouldEvaluateTargetCardinality(side, relationshipConfig);
    String otherSide = side.equals(IRelationship.SIDE1) ? IRelationship.SIDE2 : IRelationship.SIDE1;
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    List<String> allowedKlassIds = side.equals(IRelationship.SIDE1) ? relationshipProperty.getSide2().getKlassIds()
        : relationshipProperty.getSide1().getKlassIds();

    for (IRelationshipVersion addedElement : addedElements) {
      {
        try { 
          Long otherSideContentId = Long.parseLong(addedElement.getId());
          IBaseEntityDAO baseEntityDAOOfArticle = rdbmsComponentUtils.getBaseEntityDAO(otherSideContentId);
          Boolean isPresent = validateRelationSide(baseEntityDAOOfArticle, allowedKlassIds);
          // check if entity is of valid type
          if (!isPresent) {
            throw new InvalidEntityUsedForRelationshipException();
          }

          if (shouldCheckTagetCardinality) {
            evaluateTargetCardinality(otherSide, relationshipConfig.getPropertyIID(), addedElement.getId(), localeCatlogDAO);
          }
          //check if entity is linked to some Linked Variant
          if(relationshipConfig.getIsNature() && !linkedVariantPropertyIids.isEmpty()) {
            checkForIfExistingLinkedVariant(addedElement.getId());
          }
          IEntityRelationDTO entityRelationDTO = this.createRelationshipContext(addedElement, relationsSetDTO, relationshipConfig,
              relationshipProperty, relationshipElement, baseEntityDAOOfArticle.getBaseEntityDTO().getBaseEntityID());
          if (entityRelationDTO == null) {
            entityRelationDTO = RDBMSUtils.getEntityRelationDTO(baseEntityDAOOfArticle.getBaseEntityDTO().getBaseEntityID(), otherSideContentId);
          }
          
          IReferencedVariantContextModel productVariantContext = this.productVariantContexts.get(addedElement.getContextId());
          if(productVariantContext != null && !productVariantContext.getIsDuplicateVariantAllowed()) {
            validateLinkedVariantContext(entityRelationDTO.getContextualObject(), relationsSetDTO.getEntityIID(), relationshipConfig.getPropertyIID());
          }
          
          handleDefaultImage(baseEntityDAOOfArticle);
          
          relationsSetDTO.setChanged(true);
          relationsSetDTO.getRelations().add(entityRelationDTO);
          fillDataForRelationshipInheritanceForAddedAndModifiedElements(relationshipDataTransferModel, relationsSetDTO, addedElement, relationshipConfig.getIsNature());
        }
        catch (InstanceAlreadyPresentInAnotherLinkedVariantRelationshipException e) {
          throw new InstanceAlreadyPresentInAnotherLinkedVariantRelationshipException();
        }
        catch (InvalidEntityUsedForRelationshipException e) {
          throw new InvalidEntityUsedForRelationshipException();
        }
        catch(DuplicateVariantExistsException e) {
          throw new DuplicateVariantExistsException();
        }
      }
    }
    return relationsSetDTO;
  }

  private void handleDefaultImage(IBaseEntityDAO targetEntityDAO)
      throws RDBMSException
  {
    IBaseEntityDTO sourceEntityDTO = entityDAO.getBaseEntityDTO();
    IBaseEntityDTO targetEntityDTO = targetEntityDAO.getBaseEntityDTO();
    
    if (sourceEntityDTO.getBaseType().equals(BaseType.ARTICLE) &&
        targetEntityDTO.getBaseType().equals(BaseType.ASSET)) {
      
      if (sourceEntityDTO.getDefaultImageIID() == 0l && !StringUtils.isEmpty(targetEntityDTO.getHashCode())) {
        sourceEntityDTO.setDefaultImageIID(targetEntityDTO.getBaseEntityIID());
      }
    }
    else if (sourceEntityDTO.getBaseType().equals(BaseType.ASSET) &&
        targetEntityDTO.getBaseType().equals(BaseType.ARTICLE)) {
        
      if(targetEntityDTO.getDefaultImageIID() == 0l && !StringUtils.isEmpty(sourceEntityDTO.getHashCode())) {
        targetEntityDTO.setDefaultImageIID(Long.valueOf(sourceEntityDTO.getBaseEntityIID()));
        targetEntityDAO.updatePropertyRecords();
      }
    }
  }

  private void fillDataForRelationshipInheritanceForAddedAndModifiedElements(List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel,
      IRelationsSetDTO relationsSetDTO, IRelationshipVersion addedElement, Boolean isNature)
  {
    IRelationshipDataTransferInfoModel dataTransferModel = new RelationshipDataTransferInfoModel();
    dataTransferModel.setContentId(relationsSetDTO.getEntityIID());
    if (isNature) {
      dataTransferModel.getChangedNatureRelationshipIds().add(relationsSetDTO.getProperty().getCode());
    }
    else {
      dataTransferModel.getChangedRelationshipIds().add(relationsSetDTO.getProperty().getCode());
    }

    Map<String, IEntityRelationshipInfoDTO> relationshipIdAddedDeletedElementsMap = dataTransferModel
        .getRelationshipIdAddedDeletedElementsMap();
    IEntityRelationshipInfoDTO addedRemovedElements = relationshipIdAddedDeletedElementsMap
        .get(relationsSetDTO.getProperty().getPropertyIID() + "__" + relationsSetDTO.getProperty().getRelationSide());
    if (addedRemovedElements == null) {
      addedRemovedElements = new EntityRelationshipInfoDTO();
      relationshipIdAddedDeletedElementsMap.put(
          relationsSetDTO.getProperty().getPropertyCode() + "__" + relationsSetDTO.getProperty().getRelationSide(), addedRemovedElements);
    }
    addedRemovedElements.getAddedElements().add(Long.parseLong(addedElement.getId()));
    relationshipDataTransferModel.add(dataTransferModel);
  }

  private void evaluateTargetCardinality(String side, Long relationPropertyIId, String sourceInstanceIID, ILocaleCatalogDAO localeCatlogDAO)
      throws RDBMSException, Exception
  {
    List<Long> targetInstanceIIDs = localeCatlogDAO.getOtherSideInstanceIIds(side, sourceInstanceIID, relationPropertyIId);
    
    if (!targetInstanceIIDs.isEmpty()) {
      throw new CardinalityException();
    }
  }
  
  private Boolean shouldEvaluateTargetCardinality(String side, IReferencedRelationshipModel relationshipConfig)
  {
    String targetCardinality = side.equals(IRelationship.SIDE1) ? relationshipConfig.getSide1().getCardinality()
        : relationshipConfig.getSide2().getCardinality();
    return targetCardinality.equals(CommonConstants.CARDINALITY_ONE) ? true : false;
  }
  
  private void checkForIfExistingLinkedVariant(String entityId)
      throws Exception
  {
    ILocaleCatalogDAO localcatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    IConfigurationDAO configurationDao = RDBMSAppDriverManager.getDriver()
        .newConfigurationDAO();
    IBaseEntityDTO enityDTO = localcatalogDao.getEntityByIID(Long.parseLong(entityId));
    IBaseEntityDAO entityDAO = localcatalogDao.openBaseEntity(enityDTO);
    List<IPropertyDTO> propertyDTOS  = new ArrayList<IPropertyDTO>();
    for (Long linkedVariantPropertyIid : linkedVariantPropertyIids) {
      IPropertyDTO propertyDTO = configurationDao.getPropertyByIID(linkedVariantPropertyIid);
      //creating copy, as we have to change relationside(if we change in original copy ,the change will be reflected elsewhere as well)
      IPropertyDTO propDtoCopy = new PropertyDTO(propertyDTO.getPropertyIID(), propertyDTO.getCode(), propertyDTO.getPropertyType());
      propDtoCopy.setRelationSide(RelationSide.SIDE_2);
      propertyDTOS.add(propDtoCopy);
    }
    IBaseEntityDTO relationshipDTO = entityDAO.loadPropertyRecords(propertyDTOS.toArray(new IPropertyDTO[0]));
    Set<IPropertyRecordDTO> propertyRecords = relationshipDTO.getPropertyRecords();
    if (!propertyRecords.isEmpty()) {
      throw new InstanceAlreadyPresentInAnotherLinkedVariantRelationshipException();
    }
  }

  private boolean checkCardinality(int addedElementsSize,
      int updatedEntityElementsSize, IReferencedRelationshipModel relationshipConfig,
      IReferencedSectionElementModel relationshipElement) throws CardinalityException
  {
    String side = ((ReferencedSectionRelationshipModel)relationshipElement).getSide();
    IRelationshipSide relationshipSide = side.equals(IRelationship.SIDE1) ? relationshipConfig.getSide2() : relationshipConfig.getSide1();
    String cardinality = relationshipSide.getCardinality();
    if (cardinality.equals(CommonConstants.CARDINALITY_ONE)) {
      if (updatedEntityElementsSize > 0)
        throw new CardinalityException();
      if (addedElementsSize > 1) {
        throw new CardinalityException();
      }
    }
    return false;
  }
private IRelationsSetDTO modifiedElement(IRelationsSetDTO relationsSetDTO,
      IReferencedRelationshipModel relationshipConfig,
      IReferencedRelationshipPropertiesModel relationshipProperty,
      IReferencedSectionElementModel relationshipElement,
      List<IRelationshipVersion> modifiedElements, Set<Long> updatedEntityElements,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel)
  {
    if (modifiedElements == null || modifiedElements.isEmpty())
      return relationsSetDTO;
    modifiedElements.forEach(modifiedElement -> {
      try {
        IEntityRelationDTO entityRelationDTO = relationsSetDTO
            .getRelationByIID(Long.parseLong(modifiedElement.getId()));
        if(entityRelationDTO != null) {
        	entityRelationDTO = this.modifieRelationshipContext(modifiedElement, entityRelationDTO,
                    relationsSetDTO, relationshipConfig, relationshipProperty, relationshipElement,relationshipDataTransferModel);
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return relationsSetDTO;
  }
  
  private IRelationsSetDTO deletedElement(IRelationsSetDTO relationsSetDTO,
      IReferencedRelationshipModel relationshipConfig,
      IReferencedRelationshipPropertiesModel relationshipProperty,
      IReferencedSectionElementModel relationshipElement, List<String> deletedElements,
      Set<Long> updatedEntityElements, String string,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel) throws Exception
  {
    if (deletedElements == null || deletedElements.isEmpty())
      return relationsSetDTO;
    Set<Long> deletedEntityIID = new TreeSet<>();
    deletedElements.forEach(deletedElement -> {
      try {
        long deletedElementId = Long.parseLong(deletedElement);
        deletedEntityIID.add(deletedElementId);
        updatedEntityElements.remove(deletedElementId);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    
    if (!deletedEntityIID.isEmpty()) {
      fillEntitiesWithoutDefaultImage(deletedEntityIID);
      relationsSetDTO.removeRelations(deletedEntityIID.toArray(new Long[deletedEntityIID.size()]));
      fillDataForRelationshipInheritanceForDeletedElements(relationshipDataTransferModel, relationsSetDTO, deletedEntityIID, relationshipConfig.getIsNature());
    }
    return relationsSetDTO;
  }

  private void fillEntitiesWithoutDefaultImage(Set<Long> deletedEntityIID)
      throws Exception, RDBMSException
  {
    IBaseEntityDTO sourceEntityDTO = entityDAO.getBaseEntityDTO();
    BaseType sourceBaseType = sourceEntityDTO.getBaseType();
    if(sourceBaseType.equals(BaseType.ARTICLE) && deletedEntityIID.contains(sourceEntityDTO.getDefaultImageIID())) {
      sourceEntityDTO.setDefaultImageIID(0l);
      entitiesWithoutDefaultImage.add(sourceEntityDTO.getBaseEntityIID());    
      }
    
    else if (sourceBaseType.equals(BaseType.ASSET)) {
      for(long iid : deletedEntityIID) {
        IBaseEntityDAO daoOfRemovedEntity = rdbmsComponentUtils.getBaseEntityDAO(iid);
        IBaseEntityDTO dtoOfRemovedEntity = daoOfRemovedEntity.getBaseEntityDTO();
        
        if(dtoOfRemovedEntity.getBaseType().equals(BaseType.ARTICLE) &&
           dtoOfRemovedEntity.getDefaultImageIID() == sourceEntityDTO.getBaseEntityIID()) {
          entitiesWithoutDefaultImage.add(dtoOfRemovedEntity.getBaseEntityIID());
          dtoOfRemovedEntity.setDefaultImageIID(0l);
          daoOfRemovedEntity.updatePropertyRecords();
        }
      }
    }
  }
  
  private boolean isConflictExistOnTargetEntity(long otherSideEntityIID, IPropertyDTO relationProperty,
      IRelationCoupleRecordDAO relationCoupleRecordDao) throws Exception
  {
    IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().targetEntityId(otherSideEntityIID)
        .propagableRelationshipId(relationProperty.getPropertyIID()).isResolved(false)
        .couplingType(IRelationCoupleRecordDTO.CouplingType.tightlyCoupled).build();
    StringBuilder filterQuery = relationCoupleRecordDao.getFilterQuery(relationCoupleRecord);
    if (!relationCoupleRecordDao.fetchRelationCoupleRecord(filterQuery).isEmpty()) {
      return true;
    }
    return false;
  }
  
  private void fillDataForRelationshipInheritanceForDeletedElements(List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel,
      IRelationsSetDTO relationsSetDTO, Set<Long> deletedEntityIID, Boolean isNature)
  {
    IRelationshipDataTransferInfoModel dataTransferModel = new RelationshipDataTransferInfoModel();

    dataTransferModel.setContentId(relationsSetDTO.getEntityIID());
    if (isNature) {
      dataTransferModel.getChangedNatureRelationshipIds().add(relationsSetDTO.getProperty().getCode());
    }
    else {
      dataTransferModel.getChangedRelationshipIds().add(relationsSetDTO.getProperty().getCode());
    }

    Map<String, IEntityRelationshipInfoDTO> relationshipIdAddedDeletedElementsMap = dataTransferModel
        .getRelationshipIdAddedDeletedElementsMap();
    IEntityRelationshipInfoDTO addedRemovedElements = relationshipIdAddedDeletedElementsMap
        .get(relationsSetDTO.getProperty().getPropertyIID() + "__" + relationsSetDTO.getProperty().getRelationSide());
    if (addedRemovedElements == null) {
      addedRemovedElements = new EntityRelationshipInfoDTO();
      relationshipIdAddedDeletedElementsMap.put(
          relationsSetDTO.getProperty().getPropertyCode() + "__" + relationsSetDTO.getProperty().getRelationSide(), addedRemovedElements);
      for (Long deletedEntity : deletedEntityIID) {
        addedRemovedElements.getRemovedElements().add(deletedEntity);
      }
    }

    relationshipDataTransferModel.add(dataTransferModel);
  }

  public IRelationsSetDTO loadRelationsSetDTO(IReferencedRelationshipModel relationshipConfig,
      PropertyType propertyType, RelationSide relationSide) throws Exception
  {
    IPropertyDTO propertyDTO = this.newRelationshipProperty(relationshipConfig, propertyType,
        relationSide);
    IBaseEntityDTO baseEntityDTO = this.entityDAO.loadPropertyRecords(propertyDTO);
    if (baseEntityDTO.getPropertyRecords() == null || baseEntityDTO.getPropertyRecords()
        .size() <= 0) {
      // If relationship property record not created then create and return
      IRelationsSetDTO newEntityRelationsSetDTO = this.newEntityRelationsSetDTO(relationshipConfig,
          propertyType, relationSide);
      return newEntityRelationsSetDTO;
      /*this.entityDAO.createPropertyRecords(newEntityRelationsSetDTO);
      return this.loadRelationsSetDTO(relationshipConfig, propertyType, relationSide);*/
    }
    else if (baseEntityDTO.getPropertyRecords()
        .size() == 1) {
      IRelationsSetDTO relationsSetDTO = (IRelationsSetDTO) baseEntityDTO.getPropertyRecords()
          .iterator()
          .next();
      return relationsSetDTO;
    }
    else if (baseEntityDTO.getPropertyRecords()
        .size() > 1) {
      throw new Exception(
          "Multiple relationship property record present for " + relationshipConfig.getId());
    }
    else {
      return null;
    }
  }
  
  private IEntityRelationDTO createRelationshipContext(IRelationshipVersion relationshipVersion,
      IRelationsSetDTO relationsSetDTO, IReferencedRelationshipModel relationshipConfig,
      IReferencedRelationshipPropertiesModel relationshipProperty,
      IReferencedSectionElementModel relationshipElement, String otherSideEntityId) throws Exception
  {
    IReferencedVariantContextModel referencedVariantContextModel = this
        .getVariantContextConfig(relationshipVersion.getContextId());
    if (referencedVariantContextModel == null || relationshipVersion.getTags() == null
        || relationshipVersion.getTags()
            .isEmpty() && relationshipVersion.getTimeRange() == null)
      return null;
    IContextDTO contextDTO = RDBMSUtils.getContextDTO(referencedVariantContextModel);
    IEntityRelationDTO entityRelationDTO = null;
    if (contextDTO != null) {
      entityRelationDTO = RDBMSUtils
          .getEntityRelationDTO(Long.parseLong(relationshipVersion.getId()), otherSideEntityId, contextDTO);
      IContextualDataDTO sideContextualObject = entityRelationDTO.getContextualObject();
      this.createContextTagValues(relationshipVersion, sideContextualObject);
      this.setContextTimeRange(relationshipVersion, sideContextualObject);
      sideContextualObject.setChanged(true);
    }
    return entityRelationDTO;
  }
  
  public static final String LINKED_VARIANT_CONTEXT_DUPLICATE_BASE_QUERY = "select count(1) from pxp.relation re join " +
      "pxp.contextualObject co ON  re.side1contextualobjectiid = co.contextualObjectIID where" + " re.side1entityiid = ? " +
      "and co.contextCode = ? and re.propertyiid = ?";
  
  private void validateLinkedVariantContext(IContextualDataDTO sideContextualObject,
      long sideOneEntityIID, long relationshipPropertyIID) throws RDBMSException
  {
    AtomicBoolean isDuplicate = new AtomicBoolean(false);
    StringBuilder duplicateQuery = new StringBuilder(LINKED_VARIANT_CONTEXT_DUPLICATE_BASE_QUERY);
    generateGenericDuplicateQuery(duplicateQuery, sideContextualObject);
    
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          
          PreparedStatement stmt = currentConn.prepareStatement(duplicateQuery.toString());
          stmt.setLong(1, sideOneEntityIID);
          stmt.setString(2, sideContextualObject.getContextCode());
          stmt.setLong(3, relationshipPropertyIID);
          
          stmt.execute();
          
          IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
          result.next();
          isDuplicate.set(result.getLong("count") > 0);
        });
    if (isDuplicate.get()) {
      throw new DuplicateVariantExistsException();
    }
  }

  private void generateGenericDuplicateQuery(StringBuilder baseDuplicateQuery, IContextualDataDTO contextualDataDTO)
  {
    Set<ITagDTO> contextTagValues = contextualDataDTO.getContextTagValues()
        .stream()
        .filter(contextTagValue -> contextTagValue.getRange() != 0)
        .collect(Collectors.toSet());
    
    // check if time enabled, as these fields are not empty when time enabled.
    Boolean isTimeEnabled = contextualDataDTO.getContextStartTime() != 0 && contextualDataDTO.getContextEndTime() != 0;

    if (isTimeEnabled) {
      //the third parameter of function used to generate tstzrange in this query is used to denote
      // mutual inclusion or exclusion.
      baseDuplicateQuery.append(String.format(" and cxttimerange && int8range(%s, %s, '[]')",
          contextualDataDTO.getContextStartTime(), contextualDataDTO.getContextEndTime()));
    }

    if (contextTagValues.isEmpty()) {
      //context tags are empty for empty tags
      baseDuplicateQuery.append("and (co.cxttags is null or co.cxttags = '') ");
    }
    else {
      Function<ITagDTO, String> function = (ITagDTO tagValue) -> tagValue.getTagValueCode();
      String tagQuery = String.format(" and array_length(avals(cxttags), 1) = %d ", contextTagValues.size());

      Iterator<ITagDTO> tagValuesIterator = contextTagValues.iterator();
      String tagValuesQuery = "";
      while (tagValuesIterator.hasNext()) {
        ITagDTO tagValue = tagValuesIterator.next();
        String tagValueMatchQuery = String.format(" %s=>%s ", tagValue.getTagValueCode(), tagValue.getRange());

        if (tagValuesIterator.hasNext()) {
          tagValuesQuery = tagValuesQuery + tagValueMatchQuery + " , ";
        }
        else {
          tagValuesQuery = tagValuesQuery + tagValueMatchQuery;
        }
      }
      String format = String.format(" and cxttags @> '%s' ", tagValuesQuery);
      baseDuplicateQuery.append(tagQuery + format);
    }
  }
    
  private void createContextTagValues(IRelationshipVersion relationshipVersion,
      IContextualDataDTO sideContextualObject) throws Exception
  {
    // Tag Value Handling
    relationshipVersion.getTags()
        .forEach(tagInstance -> {
          try {
            ITag tagConfig = this.referencedTags.get(tagInstance.getTagId());
            Map<String, ITag> tagValueIDTag = this
                .getTagIdTagMap((List<ITag>) tagConfig.getChildren());
            tagInstance.getTagValues()
                .forEach(tagValue -> {
                  try {
                    ITag tagValueConfig = tagValueIDTag.get(tagValue.getTagId());
                    ITagDTO newTagRecordDTO = this.entityDAO.newTagDTO(tagValue.getRelevance(),
                        tagValueConfig.getCode());
                    sideContextualObject.getContextTagValues()
                        .add(newTagRecordDTO);
                  }
                  catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
  }
  
  private void setContextTimeRange(IRelationshipVersion relationshipVersion,
      IContextualDataDTO sideContextualObject) throws Exception
  {
    // Time range Handling
    IInstanceTimeRange timeRange = relationshipVersion.getTimeRange();
    if (timeRange != null) {
      Long from = timeRange.getFrom() == null ? 0 : timeRange.getFrom();
      Long to = timeRange.getTo() == null ? 0 : timeRange.getTo();
      sideContextualObject.setContextStartTime(from);
      sideContextualObject.setContextEndTime(to);
    }
  }
  
  private Map<String, ITag> getTagIdTagMap(List<ITag> childrenTags)
  {
    return childrenTags.stream()
        .collect(Collectors.toMap(tag -> tag.getId(), tag -> tag));
  }
  
  private IEntityRelationDTO modifieRelationshipContext(IRelationshipVersion relationshipVersion,
      IEntityRelationDTO entityRelationDTO, IRelationsSetDTO relationsSetDTO,
      IReferencedRelationshipModel relationshipConfig,
      IReferencedRelationshipPropertiesModel relationshipProperty,
      IReferencedSectionElementModel relationshipElement, List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel) throws Exception
  {
    IContextualDataDTO contextualObject = entityRelationDTO.getContextualObject();
    if (contextualObject != null && contextualObject.getContextualObjectIID() != 0) {
      contextualObject.getContextTagValues()
          .clear();
      this.createContextTagValues(relationshipVersion, contextualObject);
      this.setContextTimeRange(relationshipVersion, contextualObject);
      contextualObject.setChanged(true);
    }
    else {
      IReferencedVariantContextModel referencedVariantContextModel = this
          .getVariantContextConfig(relationshipVersion.getContextId());
      if (referencedVariantContextModel != null && relationshipVersion.getTags() != null
          && (!relationshipVersion.getTags()
              .isEmpty())) {
        entityRelationDTO.setContextCode(RDBMSUtils.getContextDTO(referencedVariantContextModel)
            .getCode());
        this.createContextTagValues(relationshipVersion, contextualObject);
        this.setContextTimeRange(relationshipVersion, contextualObject);
        contextualObject.setChanged(true);
      }
    }
    fillDataForRelationshipInheritanceForAddedAndModifiedElements(relationshipDataTransferModel, relationsSetDTO, relationshipVersion, relationshipConfig.getIsNature());
    return entityRelationDTO;
  }
  
  private IReferencedVariantContextModel getVariantContextConfig(String variantContextString)
      throws Exception
  {
    IReferencedVariantContextModel referencedVariantContextModel = this.relationshipVariantContexts
        .get(variantContextString);
    if (referencedVariantContextModel == null) {
      referencedVariantContextModel = this.productVariantContexts.get(variantContextString);
    }
    return referencedVariantContextModel;
  }

  private Boolean validateRelationSide(IBaseEntityDAO baseEntityDAO, List<String> allowedKlassIds) {
    Set<IClassifierDTO> otherClassifiers = baseEntityDAO.getBaseEntityDTO().getOtherClassifiers();
    otherClassifiers.add(baseEntityDAO.getBaseEntityDTO().getNatureClassifier());
    Boolean isPresent = otherClassifiers.stream().filter(classifier -> allowedKlassIds.contains(classifier.getCode())).findAny().isPresent();
    return isPresent;
  }

}
