package com.cs.core.runtime.interactor.utils.klassinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.relationship.ElementsRelationshipInformation;
import com.cs.core.runtime.interactor.entity.relationship.IElementsRelationshipInformation;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

public class ConvertRelationSetToRelationInstance {
  
  private IBaseEntityDTO                                    baseEntityDTO;
  private IGetConfigDetailsForCustomTabModel                configDetails;
  private Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElements;
  private Map<String, List<IKlassInstanceInformationModel>> referenceNatureRelationshipInstanceElements;
  private List<IKlassInstanceRelationshipInstance>          contentRelationships;
  private List<IKlassInstanceRelationshipInstance>          natureRelationships;
  private RDBMSComponentUtils                               rdbmsComponentUtils;
  private Set<String>                                       propertyCodesToFetch;
  
  public ConvertRelationSetToRelationInstance(IBaseEntityDTO baseEntityDTO,
      IGetConfigDetailsForCustomTabModel configDetails, RDBMSComponentUtils rdbmsComponentUtils, Set<String> propertyCodesToFetch) throws Exception
  {
    super();
    this.baseEntityDTO = baseEntityDTO;
    this.configDetails = configDetails;
    this.rdbmsComponentUtils = rdbmsComponentUtils;
    this.propertyCodesToFetch = propertyCodesToFetch;
    referenceNatureRelationshipInstanceElements = new HashMap<String, List<IKlassInstanceInformationModel>>();
    referenceRelationshipInstanceElements = new HashMap<String, List<IKlassInstanceInformationModel>>();
    contentRelationships = new ArrayList<IKlassInstanceRelationshipInstance>();
    natureRelationships = new ArrayList<IKlassInstanceRelationshipInstance>();
    prepareRelationshipInstance();
  }
  
  public void prepareRelationshipInstance() throws Exception
  {
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails
        .getReferencedNatureRelationships();
    Map<String, IReferencedRelationshipModel> referencedRelationships = configDetails
        .getReferencedRelationships();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    if (propertyCodesToFetch != null) {
      for(IPropertyRecordDTO propertyRecord : baseEntityDTO.getPropertyRecords()) {
        if(propertyCodesToFetch.contains(propertyRecord.getProperty().getPropertyCode())) {
          if (propertyRecord instanceof IRelationsSetDTO) {
            IReferencedRelationshipModel referencedRelationship = referencedRelationships
                .get(propertyRecord.getProperty().getPropertyCode());
            if (referencedRelationship != null)
              fillRelationInformation(referencedElements, propertyRecord, referencedRelationship);
            IGetReferencedNatureRelationshipModel referencedNatureRelationship = referencedNatureRelationships
                .get(propertyRecord.getProperty().getPropertyCode());
            if (referencedNatureRelationship != null)
              fillNatureRelationInformation(referencedElements, propertyRecord, referencedNatureRelationship);
          }
        }
    }
    }
  }

  private void fillRelationInformation(
      Map<String, IReferencedSectionElementModel> referencedElements,
      IPropertyRecordDTO propertyRecord,
      IReferencedRelationshipModel referencedRelationship) throws Exception
  {
    IReferencedSectionRelationshipModel relationshipElement = (IReferencedSectionRelationshipModel) referencedElements
        .get(referencedRelationship.getSide1()
            .getElementId());
    if (relationshipElement == null) {
      relationshipElement = (IReferencedSectionRelationshipModel) referencedElements
          .get(referencedRelationship.getSide2()
              .getElementId());
    }
    IKlassInstanceRelationshipInstance klassInstanceRelationshipInstance = getRelationshipInstance(referencedRelationship, relationshipElement);
   contentRelationships.add(klassInstanceRelationshipInstance);
   List<IKlassInstanceInformationModel> klassInstanceElements = getRelationshipInstanceElements
      ((IRelationsSetDTO) propertyRecord, klassInstanceRelationshipInstance);
   if (!klassInstanceElements.isEmpty()) {
    referenceRelationshipInstanceElements.put(relationshipElement.getId(), klassInstanceElements);
   }
  }
  
  private void fillNatureRelationInformation(
      Map<String, IReferencedSectionElementModel> referencedElements,
      IPropertyRecordDTO propertyRecord,
      IGetReferencedNatureRelationshipModel referencedNatureRelationship) throws Exception
  {
    IReferencedSectionRelationshipModel relationshipElement = (IReferencedSectionRelationshipModel) referencedElements
        .get(referencedNatureRelationship.getSide1()
            .getElementId());
    if (relationshipElement == null) {
      relationshipElement = (IReferencedSectionRelationshipModel) referencedElements
          .get(referencedNatureRelationship.getSide2()
              .getElementId());
    }
    IKlassInstanceRelationshipInstance klassInstanceRelationshipInstance = getRelationshipInstance(referencedNatureRelationship, relationshipElement);
   natureRelationships.add(klassInstanceRelationshipInstance);
   List<IKlassInstanceInformationModel> klassInstanceElements = getRelationshipInstanceElements
      ((IRelationsSetDTO) propertyRecord, klassInstanceRelationshipInstance);
   if (!klassInstanceElements.isEmpty()) {
    referenceNatureRelationshipInstanceElements.put(relationshipElement.getId(), klassInstanceElements);
   }
  }
  
  private List<IKlassInstanceInformationModel> getRelationshipInstanceElements(
      IRelationsSetDTO relationsSetDTO, IKlassInstanceRelationshipInstance relationshipInstance)
      throws Exception
  {
    if (relationsSetDTO == null || relationsSetDTO.getRelations() == null || relationsSetDTO.getRelations()
            .isEmpty())
      return new ArrayList<>();
    
    List<IKlassInstanceInformationModel> klassInstanceElements = new ArrayList<>();
    List<String> elementIds = relationshipInstance.getElementIds();
    Map<String, IElementsRelationshipInformation> elementsRelationshipInfo = relationshipInstance
        .getElementsRelationshipInfo();
    
    Map<String, List<IContentTagInstance>> elementTagMapping = relationshipInstance
        .getElementTagMapping();
    Map<String, IInstanceTimeRange> elementTimeRangeMapping = relationshipInstance
        .getElementTimeRangeMapping();
    
    relationsSetDTO.getRelations()
        .forEach(entityRelationDTO -> {
          IBaseEntityDTO baseEntityDTO;
          try {
            long starTime = System.currentTimeMillis();
            baseEntityDTO = this.rdbmsComponentUtils
                .getBaseEntityDTO(entityRelationDTO.getOtherSideEntityIID());
            RDBMSLogger.instance()
                .debug("NA|RDBMS|" + this.getClass()
                    .getSimpleName() + "|getRelationshipInstanceElements|getEntityByIID| %d ms",
                    System.currentTimeMillis() - starTime);
            if (baseEntityDTO != null) {
              IKlassInstanceInformationModel klassInstanceElement = KlassInstanceBuilder
                  .getKlassInstanceInformationModel(baseEntityDTO,rdbmsComponentUtils);
              klassInstanceElements.add(klassInstanceElement);
              
              // Element info
              elementIds.add(klassInstanceElement.getId());
              
              // Context Info
              IContextualDataDTO sideContextualObject = entityRelationDTO.getContextualObject();
              if (sideContextualObject != null
                  && sideContextualObject.getContextualObjectIID() != 0) {
                
                // Tag values
                List<IContentTagInstance> contentTagInstances = this
                    .getContextTagInstances(sideContextualObject);
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
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
    return klassInstanceElements;
  }
  
  public  IKlassInstanceRelationshipInstance getRelationshipInstance(
      IRelationship relationshipConfig, IReferencedSectionRelationshipModel relationshipElement)
      throws Exception
  {
    IKlassInstanceRelationshipInstance relationshipInstance = new KlassInstanceRelationshipInstance();
    relationshipInstance.setRelationshipId(relationshipElement.getPropertyId());
    relationshipInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix()));
    relationshipInstance.setSideId(relationshipElement.getId());
    return relationshipInstance;
  }
  
  private List<IContentTagInstance> getContextTagInstances(IContextualDataDTO contextualDataDTO)
  {
    List<IContentTagInstance> tagInstances = new ArrayList<>();
    
    contextualDataDTO.getContextTagValues()
        .forEach(tagRecordDTO -> {
          try {
            String tagInstanceId = String.valueOf(contextualDataDTO.getContextualObjectIID())
                + String.valueOf(tagRecordDTO.getTagValueCode());
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
    
    return tagInstances;
  }
  
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements(){
    return referenceRelationshipInstanceElements;
  }
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements(){
    return referenceNatureRelationshipInstanceElements;
  }
  
  public List<IKlassInstanceRelationshipInstance> getContentRelationships(){
    return contentRelationships;
  }
  
  public List<IKlassInstanceRelationshipInstance> getNatureRelationships(){
    return natureRelationships;
  }
  
  
}
