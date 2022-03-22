package com.cs.core.runtime.interactor.usecase.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.strategy.usecase.target.IGetTargetKlassesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.targetinstance.GetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesStrategyModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeForQuicklistModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractTaxonomyHierarchyForRelationshipQuickList<P extends IGetTaxonomyTreeForQuicklistModel, R extends ICategoryInformationModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected IGetTargetKlassesStrategy getTargetKlassesStrategy;
  
  @Autowired
  protected ISessionContext           context;
  
  protected abstract IConfigEntityTreeInformationModel getTaxonomyHierarchy(IIdParameterModel model)
      throws Exception;
  
  protected abstract ICategoryInformationModel executeGetTaxonomyContentCountForQuickList(P model)
      throws Exception;
  
  @SuppressWarnings("unchecked")
  @Override
  public R executeInternal(P dataModel) throws Exception
  {
    IGetTargetKlassesModel allowedTypesModel = new GetTargetKlassesModel();
    allowedTypesModel.setKlassId(dataModel.getTypeKlassId());
    allowedTypesModel.setRelationshipId(dataModel.getRelationshipId());
    allowedTypesModel.setUserId(context.getUserId());
    IGetTargetKlassesStrategyModel targetKlassesStrategyModel = getTargetKlasses(allowedTypesModel,
        dataModel);
    
    List<String> targetKlassIds = targetKlassesStrategyModel.getAllowedTypes();
    dataModel.setKlassIdsHavingRP(new HashSet<>(targetKlassIds));
    
    dataModel.setIsKlassTaxonomy(false);
    String clickedTaxonomyId = dataModel.getClickedTaxonomyId();
    IIdParameterModel idParameterModel = new IdParameterModel(clickedTaxonomyId);
    List<IConfigEntityTreeInformationModel> categoryInfo = new ArrayList<>();
    categoryInfo.add(getTaxonomyHierarchy(idParameterModel));
    dataModel.setCategoryInfo(categoryInfo);
    
    dataModel.setCurrentUserId(context.getUserId());
    dataModel.setTargetType(targetKlassesStrategyModel.getKlassType());
    return (R) executeGetTaxonomyContentCountForQuickList(dataModel);
  }
  
  protected IGetTargetKlassesStrategyModel getTargetKlasses(IGetTargetKlassesModel model,
      P dataModel) throws Exception
  {
    return getTargetKlassesStrategy.execute(model);
  }
}
