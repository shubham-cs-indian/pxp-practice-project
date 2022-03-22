package com.cs.core.runtime.klassinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.attribute.IGetAttributesStrategy;
import com.cs.core.config.strategy.usecase.attribute.IGetRolesStrategy;
import com.cs.core.config.strategy.usecase.tag.IGetTagsByIdStrategy;
import com.cs.core.config.strategy.usecase.task.IGetConfigDetailsForTasksTabStrategy;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.runtime.builder.TaskInstanceBuilder;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestStrategyModelForTasksTab;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModelForTasksTab;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.taskinstance.GetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceInformationModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForTasksTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.utils.BaseEntityUtils;

@SuppressWarnings("unchecked")
public abstract class AbstractGetInstanceForTaskTabService<P extends IGetInstanceRequestModel, R extends IGetTaskInstanceResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected IGetConfigDetailsForTasksTabStrategy getConfigDetailsForTasksTabStrategy;
  
  @Autowired
  protected IGetAttributesStrategy               getAttributesByIdsStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                  rdbmsComponentUtils;
  
  @Autowired
  protected IGetTagsByIdStrategy                 getTagsByIdsStrategy;
  
  @Autowired
  protected IGetRolesStrategy                    getRolesByIdsStrategy;
  
  @Autowired
  protected PermissionUtils                      permissionUtils;
  
  @Autowired
  protected KlassInstanceUtils                   klassInstanceUtils;
  

  
  protected abstract IGetTaskInstanceResponseModel executeGetKlassInstance(
      IGetInstanceRequestStrategyModel getKlassInstanceTreeStrategyModel) throws Exception;
  
  protected abstract IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception;
  
  @Override
  protected R executeInternal(P instanceRequestModel) throws Exception
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils
        .getBaseEntityDAO(Long.parseLong(instanceRequestModel.getId()));
    
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setTemplateId(instanceRequestModel.getTemplateId());
    multiclassificationRequestModel.getKlassIds()
        .addAll(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO));
    multiclassificationRequestModel.getSelectedTaxonomyIds()
        .addAll(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers()));
    
    multiclassificationRequestModel.setLanguageCodes(baseEntityDAO.getBaseEntityDTO().getLocaleIds());
    
    IGetConfigDetailsForTasksTabModel configDetails = getConfigDetails(
        multiclassificationRequestModel);
    
    IGetInstanceRequestStrategyModel getInstanceRequestStrategyModel = new GetInstanceRequestStrategyModelForTasksTab(
        instanceRequestModel);
    getInstanceRequestStrategyModel.setRoleId(instanceRequestModel.getRoleId());
    ((IGetInstanceRequestStrategyModelForTasksTab) getInstanceRequestStrategyModel)
        .setReferencedTaskIds(configDetails.getReferencedTasks()
            .keySet());
    ((IGetInstanceRequestStrategyModelForTasksTab) getInstanceRequestStrategyModel)
        .setReferencedLifeCycleStatusTags(configDetails.getReferencedLifeCycleStatusTags());
    ((IGetInstanceRequestStrategyModelForTasksTab) getInstanceRequestStrategyModel)
        .setPersonalTaskIds(configDetails.getPersonalTaskIds());
    
    IReferencedTemplatePermissionModel referencedPermissionModel = configDetails
        .getReferencedPermissions();
    ((IGetInstanceRequestStrategyModelForTasksTab) getInstanceRequestStrategyModel)
        .setTaskIdsHavingReadPermissions(
            referencedPermissionModel.getTaskIdsHavingReadPermissions());
    ((IGetInstanceRequestStrategyModelForTasksTab) getInstanceRequestStrategyModel)
        .setTaskIdsForRolesHavingReadPermission(
            referencedPermissionModel.getTaskIdsForRolesHavingReadPermission());
    ((IGetInstanceRequestStrategyModelForTasksTab) getInstanceRequestStrategyModel)
        .setEntities(referencedPermissionModel.getEntitiesHavingRP());
    ((IGetInstanceRequestStrategyModelForTasksTab) getInstanceRequestStrategyModel)
        .setKlassIdsHavingRP(referencedPermissionModel.getKlassIdsHavingRP());
    
    
    IGetTaskInstanceResponseModel returnModel = new GetTaskInstanceResponseModel();
    
    this.getKlassInstance(instanceRequestModel, configDetails, baseEntityDAO, returnModel);
    this.getTaskInstanceList(instanceRequestModel, configDetails, baseEntityDAO, returnModel);
    
    Map<String, IIdAndTypeModel> elementsMapping = returnModel.getReferencedElementsMapping();
    Map<String, String> referencedElements = new HashMap<>();
    fillReferencedElements(referencedElements, elementsMapping);
    returnModel.setReferencedElements(referencedElements);
    returnModel.setReferencedElementsMapping(elementsMapping);
    
    returnModel.setConfigDetails(configDetails);
    returnModel.setVariantOfLabel(klassInstanceUtils.getVariantOfLabel(baseEntityDAO, configDetails.getLinkedVariantCodes()));
    
    permissionUtils.manageKlassInstancePermissions(returnModel);
    
    return (R) returnModel;
  }
  
  protected void fillReferencedElements(Map<String, String> referencedElements,
      Map<String, IIdAndTypeModel> elementsMapping) throws Exception
  {
    List<String> attributeIds = new ArrayList<String>();
    List<String> tagIds = new ArrayList<String>();
    List<String> roleIds = new ArrayList<String>();
    for (Map.Entry<String, IIdAndTypeModel> entry : elementsMapping.entrySet()) {
      String id = entry.getKey();
      IIdAndTypeModel model = entry.getValue();
      if (model.getType()
          .equals(Constants.ATTRIBUTE)) {
        attributeIds.add(model.getId());
      }
      else if (model.getType()
          .equals(Constants.TAGS)) {
        tagIds.add(model.getId());
      }
      else {
        roleIds.add(model.getId());
      }
    }
    IListModel<IAttribute> attributes = null;
    IListModel<ITag> tags = null;
    IListModel<IConfigEntityInformationModel> roles = null;
    
    if (attributeIds.size() > 0) {
      attributes = getAttributesByIds(attributeIds);
    }
    
    if (tagIds.size() > 0) {
      tags = getTagsByIds(tagIds);
    }
    
    if (roleIds.size() > 0) {
      roles = getRolesByIds(roleIds);
    }
    
    if (attributes != null) {
      for (IAttribute attribute : attributes.getList()) {
        referencedElements.put(attribute.getId(), attribute.getLabel());
      }
    }
    
    if (tags != null) {
      for (ITag tag : tags.getList()) {
        referencedElements.put(tag.getId(), tag.getLabel());
      }
    }
    
    if (roles != null) {
      for (IConfigEntityInformationModel role : roles.getList()) {
        referencedElements.put(role.getId(), role.getLabel());
      }
    }
  }
  
  protected IGetConfigDetailsForTasksTabModel getConfigDetails(
      IMulticlassificationRequestModel model) throws Exception
  {
    return getConfigDetailsForTasksTabStrategy.execute(model);
  }
  
  protected IListModel<IAttribute> getAttributesByIds(List<String> attributeIds) throws Exception
  {
    IIdsListParameterModel idsListModel = new IdsListParameterModel();
    idsListModel.setIds(attributeIds);
    return getAttributesByIdsStrategy.execute(idsListModel);
  }
  
  protected IListModel<ITag> getTagsByIds(List<String> tagIds) throws Exception
  {
    IIdsListParameterModel idsListModel = new IdsListParameterModel();
    idsListModel.setIds(tagIds);
    return getTagsByIdsStrategy.execute(idsListModel);
  }
  
  protected IListModel<IConfigEntityInformationModel> getRolesByIds(List<String> roleIds)
      throws Exception
  {
    IIdsListParameterModel idsListModel = new IdsListParameterModel();
    idsListModel.setIds(roleIds);
    return getRolesByIdsStrategy.execute(idsListModel);
  }
  
  protected void getKlassInstance(IGetInstanceRequestModel getKlassInstanceTreeStrategyModel,
      IGetConfigDetailsForTasksTabModel configDetails, IBaseEntityDAO baseEntityDAO,
      IGetTaskInstanceResponseModel returnModel) throws Exception
  {
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO,
        configDetails, rdbmsComponentUtils);
    returnModel
        .setKlassInstance((IContentInstance) klassInstanceBuilder.getKlassInstance());
  }
  
  protected void getTaskInstanceList(P getKlassInstanceTreeStrategyModel,
      IGetConfigDetailsForTasksTabModel configDetails, IBaseEntityDAO baseEntityDAO,
      IGetTaskInstanceResponseModel returnModel) throws Exception
  {
    ITaskRecordDAO taskRecordDAO = this.rdbmsComponentUtils.openTaskDAO();
    
    //Task info list
    List<ITaskRecordDTO> taskInstances = taskRecordDAO
        .getAllTaskByBaseEntityIID(baseEntityDAO.getBaseEntityDTO()
            .getBaseEntityIID(), configDetails.getRoleIdOfCurrentUser());
    List<ITaskInstanceInformationModel> taskInstanceList = returnModel.getTaskInstanceList();
    for (ITaskRecordDTO taskRecordDTO : taskInstances) {
      ITaskModel taskConfig = configDetails.getReferencedTasks().get(taskRecordDTO.getTask().getCode());
      ITaskInstanceInformationModel taskInstanceInformationModel = TaskInstanceBuilder.getTaskInstanceInformationModel(taskRecordDTO, taskConfig);
      if(taskInstanceInformationModel != null) {
        taskInstanceList.add(taskInstanceInformationModel);
      }
      
      if(taskRecordDTO.getPropertyIID() != 0) {
        this.getReferenceElement(taskRecordDTO, returnModel);
      }
    }
    returnModel.setTaskInstanceList(taskInstanceList);
    
    //Task count
    int taskCountOnBaseEntity = taskRecordDAO.getTaskCountOnBaseEntity(baseEntityDAO.getBaseEntityDTO()
            .getBaseEntityIID(), configDetails.getRoleIdOfCurrentUser());
    returnModel.setTasksCount(taskCountOnBaseEntity);
  }
  
  protected void getReferenceElement(ITaskRecordDTO taskRecordDTO, IGetTaskInstanceResponseModel returnModel) throws Exception
  {
    Map<String, IIdAndTypeModel> referencedElementsMapping = returnModel.getReferencedElementsMapping();
    IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByIID(taskRecordDTO.getPropertyIID());
    IIdAndTypeModel idAndTypeModel = new IdAndTypeModel();
    idAndTypeModel.setId(propertyDTO.getCode());
    if(propertyDTO.getSuperType() == SuperType.ATTRIBUTE) {
      idAndTypeModel.setType(Constants.ATTRIBUTE);
    }else if(propertyDTO.getSuperType() == SuperType.TAGS) {
      idAndTypeModel.setType(Constants.TAGS);
    }
    referencedElementsMapping.put(propertyDTO.getCode(), idAndTypeModel);
    
    Map<String, String> referencedElements = returnModel.getReferencedElements();
    referencedElements.put(propertyDTO.getCode(), propertyDTO.getCode());
  }
  
}
