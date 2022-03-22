package com.cs.core.runtime.interactor.usecase.instance;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.targetinstance.GetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeForQuicklistModel;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForHierarchyRelationshipQuicklistModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractKlassTaxonomyForRelationshipQuickList<P extends IGetTaxonomyTreeForQuicklistModel, R extends ICategoryInformationModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected ISessionContext context;
  
  protected abstract IConfigDetailsForHierarchyRelationshipQuicklistModel getConfigDetails(
      IGetTargetKlassesModel model, P dataModel) throws Exception;
  
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
    
    IConfigDetailsForHierarchyRelationshipQuicklistModel configDetails = getConfigDetails(
        allowedTypesModel, dataModel);
    
    List<IConfigEntityTreeInformationModel> childrenCategoryInfo = configDetails.getCategoryInfo();
    
    IConfigEntityTreeInformationModel parentCategoryInfo = new ConfigEntityTreeInformationModel();
    parentCategoryInfo.setId("-1");
    parentCategoryInfo.setChildren(childrenCategoryInfo);
    
    List<IConfigEntityTreeInformationModel> categoryInfo = new ArrayList<>();
    categoryInfo.add(parentCategoryInfo);
    
    dataModel.setCategoryInfo(categoryInfo);
    dataModel.setCurrentUserId(context.getUserId());
    dataModel.setIsKlassTaxonomy(true);
    dataModel.setTargetType(configDetails.getKlassType());
    dataModel.setKlassIdsHavingRP(configDetails.getAllowedTypes());
    dataModel.setTaxonomyIdsHavingRP(configDetails.getTaxonomyIdsHavingRP());
    
    ICategoryInformationModel returnModel = executeGetTaxonomyContentCountForQuickList(dataModel);
    return (R) returnModel;
  }
}
