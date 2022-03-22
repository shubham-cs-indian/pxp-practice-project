package com.cs.core.config.strategy.usecase.supplier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.klass.GetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestForRelationshipsModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestForRelationshipsModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetMultiClassificationKlassDetailsStrategy;

@Component("getMultiClassificationSupplierDetailsStrategy")
public class GetMultiClassificationSupplierDetailsStrategy extends OrientDBBaseStrategy
    implements IGetMultiClassificationKlassDetailsStrategy {
  
  public static final String useCase = "GetMultiClassificationSupplierDetails";
  
  @Override
  public IGetMultiClassificationKlassDetailsModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put("userId", model.getUserId());
    map.put("klassIds", model.getKlassIds());
    map.put("taxonomyIds", model.getSelectedTaxonomyIds());
    map.put("isUnlinkedRelationships", model.getIsUnlinkedRelationships());
    map.put(IMulticlassificationRequestModel.TAXONOMY_IDS_FOR_DETAILS,
        model.getTaxonomyIdsForDetails());
    if (model instanceof MulticlassificationRequestForRelationshipsModel) {
      map.put(IMulticlassificationRequestForRelationshipsModel.X_RAY_ATTRIBUTES,
          ((MulticlassificationRequestForRelationshipsModel) model).getXRayAttributes());
      map.put(IMulticlassificationRequestForRelationshipsModel.X_RAY_TAGS,
          ((MulticlassificationRequestForRelationshipsModel) model).getXRayTags());
    }
    return execute(GET_MULTI_CLASSIFICATION_SUPPLIER_DETAILS, map,
        GetMultiClassificationKlassDetailsModel.class);
  }
}
