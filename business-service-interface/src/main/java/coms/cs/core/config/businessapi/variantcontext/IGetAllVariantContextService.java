package coms.cs.core.config.businessapi.variantcontext;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetAllVariantContextsResponseModel;

public interface IGetAllVariantContextService extends IGetConfigService<IConfigGetAllRequestModel, IGetAllVariantContextsResponseModel> {

}
