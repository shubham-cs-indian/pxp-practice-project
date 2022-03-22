package com.cs.bds.runtime.usecase.relationshipInheritance;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.relationship.dto.RelationCoupleRecordDTO.RelationCoupleRecordDTOBuilder;
import com.cs.core.rdbms.relationship.idao.IRelationCoupleRecordDAO;
import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTO;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflictingModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IResolveRelationshipConflictsRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.GetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForSaveRelationshipInstancesStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.utils.BaseEntityUtils;

@Service
public class ResolveRelationshipInheritanceConflictService
    extends AbstractRuntimeService<IResolveRelationshipConflictsRequestModel, IGetKlassInstanceCustomTabModel>
    implements IResolveRelationshipInheritanceConflictService {
  
  @Autowired
  protected RDBMSComponentUtils                                  rdbmsComponentUtils;
  
  @Autowired
  protected KlassInstanceUtils                                    klassInstanceUtils;
  
  @Autowired
  RelationshipInheritance                                         relationshipInheritance;
  
  @Autowired
  protected IGetConfigDetailsForSaveRelationshipInstancesStrategy getConfigDetailsForSaveRelationshipInstancesStrategy;
  
  @Override
  public IGetKlassInstanceCustomTabModel executeInternal(IResolveRelationshipConflictsRequestModel dataModel) throws Exception
  {
    for (IRelationshipConflictingModel resolveConflict : dataModel.getResolvedConflicts()) {
      
      IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = getConfigDetails(resolveConflict);
      Map<String, IReferencedRelationshipModel> referenceRelationship = configDetails.getReferencedRelationships();
      IReferencedRelationshipModel relationship = referenceRelationship.get(resolveConflict.getPropagableRelationshipId());
      Set<IEntityRelationDTO> sourceRelationsList = getRelations(resolveConflict.getConflicts().get(0).getSourceContentId(), relationship);
      Set<IEntityRelationDTO> targetRelationsList = getRelations(dataModel.getTargetId(), relationship);
      
      IBaseEntityDTO entityDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getEntityByIID(Long.parseLong(dataModel.getTargetId()));
      IPropertyDTO relationProperty = RDBMSUtils.getPropertyByCode(resolveConflict.getPropagableRelationshipId());
      
      
      BaseEntityDAO baseEntityDAO = (BaseEntityDAO) rdbmsComponentUtils.getLocaleCatlogDAO().openBaseEntity(entityDTO);
      
      IContentRelationshipInstanceModel modifiedRelationship = new ContentRelationshipInstanceModel();
      
      modifiedRelationship.setRelationshipId(relationProperty.getCode());
      modifiedRelationship.setId(relationProperty.getPropertyCode());
      modifiedRelationship.setSideId(resolveConflict.getPropagableRelationshipSideId());
      
      if(!sourceRelationsList.isEmpty()) {
        modifiedRelationship.setBaseType(BaseEntityUtils.getBaseTypeString(rdbmsComponentUtils.getLocaleCatlogDAO().getEntityByIID( sourceRelationsList.iterator().next().getOtherSideEntityIID()).getBaseType()));
      } 
      
      relationshipInheritance.addRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, baseEntityDAO, modifiedRelationship);
      
      relationshipInheritance.removeRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, modifiedRelationship);
      
      relationshipInheritance.modifyRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, baseEntityDAO, modifiedRelationship);
      
      List<IContentRelationshipInstanceModel> modifiedRelationships  =new ArrayList<>();
     
      if(!modifiedRelationship.getAddedElements().isEmpty() || !modifiedRelationship.getDeletedElements().isEmpty() || !modifiedRelationship.getModifiedElements().isEmpty()) {
        modifiedRelationships.add(modifiedRelationship);
      }
      
      relationshipInheritance.adaptRelationOnRelationshipInheritance( entityDTO, rdbmsComponentUtils.getLocaleCatlogDAO(),  entityDTO.getBaseType(),
         modifiedRelationships);
      
      // when relation to tranfere is empty & conflic needs to  be resolve
      IRelationCoupleRecordDAO relationCoupleRecordDao = rdbmsComponentUtils.getLocaleCatlogDAO().openRelationCoupleRecordDAO();
      IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().isResolved(true)
          .targetEntityId(Long .parseLong(dataModel.getTargetId())).propagableRelationshipId(relationProperty.getPropertyIID()).propagableRelationshipSideId(resolveConflict.getPropagableRelationshipSideId()).build();
      relationCoupleRecordDao.updateConflictResolvedStatus(relationCoupleRecord);
    }
    return (IGetKlassInstanceCustomTabModel)klassInstanceUtils.prepareDataForResponse(dataModel );
  }

  private Set<IEntityRelationDTO> getRelations(String entityId, IReferencedRelationshipModel relationship)
      throws NumberFormatException, Exception
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(entityId));
    IPropertyDTO propertyDto = ConfigurationDAO.instance().getPropertyByIID(relationship.getPropertyIID());
    IBaseEntityDTO result = baseEntityDAO.loadPropertyRecords(propertyDto);
    Set<IEntityRelationDTO> relations = new HashSet<>();
    for(IPropertyRecordDTO sourceRelationRecord : result.getPropertyRecords()) {
      IRelationsSetDTO sourceRelation = (IRelationsSetDTO)  sourceRelationRecord;
      relations = sourceRelation.getRelations();
    }
    return relations;
  }
  
  private IGetConfigDetailsForSaveRelationshipInstancesResponseModel getConfigDetails(IRelationshipConflictingModel resolveConflict)
      throws Exception
  {
    
    IGetConfigDetailsForSaveRelationshipInstancesRequestModel configDetailsRequestModel = new GetConfigDetailsForSaveRelationshipInstancesRequestModel();
    configDetailsRequestModel.setRelationshipIds(new ArrayList<>(List.of(resolveConflict.getPropagableRelationshipId())));
    configDetailsRequestModel.setNatureRelationshipIds(new ArrayList<>(List.of(resolveConflict.getConflicts().get(0).getRelationshipId())));
    
    IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = getConfigDetailsForSaveRelationshipInstancesStrategy
        .execute(configDetailsRequestModel);
    return configDetails;
  }
  
}
