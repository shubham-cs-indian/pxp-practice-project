package coms.cs.core.config.businessapi.variantcontext;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantContextReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteVariantContextService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteVariantContextReturnModel> {

}
