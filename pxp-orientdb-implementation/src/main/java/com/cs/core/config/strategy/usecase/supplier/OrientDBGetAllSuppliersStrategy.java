package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.entity.klass.IKlassBasic;
import com.cs.core.config.interactor.entity.supplier.ISupplier;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrientDBGetAllSuppliersStrategy extends OrientDBBaseStrategy
    implements IGetAllSuppliersStrategy {
  
  public static final String useCase = "GetSuppliers";
  
  @Override
  public IListModel<IKlassInformationModel> execute(ISupplierModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    ListModel<ISupplier> targetList = execute(useCase, requestMap,
        new TypeReference<ListModel<ISupplier>>()
        {
          
        });
    List<IKlassInformationModel> targetModelsList = new ArrayList<>();
    for (ISupplier target : targetList.getList()) {
      targetModelsList.add(new KlassInformationModel((IKlassBasic) target));
    }
    IListModel<IKlassInformationModel> returnModel = new ListModel<>();
    returnModel.setList(targetModelsList);
    return returnModel;
  }
}
