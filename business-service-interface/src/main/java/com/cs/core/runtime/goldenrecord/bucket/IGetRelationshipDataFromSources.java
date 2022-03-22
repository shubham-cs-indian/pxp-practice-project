package com.cs.core.runtime.goldenrecord.bucket;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetRelationshipDataFromSourcesRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetRelationshipDataFromSourcesResponseModel;

public interface IGetRelationshipDataFromSources
    extends IRuntimeService<IGetRelationshipDataFromSourcesRequestModel, IGetRelationshipDataFromSourcesResponseModel> {
  
}