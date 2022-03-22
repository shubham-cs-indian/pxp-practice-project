package com.cs.core.runtime.interactor.usecase.base;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantWithNumberModel;
import com.cs.core.config.strategy.usecase.tag.IGetDisplayTagIdsStrategy;
import com.cs.core.config.strategy.usecase.variant.IGetAllVariantsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.klassinstance.GetAllKlassInstancesVariantsModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllKlassInstancesVariantsModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGetAllInstanceVariants<P extends IIdParameterModel, R extends IListModel<? extends IKlassInstanceInformationModel>>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected IGetAllVariantsStrategy   getAllVariantsStrategy;
  
  @Autowired
  protected ISessionContext           context;
  
  @Autowired
  protected IGetDisplayTagIdsStrategy getDisplayTagIdsStrategy;
  
  protected abstract R executeGetAllInstanceVariants(
      IGetAllKlassInstancesVariantsModel getAllKlassInstancesVariantsModel) throws Exception;
  
  @SuppressWarnings("unchecked")
  @Override
  public R executeInternal(P dataModel) throws Exception
  {
    IListModel<IVariantWithNumberModel> idsModel = getAllVariantsStrategy.execute(dataModel);
    List<String> listOfIds = new ArrayList<String>();
    
    for (IVariantWithNumberModel v : idsModel.getList()) {
      listOfIds.add(v.getId());
    }
    
    IGetAllKlassInstancesVariantsModel getAllKlassInstancesVariantsModel = new GetAllKlassInstancesVariantsModel();
    getAllKlassInstancesVariantsModel.setIds(listOfIds);
    
    IListModel<String> dimensionalTagsIdsModel = getDisplayTagIdsStrategy.execute(null);
    getAllKlassInstancesVariantsModel
        .setDimensionalTagIds((Collection<String>) dimensionalTagsIdsModel.getList());
    
    String loginUserId = context.getUserId();
    getAllKlassInstancesVariantsModel.setCurrentUserId(loginUserId);
    
    return null;
  }
}
