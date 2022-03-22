package com.cs.core.rdbms.entity.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.entity.idto.IBaseEntityChildrenDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.List;
import java.util.Set;

/**
 * Merging process between 2 base entity DTO
 *
 * @author vallee
 */
public class BaseEntityMerger {
  
  private final BaseEntityDTO refDTO;
  
  /**
   * Constructor by initializing the reference DTO
   *
   * @param referenceDTO
   */
  public BaseEntityMerger(IBaseEntityDTO referenceDTO)
  {
    refDTO = (BaseEntityDTO) referenceDTO;
  }
  
  /**
   * @return the reference entity as result of transforms
   */
  public BaseEntityDTO getEntity()
  {
    return refDTO;
  }
  
  /**
   * Synchronize changes that happened on this base entity with the reference
   *
   * @param jsonTimelineData
   *          change description
   * @param changedEntity
   *          entity content as result of the change
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void synchronizeChange(IJSONContent jsonTimelineData, BaseEntityDTO changedEntity) throws CSFormatException, RDBMSException
  {
    TimelineDTO changes = new TimelineDTO();
    changes.fromJSON(jsonTimelineData.toString());
    for (ITimelineDTO.ChangeCategory changeEvent : changes.getCategories()) {
      for (ICSEElement changeDef : changes.getElements(changeEvent)
          .getSubElements()) {
        synchronizeUnitChange(changeDef, changeEvent, changedEntity);
      }
    }
  }
  
  /**
   * returning propertyIID from CSExpression
   * 
   * @param changeDef
   * @return
   */
  private long getPropertyIIDFromCSEElement(ICSEElement changeDef) throws RDBMSException
  {
    String code = ((CSEObject) changeDef).getCode();
    IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(code);
    return propertyByCode.getPropertyIID();
  }
  
  /**
   * Synchronize a unit set of changes with the reference base entity
   *
   * @param changeDef
   * @param timeLineEvent
   * @param changedEntity
   * @throws CSFormatException
   */
  private void synchronizeUnitChange(ICSEElement changeDef,
      ITimelineDTO.ChangeCategory timeLineEvent, BaseEntityDTO changedEntity) throws CSFormatException, RDBMSException
  {
    switch (timeLineEvent) {
      case UpdatedEntity:
        refDTO.setEntityExtension(changedEntity.getEntityExtension().toString());
        break;
      
      case CreatedRecord:
        long propertyIID = getPropertyIIDFromCSEElement(changeDef);
        IPropertyRecordDTO propertyRecord = refDTO.getPropertyRecord(propertyIID);
        if (propertyRecord == null) {
          refDTO.addPropertyRecord(changedEntity.getPropertyRecord(propertyIID));
        }
        else {
          refDTO.getPropertyRecords().remove(changedEntity.getPropertyRecord(propertyIID));
          refDTO.addPropertyRecord(changedEntity.getPropertyRecord(propertyIID));
        }
        break;
      
      case UpdatedRecord:
        long targetIID = getPropertyIIDFromCSEElement(changeDef);
        propertyRecord = refDTO.getPropertyRecord(targetIID);
        if (propertyRecord != null) {
          SuperType superType = propertyRecord.getProperty().getSuperType();
          if (superType.equals(SuperType.ATTRIBUTE) || superType.equals(SuperType.TAGS)) {
            IPropertyRecordDTO targetPropertyRecord = changedEntity.getPropertyRecord(targetIID);
            if(targetPropertyRecord != null){
              refDTO.getPropertyRecords().remove(targetPropertyRecord);
              refDTO.addPropertyRecord(targetPropertyRecord);
            }
          }
        }
        break;
      
      case DeletedRecord:
        long deletedPropertyIID = getPropertyIIDFromCSEElement(changeDef);
        propertyRecord = refDTO.getPropertyRecord(deletedPropertyIID);
        if (propertyRecord != null) {
          SuperType superType = propertyRecord.getProperty().getSuperType();
          if (superType.equals(SuperType.ATTRIBUTE) || superType.equals(SuperType.TAGS)) {
              refDTO.getPropertyRecords().remove(propertyRecord);
          }
        }
        break;
        
      case CreatedRelation:
        break;
      
      case AddedRelation:
        propertyIID = getPropertyIIDFromCSEElement(changeDef);
        Set<IEntityRelationDTO> relations = ((RelationsSetDTO) changedEntity.getPropertyRecord(propertyIID)).getRelations();

        propertyRecord = refDTO.getPropertyRecord(propertyIID);
        if (propertyRecord == null)
          refDTO.addPropertyRecord(changedEntity.getPropertyRecord(propertyIID));
        ((RelationsSetDTO) refDTO.getPropertyRecord(propertyIID))
            .addRelations(relations.toArray(new IEntityRelationDTO[relations.size()]));
        break;
      
      case RemovedRelation:
        propertyIID = getPropertyIIDFromCSEElement(changeDef);
        IPropertyRecordDTO relation = changedEntity.getPropertyRecord(propertyIID);
        if(relation == null){
          break;
        }
        List<Long> sideBaseEntityIIDs = ((RelationsSetDTO) relation).getSideBaseEntityIIDs();

        if((RelationsSetDTO) refDTO.getPropertyRecord(propertyIID) !=null) {
          ((RelationsSetDTO) refDTO.getPropertyRecord(propertyIID)).setRelations(
              sideBaseEntityIIDs.toArray(new Long[sideBaseEntityIIDs.size()]));
        }

          RelationsSetDTO outRelation = new RelationsSetDTO();
          outRelation.setIID(getPropertyIIDFromCSEElement(changeDef));
          refDTO.getPropertyRecords().remove(outRelation);
        break;
      
      case RemovedChild:
        IBaseEntityChildrenDTO childDTO = new BaseEntityChildrenDTO();
        childDTO.setChildrenID(((CSEObject) changeDef).getCode());
        childDTO.setChildrenIID(((CSEObject) changeDef).getIID());
        refDTO.getChildren().remove(childDTO);
        break;
      
      case AddedChild:
        IBaseEntityChildrenDTO addedChild = new BaseEntityChildrenDTO();
        addedChild.setChildrenID(((CSEObject) changeDef).getCode());
        addedChild.setChildrenIID(((CSEObject) changeDef).getIID());
        refDTO.getChildren().add(addedChild);
        break;
      
      case AddedClassifier:
        refDTO.getOtherClassifiers().addAll(changedEntity.getOtherClassifiers());
        break;
      
      case RemovedClassifier:
        refDTO.getOtherClassifiers().retainAll(changedEntity.getOtherClassifiers());
        break;
      
      case NewDefaultImageIID:
        refDTO.setDefaultImageIID(changedEntity.getDefaultImageIID());
        break;
        
      case CreatedTranslation:
      case DeletedTranslation:
        refDTO.setLocaleIds(changedEntity.getLocaleIds());
        break;
      
      default:
        break;
    }
  }

}
