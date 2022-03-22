package com.cs.core.runtime.klassinstance;

import com.cs.config.standard.IConfigMap;
import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.attribute.DateInstanceModel;
import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.variants.*;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractGetPropertiesVariantInstancesInTableViewService<P extends IGetPropertiesVariantInstanceInTableViewRequestModel, R extends IGetPropertiesVariantInstancesInTableViewModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected IGetConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategy getPropertiesVariantInstancesInTableViewStrategy;
  
  @Autowired
  protected ISessionContext                                                      context;
  
  @Autowired
  protected PermissionUtils                                                      permissionUtils;
  
  @Autowired
  protected RDBMSComponentUtils                                                  rdbmsComponentUtils;
  
  protected abstract IGetPropertiesVariantInstancesInTableViewModel executeGetVariantInstances(
      IGetPropertiesVariantInstancesInTableViewRequestStrategyModel requestModel) throws Exception;
  
  protected abstract IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception;
  
  @Override
  protected R executeInternal(P requestModel) throws Exception
  {
    
    String parentId = requestModel.getParentId();
    String klassInstanceId = requestModel.getKlassInstanceId();
    String contextId = requestModel.getContextId();
    String instanceId = getInstanceIdToGetType(parentId, klassInstanceId);
    
    IBaseEntityDAO baseEntityDao = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(instanceId));
    IKlassInstanceTypeModel klassInstanceTypeModel = getKlassInstanceType(instanceId);
    
    List<String> classifiers = BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDao);
    List<String> taxonomyIds = BaseEntityUtils
        .getReferenceTaxonomyIdsFromBaseEntity(baseEntityDao.getClassifiers());
    
    klassInstanceTypeModel.setTypes(classifiers);
    klassInstanceTypeModel.setSelectedTaxonomyIds(taxonomyIds);
    
    IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel requestStratetgyModel = getConfigDetailsRequestModel(
        requestModel, klassInstanceTypeModel);
    
    IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel configDetails = getPropertiesVariantInstancesInTableViewStrategy
        .execute(requestStratetgyModel);
    IGetPropertiesVariantInstancesInTableViewModel returnModel = new GetPropertiesVariantInstancesInTableViewModel();
    
    IGetFilterInfoModel filterInfo = new GetFilterInfoModel();
    fillColumnDetails(configDetails, returnModel, contextId);
    returnModel.setFilterInfo(filterInfo);
    returnModel.setConfigDetails(configDetails);
    
    IPropertyDTO propertyDto = getPropertyDTOForReferenceAttribute(configDetails);
    
    IBaseEntityDTO baseEntityDto = baseEntityDao.loadPropertyRecords(propertyDto);
    
    List<IPropertyVariantRowIdParameterModel> rows = new ArrayList<>();
    
    fillResponseModel(baseEntityDto, configDetails, rows, contextId);
    
    returnModel.setRows(rows);
    permissionUtils.manageVariantInstancePermissions(configDetails);
    removeRowPropertiesNotHavingRP(configDetails, returnModel);
    
    return (R) returnModel;
  }
  
  private void fillResponseModel(IBaseEntityDTO baseEntityDto,
      IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel configDetails,
      List<IPropertyVariantRowIdParameterModel> rows, String contextId)
  {
    IReferencedVariantContextModel referencedContext = configDetails.getReferencedVariantContexts().getEmbeddedVariantContexts().get(contextId);
    Boolean isTimeEnabled = referencedContext.getIsTimeEnabled();
    baseEntityDto.getPropertyRecords()
        .stream()
        .forEach(propertyRecord -> {
          IPropertyVariantRowIdParameterModel rowProperty = new PropertyVariantRowIdParameterModel();
          
          fillRowDetails(rowProperty, (IValueRecordDTO) propertyRecord);
          
          Map<String, IPropertyInstance> rowProperties = new HashMap<>();
          fillAttributeInstance(rowProperties, (IValueRecordDTO) propertyRecord,
              configDetails.getReferencedAttributes());
          
          IContextualDataDTO contextualObject = ((IValueRecordDTO) propertyRecord)
              .getContextualObject();
          
          getTagValueProperties(configDetails, rowProperties, contextualObject);
          
          if (isTimeEnabled) {
            long contextEndTime = contextualObject.getContextEndTime();
            long contextStartTime = contextualObject.getContextStartTime();
            
            IDateInstanceModel from = new DateInstanceModel();
            from.setValue(String.valueOf(contextStartTime));
            from.setBaseType(CommonConstants.DATE_INSTANCE_BASE_TYPE);
            from.setId(IInstanceTimeRange.FROM);
            rowProperties.put(IInstanceTimeRange.FROM, from);
            
            IDateInstanceModel to = new DateInstanceModel();
            to.setValue(String.valueOf(contextEndTime));
            to.setBaseType(CommonConstants.DATE_INSTANCE_BASE_TYPE);
            to.setId(IInstanceTimeRange.TO);
            rowProperties.put(IInstanceTimeRange.TO, to);
          }
          
          rowProperty.setProperties(rowProperties);
          rows.add(rowProperty);
        });
  }
  
  private void fillRowDetails(IPropertyVariantRowIdParameterModel rowProperty,
      IValueRecordDTO valueRecord)
  {
    
    rowProperty.setAttributeId(valueRecord.getProperty()
        .getCode());
    rowProperty.setCreationLanguage(valueRecord.getLocaleID());
    rowProperty.setId(KlassInstanceBuilder.getAttributeID(valueRecord));
    rowProperty.setOriginalInstanceId(Long.toString(valueRecord.getValueIID()));
  }
  
  private void fillAttributeInstance(Map<String, IPropertyInstance> rowProperties,
      IValueRecordDTO valueRecord, Map<String, IAttribute> referencedAttributes)
  {
    
    IAttributeInstance attributeInstance = new AttributeInstance();
    String attributeCode = valueRecord.getProperty().getCode();
    attributeInstance.setAttributeId(attributeCode);
    attributeInstance.setValue(valueRecord.getValue());
    attributeInstance.setId(KlassInstanceBuilder.getAttributeID(valueRecord));
    String type = (referencedAttributes.get(attributeCode)).getType();
    if (type.equals(CommonConstants.HTML_TYPE_ATTRIBUTE)) {
      attributeInstance.setValueAsHtml(valueRecord.getAsHTML());
    }
    rowProperties.put(CommonConstants.VALUE_PROPERTY, attributeInstance);
  }
  
  private void getTagValueProperties(
      IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel configDetails,
      Map<String, IPropertyInstance> rowProperties, IContextualDataDTO contextualObject)
  {
    
    contextualObject.getContextTagValues()
        .forEach(tagValue -> {
          ITagInstanceValue tagInstanceValue = new TagInstanceValue();
          tagInstanceValue.setCode(tagValue.getTagValueCode());
          tagInstanceValue.setId(tagValue.getTagValueCode());
          tagInstanceValue.setRelevance(tagValue.getRange());
          tagInstanceValue.setTagId(tagValue.getTagValueCode());
          
          Map<String, ITag> referencedTags = configDetails.getReferencedTags();
          
          referencedTags.values()
              .forEach(referencedTag -> {
                referencedTag.getChildren()
                    .forEach(tagChildren -> {
                      if (tagChildren.getId()
                          .equals(tagInstanceValue.getTagId())) {
                        if (rowProperties.get(referencedTag.getId()) == null) {
                          
                          ITagInstance tagInstance = new TagInstance();
                          
                          tagInstance.setTagId(referencedTag.getId());
                          tagInstance.setId(Long.toString(referencedTag.getPropertyIID())
                              + KlassInstanceBuilder.ATTRIBUTE_ID_SEPARATOR
                              + contextualObject.getContextualObjectIID());
                          tagInstance.setContextInstanceId(
                              Long.toString(contextualObject.getContextualObjectIID()));
                          tagInstance.getTagValues()
                              .add(tagInstanceValue);
                          
                          IDateInstanceModel endDate = new DateInstanceModel();
                          endDate.setId(CommonConstants.TO_PROPERTY);
                          endDate.setValue(Long.toString(contextualObject.getContextEndTime()));
                          
                          IDateInstanceModel startDate = new DateInstanceModel();
                          startDate.setId(CommonConstants.FROM_PROPERTY);
                          startDate.setValue(Long.toString(contextualObject.getContextStartTime()));
                          
                          rowProperties.put(tagInstance.getTagId(), tagInstance);
                          rowProperties.put(CommonConstants.TO_PROPERTY, endDate);
                          rowProperties.put(CommonConstants.FROM_PROPERTY, startDate);
                        }
                        else {
                          ITagInstance tagInstance = (ITagInstance) rowProperties
                              .get(referencedTag.getId());
                          tagInstance.getTagValues()
                              .add(tagInstanceValue);
                        }
                      }
                    });
              });
        });
  }
  
  public void fillAttributesAndTagsRecord(Set<IPropertyRecordDTO> propertyRecords,
      Map<Long, IValueRecordDTO> attributeRecords, Map<Long, ITagsRecordDTO> tagRecords)
  {
    
    for (IPropertyRecordDTO propertyRecord : propertyRecords) {
      if (propertyRecord instanceof ValueRecordDTO) {
        long propertyIId = propertyRecord.getProperty()
            .getPropertyIID();
        attributeRecords.put(propertyIId, (IValueRecordDTO) propertyRecord);
      }
      else {
        long propertyIID = propertyRecord.getProperty()
            .getPropertyIID();
        tagRecords.put(propertyIID, (ITagsRecordDTO) propertyRecord);
      }
    }
  }
  
  private IPropertyDTO getPropertyDTOForReferenceAttribute(
      IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel configDetails) throws Exception
  {
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    IPropertyDTO propertyDTO = null;
    
    for (Map.Entry<String, IAttribute> referencedAttribute : referencedAttributes.entrySet()) {
      IAttribute attribute = referencedAttribute.getValue();
      
      PropertyType propertyType = IConfigMap.getPropertyType(attribute.getType());
      if (propertyType != null) {
        propertyDTO = new PropertyDTO(attribute.getPropertyIID(), attribute.getId(), propertyType);
      }
    }
    
    return propertyDTO;
  }
  
  protected String getInstanceIdToGetType(String parentId, String klassInstanceId)
  {
    
    return parentId != null ? parentId : klassInstanceId;
  }
  
  private void removeRowPropertiesNotHavingRP(
      IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel configDetails,
      IGetPropertiesVariantInstancesInTableViewModel returnModel)
  {
    
    Set<String> visiblePropertyIds = configDetails.getVisiblePropertyIds();
    List<IPropertyVariantRowIdParameterModel> rows = returnModel.getRows();
    List<IPropertyVariantRowIdParameterModel> rowsToRemove = new ArrayList<>();
    for (IPropertyVariantRowIdParameterModel row : rows) {
      if (!visiblePropertyIds.contains(row.getAttributeId())) {
        rowsToRemove.add(row);
      }
    }
    rows.removeAll(rowsToRemove);
  }
  
  protected IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel getConfigDetailsRequestModel(
      P requestModel, IKlassInstanceTypeModel klassInstanceTypeModel)
  {
    IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel requestStratetgyModel = new ConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel();
    requestStratetgyModel.setCurrentUserId(context.getUserId());
    requestStratetgyModel.setAttributeId(requestModel.getAttributeId());
    requestStratetgyModel.setContextId(requestModel.getContextId());
    requestStratetgyModel.getKlassIds()
        .addAll(klassInstanceTypeModel.getTypes());
    requestStratetgyModel.setTemplateId(requestModel.getTemplateId());
    requestStratetgyModel.setTaxonomyIds(klassInstanceTypeModel.getSelectedTaxonomyIds());
    return requestStratetgyModel;
  }
  
  protected String getVariantInstanceId(String parentId, String klassInstanceId)
  {
    String variantInstanceId = null;
    if (!parentId.equals(klassInstanceId)) {
      variantInstanceId = parentId;
    }
    return variantInstanceId;
  }
  
  protected void fillColumnDetails(
      IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel configDetails,
      IGetPropertiesVariantInstancesInTableViewModel returnModel, String contextId)
  {
    
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    IReferencedVariantContextModel referencedContext = configDetails.getReferencedVariantContexts().getEmbeddedVariantContexts().get(contextId);
    Boolean isTimeEnabled = referencedContext.getIsTimeEnabled();
    List<IIdParameterModel> columns = new ArrayList<>();
    
    if(isTimeEnabled) {
      IIdParameterModel fromColumn = new IdParameterModel();
      fromColumn.setId(IInstanceTimeRange.FROM);
      fromColumn.setType(CommonConstants.DATE_TYPE);
      columns.add(fromColumn);
      
      IIdParameterModel toColumn = new IdParameterModel();
      toColumn.setId(IInstanceTimeRange.TO);
      toColumn.setType(CommonConstants.DATE_TYPE);
      columns.add(toColumn);
    }
    
    referencedTags.values()
        .forEach(referencedTag -> {
          IIdParameterModel column = new IdParameterModel();
          
          column.setId(referencedTag.getId());
          column.setType(CommonConstants.TAG_PROPERTY);
          columns.add(column);
        });
    
    IIdParameterModel column = new IdParameterModel();
    
    column.setId(CommonConstants.VALUE_PROPERTY);
    column.setType("custom");
    
    columns.add(column);
    
    returnModel.setColumns(columns);
  }
}
