package com.cs.core.config.klass;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassTaxonomyTreeModel;

public interface IGetAllKlassesTreeService
    extends IGetConfigService<IIdParameterModel, IListModel<IKlassTaxonomyTreeModel>> {

}
