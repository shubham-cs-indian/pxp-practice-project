package com.cs.core.runtime.staticcollection;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.data.Text;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dto.SortDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.rdbms.entity.idto.ISortDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.runtime.instancetree.InstanceTreeUtils;
import com.cs.core.runtime.interactor.constants.SystemLabels;
import com.cs.core.runtime.interactor.model.collections.GetStaticCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionModel;
import com.cs.core.runtime.interactor.model.collections.ISaveStaticCollectionModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetPostConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.cs.core.runtime.interactor.model.instancetree.AppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.IAppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.FilterUtils;
import com.cs.core.runtime.interactor.utils.StaticCollectionUtilsToBeRefactored;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetPostConfigDetailsForNewInstanceTreeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@SuppressWarnings({ "unchecked" })
public abstract class AbstractSaveStaticCollection<P extends ISaveStaticCollectionModel, R extends IGetStaticCollectionModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected RDBMSComponentUtils                             rdbmsComponentUtils;
  
  @Autowired
  protected FilterUtils                                     filterUtils;
  
  @Autowired
  protected StaticCollectionUtilsToBeRefactored             staticCollectionUtils;
  
  @Autowired
  protected SearchAssembler                                 searchAssembler;
  
  @Autowired
  protected InstanceTreeUtils                               instanceTreeUtils;
  
  @Autowired
  protected GetAllUtils                                     getAllUtils;
  
  @Autowired
  protected IGetPostConfigDetailsForNewInstanceTreeStrategy getPostConfigDetailsForNewInstanceTreeStrategy;
  
  @Autowired
  protected CollectionValidations                           collectionValidations;
  
  @SuppressWarnings("unused")
  @Override
  public IGetStaticCollectionModel executeInternal(ISaveStaticCollectionModel saveStaticCollectionModel) throws Exception
  {
    collectionValidations.validate(saveStaticCollectionModel);
    
    IGetKlassInstanceTreeStrategyModel filterResultsToSaveModel = saveStaticCollectionModel.getFilterResultsToSave();
    
    IConfigDetailsForGetNewInstanceTreeModel configDetails = null;
    String moduleId = null;
    if (filterResultsToSaveModel != null) {
      moduleId = filterResultsToSaveModel.getModuleId();
      configDetails = staticCollectionUtils.processFilterDataAndReturnConfigDetails(filterResultsToSaveModel);
    }
    else {
      moduleId = saveStaticCollectionModel.getModuleId();
      configDetails = staticCollectionUtils.processFilterDataAndReturnConfigDetails(saveStaticCollectionModel);
    }
    
    IGetStaticCollectionModel returnModel = executeSaveInstance(saveStaticCollectionModel, configDetails);
    return returnModel;
  }
  
  public String generateSearchExpression(ISaveStaticCollectionModel dataModel, IConfigDetailsForGetNewInstanceTreeModel configDetails,
      Long collectionId) throws Exception
  {
    List<String> selectedTypes = dataModel.getSelectedTypes();
    List<String> selectedTaxonomyIds = dataModel.getSelectedTaxonomyIds();
    selectedTypes.addAll(configDetails.getKlassIdsHavingRP());
    selectedTaxonomyIds.addAll(configDetails.getTaxonomyIdsHavingRP());
    StringBuilder generateEntityFilterExpression = new StringBuilder();
    StringBuilder searchExpression = searchAssembler.getBaseQuery(BaseType.UNDEFINED);
    if (!selectedTypes.isEmpty() || !selectedTaxonomyIds.isEmpty()) {
      generateEntityFilterExpression = searchAssembler.generateEntityFilterExpression(selectedTypes, selectedTaxonomyIds, false, configDetails.getMajorTaxonomyIds());
    }
    
    String expressionForCollectionBaseEntities = String.format(" $entity belongsto [b>%s $iid=%d] ", "staticCollection", collectionId);
    
    if (!generateEntityFilterExpression.toString().isEmpty() && !expressionForCollectionBaseEntities.isEmpty()) {
      generateEntityFilterExpression.append(" and ");
      generateEntityFilterExpression.append(expressionForCollectionBaseEntities);
    }
    else if (!expressionForCollectionBaseEntities.isEmpty()) {
      searchExpression.append(expressionForCollectionBaseEntities);
    }
    
    if (!generateEntityFilterExpression.toString().isEmpty()) {
      searchExpression.append(generateEntityFilterExpression);
    }
    
    String allSearch = dataModel.getAllSearch();
    String evaluationExpression = searchAssembler.getEvaluationExpression(dataModel.getAttributes(), dataModel.getTags(), allSearch);
    if (!allSearch.isEmpty()) {
      String join = String.format(" [P>search] contains %s ", Text.escapeStringWithQuotes(allSearch));
      String searchEpxression = String.format("(%s)", join);
      
      if (!join.isEmpty() && !evaluationExpression.isEmpty()) {
        evaluationExpression = evaluationExpression + " and " + searchEpxression;
      }
      else if (!join.isEmpty()) {
        evaluationExpression = searchEpxression;
      }
    }
    
    if (!evaluationExpression.isEmpty()) {
      searchExpression.append(" where ").append(evaluationExpression);
    }
    return searchExpression.toString();
  }
  
  protected IGetStaticCollectionModel executeSaveInstance(ISaveStaticCollectionModel saveStaticCollectionModel,
      IConfigDetailsForGetNewInstanceTreeModel configDetails) throws Exception
  {
    List<IIdAndTypeModel> addedKlassInstanceIds = saveStaticCollectionModel.getAddedKlassInstanceIds();
    List<Long> addedBaseEntityIIDs = new ArrayList<>();
    List<Long> removedBaseEntityIIDs = new ArrayList<>();
    for (IIdAndTypeModel idAndType : addedKlassInstanceIds) {
      addedBaseEntityIIDs.add(Long.parseLong(idAndType.getId()));
    }
    List<IIdAndTypeModel> removedKlassInstanceIds = saveStaticCollectionModel.getRemovedKlassInstanceIds();
    
    for (IIdAndTypeModel idAndType : removedKlassInstanceIds) {
      removedBaseEntityIIDs.add(Long.parseLong(idAndType.getId()));
    }
    saveStaticCollectionModel.getIsPublic();
    
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    
    long collectionIID = Long.parseLong(saveStaticCollectionModel.getId());
    ICollectionDTO newCollectionDTO = rdbmsComponentUtils.newCollectionDTO(CollectionType.staticCollection,
        saveStaticCollectionModel.getLabel(), "", "-1", saveStaticCollectionModel.getIsPublic(), new JSONContent(), new HashSet<>());
    collectionDAO.updateCollectionRecords(collectionIID, newCollectionDTO, addedBaseEntityIIDs, removedBaseEntityIIDs);
    
    List<Long> entitiesToUpdateInElastic = new ArrayList<>(addedBaseEntityIIDs);
    entitiesToUpdateInElastic.addAll(removedBaseEntityIIDs);
    for (Long entityId : entitiesToUpdateInElastic) {
      rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(entityId, IEventDTO.EventType.ELASTIC_UPDATE);
    }
    
    ICollectionDTO updatedCollectionRecordDTO = collectionDAO.getCollection(collectionIID, CollectionType.staticCollection);
    IGetStaticCollectionModel returnModel = new GetStaticCollectionModel();
    
    returnModel.setId(Long.toString(collectionIID));
    returnModel.setLabel(updatedCollectionRecordDTO.getCollectionCode());
    returnModel.setIsPublic(updatedCollectionRecordDTO.getIsPublic());
    returnModel
    .setParentId(updatedCollectionRecordDTO.getParentIID() == 0 ? "-1" : Long.toString(updatedCollectionRecordDTO.getParentIID()));
    returnModel.setType(updatedCollectionRecordDTO.getCollectionType().toString());
    returnModel.setCreatedOn(updatedCollectionRecordDTO.getCreatedTrack().getWhen());
    returnModel.setCreatedBy(updatedCollectionRecordDTO.getCreatedTrack().getWho());
    returnModel.setLastModifiedBy(updatedCollectionRecordDTO.getLastModifiedTrack().getWho());
    // response is required only when collection is saved from quicklist ,from quicklist moduleId is "allmodule" 
    if (saveStaticCollectionModel.getModuleId().equals("allmodule")) {
      List<String> klassInstanceIds = new ArrayList<>();
      List<IKlassInstanceInformationModel> klassInstances = new ArrayList<>();
      
      String searchExpression = generateSearchExpression(saveStaticCollectionModel, configDetails, collectionIID);
      ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
      List<ISortModel> sortOptionsModel = getSortOptions(saveStaticCollectionModel);
      List<ISortDTO> sortOptions = sortOptionsModel.stream()
          .map(x -> new SortDTO(x.getSortField(), ISortDTO.SortOrder.valueOf(x.getSortOrder().toLowerCase()), x.getIsNumeric()))
          .collect(Collectors.toList());
      IRDBMSOrderedCursor<IBaseEntityDTO> cursor = getAllUtils.getCursor(localeCatalogDao, sortOptions, searchExpression);
      returnModel.setTotalContents(cursor.getCount());
      List<IBaseEntityDTO> baseEntityDTOs = cursor.getNext(saveStaticCollectionModel.getFrom(), saveStaticCollectionModel.getSize());
      
      List<INewApplicableFilterModel> filterData = getAllUtils.getApplicableFilters(configDetails.getFilterData(), searchExpression, false);
      returnModel.setFilterData(filterData);
      
      Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
      Map<String, ITag> referencedTags = configDetails.getReferencedTags();
      Set<IPropertyDTO> properties = getAllUtils.getXrayProperties(referencedAttributes, referencedTags);
      for (IBaseEntityDTO baseEntityDTO : baseEntityDTOs) {
        
        IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO,
            rdbmsComponentUtils);
        klassInstanceIds.add(Long.toString(baseEntityDTO.getBaseEntityIID()));
        klassInstances.add(klassInstanceInformationModel);
        getAllUtils.getXrayPropertyDetails(referencedAttributes, referencedTags, baseEntityDTO, properties, klassInstanceInformationModel);
      }
      returnModel.setReferencedAttributes(configDetails.getReferencedAttributes());
      returnModel.setKlassInstanceIds(klassInstanceIds);
      returnModel.setKlassInstances(klassInstances);
      addSortOptionsToResponseModel(saveStaticCollectionModel, returnModel);
      instanceTreeUtils.addRuleViolationFilter(true, returnModel.getFilterData());
      fillPostConfigDetails(configDetails, returnModel);
    }
    return returnModel;
  }
  
  private void addSortOptionsToResponseModel(ISaveStaticCollectionModel saveStaticCollectionModel, IGetStaticCollectionModel responseModel)
  {
    List<ISortModel> sortOptions = saveStaticCollectionModel.getSortOptions();
    List<IAppliedSortModel> appliedSortOptions = new ArrayList<>();
    
    if (sortOptions.isEmpty()) {
      IAppliedSortModel appliedSortModel = new AppliedSortModel();
      appliedSortModel.setCode(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE);
      appliedSortModel.setIsNumeric(false);
      appliedSortModel.setLabel(SystemLabels.LAST_MODIFIED_ATTRIBUTE_LABEL);
      appliedSortModel.setSortField(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE);
      appliedSortModel.setSortOrder(CommonConstants.SORTORDER_DESC);
      appliedSortOptions.add(appliedSortModel);
    }
    else {
      for (ISortModel sort : sortOptions) {
        IAppliedSortModel appliedSortModel = new AppliedSortModel();
        appliedSortModel.setIsNumeric(sort.getIsNumeric());
        appliedSortModel.setSortField(sort.getSortField());
        appliedSortModel.setSortOrder(sort.getSortOrder());
        appliedSortOptions.add(appliedSortModel);
      }
    }
    responseModel.setAppliedSortData(appliedSortOptions);
  }
  
  protected List<ISortModel> getSortOptions(ISaveStaticCollectionModel saveStaticCollectionModel)
  {
    List<ISortModel> sortOptionsToReturn = new ArrayList<ISortModel>();
    List<ISortModel> dataSortOptions = saveStaticCollectionModel.getSortOptions();
    
    if (dataSortOptions.isEmpty()) {
      ISortModel sortModel = new SortModel(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE, CommonConstants.SORTORDER_DESC, true);
      sortOptionsToReturn.add(sortModel);
      return sortOptionsToReturn;
    }
    
    dataSortOptions.forEach((iAppliedSortModel) -> {
      ISortModel sortModel = new SortModel();
      sortModel.setIsNumeric(iAppliedSortModel.getIsNumeric());
      sortModel.setSortField(iAppliedSortModel.getSortField());
      sortModel.setSortOrder(iAppliedSortModel.getSortOrder());
      sortOptionsToReturn.add(sortModel);
    });
    return sortOptionsToReturn;
  }
  
  protected void fillPostConfigDetails(IConfigDetailsForNewInstanceTreeModel configDetails, IGetStaticCollectionModel responseModel)
      throws Exception
  {
    List<String> appliedSortAttributeIds = new ArrayList<>();
    for (IAppliedSortModel sortModel : responseModel.getAppliedSortData()) {
      appliedSortAttributeIds.add(sortModel.getSortField());
    }
    IGetPostConfigDetailsRequestModel postConfigDetailsRequestModel = new GetPostConfigDetailsRequestModel();
    postConfigDetailsRequestModel.setAttributeIds(appliedSortAttributeIds);
    postConfigDetailsRequestModel.setUserId(context.getUserId());
    IGetPostConfigDetailsForNewInstanceTreeModel postConfigDetails = getPostConfigDetailsForNewInstanceTreeStrategy
        .execute(postConfigDetailsRequestModel);
    
    for (IAppliedSortModel sortModel : responseModel.getAppliedSortData()) {
      IAttribute iAttribute = postConfigDetails.getReferencedAttributes().get(sortModel.getSortField());
      if (iAttribute != null) {
        sortModel.setLabel(iAttribute.getLabel());
      }
    }
  }
  
}
