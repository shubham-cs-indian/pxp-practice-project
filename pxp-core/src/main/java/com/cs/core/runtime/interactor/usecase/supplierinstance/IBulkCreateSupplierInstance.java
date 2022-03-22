package com.cs.core.runtime.interactor.usecase.supplierinstance;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IBulkCreateSupplierInstance
    extends IRuntimeInteractor<IBulkCreateInstanceModel, IPluginSummaryModel> {
}
