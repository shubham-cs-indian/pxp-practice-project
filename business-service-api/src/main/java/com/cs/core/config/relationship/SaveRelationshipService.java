package com.cs.core.config.relationship;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.BGPRelationshipContextRemoveDTO;
import com.cs.core.bgprocess.dto.RelationshipDataTransferOnConfigChangeDTO;
import com.cs.core.bgprocess.idto.IBGPRelationshipContextRemoveDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IRelationshipDataTransferOnConfigChangeDTO;
import com.cs.core.config.interactor.model.relationship.*;
import com.cs.core.config.strategy.usecase.relationship.ISaveRelationshipStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.coupling.dto.ModifiedPropertiesCouplingDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedPropertiesCouplingDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaveRelationshipService extends AbstractSaveConfigService<ISaveRelationshipModel, IGetRelationshipModel>
    implements ISaveRelationshipService {
  
  private static final String         SERVICE      = "RELATIONSHIP_DATA_TRANSFER_ON_CONFIG_CHANGE";
  
  private static final BGPPriority    BGP_PRIORITY = BGPPriority.HIGH;
  
  @Autowired
  protected ISaveRelationshipStrategy saveRelationshipStrategy;
  
  @Autowired
  protected RDBMSComponentUtils       rdbmsComponentUtils;
  
  @Override
  public IGetRelationshipModel executeInternal(ISaveRelationshipModel dataModel) throws Exception
  {
    RelationshipValidations.validateSave(dataModel);
    ISaveRelationshipStrategyResponseModel responseModel = saveRelationshipStrategy.execute(dataModel);
    prepareDataForValuePropagationOnSaveRelationship(dataModel);
    prepareDataForConfigCleanup(responseModel.getRemovedContextInfo());

    IGetRelationshipModel returnModel = responseModel.getRelationshipResponse();
    returnModel.setAuditLogInfo(responseModel.getAuditLogInfo());
    return returnModel;
  }
  
  private void prepareDataForValuePropagationOnSaveRelationship(ISaveRelationshipModel dataModel) throws Exception
  {
    List<Long> deletedPropertyIIDs = new ArrayList<>();
    List<Long> side1AddedPropertyIIDs = new ArrayList<>();
    List<Long> side2AddedPropertyIIDs = new ArrayList<>();
    List<IModifiedPropertiesCouplingDTO> modifiedProperties = new ArrayList<>();
    
    Long relationIID = dataModel.getPropertyIID();
    
    fillDeletedPropertyIIDs(dataModel, deletedPropertyIIDs);
    fillAddedPropertyIIDs(dataModel, side1AddedPropertyIIDs, side2AddedPropertyIIDs);
    fillModifiedPropertyIIDs(dataModel, modifiedProperties);
    
    IRelationshipDataTransferOnConfigChangeDTO relationshipDataTransferOnConfigChange = new RelationshipDataTransferOnConfigChangeDTO();
    relationshipDataTransferOnConfigChange.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    relationshipDataTransferOnConfigChange.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    relationshipDataTransferOnConfigChange
        .setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    relationshipDataTransferOnConfigChange.setUserId(transactionThread.getTransactionData().getUserId());
    relationshipDataTransferOnConfigChange.setRelationshipIID(relationIID);
    relationshipDataTransferOnConfigChange.setDeletedPropertyIIDs(deletedPropertyIIDs);
    relationshipDataTransferOnConfigChange.setSide1AddedPropertyIIDs(side1AddedPropertyIIDs);
    relationshipDataTransferOnConfigChange.setSide2AddedPropertyIIDs(side2AddedPropertyIIDs);
    relationshipDataTransferOnConfigChange.setModifiedProperties(modifiedProperties);
    relationshipDataTransferOnConfigChange.setIsNature(false);
    BGPDriverDAO.instance().submitBGPProcess(context.getUserName(), SERVICE, "", BGP_PRIORITY,
        new JSONContent(relationshipDataTransferOnConfigChange.toJSON()));
  }
  
  private void fillModifiedPropertyIIDs(ISaveRelationshipModel dataModel, List<IModifiedPropertiesCouplingDTO> modifiedPropertiesDTO)
      throws RDBMSException
  {
    List<IModifiedRelationshipPropertyModel> modifiedProperties = dataModel.getModifiedAttributes();
    modifiedProperties.addAll(dataModel.getModifiedTags());
    
    for (IModifiedRelationshipPropertyModel attribute : modifiedProperties) {
      IModifiedPropertiesCouplingDTO couplingDTO = new ModifiedPropertiesCouplingDTO();
      couplingDTO.setPropertyIID(ConfigurationDAO.instance().getPropertyByCode(attribute.getId()).getPropertyIID());
      
      if (attribute.getCouplingType().equals(CommonConstants.TIGHTLY_COUPLED)) {
        couplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP.ordinal());
        couplingDTO.setCouplingType(CouplingBehavior.TIGHTLY.ordinal());
      }
      else {
        couplingDTO.setCouplingSourceType(CouplingType.DYN_RELATIONSHIP.ordinal());
        couplingDTO.setCouplingType(CouplingBehavior.DYNAMIC.ordinal());
      }
      modifiedPropertiesDTO.add(couplingDTO);
    }
  }
  
  private void fillAddedPropertyIIDs(ISaveRelationshipModel dataModel, List<Long> side1AddedPropertyIIDs, List<Long> side2AddedPropertyIIDs)
      throws RDBMSException
  {
    for (IModifiedRelationshipPropertyModel attribute : dataModel.getAddedAttributes()) {
      if (attribute.getSide().equals("side1")) {
        long side1PropertyIID = ConfigurationDAO.instance().getPropertyByCode(attribute.getId()).getPropertyIID();
        side1AddedPropertyIIDs.add(side1PropertyIID);
      }
      else if (attribute.getSide().equals("side2")) {
        long side2PropertyIID = ConfigurationDAO.instance().getPropertyByCode(attribute.getId()).getPropertyIID();
        side2AddedPropertyIIDs.add(side2PropertyIID);
      }
    }
    
    for (IModifiedRelationshipPropertyModel tag : dataModel.getAddedTags()) {
      if (tag.getSide().equals("side1")) {
        long side1PropertyIID = ConfigurationDAO.instance().getPropertyByCode(tag.getId()).getPropertyIID();
        side1AddedPropertyIIDs.add(side1PropertyIID);
      }
      else if (tag.getSide().equals("side2")) {
        long side2PropertyIID = ConfigurationDAO.instance().getPropertyByCode(tag.getId()).getPropertyIID();
        side2AddedPropertyIIDs.add(side2PropertyIID);
      }
    }
  }
  
  private void fillDeletedPropertyIIDs(ISaveRelationshipModel dataModel, List<Long> deletedPropertyIIDs) throws RDBMSException
  {
    
    List<IModifiedRelationshipPropertyModel> deletedProperties = dataModel.getDeletedAttributes();
    deletedProperties.addAll(dataModel.getDeletedTags());
    
    for (IModifiedRelationshipPropertyModel attribute : deletedProperties) {
      long propertyIID = ConfigurationDAO.instance().getPropertyByCode(attribute.getId()).getPropertyIID();
      deletedPropertyIIDs.add(propertyIID);
    }
  }
  
  private void prepareDataForConfigCleanup(IRemovedContextInfoModel removedContextInfo) throws Exception
  {
    if (removedContextInfo.getRemovedSide1ContextId().isEmpty() && removedContextInfo.getRemovedSide2ContextId().isEmpty()) {
      return;
    }
    IBGPRelationshipContextRemoveDTO relationshipContextRemoveDTO = new BGPRelationshipContextRemoveDTO();
    relationshipContextRemoveDTO.setRelationshipPropertyId(removedContextInfo.getRelationshipPropertyId());
    relationshipContextRemoveDTO.setRemovedSide1ContextId(removedContextInfo.getRemovedSide1ContextId());
    relationshipContextRemoveDTO.setRemovedSide2ContextId(removedContextInfo.getRemovedSide2ContextId());
    BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), "SAVE_RELATIONSHIP", "",
        IBGProcessDTO.BGPPriority.LOW, new JSONContent(relationshipContextRemoveDTO.toJSON()));
    
  }
}
