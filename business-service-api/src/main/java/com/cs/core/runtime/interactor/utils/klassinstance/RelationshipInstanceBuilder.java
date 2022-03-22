package com.cs.core.runtime.interactor.utils.klassinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInstanceModel;
import com.cs.core.config.interactor.model.relationship.RelationshipInstanceModel;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.runtime.builder.IRelationshipInstanceBuilder;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.relationship.IElementsRelationshipInformation;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;

@SuppressWarnings("unchecked")
public class RelationshipInstanceBuilder implements IRelationshipInstanceBuilder {
  
  public static final String                                 ID_SEPARATOR                  = "-";
  
  private IBaseEntityDAO                                baseEntityDAO;
  private IBaseEntityDTO                                baseEntityDTO;
  private RDBMSComponentUtils                           rdbmsComponentUtils;
  private List<IPropertyDTO>                            relationshipProperties    = new ArrayList<>();
  private IRelationshipInstanceModel                    relationshipInstanceModel = new RelationshipInstanceModel();
  private Map<Long, List<IKlassInstanceRelationshipInstance>> relationshipInstancesMap  = new HashMap<>();
  private IGetConfigDetailsForCustomTabModel            configDetails;
  private Map<String, IReferencedSectionElementModel>   referencedElements;
  
  public RelationshipInstanceBuilder(RDBMSComponentUtils rdbmsComponentUtils,
      IGetConfigDetailsForCustomTabModel configDetails)
  {
    this.rdbmsComponentUtils = rdbmsComponentUtils;
    this.configDetails = configDetails;
    this.referencedElements = configDetails.getReferencedElements();
  }
  
  @Override
  public IRelationshipInstanceBuilder baseEntityDAO(IBaseEntityDAO baseEntityDAO)
  {
    this.baseEntityDAO = baseEntityDAO;
    return this;
  }
  
  @Override
  public IRelationshipInstanceBuilder baseEntityDTO(IBaseEntityDTO baseEntityDTO)
  {
    this.baseEntityDTO = baseEntityDTO;
    return this;
  }
  
  public IRelationshipInstanceModel build() throws Exception
  {
    // fill all contentrelationship and nature relationship
    fillRelationshipInstances();
    // baseEntityDAO null means relationship is already loaded into baseEntityDTO
    if (baseEntityDAO != null) {
      baseEntityDTO = baseEntityDAO.loadPropertyRecords(
          relationshipProperties.toArray(new IPropertyDTO[relationshipProperties.size()]));
    }
    
    
    for (IPropertyRecordDTO propertyRecord : baseEntityDTO.getPropertyRecords()) {
      if (propertyRecord instanceof IRelationsSetDTO) {
        IReferencedRelationshipModel referencedRelationship = configDetails.getReferencedRelationships()
            .get(propertyRecord.getProperty().getPropertyCode());
        if (referencedRelationship != null)
          fillRelationInformation(propertyRecord);
        IGetReferencedNatureRelationshipModel referencedNatureRelationship = configDetails.getReferencedNatureRelationships()
            .get(propertyRecord.getProperty().getPropertyCode());
        if (referencedNatureRelationship != null)
          fillNatureRelationInformation(propertyRecord, referencedNatureRelationship);
      }
    }
    return relationshipInstanceModel;
  }
  
  private void fillRelationInformation(IPropertyRecordDTO propertyRecord) throws Exception
  {
    List<IKlassInstanceRelationshipInstance> relationshipInstances = relationshipInstancesMap
        .get(propertyRecord.getProperty().getIID());
    IReferencedRelationshipModel referencedRelationship = configDetails.getReferencedRelationships()
        .get(propertyRecord.getProperty().getPropertyCode());
    
    if(relationshipInstances != null && !relationshipInstances.isEmpty()) {
    	
    	for(IKlassInstanceRelationshipInstance relationshipInstance: relationshipInstances) {
    	  
    	  //fill relationship elements only when side from propertyrecord and relationshipInstance are same(self relationship scenario)
    	  String sideId = relationshipInstance.getSideId();
    	  Boolean isValidSide = checkIfSideIsValid(propertyRecord, referencedRelationship,sideId);
    	  if(!isValidSide) {
    	    continue;
    	  }
        
    		List<IKlassInstanceInformationModel> klassInstanceElements = getRelationshipInstanceElements(
    				(IRelationsSetDTO) propertyRecord, relationshipInstance);
    		if (!klassInstanceElements.isEmpty()) {
    			if(relationshipInstance.getElementIds() != null && !relationshipInstance.getElementIds().isEmpty()) {
    				relationshipInstanceModel.getReferenceRelationshipInstanceElements()
    				.put(sideId, klassInstanceElements);
    			}
    		}
    	}
    }
  }

  private Boolean checkIfSideIsValid(IPropertyRecordDTO propertyRecord,
      IReferencedRelationshipModel referencedRelationship, String sideId)
  {
    RelationSide side = ((IRelationsSetDTO)propertyRecord).getSide();
    String sideIdToCheck = side.equals(RelationSide.SIDE_1) ? referencedRelationship.getSide1().getElementId()
        : referencedRelationship.getSide2().getElementId();
    if(!sideId.equals(sideIdToCheck)) {
      return false;
    }
    return true;
  }
  
  private void fillNatureRelationInformation(IPropertyRecordDTO propertyRecord,
      IGetReferencedNatureRelationshipModel referencedNatureRelationship) throws Exception
  {
    List<IKlassInstanceRelationshipInstance> relationshipInstances = relationshipInstancesMap
        .get(propertyRecord.getProperty().getIID());
    
    for(IKlassInstanceRelationshipInstance relationshipInstance: relationshipInstances) {
    	List<IKlassInstanceInformationModel> klassInstanceElements = getRelationshipInstanceElements(
    			(IRelationsSetDTO) propertyRecord, relationshipInstance);
    	if (!klassInstanceElements.isEmpty()) {
    		relationshipInstanceModel.getReferenceNatureRelationshipInstanceElements()
    		.put(relationshipInstance.getSideId(), klassInstanceElements);
    	}
    }
  }
  
  private void fillRelationshipInstances() throws Exception
  {
    configDetails.getReferencedRelationships().values()
        .forEach(relationshipConfig -> {
          try {
            List<IKlassInstanceRelationshipInstance> relationshipnInstances = fetchRelationshipInstanceAndPreparePropertyDTO(
                relationshipProperties, relationshipConfig);
            relationshipInstanceModel.getContentRelationships().addAll(relationshipnInstances);
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
    configDetails.getReferencedNatureRelationships().values()
        .forEach(relationshipConfig -> {
          try {
            List<IKlassInstanceRelationshipInstance> relationshipnInstance = fetchRelationshipInstanceAndPreparePropertyDTO(
                relationshipProperties, relationshipConfig);
            relationshipInstanceModel.getNatureRelationships().addAll(relationshipnInstance);
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
  }

  private List<IKlassInstanceRelationshipInstance> fetchRelationshipInstanceAndPreparePropertyDTO(
      List<IPropertyDTO> relationshipProperties, IReferencedRelationshipModel relationshipConfig)
      throws Exception, RDBMSException
  {
	List<IKlassInstanceRelationshipInstance> klassInstanceRelationshipInstances = new ArrayList<>();
	
	IReferencedSectionRelationshipModel relationshipElement = (IReferencedSectionRelationshipModel) this.referencedElements
        .get(relationshipConfig.getSide1().getElementId());
	
    RelationSide relationSide = RelationSide.SIDE_1;
    if (relationshipElement != null) {
    	IKlassInstanceRelationshipInstance relationshipInstance = getRelationshipInstance(
    	        relationshipConfig, relationshipElement);
    	klassInstanceRelationshipInstances.add(relationshipInstance);
    	relationshipInstancesMap.put(relationshipConfig.getPropertyIID(), klassInstanceRelationshipInstances);
    	
    	if (baseEntityDAO != null) {
    		IPropertyDTO propertyDTO = baseEntityDAO.newPropertyDTO(relationshipConfig.getPropertyIID(),
    				relationshipConfig.getCode(), relationshipConfig.getIsNature() ?  PropertyType.NATURE_RELATIONSHIP : PropertyType.RELATIONSHIP);
    		propertyDTO.setRelationSide(relationSide);
    		relationshipProperties.add(propertyDTO);
    	}
	}
    relationshipElement = (IReferencedSectionRelationshipModel) this.referencedElements.get(relationshipConfig.getSide2().getElementId());
    if(relationshipElement!=null) {
    	
    	RelationSide relationSideForSelfRelation = RelationSide.SIDE_2;
		 IKlassInstanceRelationshipInstance relationshipInstanceForSide2 = getRelationshipInstance(
				 relationshipConfig, relationshipElement);
		klassInstanceRelationshipInstances.add(relationshipInstanceForSide2);
		relationshipInstancesMap.put(relationshipConfig.getPropertyIID(), klassInstanceRelationshipInstances);
		
		if (baseEntityDAO != null) {
			
			IPropertyDTO propertyDTO = new PropertyDTO(relationshipConfig.getPropertyIID(),
					relationshipConfig.getCode(), relationshipConfig.getIsNature() ?  PropertyType.NATURE_RELATIONSHIP : PropertyType.RELATIONSHIP);
			propertyDTO.setRelationSide(relationSideForSelfRelation);
			relationshipProperties.add(propertyDTO);
		}
    }
    return klassInstanceRelationshipInstances;
  }
  
  private IKlassInstanceRelationshipInstance getRelationshipInstance(
      IRelationship relationshipConfig, IReferencedSectionRelationshipModel relationshipElement)
      throws Exception
  {
    IKlassInstanceRelationshipInstance relationshipInstance = new KlassInstanceRelationshipInstance();
    relationshipInstance.setRelationshipId(relationshipElement.getPropertyId());
    relationshipInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix()));
    relationshipInstance.setSideId(relationshipElement.getId());
    return relationshipInstance;
  }
  
  private List<IKlassInstanceInformationModel> getRelationshipInstanceElements(
      IRelationsSetDTO relationsSetDTO, IKlassInstanceRelationshipInstance relationshipInstance)
      throws Exception
  {
    Set<IEntityRelationDTO> relations = relationsSetDTO.getRelations();
    if (relationsSetDTO == null || relations == null
        || relationshipInstance == null || relations
            .isEmpty())
      return new ArrayList<>();
    
    Map<Long, IKlassInstanceInformationModel> klassInstanceElements = new HashMap<>();
    Map<String, IElementsRelationshipInformation> elementsRelationshipInfo = relationshipInstance
        .getElementsRelationshipInfo();
    
    Map<String, List<IContentTagInstance>> elementTagMapping = relationshipInstance
        .getElementTagMapping();
    Map<String, IInstanceTimeRange> elementTimeRangeMapping = relationshipInstance
        .getElementTimeRangeMapping();

    List<String> otherSideEntityIIDs = relations.stream().map(x -> String.valueOf(x.getOtherSideEntityIID())).collect(Collectors.toList());
    List<IBaseEntityDTO> baseEntitiesByIIDs = rdbmsComponentUtils.getLocaleCatlogDAO().getBaseEntitiesByIIDs(otherSideEntityIIDs);
    Map<Long, IBaseEntityDTO> baseEntities = baseEntitiesByIIDs.stream().collect(Collectors.toMap(x -> x.getBaseEntityIID(), y -> y));
    
    // multiple base entity have the same default image
    Map<Long, List<Long>> defaultImageIIDVsBaseEntityIIDs = new HashMap<>();
    for (IBaseEntityDTO baseEntitiesByIID : baseEntitiesByIIDs) {
      if (defaultImageIIDVsBaseEntityIIDs.containsKey(baseEntitiesByIID.getDefaultImageIID())) {
        defaultImageIIDVsBaseEntityIIDs.get(baseEntitiesByIID.getDefaultImageIID()).add(baseEntitiesByIID.getBaseEntityIID());
      }
      else {
        List<Long> list = new ArrayList<>();
        list.add(baseEntitiesByIID.getBaseEntityIID());
        defaultImageIIDVsBaseEntityIIDs.put(baseEntitiesByIID.getDefaultImageIID(), list);
      }
    }

    for (IEntityRelationDTO entityRelationDTO : relations) {
      long starTime = System.currentTimeMillis();
      IBaseEntityDTO otherSideBaseEntityDTO = baseEntities.get(entityRelationDTO.getOtherSideEntityIID());

      RDBMSLogger.instance()
          .debug("NA|RDBMS|" + this.getClass().getSimpleName() + "|getRelationshipInstanceElements|getEntityByIID| %d ms",
              System.currentTimeMillis() - starTime);
      if (otherSideBaseEntityDTO != null && !otherSideBaseEntityDTO.isMerged()) {
        IKlassInstanceInformationModel klassInstanceElement = KlassInstanceBuilder.getKlassInstanceInformationModel(otherSideBaseEntityDTO,
            rdbmsComponentUtils);

        klassInstanceElements.put(entityRelationDTO.getOtherSideEntityIID(), klassInstanceElement);

        if (!configDetails.getReferencedNatureRelationships().isEmpty() && configDetails.getReferencedNatureRelationships() != null) {
          relationshipInstance.getElementIds().add(klassInstanceElement.getId());
        }
        else {
          insertElementIds(relationsSetDTO, relationshipInstance, entityRelationDTO);
        }
        // Element info
        //elementIds.add(klassInstanceElement.getId());

        // Context Info
        IContextualDataDTO sideContextualObject = entityRelationDTO.getContextualObject();
        if (sideContextualObject != null && sideContextualObject.getContextualObjectIID() != 0) {

          // Tag values
          List<IContentTagInstance> contentTagInstances = getContextTagInstances(sideContextualObject);
          // IContentTagInstance contextTagInstance =
          // this.getContextTagInstance(sideContextualObject);
          if (contentTagInstances != null && !contentTagInstances.isEmpty()) {
            elementTagMapping.put(klassInstanceElement.getId(), contentTagInstances);
          }

          // Time range
          IInstanceTimeRange instanceTimeRange = new InstanceTimeRange();
          instanceTimeRange.setFrom(sideContextualObject.getContextStartTime());
          instanceTimeRange.setTo(sideContextualObject.getContextEndTime());
          elementTimeRangeMapping.put(klassInstanceElement.getId(), instanceTimeRange);
        }
      }
    }
    Map<Long, IJSONContent> extensions = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getEntityExtensionByIIDs(defaultImageIIDVsBaseEntityIIDs.keySet());

    for(Map.Entry<Long, IJSONContent> entity : extensions.entrySet()){
      IAssetAttributeInstanceInformationModel assetInfoModel = BaseEntityUtils.fillAssetInformationModel(entity.getKey(), entity.getValue(),null);
      List<Long> otherSideEntityIIDss = defaultImageIIDVsBaseEntityIIDs.get(entity.getKey());
      assetInfoModel.setIsDefault(true);
      for (Long otherSideEntityIID : otherSideEntityIIDss) {
        if (klassInstanceElements.get(otherSideEntityIID) == null) {
          continue;
        }
        IKlassInstanceInformationModel klassInfoModel = klassInstanceElements.get(otherSideEntityIID);
        List<IAssetAttributeInstanceInformationModel> referencedAssets = new ArrayList<>();
        referencedAssets.add(assetInfoModel);
        klassInfoModel.setReferencedAssets(referencedAssets);
      }
    }
    relationshipInstance.setTotalCount((long) relationshipInstance.getElementIds().size());
    return new ArrayList<>(klassInstanceElements.values());
  }
  
  private void insertElementIds(IRelationsSetDTO relationsSetDTO, IKlassInstanceRelationshipInstance relationshipInstance,
		  IEntityRelationDTO entityRelationDTO) {

	  String elementId = null;
	  IReferencedRelationshipModel referencedRelationship = configDetails.getReferencedRelationships().get(relationsSetDTO.getProperty().getPropertyCode());

	  if(relationsSetDTO.getSide().equals(RelationSide.SIDE_1)) {
		  if(relationsSetDTO.getEntityIID() == entityRelationDTO.getOtherSideEntityIID()) {
			  elementId = referencedRelationship.getSide2().getElementId();

		  }else if(relationsSetDTO.getEntityIID() != entityRelationDTO.getOtherSideEntityIID()){
			  elementId = referencedRelationship.getSide1().getElementId();
		  }
	  }else {
		  if(relationsSetDTO.getEntityIID() == entityRelationDTO.getOtherSideEntityIID()) {
			  elementId = referencedRelationship.getSide1().getElementId();

		  }else if(relationsSetDTO.getEntityIID() != entityRelationDTO.getOtherSideEntityIID()){
			  elementId = referencedRelationship.getSide2().getElementId();
		  }
	  }

	  if(relationshipInstance.getSideId().equals(elementId)) {
		  relationshipInstance.getElementIds().add(Long.toString(entityRelationDTO.getOtherSideEntityIID()));
	  }
  }

  private void fillReferencedAssets(IKlassInstanceInformationModel klassInstanceElement, IBaseEntityDTO baseEntityDTO) throws Exception
  {
    List<IAssetAttributeInstanceInformationModel> referencedAssets = new ArrayList<>();
    long defaultImageIID = baseEntityDTO.getDefaultImageIID();
    if (defaultImageIID != 0l) {
      IBaseEntityDTO baseEntityDTOByIID = rdbmsComponentUtils.getBaseEntityDTO(defaultImageIID);
      IAssetAttributeInstanceInformationModel assetInfoModel = BaseEntityUtils.fillAssetInformationModel(baseEntityDTOByIID);
      assetInfoModel.setIsDefault(true);
      referencedAssets.add(assetInfoModel);
    }
    klassInstanceElement.setReferencedAssets(referencedAssets);
  }

  private List<IContentTagInstance> getContextTagInstances(IContextualDataDTO contextualDataDTO)
  {
    List<IContentTagInstance> tagInstances = new ArrayList<>();
    Map<String, IContentTagInstance> tagInstanceType = new HashMap<>();
    contextualDataDTO.getContextTagValues()
        .forEach(tagRecordDTO -> {
          try {
            // only fill those tagInstance who's relevance is 100
            if (tagRecordDTO.getRange() != 0) {
              String tagCode = tagRecordDTO.getTagValueCode();
              for (ITag referencedTag : configDetails.getReferencedTags().values()) {
                List<ITreeEntity> treeEntities = (List<ITreeEntity>) referencedTag.getChildren();
                for (ITreeEntity treeEntity : treeEntities) {
                  if (treeEntity.getId().equals(tagCode)) {
                    String tagInstanceId = String
                        .valueOf(contextualDataDTO.getContextualObjectIID()) + ID_SEPARATOR  + String.valueOf(referencedTag.getCode());
                    ITagInstance tagInstance = (ITagInstance) tagInstanceType.get(tagInstanceId);
                    
                    if (tagInstance == null) {
                      tagInstance = new TagInstance();
                      tagInstance.setId(tagInstanceId);
                      tagInstance.setContextInstanceId(Long.toString(contextualDataDTO.getContextualObjectIID()));
                      tagInstance.setBaseType(tagInstance.getClass().getName());
                      tagInstance.setTagId(referencedTag.getCode());
                      tagInstanceType.put(tagInstanceId, tagInstance);
                      tagInstances.add(tagInstance);
                    }
                    
                    ITagInstanceValue tagInstanceValue = new TagInstanceValue();
                    String tagValueInstanceId = String
                        .valueOf(contextualDataDTO.getContextualObjectIID()) + ID_SEPARATOR + String.valueOf(tagCode);
                    tagInstanceValue.setId(tagValueInstanceId);
                    tagInstanceValue.setTagId(tagCode);
                    tagInstanceValue.setCode(tagCode);
                    tagInstanceValue.setRelevance(tagRecordDTO.getRange());
                    tagInstance.getTagValues().add(tagInstanceValue);
                  }
                }
              }
            }
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
    
    return tagInstances;
  }
  
}
