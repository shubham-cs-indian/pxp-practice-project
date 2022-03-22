package com.cs.core.runtime.klassinstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsResponseModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetTaxonomiesTreeStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetDefaultKlassesForModulesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetFilterAndSortDataStrategy;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.SortDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.ISortDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.runtime.builder.TaskInstanceBuilder;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskInstance;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForInstanceTreeGetRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import com.cs.core.runtime.interactor.model.searchable.InstanceSearchModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceListModel;
import com.cs.core.runtime.interactor.model.taskinstance.TaskInstanceListModel;
import com.cs.core.runtime.interactor.model.typeswitch.GetAllowedTypesForModulesModel;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesForModulesModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetConfigDetailsForInstanceTreeGetStrategy;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetPostConfigDetailsStrategy;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;

@Service
public class GetInstanceTreeService extends AbstractRuntimeService<IGetKlassInstanceTreeStrategyModel, IGetKlassInstanceTreeModel>
    implements IGetInstanceTreeService {
  
  @Autowired
  protected IGetConfigDetailsForInstanceTreeGetStrategy getConfigDetailsForInstanceTreeGetStrategy;
  
  @Autowired
  protected IGetDefaultKlassesForModulesStrategy        getDefaultKlassesForModulesStrategy;
  
  @Autowired
  protected ModuleMappingUtil                           moduleMappingUtil;
  
  @Autowired
  protected IGetPostConfigDetailsStrategy               postConfigDetailsForGetInstanceTreeStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  protected KlassInstanceUtils                          klassInstanceUtils;
  
  @Autowired
  protected IGetFilterAndSortDataStrategy               getFilterAndSortDataForKlassStrategy;
  
  @Autowired
  protected SearchAssembler                             searchAssembler;
  
  @Autowired
  protected GetAllUtils                                 getAllUtils;
  
  @Autowired
  protected IGetTaxonomiesTreeStrategy                  getTaxonomiesTreeStrategy;
  
  @Autowired
  protected ISessionContext                             context;
  
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(IGetKlassInstanceTreeStrategyModel dataModel) throws Exception
  {
    
    String loginUserId = rdbmsComponentUtils.getUserID();
    String moduleId = dataModel.getModuleId();
    IModule module = getModule(moduleId);
    List<String> moduleEntities = module.getEntities();
    List<String> xRayAttributes = dataModel.getXRayAttributes();
    List<String> xRayTags = dataModel.getXRayTags();
    IConfigDetailsForInstanceTreeGetRequestModel model = new ConfigDetailsForInstanceTreeGetRequestModel();
    
    model.setUserId(loginUserId);
    model.setAllowedEntities(moduleEntities);
    model.setIsCalendarView(dataModel.getIsCalendarView());
    model.setXRayAttributes(xRayAttributes);
    model.setXRayTags(xRayTags);
    model.setKpiId(dataModel.getKpiId());
    IConfigDetailsForInstanceTreeGetModel configDetails = getConfigDetails(model);
    module.getEntities().retainAll(configDetails.getAllowedEntities());
    
    IGetKlassInstanceTreeModel responseModel = new GetKlassInstanceTreeModel();
    
    IGetFilterAndSortDataRequestModel filterAndSortDataRequestModel = KlassInstanceUtils.prepareSortAndFilterDataRequestModel(dataModel);
    IGetFilterInfoModel filterInfo = getFilterInfo(filterAndSortDataRequestModel);
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    List<ISortModel> sortOptionsModel = dataModel.getSortOptions();
    if (sortOptionsModel.isEmpty()) {
      ISortModel sortModel = new SortModel("lastmodifiedattribute", "desc", true);
      sortOptionsModel.add(sortModel);
    }
    List<ISortDTO> sortOptions = sortOptionsModel.stream()
        .map(x -> new SortDTO(x.getSortField(), ISortDTO.SortOrder.valueOf(x.getSortOrder().toLowerCase()), x.getIsNumeric()))
        .collect(Collectors.toList());
    InstanceSearchModel searchModel = (InstanceSearchModel) dataModel;
    searchModel.getSelectedTypes().addAll(configDetails.getKlassIdsForKPI());
    searchModel.getSelectedTaxonomyIds().addAll(configDetails.getTaxonomyIdsForKPI());
    
    String searchExpression = generateSearchExpression(searchModel);
    IRDBMSOrderedCursor<IBaseEntityDTO> cursor = getAllUtils.getCursor(localeCatalogDao, sortOptions, searchExpression);
    responseModel.setTotalContents(cursor.getCount());
    getAllUtils.fillCountsForFilter(filterInfo, searchExpression, false);
    getAllUtils.fillCountsForDataQualityRules(filterInfo, searchExpression, false);
    responseModel.setFilterInfo(filterInfo);
    // responseModel.setTaxonomies(getTaxonomyParentList(dataModel,
    // generateSearchExpressionForTaxonomy((InstanceSearchModel) dataModel)));
    List<IBaseEntityDTO> baseEntityDTOs = cursor.getNext(dataModel.getFrom(), dataModel.getSize());
    
    if (baseEntityDTOs != null && !baseEntityDTOs.isEmpty()) {
      List<IKlassInstanceInformationModel> responseList = responseModel.getChildren();
      populateResponseList(responseList, baseEntityDTOs, configDetails);
    }
    
    Set<String> typeIdsSet = new HashSet<>();
    Set<Long> klassInstanceIds = new HashSet<>();
    List<IKlassInstanceInformationModel> children = responseModel.getChildren();
    for (IKlassInstanceInformationModel child : children) {
      klassInstanceIds.add(Long.parseLong(child.getId()));
      for (String type : child.getTypes()) {
        typeIdsSet.add(type);
      }
    }
    
    this.handleTask(children, klassInstanceIds, dataModel, responseModel);
    
    IGetPostConfigDetailsRequestModel requestModel = new GetPostConfigDetailsRequestModel();
    requestModel.getKlassIds().addAll(typeIdsSet);
    IGetPostConfigDetailsResponseModel configResponseModel = postConfigDetailsForGetInstanceTreeStrategy.execute(requestModel);
    long starTime4 = System.currentTimeMillis();
    RDBMSLogger.instance().debug(
        "NA|OrientDB|" + this.getClass().getSimpleName() + "|executeInternal|postConfigDetailsForGetInstanceTreeStrategy| %d ms",
        System.currentTimeMillis() - starTime4);
    responseModel.setReferencedKlasses(configResponseModel.getReferencedKlasses());
    
    responseModel.setXRayConfigDetails(configDetails.getXRayConfigDetails());
    
    IListModel<IGetAllowedTypesForModulesModel> listModel = new ListModel<>();
    List<IGetAllowedTypesForModulesModel> list = new ArrayList<>();
    
    IGetAllowedTypesForModulesModel allowedTypeModel = new GetAllowedTypesForModulesModel();
    allowedTypeModel.setStandardKlassIds(KlassInstanceUtils.getStandardKlassIds(module.getEntities()));
    list.add(allowedTypeModel);
    listModel.setList(list);
    long starTime5 = System.currentTimeMillis();
    IGetDefaultKlassesModel defaultKlassesModel = getDefaultKlassesForModulesStrategy.execute(listModel);
    RDBMSLogger.instance().debug(
        "NA|OrientDB|" + this.getClass().getSimpleName() + "|executeInternal|postConfigDetailsForGetInstanceTreeStrategy| %d ms",
        System.currentTimeMillis() - starTime5);
    responseModel.setDefaultTypes(defaultKlassesModel);
    responseModel.setFrom(dataModel.getFrom());
    
    return responseModel;
  }
  
  private void handleTask(List<IKlassInstanceInformationModel> klassInstanceInformation, Set<Long> klassInstanceIds,
      IGetKlassInstanceTreeStrategyModel dataModel, IGetKlassInstanceTreeModel responseModel) throws Exception
  {
    ITimeRange selectedTimeRange = dataModel.getSelectedTimeRange();
    if (selectedTimeRange != null && selectedTimeRange.getStartTime() != null && selectedTimeRange.getEndTime() != null) {
      
      Map<String, ITaskInstanceListModel> tasks = responseModel.getTasks();
      
      long startDate = selectedTimeRange.getStartTime();
      long endDate = selectedTimeRange.getEndTime();
      
      ITaskRecordDAO taskRecordDAO = rdbmsComponentUtils.openTaskDAO();
      Map<Long, List<ITaskRecordDTO>> allTaskByBaseEntityIIDs = taskRecordDAO.getAllTaskByBaseEntityIIDs(klassInstanceIds,
          this.rdbmsComponentUtils.getUserID(), startDate, endDate);
      
      allTaskByBaseEntityIIDs.forEach((baseEntityIID, taskRecords) -> {
        
        ITaskInstanceListModel taskInstanceListModel = new TaskInstanceListModel();
        List<ITaskInstance> taskList = new ArrayList<>();
        taskRecords.forEach(taskRecord -> {
          try {
            ITaskInstance taskInstance = TaskInstanceBuilder.getTakInstanceForCalenderView(taskRecord, rdbmsComponentUtils);
            taskList.add(taskInstance);
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
        taskInstanceListModel.setList(taskList);
        tasks.put(String.valueOf(baseEntityIID), taskInstanceListModel);
      });
    }
    
  }
  
  private void populateResponseList(List<IKlassInstanceInformationModel> responseList, List<IBaseEntityDTO> listOfDTO,
      IConfigDetailsForInstanceTreeGetModel configDetails) throws Exception
  {
    
    // Get X-ray properties
    Map<String, IAttribute> referencedAttributes = configDetails.getXRayConfigDetails().getReferencedAttributes();
    Map<String, ITag> referencedTags = configDetails.getXRayConfigDetails().getReferencedTags();
    Set<IPropertyDTO> properties = getAllUtils.getXrayProperties(referencedAttributes, referencedTags);
    
    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
      
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO,
          rdbmsComponentUtils);
      responseList.add(klassInstanceInformationModel);
      // X-ray property instance
      getAllUtils.getXrayPropertyDetails(referencedAttributes, referencedTags, baseEntityDTO, properties, klassInstanceInformationModel);
    }
  }
  
  public IModule getModule(String moduleId) throws Exception
  {
    
    List<IModule> modules = moduleMappingUtil.getRuntimeModule();
    
    for (IModule iModule : modules) {
      if (iModule.getId().equals(moduleId)) {
        return iModule;
      }
    }
    
    return null;
  }
  
  protected IConfigDetailsForInstanceTreeGetModel getConfigDetails(IConfigDetailsForInstanceTreeGetRequestModel model) throws Exception
  {
    return getConfigDetailsForInstanceTreeGetStrategy.execute(model);
  }
  
  protected IGetFilterInfoModel getFilterInfo(IGetFilterAndSortDataRequestModel idsModel) throws Exception
  {
    IGetFilterInformationModel filterInformationModel = getFilterAndSortDataForKlassStrategy.execute(idsModel);
    return getAllUtils.getFilterInfoModel(filterInformationModel);
  }
  
  public String generateSearchExpression(InstanceSearchModel dataModel)
  {
    String allSearch = dataModel.getAllSearch();
    String moduleId = dataModel.getModuleId();
    StringBuilder searchExpression = searchAssembler.getScope(dataModel.getSelectedTypes(), dataModel.getSelectedTaxonomyIds(),
        searchAssembler.getBaseTypeByModule(moduleId));
    
    List<? extends IPropertyInstanceFilterModel> attributes = dataModel.getAttributes();
    List<? extends IPropertyInstanceFilterModel> tags = dataModel.getTags();
    String evaluationExpression = searchAssembler.getEvaluationExpression(attributes, tags, allSearch);
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
  
  /*  protected List<ITaxonomyInformationModel> getTaxonomyParentList(IGetKlassInstanceTreeStrategyModel requestModel, String searchExpression)
      throws Exception
  {
    IIdsListParameterModel idListParameterModel = new IdsListParameterModel();
    ICategoryTreeInformationModel taxonomyTree = getTaxonomiesTreeStrategy.execute(idListParameterModel);
    List<String> rootTaxonomyIds = new ArrayList<>();
    getAllUtils.getFlatIdsList(taxonomyTree.getCategoryInfo(), rootTaxonomyIds);
    if(rootTaxonomyIds.isEmpty()) {
      return new ArrayList<>();
    }  
    return (List<ITaxonomyInformationModel>) getAllUtils.fillTaxonomyCountInfo(false, searchExpression, rootTaxonomyIds, ClassifierType.HIERARCHY, taxonomyTree.getCategoryInfo());
  }*/
  
  public String generateSearchExpressionForTaxonomy(InstanceSearchModel dataModel)
  {
    String allSearch = dataModel.getAllSearch();
    String moduleId = dataModel.getModuleId();
    StringBuilder searchExpression = searchAssembler.getBaseQuery(searchAssembler.getBaseTypeByModule(moduleId));
    List<? extends IPropertyInstanceFilterModel> attributes = dataModel.getAttributes();
    List<? extends IPropertyInstanceFilterModel> tags = dataModel.getTags();
    String evaluationExpression = searchAssembler.getEvaluationExpression(attributes, tags, allSearch);
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
}
