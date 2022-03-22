package coms.cs.core.config.businessapi.variantcontext;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.IBulkSaveVariantContextsResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGridEditVariantContextInformationModel;

public interface IBulkSaveVariantContextsService
    extends ISaveConfigService<IListModel<IGridEditVariantContextInformationModel>, IBulkSaveVariantContextsResponseModel> {

}
