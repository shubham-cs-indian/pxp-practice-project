package com.cs.core.runtime.interactor.usecase.taxonomy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.strategy.usecase.target.IGetTargetKlassesStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.IGetTaxonomyTreeByLeafIdsStrategy;
import com.cs.core.runtime.interactor.model.configdetails.CategoryInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.targetinstance.GetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesStrategyModel;
import com.cs.core.runtime.interactor.model.taxonomy.GetTaxonomyTreeLeafIdsStrategyModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeForQuicklistModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeLeafIdsStrategyModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetTaxonomyTreeByLeafIdsForRelationshipQuicklist
    extends AbstractRuntimeInteractor<IGetTaxonomyTreeForQuicklistModel, ICategoryInformationModel>
    implements IGetTaxonomyTreeByLeafIdsForRelationshipQuicklist {
  
  @Autowired
  protected ISessionContext                                          context;
  
  @Autowired
  protected IGetTargetKlassesStrategy                                getTargetKlassesStrategy;
  
  @Autowired
  protected IGetTaxonomyTreeByLeafIdsStrategy                        getTaxonomyTreeByLeafIdsStrategy;
  
  /*@Autowired
  protected IGetTaxonomyContentCountForRelationshipQuicklistStrategy getTaxonomyContentCountForRelationshipQuicklistStrategy;
  */
  @Override
  public ICategoryInformationModel executeInternal(IGetTaxonomyTreeForQuicklistModel dataModel)
      throws Exception
  {
    IGetTargetKlassesModel allowedTypesModel = new GetTargetKlassesModel();
    allowedTypesModel.setKlassId(dataModel.getTypeKlassId());
    allowedTypesModel.setRelationshipId(dataModel.getRelationshipId());
    allowedTypesModel.setUserId(context.getUserId());
    IGetTargetKlassesStrategyModel targetKlassesStrategyModel = getTargetKlassesStrategy
        .execute(allowedTypesModel);
    
    List<String> targetKlassIds = targetKlassesStrategyModel.getAllowedTypes();
//    dataModel.setKlassIdsHavingRP(targetKlassIds);
    
    dataModel.setCurrentUserId(context.getUserId());
    dataModel.setTargetType(targetKlassesStrategyModel.getKlassType());
    
    List<IConfigEntityTreeInformationModel> categoryInfo = new ArrayList<>();
    
    List<String> leafIds = dataModel.getLeafIds();
    dataModel.setIsKlassTaxonomy(false);
    
    IGetTaxonomyTreeLeafIdsStrategyModel strategyModel = new GetTaxonomyTreeLeafIdsStrategyModel();
    strategyModel.setParentTaxonomyId(dataModel.getParentTaxonomyId());
    strategyModel.setLeafIds(leafIds);
    
    ICategoryInformationModel categoryInfoModel = getTaxonomyTreeByLeafIdsStrategy
        .execute(strategyModel);
    if (categoryInfoModel != null) {
      categoryInfo.add(categoryInfoModel);
    }
    
    if (categoryInfo.isEmpty()) {
      return new CategoryInformationModel();
    }
    
    dataModel.setCategoryInfo(categoryInfo);
    /*
    return getTaxonomyContentCountForRelationshipQuicklistStrategy.execute(dataModel);*/
    return categoryInfoModel;
  }
  
}
