package com.cs.core.runtime.bulkpropagation;

import com.cs.core.bgprocess.dto.BaseEntityBulkUpdateDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IBaseEntityBulkUpdateDTO;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForRuntimeEntitiesResponseModel;
import com.cs.core.config.interactor.model.klass.IAttributeIdValueModel;
import com.cs.core.config.interactor.model.klass.ITagIdValueModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveBulkEditPermission;
import com.cs.core.runtime.interactor.model.configuration.IBulkApplyValueRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BulkApplyValuesService extends AbstractBulkApplyValuesService<IBulkApplyValueRequestModel, IIdsListParameterModel>
    implements IBulkApplyValuesService {
  
  @Autowired
  protected TransactionThreadData transactionThread;
  
  @Autowired
  protected WorkflowUtils         workflowUtils;
  
  @Autowired
  protected PermissionUtils       permissionUtils;
  
  protected IIdsListParameterModel executeInternal(IBulkApplyValueRequestModel requestModel) throws Exception
  {
    checkBulkEditPermission();
    permissionCheckBeforeTypeSwitch(requestModel);
    
    IIdsListParameterModel returnModel = new IdsListParameterModel();
    TransactionData transactionData = transactionThread.getTransactionData();
    IBaseEntityBulkUpdateDTO bulkUpdateDTO = prepareBulkUpdateDTO(transactionData, requestModel);
    workflowUtils.executeApplicationEvent(IApplicationTriggerModel.ApplicationActionType.BULK_UPDATE, bulkUpdateDTO.toJSON(),
        BGPPriority.MEDIUM);
    return returnModel;
  }
  
  private IBaseEntityBulkUpdateDTO prepareBulkUpdateDTO(TransactionData transactionData, IBulkApplyValueRequestModel requestModel)
      throws Exception
  {
    String physicalCatalogId = transactionData.getPhysicalCatalogId();
    String localeID = transactionData.getDataLanguage();
    String organizationCode = transactionData.getOrganizationId();
    String userId = transactionData.getUserId();
    String userName = transactionData.getUserName();
    
    List<String> addedClassifierIds = new ArrayList<>(requestModel.getAddedTaxonomyIds());
    addedClassifierIds.addAll(requestModel.getAddedKlassIds());
    List<IClassifierDTO> addedClassifiers = new ArrayList<>();
    
    List<String> removedClassifierIds = new ArrayList<>(requestModel.getDeletedTaxonomyIds());
    removedClassifierIds.addAll(requestModel.getDeletedKlassIds());
    List<IClassifierDTO> removedClassifiers = new ArrayList<>();
    
    IBaseEntityBulkUpdateDTO entryData = new BaseEntityBulkUpdateDTO();
    
    List<Long> baseEntityIIDs = new ArrayList<>();
    for (IIdAndBaseType klass : requestModel.getKlassInstances()) {
      baseEntityIIDs.add(Long.valueOf(klass.getId()));
    }
    
    for (IAttributeIdValueModel attribute : requestModel.getAttributes()) {
      handleAttributes(localeID, entryData, attribute);
    }
    
    for (ITagIdValueModel tag : requestModel.getTags()) {
      handleTags(entryData, tag);
    }
    
    for (String addedClassifierId : addedClassifierIds) {
      IClassifierDTO addedClassifierDto = ConfigurationDAO.instance().getClassifierByCode(addedClassifierId);
      addedClassifiers.add(addedClassifierDto);
    }
    
    for (String removedClassifierId : removedClassifierIds) {
      IClassifierDTO removedClassifierDto = ConfigurationDAO.instance().getClassifierByCode(removedClassifierId);
      removedClassifiers.add(removedClassifierDto);
    }
    
    entryData.getAddedClassifiers().addAll(addedClassifiers);
    entryData.getRemovedClassifiers().addAll(removedClassifiers);
    
    entryData.setBaseEntityIIDs(baseEntityIIDs);
    entryData.setLocaleID(localeID);
    entryData.setCatalogCode(physicalCatalogId);
    entryData.setOrganizationCode(organizationCode);
    entryData.setUserId(userId);
    entryData.setUserName(userName);
    return entryData;
  }
  
  private void handleAttributes(String dataLanguage, IBaseEntityBulkUpdateDTO entryData, IAttributeIdValueModel attribute)
      throws Exception, RDBMSException
  {
    IPropertyDTO attributeDTO = RDBMSUtils.newPropertyDTO(
        ConfigurationDAO.instance().getPropertyByCode(attribute.getAttributeId()).getPropertyIID(), null, attribute.getAttributeId(),
        PropertyType.UNDEFINED);
    String value = attribute.getValue();
    String valueAsHtml = attribute.getValueAsHtml();
    IValueRecordDTO attributeValueRecord = RDBMSUtils.newValueRecordDTO(0L, 0L, attributeDTO, value, dataLanguage);
    if (!valueAsHtml.isEmpty()) {
      attributeValueRecord.setAsHTML(valueAsHtml);
    }
    entryData.getPropertyRecords().add(attributeValueRecord);
  }
  
  private void handleTags(IBaseEntityBulkUpdateDTO entryData, ITagIdValueModel tag) throws Exception
  {
    String propertyCode = tag.getTagId();
    long propertyIID = ConfigurationDAO.instance().getPropertyByCode(propertyCode).getPropertyIID();
    IPropertyDTO tagDTO = RDBMSUtils.newPropertyDTO(propertyIID, propertyCode, propertyCode, PropertyType.TAG);
    
    ITagsRecordDTO tagRecord = RDBMSUtils.newTagsRecordDTO(0L, tagDTO);
    List<ITagDTO> tagValueRecords = new ArrayList<>();
    tag.getTagValues().forEach(tagValue -> {
      try {
        ITagDTO dto = RDBMSUtils.newTagRecordDTO(0L, tagValue.getRelevance(), tagValue.getTagId(), tagValue.getTagId(), propertyIID);
        tagValueRecords.add(dto);
      }
      catch (Exception e) {
        new RuntimeException(e);
      }
    });
    tagRecord.setTags(tagValueRecords.toArray(new ITagDTO[tagValueRecords.size()]));
    entryData.getPropertyRecords().add(tagRecord);
  }
  
  private void permissionCheckBeforeTypeSwitch(IBulkApplyValueRequestModel requestModel) throws Exception
  {
    Map<String, IGlobalPermission> globalPermissionMap = getGlobalPermissionForTaxonomyAndKlass(requestModel);
    requestModel.setAddedTaxonomyIds(filterAddedTypeAndTaxonomy(requestModel.getAddedTaxonomyIds(), globalPermissionMap));
    requestModel.setAddedKlassIds(filterAddedTypeAndTaxonomy(requestModel.getAddedKlassIds(), globalPermissionMap));
    requestModel.setDeletedTaxonomyIds(filterDeletedTypeAndTaxonomy(requestModel.getDeletedTaxonomyIds(), globalPermissionMap));
    requestModel.setDeletedKlassIds(filterDeletedTypeAndTaxonomy(requestModel.getDeletedKlassIds(), globalPermissionMap));
  }
  
  protected Map<String, IGlobalPermission> getGlobalPermissionForTaxonomyAndKlass(IBulkApplyValueRequestModel requestModel) throws Exception
  {
    String userId = context.getUserId();
    List<String> taxonomyIds = new ArrayList<>();
    taxonomyIds.addAll(requestModel.getAddedTaxonomyIds());
    taxonomyIds.addAll(requestModel.getDeletedTaxonomyIds());
    
    List<String> klassIds = new ArrayList<>();
    klassIds.addAll(requestModel.getAddedKlassIds());
    klassIds.addAll(requestModel.getDeletedKlassIds());
    
    IGetGlobalPermissionForRuntimeEntitiesResponseModel globalPermissionModel = permissionUtils
        .getGlobalPermissionsForRuntimeEntities(userId, taxonomyIds, klassIds);
    Map<String, IGlobalPermission> globalPermissions = globalPermissionModel.getGlobalPermissions();
    
    return globalPermissions;
  }
  
  protected List<String> filterAddedTypeAndTaxonomy(List<String> listOfAddedIds, Map<String, IGlobalPermission> globalPermissions)
  {
    return listOfAddedIds.stream().filter(idToCreate -> globalPermissions.get(idToCreate).getCanCreate()).collect(Collectors.toList());
  }
  
  protected List<String> filterDeletedTypeAndTaxonomy(List<String> listOfDeletedIds, Map<String, IGlobalPermission> globalPermissions)
  {
    return listOfDeletedIds.stream().filter(idToDelete -> globalPermissions.get(idToDelete).getCanDelete()).collect(Collectors.toList());
  }
  
  private void checkBulkEditPermission() throws Exception
  {
    IFunctionPermissionModel functionPermission = permissionUtils.getFunctionPermissionByUserId();
    if (!functionPermission.getCanBulkEdit()) {
      throw new UserNotHaveBulkEditPermission();
    }
  }

}
