package com.cs.core.runtime.interactor.usecase.taxonomy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.klass.ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistResponseModel;
import com.cs.core.config.strategy.usecase.klass.IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistStrategy;
import com.cs.core.runtime.interactor.model.configdetails.CategoryInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeForVariantLinkedInstancesQuicklistModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
@SuppressWarnings("unchecked")
public class GetTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklist extends
    AbstractRuntimeInteractor<IGetTaxonomyTreeForVariantLinkedInstancesQuicklistModel, ICategoryInformationModel>
    implements IGetTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklist {
  
  @Autowired
  protected ISessionContext                                                                  context;
  
  /*@Autowired
  protected IGetTaxonomyContentCountStrategy                                                 getTaxonomyContentCountStrategy;
  */
  /*
  @Autowired
  protected IGetTaxonomyTreeByLeafIdsStrategy            getTaxonomyTreeByLeafIdsStrategy;
  
  @Autowired
  protected IGetKlassesTreeStrategy                      getKlassesTreeStrategy;
  
  @Autowired
  protected IConfigDetailsForTaxonomyHierarchyStrategy   getAllowedEntitiesAndKlassIdsHavingRPStrategy;
  */
  
  @Autowired
  protected IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistStrategy configDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistStrategy;
  
  @Override
  public ICategoryInformationModel executeInternal(
      IGetTaxonomyTreeForVariantLinkedInstancesQuicklistModel dataModel) throws Exception
  {
    String entityId = dataModel.getEntityId();
    List<String> entities = new ArrayList<>();
    entities.add(entityId);
    
    /*
    if (parentTaxonomyId.isEmpty() && leafIds.isEmpty()) {
      dataModel.setIsKlassTaxonomy(true);
      List<IConfigEntityTreeInformationModel> klassTreeChildren = getKlassTree(entities)
          .getCategoryInfo();
      ICategoryInformationModel klassTaxonomy = new CategoryInformationModel();
      klassTaxonomy.setId("-1");
      klassTaxonomy.setChildren(klassTreeChildren);
      categoryInfo.add(klassTaxonomy);
    }
    else {
      dataModel.setIsKlassTaxonomy(false);
      IGetTaxonomyTreeLeafIdsStrategyModel strategyModel = new GetTaxonomyTreeLeafIdsStrategyModel();
      strategyModel.setParentTaxonomyId(parentTaxonomyId);
      strategyModel.setLeafIds(leafIds);
      ICategoryInformationModel categoryInfoModel = getTaxonomyTreeByLeafIdsStrategy
          .execute(strategyModel);
      if (categoryInfoModel != null) {
        categoryInfo.add(categoryInfoModel);
      }
    }
    */
    
    List<String> leafIds = dataModel.getLeafIds();
    String parentTaxonomyId = dataModel.getParentTaxonomyId();
    Boolean isKlassTaxonomy = parentTaxonomyId.isEmpty() && leafIds.isEmpty();
    
    IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel requestModel = new ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel(
        context.getUserId(), entityId, parentTaxonomyId, leafIds, isKlassTaxonomy);
    IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistResponseModel responseModel = configDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistStrategy
        .execute(requestModel);
    
    List<IConfigEntityTreeInformationModel> categoryInfo = new ArrayList<>();
    categoryInfo.add(responseModel.getCategoryInfo());
    if (categoryInfo.isEmpty()) {
      return new CategoryInformationModel();
    }
    
    dataModel.setIsKlassTaxonomy(isKlassTaxonomy);
    dataModel.setCategoryInfo(categoryInfo);
    dataModel.setModuleEntities(entities);
    dataModel.setCurrentUserId(context.getUserId());
    dataModel.setKlassIdsHavingRP(responseModel.getKlassIdsHavingRP());
        ICategoryInformationModel infoModel = null/*getTaxonomyContentCountStrategy.execute(dataModel);*/;
    return infoModel;
    
  }
  
  /*
  private ICategoryTreeInformationModel getKlassTree(List<String> entities) throws Exception
  {
    IIdsListParameterModel listModel = new IdsListParameterModel();
    List<String> ids = KlassInstanceUtils.getStandardKlassIds(entities);
    listModel.setIds(ids);
    return getKlassesTreeStrategy.execute(listModel);
  }
  */
}
