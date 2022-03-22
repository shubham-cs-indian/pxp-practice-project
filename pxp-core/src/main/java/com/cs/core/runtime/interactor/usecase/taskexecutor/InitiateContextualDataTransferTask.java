package com.cs.core.runtime.interactor.usecase.taskexecutor;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.context.GetContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.context.IContextualDataTransferInputModel;
import com.cs.core.runtime.interactor.model.context.IGetContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IGetConfigDetailsForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.IInitiateContextualDataTransferTask;
import com.cs.core.runtime.strategy.model.couplingtype.IIdCodeCouplingTypeModel;
import com.cs.core.runtime.strategy.model.couplingtype.IPropertiesIdCodeCouplingTypeModel;
import com.cs.core.runtime.strategy.model.couplingtype.IPropertiesIdCouplingTypeMapModel;
import com.cs.core.runtime.strategy.model.couplingtype.PropertiesIdCouplingTypeMapModel;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetConfigDetailsForContextualDataTransferStrategy;

@Component
public class InitiateContextualDataTransferTask
    extends AbstractRuntimeInteractor<IContextualDataTransferInputModel, IModel>
    implements IInitiateContextualDataTransferTask {
  
  /*@Autowired
  protected IGetKlassInstanceTypeInfoStrategy                  getKlassInstanceTypeInfoStrategy;*/
  
  @Autowired
  protected IGetConfigDetailsForContextualDataTransferStrategy getConfigDetailsForContextualDataTransferStrategy;
  
  /*@Autowired
  protected IPrepareDataForContextualDataTransferStrategy      prepareDataForContextualDataTransferStrategy;*/

  @Override
  protected IModel executeInternal(IContextualDataTransferInputModel inputModel) throws Exception
  {
    /*
    
    IDataTransferInputModel dataTransfer = inputModel.getDataTransfer();
    
    IIdAndBaseTypeModel idBaseTypeModel = new IdAndBaseTypeModel();
    idBaseTypeModel.setId(dataTransfer.getContentId());
    idBaseTypeModel.setBaseType(dataTransfer.getBaseType());
    IKlassInstanceTypeInfoModel klassInstanceTypeInfoModel= getKlassInstanceTypeInfoStrategy.execute(idBaseTypeModel);
    
    IGetConfigDetailsForContextualDataTransferModel configDetail = getConfigDetailsForContextualDataTransferStrategy.execute(klassInstanceTypeInfoModel);
    
    List<String> modifiedLanguageCodes = dataTransfer.getModifiedLanguageCodes();
    
    Map<String, IPropertiesIdCodeCouplingTypeModel> contextualDataTransfer = configDetail.getContextualDataTransfer();
    Stream<Entry<String, IPropertiesIdCodeCouplingTypeModel>> entrySetStream = contextualDataTransfer.entrySet().stream();
    
    Stream<Map.Entry<String, IPropertiesIdCouplingTypeMapModel>> contextualDataTransferStream = entrySetStream
        .map(entry -> collectFilterMap(entry, dataTransfer));
    
    Stream<Entry<String, IPropertiesIdCouplingTypeMapModel>> filter = contextualDataTransferStream.filter(entry -> entry != null);
    Map<String, IPropertiesIdCouplingTypeMapModel> filterContextualDataTransfer = filter.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    
    
    IPropertiesIdCodeCouplingTypeModel contextualDataInheritance = configDetail.getContextualDataInheritance();
    IPropertiesIdCouplingTypeMapModel newContextualDataInheritance = getFilterInheritanceContextualData(contextualDataInheritance, dataTransfer);
    
    
    if (sholuldInitiateDataTransfer(filterContextualDataTransfer, contextualDataInheritance)) {
    
      IGetContextualDataTransferModel requestModel = preparerequestModel(inputModel, configDetail,
          modifiedLanguageCodes, filterContextualDataTransfer, newContextualDataInheritance);
    
      IListModel<IContentsPropertyDiffModel> contentDiffListModel = prepareDataForContextualDataTransferStrategy.execute(requestModel);
      Collection<? extends IContentsPropertyDiffModel> contentDiffList = contentDiffListModel.getList();
    
      kafkaUtils.prepareMessageData((List<? extends IModel>) contentDiffList, PropagateValuesHandlerTask.class.getName(), IContentsPropertyDiffModel.CONTENT_ID);
    }
    */
    return null;
  }
  
  private boolean sholuldInitiateDataTransfer(
      Map<String, IPropertiesIdCouplingTypeMapModel> filterContextualDataTransfer,
      IPropertiesIdCodeCouplingTypeModel contextualDataInheritance)
  {
    return !filterContextualDataTransfer.isEmpty()
        || !contextualDataInheritance.getDependentAttributes()
            .isEmpty()
        || !contextualDataInheritance.getIndependentAttributes()
            .isEmpty()
        || !contextualDataInheritance.getTags()
            .isEmpty();
  }
  
  private IGetContextualDataTransferModel preparerequestModel(
      IContextualDataTransferInputModel inputModel,
      IGetConfigDetailsForContextualDataTransferModel configDetail,
      List<String> modifiedLanguageCodes,
      Map<String, IPropertiesIdCouplingTypeMapModel> filterContextualDataTransfer,
      IPropertiesIdCouplingTypeMapModel newContextualDataInheritance)
  {
    IDataTransferInputModel dataTransfer = inputModel.getDataTransfer();
    IGetContextualDataTransferModel requestModel = new GetContextualDataTransferModel();
    requestModel.setContentId(dataTransfer.getContentId());
    requestModel.setBaseType(dataTransfer.getBaseType());
    
    requestModel
        .setvariantIdsToExcludeForDataTransfer(inputModel.getVariantIdsToExcludeForDataTransfer());
    requestModel.setDataInheritance(newContextualDataInheritance);
    requestModel.setContextKlassId(configDetail.getContextKlassId());
    requestModel.setDataTransfer(filterContextualDataTransfer);
    requestModel
        .setDependentAttributeIdsToTransfer(configDetail.getDependentAttributeIdsToTransfer());
    requestModel
        .setIndependentAttributeIdsToTransfer(configDetail.getIndependentAttributeIdsToTransfer());
    requestModel.setDependentAttributeIdsToInheritaance(
        configDetail.getDependentAttributeIdsToInheritance());
    requestModel.setIndependentAttributeIdsToInheritaance(
        configDetail.getIndependentAttributeIdsToInheritance());
    requestModel.setTagIdsToTransfer(configDetail.getTagIdsToTransfer());
    requestModel.setTagIdsToInheritaance(configDetail.getTagIdsToInheritance());
    requestModel.setModifiedLanguageCodes(modifiedLanguageCodes);
    requestModel.setContextKlassId(configDetail.getContextKlassId());
    return requestModel;
  }
  
  private List<String> getChangedAttributeIds(
      Map<String, List<String>> changedDependentAttributeIdsMap)
  {
    List<String> changedAttributeIds = changedDependentAttributeIdsMap.values()
        .stream()
        .flatMap(List::stream)
        .distinct()
        .collect(Collectors.toList());
    return changedAttributeIds;
  }
  
  private IPropertiesIdCouplingTypeMapModel getFilterInheritanceContextualData(
      IPropertiesIdCodeCouplingTypeModel contextualDataInheritance,
      IDataTransferInputModel inputModel)
  {
    List<String> addedAttributeIds = inputModel.getAddedAttributeIds();
    List<String> addedTagIds = inputModel.getAddedTagIds();
    Map<String, List<String>> addedDependentAttributeIdsMap = inputModel
        .getAddedDependentAttributeIdsMap();
    List<String> addedDependentAttributeIds = getChangedAttributeIds(addedDependentAttributeIdsMap);
    
    List<String> modifiedLanguageCodes = inputModel.getModifiedLanguageCodes();
    
    IPropertiesIdCouplingTypeMapModel newContextualDataInheritance = new PropertiesIdCouplingTypeMapModel();
    
    List<IIdCodeCouplingTypeModel> transferInependentAttributes = contextualDataInheritance
        .getIndependentAttributes();
    List<IIdCodeCouplingTypeModel> transferTags = contextualDataInheritance.getTags();
    List<IIdCodeCouplingTypeModel> transferDependentAttributes = contextualDataInheritance
        .getDependentAttributes();
    
    if (!transferInependentAttributes.isEmpty() || !transferTags.isEmpty()
        || !transferDependentAttributes.isEmpty()) {
      newContextualDataInheritance = new PropertiesIdCouplingTypeMapModel();
      List<IIdCodeCouplingTypeModel> filterAttributes = getFilterAttributes(addedAttributeIds,
          transferInependentAttributes);
      List<IIdCodeCouplingTypeModel> filterTags = getFilterTags(addedTagIds, transferTags);
      List<IIdCodeCouplingTypeModel> filterDependentAttributes = getFilterAttributes(
          addedDependentAttributeIds, transferDependentAttributes);
      
      Map<String, List<IIdCodeCouplingTypeModel>> transferdependentAttributesMap = new HashMap<String, List<IIdCodeCouplingTypeModel>>();
      modifiedLanguageCodes.forEach(code -> {
        transferdependentAttributesMap.put(code, filterDependentAttributes);
      });
      
      newContextualDataInheritance.setTransferIndependentAttributes(filterAttributes);
      newContextualDataInheritance.setTransferTags(filterTags);
      newContextualDataInheritance.setTransferDependentAttributes(transferdependentAttributesMap);
    }
    
    return newContextualDataInheritance;
  }
  
  private Map.Entry<String, IPropertiesIdCouplingTypeMapModel> collectFilterMap(
      Entry<String, IPropertiesIdCodeCouplingTypeModel> entry, IDataTransferInputModel inputModel)
  {
    List<String> changedAttributeIds = inputModel.getChangedAttributeIds();
    List<String> changedTagsIds = inputModel.getChangedTagsIds();
    Map<String, List<String>> changedDependentAttributeIdsMap = inputModel
        .getChangedDependentAttributeIdsMap();
    List<String> changedDependentAttributeIds = getChangedAttributeIds(
        changedDependentAttributeIdsMap);
    List<String> modifiedLanguageCodes = inputModel.getModifiedLanguageCodes();
    
    IPropertiesIdCodeCouplingTypeModel dataTransferPropertyModel = entry.getValue();
    List<IIdCodeCouplingTypeModel> transferIndependentAttributes = getFilterIndependentAttributesForTransfer(
        dataTransferPropertyModel, changedAttributeIds);
    Map<String, List<IIdCodeCouplingTypeModel>> transferDependentAttributes = getFilterDependentAttributeForTransfer(
        dataTransferPropertyModel, changedDependentAttributeIds, modifiedLanguageCodes);
    List<IIdCodeCouplingTypeModel> transferInependentTags = getFilterTagsForTransfer(
        dataTransferPropertyModel, changedTagsIds);
    
    Map.Entry<String, IPropertiesIdCouplingTypeMapModel> newEntry = null;
    if (!transferIndependentAttributes.isEmpty() || transferInependentTags.isEmpty()) {
      IPropertiesIdCouplingTypeMapModel newDataTransferPropertyModel = new PropertiesIdCouplingTypeMapModel();
      newDataTransferPropertyModel.setTransferIndependentAttributes(transferIndependentAttributes);
      newDataTransferPropertyModel.setTransferTags(transferInependentTags);
      newDataTransferPropertyModel.setTransferDependentAttributes(transferDependentAttributes);
      newEntry = new AbstractMap.SimpleEntry<String, IPropertiesIdCouplingTypeMapModel>(
          entry.getKey(), newDataTransferPropertyModel);
    }
    
    return newEntry;
  }
  
  private Map<String, List<IIdCodeCouplingTypeModel>> getFilterDependentAttributeForTransfer(
      IPropertiesIdCodeCouplingTypeModel dataTransferPropertyModel,
      List<String> changedDependentAttributeIds, List<String> modifiedLanguageCodes)
  {
    List<IIdCodeCouplingTypeModel> transferdependentAttributes = dataTransferPropertyModel
        .getDependentAttributes();
    List<IIdCodeCouplingTypeModel> transferFilterAttributes = getFilterAttributes(
        changedDependentAttributeIds, transferdependentAttributes);
    
    Map<String, List<IIdCodeCouplingTypeModel>> transferdependentAttributesMap = new HashMap<String, List<IIdCodeCouplingTypeModel>>();
    modifiedLanguageCodes.forEach(code -> {
      transferdependentAttributesMap.put(code, transferFilterAttributes);
    });
    
    return transferdependentAttributesMap;
  }
  
  private List<IIdCodeCouplingTypeModel> getFilterIndependentAttributesForTransfer(
      IPropertiesIdCodeCouplingTypeModel dataTransferPropertyModel,
      List<String> changedAttributeIds)
  {
    List<IIdCodeCouplingTypeModel> transferInependentAttributes = dataTransferPropertyModel
        .getIndependentAttributes();
    List<IIdCodeCouplingTypeModel> transferFilterAttributes = getFilterAttributes(
        changedAttributeIds, transferInependentAttributes);
    
    return transferFilterAttributes;
  }
  
  private List<IIdCodeCouplingTypeModel> getFilterAttributes(List<String> changedAttributeIds,
      List<IIdCodeCouplingTypeModel> transferInependentAttributes)
  {
    Predicate<IIdCodeCouplingTypeModel> predicate = attribute -> (changedAttributeIds
        .contains(attribute.getId()));
    
    List<IIdCodeCouplingTypeModel> transferFilterAttributes = transferInependentAttributes.stream()
        .filter(predicate)
        .collect(Collectors.toList());
    return transferFilterAttributes;
  }
  
  private List<IIdCodeCouplingTypeModel> getFilterTagsForTransfer(
      IPropertiesIdCodeCouplingTypeModel dataTransferPropertyModel, List<String> changedTagsIds)
  {
    List<IIdCodeCouplingTypeModel> transferTags = dataTransferPropertyModel.getTags();
    List<IIdCodeCouplingTypeModel> transferFilterTags = getFilterTags(changedTagsIds, transferTags);
    return transferFilterTags;
  }
  
  private List<IIdCodeCouplingTypeModel> getFilterTags(List<String> changedTagsIds,
      List<IIdCodeCouplingTypeModel> transferTags)
  {
    Predicate<IIdCodeCouplingTypeModel> predicate = tag -> changedTagsIds.contains(tag.getId());
    List<IIdCodeCouplingTypeModel> transferInependentTags = transferTags.stream()
        .filter(predicate)
        .collect(Collectors.toList());
    return transferInependentTags;
  }
}
