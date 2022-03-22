package com.cs.core.runtime.staticcollection;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.data.Text;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.entity.dto.SortDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.rdbms.entity.idto.ISortDTO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.runtime.interactor.model.collections.ICreateStaticCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IStaticCollectionModel;
import com.cs.core.runtime.interactor.model.collections.StaticCollectionModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.searchable.InstanceSearchModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractCreateStaticCollection<P extends ICreateStaticCollectionModel, R extends IStaticCollectionModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected RDBMSComponentUtils   rdbmsComponentUtils;
  
  @Autowired
  protected SearchAssembler       searchAssembler;
  
  @Autowired
  protected GetAllUtils           getAllUtils;
  
  @Autowired
  protected CollectionValidations collectionValidations;
  
  @Override
  public R executeInternal(ICreateStaticCollectionModel model) throws Exception
  {
    collectionValidations.validate(model);
    
    List<String> klassInstanceIds = model.getKlassInstanceIds();
    Set<Long> linkedBaseEntityIIDs = new HashSet<Long>();
    for (String id : klassInstanceIds) {
      linkedBaseEntityIIDs.add(Long.valueOf(id));
    }
    
    evaluateFiltersAndFillBaseEntityIIds(model, linkedBaseEntityIIDs);
    ICollectionDTO newCollectionDTO = rdbmsComponentUtils.newCollectionDTO(CollectionType.staticCollection, model.getLabel(), "",
        model.getParentId(), model.getIsPublic(), new JSONContent(), linkedBaseEntityIIDs);
    
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    
    ICollectionDTO createCollectionRecord = collectionDAO.createCollection(newCollectionDTO);
    
    IStaticCollectionModel responseModel = prepareResponseModel(createCollectionRecord);
    
    for (Long entityId : linkedBaseEntityIIDs) {
      rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(entityId, IEventDTO.EventType.ELASTIC_UPDATE);
    }
    return (R) responseModel;
  }
  
  private void evaluateFiltersAndFillBaseEntityIIds(ICreateStaticCollectionModel model, Set<Long> linkedBaseEntityIIDs)
      throws RDBMSException, Exception
  {
    IGetKlassInstanceTreeStrategyModel filterResultsToSave = model.getFilterResultsToSave();
    if (filterResultsToSave != null) {
      InstanceSearchModel searchModel = (InstanceSearchModel) filterResultsToSave;
      String searchExpression = generateSearchExpression(searchModel);
      
      List<ISortDTO> sortOptions = filterResultsToSave.getSortOptions().stream()
          .map(x -> new SortDTO(x.getSortField(), ISortDTO.SortOrder.valueOf(x.getSortOrder().toLowerCase()), x.getIsNumeric()))
          .collect(Collectors.toList());
      IRDBMSOrderedCursor<IBaseEntityDTO> cursor = getAllUtils.getCursor(rdbmsComponentUtils.getLocaleCatlogDAO(), sortOptions,
          searchExpression);
      
      List<IBaseEntityDTO> baseEntityDTOs = cursor.getNext((int) cursor.getCount());
      
      if (baseEntityDTOs != null && !baseEntityDTOs.isEmpty()) {
        linkedBaseEntityIIDs.addAll(getKlassInstanceIds(baseEntityDTOs));
      }
    }
  }
  
  private IStaticCollectionModel prepareResponseModel(ICollectionDTO createCollectionRecord)
  {
    IStaticCollectionModel responseModel = new StaticCollectionModel();
    responseModel.setId(Long.toString(createCollectionRecord.getCollectionIID()));
    responseModel.setLabel(createCollectionRecord.getCollectionCode());
    responseModel.setType(Long.toString(createCollectionRecord.getCollectionType().ordinal()));
    responseModel.setParentId(Long.toString(createCollectionRecord.getParentIID()));
    responseModel.setCreatedOn(createCollectionRecord.getCreatedTrack().getWhen());
    responseModel.setCreatedBy(createCollectionRecord.getCreatedTrack().getWho());
    responseModel.setIsPublic(createCollectionRecord.getIsPublic());
    responseModel.setPhysicalCatelogId(createCollectionRecord.getCatalogCode());
    
    Set<Long> linkedBaseEntityIIDs = createCollectionRecord.getLinkedBaseEntityIIDs();
    List<String> klassInstanceIds = new ArrayList<>();
    for (Long id : linkedBaseEntityIIDs) {
      klassInstanceIds.add(Long.toString(id));
    }
    
    responseModel.setKlassInstanceIds(klassInstanceIds);
    return responseModel;
  }
  
  @SuppressWarnings("rawtypes")
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
  
  private Set<Long> getKlassInstanceIds(List<IBaseEntityDTO> listOfDTO) throws Exception
  {
    Set<Long> klassInstanceIds = new HashSet<>();
    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
      klassInstanceIds.add(baseEntityDTO.getBaseEntityIID());
    }
    return klassInstanceIds;
  }
  
}
