package com.cs.core.runtime.strategy.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.runtime.interactor.model.bulkpropagation.ContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.datarule.IAddedRemovedElementsModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.relationship.EntityRelationshipInfoModel;
import com.cs.core.runtime.interactor.model.relationship.IEntityRelationshipInfoModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInputModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipDataTransferInputModel;
import com.cs.core.runtime.interactor.model.transfer.DataTransferInputModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RuntimeBeanFactory;

@Component
public class BulkPropagationUtil {
  
  /**
   * @param dataTransferInformation
   * @throws Exception
   * @author Aayush
   */
  public void initiateVirtualReferencesUpdateOnAdditionOrRemovalOfRelationshipElements(
      List<IRelationshipDataTransferInfoModel> dataTransferInformation) throws Exception
  {
    if (dataTransferInformation.isEmpty()) {
      return;
    }
    
    IContentDiffModelToPrepareDataForBulkPropagation contentDiffModel = new ContentDiffModelToPrepareDataForBulkPropagation();
    contentDiffModel.setUpdateCriidAndDefaultAssetInstanceIdInfoModel(dataTransferInformation);

    //TODO: BGP
  }  
  /**
   * @param contentId:
   *          being saved or created or cloned
   * @param dataTransferInformation
   * @param langaugeCodes
   * @param list
   * @throws Exception
   * @author Aayush
   */
  public void initiateRelationshipDataTransfer(String contentId,
      List<IRelationshipDataTransferInfoModel> dataTransferInformation, List<String> langaugeCodes)
      throws Exception
  {
    for (IRelationshipDataTransferInfoModel iRelationshipDataTransferInfoModel : dataTransferInformation) {
      if (!iRelationshipDataTransferInfoModel.getContentId()
          .equals(contentId)) {
        continue;
      }
      
      IRelationshipDataTransferInputModel dataForRelationshipDataTransfer = new RelationshipDataTransferInputModel();
      
      IDataTransferInputModel dataForDataTransfer = new DataTransferInputModel();
    //  dataForDataTransfer.setContentId(iRelationshipDataTransferInfoModel.getContentId());
   //   dataForDataTransfer.setBaseType(iRelationshipDataTransferInfoModel.getBaseType());
      dataForDataTransfer.setModifiedLanguageCodes(langaugeCodes);
      dataForRelationshipDataTransfer.setDataTransfer(dataForDataTransfer);
       dataForRelationshipDataTransfer.setModifiedRelationships(
          getModifiedRelationshipDataOnAddedElements(iRelationshipDataTransferInfoModel));
      
      if (!dataForRelationshipDataTransfer.getModifiedRelationships()
          .isEmpty()) {
      //TODO: BGP
        
      }
      
      break;
    }
  }
  
  /**
   * @param contentId
   * @param baseType
   * @param typesToApply
   * @param taxonomiesToApply
   * @throws Exception
   */
  public void handlePropagationForRules(String contentId, String baseType,
      List<String> typesToApply, List<String> taxonomiesToApply) throws Exception
  {
    IKlassInstanceTypeSwitchModel switchTypeRequest = new KlassInstanceTypeSwitchModel();
    switchTypeRequest.setKlassInstanceId(contentId);
    switchTypeRequest.setAddedSecondaryTypes(typesToApply);
    switchTypeRequest.setAddedTaxonomyIds(taxonomiesToApply);
    switchTypeRequest.setTabId(SystemLevelIds.OVERVIEW_TAB);
    switchTypeRequest.setTabType(CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE);
    switchTypeRequest.setShouldCheckPermission(false);
    switchTypeRequest.setIsMinorTaxonomySwitch(false);
    switchTypeRequest.setIsNatureKlassSwitched(false);
    
    String switchTypeClassName = RuntimeBeanFactory.getSwitchTypeStrategy(baseType);
   //TODO: BGP
  }
  
  /**
   * **************************************************** private methods
   * **********************************************************
   */
  
  /**
   * @param dataTransferInfo
   * @return
   */
 private Map<String, IEntityRelationshipInfoModel> getModifiedRelationshipDataOnAddedElements(
     IRelationshipDataTransferInfoModel dataTransferInfo)
 {
   Map<String, IEntityRelationshipInfoModel> modifiedRelationshipMap = new HashMap<>();
   Map<String, IEntityRelationshipInfoDTO> relationshipIdAddedDeletedElementsMap = dataTransferInfo
       .getRelationshipIdAddedDeletedElementsMap();
   for (Map.Entry<String, IEntityRelationshipInfoDTO> entry : relationshipIdAddedDeletedElementsMap
       .entrySet()) {
     
     String relationshipSideId = entry.getKey();
     String[] split = relationshipSideId.split("__");
     String relationshipId = split[0];
     String sideId = split[1];
     
     IEntityRelationshipInfoDTO addedRemovedModel = entry.getValue();
     IEntityRelationshipInfoModel addedRelationship = new EntityRelationshipInfoModel();
     addedRelationship.setRelationshipId(relationshipId);
     addedRelationship.setSideId(sideId);
     //addedRelationship.setAddedEntities(addedRemovedModel.getAddedElements());
     //addedRelationship.setRemovedEntities(addedRemovedModel.getRemovedElements());
     modifiedRelationshipMap.put(relationshipId, addedRelationship);
   }
   return modifiedRelationshipMap;
 }
}
