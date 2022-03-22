package com.cs.core.runtime.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.goldenrecord.ITypesInfoWithRuleIdModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetKlassInstancesByKlassAndTaxonomyIdsAndDeleteBucketStrategy
    extends IRuntimeStrategy<ITypesInfoWithRuleIdModel, IListModel<IContentTypeIdsInfoModel>> {
  
}
