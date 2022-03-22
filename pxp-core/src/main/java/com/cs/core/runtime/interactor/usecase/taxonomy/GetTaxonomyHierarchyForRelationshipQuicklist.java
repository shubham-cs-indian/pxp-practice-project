package com.cs.core.runtime.interactor.usecase.taxonomy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.strategy.usecase.taxonomy.IGetTaxonomyHierarchyStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForQuickListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.targetinstance.GetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeForQuicklistModel;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetConfigDetailsForQuicklistStrategy;

@Service
public class GetTaxonomyHierarchyForRelationshipQuicklist extends
    AbstractTaxonomyHierarchyForRelationshipQuickList<IGetTaxonomyTreeForQuicklistModel, ICategoryInformationModel>
    implements IGetTaxonomyHierarchyForRelationshipQuicklist {
  
  @Autowired
  protected IGetTaxonomyHierarchyStrategy                            getTaxonomyHierarchyStrategy;
  
  @Autowired
  protected IGetConfigDetailsForQuicklistStrategy                    getConfigDetailsForQuicklistStrategy;
  
  /*@Autowired
  protected IGetTaxonomyContentCountForRelationshipQuicklistStrategy getTaxonomyContentCountForRelationshipQuicklistStrategy;
  */
  @Override
  protected ICategoryInformationModel executeGetTaxonomyContentCountForQuickList(
      IGetTaxonomyTreeForQuicklistModel dataModel) throws Exception
  {
    IGetTargetKlassesModel allowedTypesModel = new GetTargetKlassesModel();
    allowedTypesModel.setKlassId(dataModel.getTypeKlassId());
    allowedTypesModel.setRelationshipId(dataModel.getRelationshipId());
    allowedTypesModel.setUserId(context.getUserId());
    allowedTypesModel.setSideId(dataModel.getSideId());
    IConfigDetailsForQuickListModel targetKlassesStrategyModel = getConfigDetailsForQuicklistStrategy
        .execute(allowedTypesModel);
    
    List<String> targetKlassIds = targetKlassesStrategyModel.getAllowedTypes();
    dataModel.setKlassIdsHavingRP(new HashSet<>(targetKlassIds));
    
    dataModel.setIsKlassTaxonomy(false);
    String clickedTaxonomyId = dataModel.getClickedTaxonomyId();
    IIdParameterModel idParameterModel = new IdParameterModel(clickedTaxonomyId);
    List<IConfigEntityTreeInformationModel> categoryInfo = new ArrayList<>();
    categoryInfo.add(getTaxonomyHierarchyStrategy.execute(idParameterModel));
    dataModel.setCategoryInfo(categoryInfo);
    
    dataModel.setCurrentUserId(context.getUserId());
    dataModel.setTargetType(targetKlassesStrategyModel.getKlassType());
    
    return /*getTaxonomyContentCountForRelationshipQuicklistStrategy.execute(dataModel);*/ null;
  }
  
  @Override
  protected IConfigEntityTreeInformationModel getTaxonomyHierarchy(IIdParameterModel model)
      throws Exception
  {
    return getTaxonomyHierarchyStrategy.execute(model);
  }
  
}
