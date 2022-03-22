package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.entity.supplier.ISupplier;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.interactor.model.supplier.SupplierModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrientDBGetAllMasterSupplierStrategy extends OrientDBBaseStrategy
    implements IGetAllMasterSuppliersStrategy {
  
  public static final String useCase = "GetAllSuppliers";
  
  @Override
  public IListModel<ISupplierModel> execute(ISupplierModel model) throws Exception
  {
    List<ISupplierModel> targetModelsList = new ArrayList<ISupplierModel>();
    Map<String, Object> requestMap = new HashMap<>();
    ListModel<ISupplier> targetList = execute(useCase, requestMap,
        new TypeReference<ListModel<ISupplier>>()
        {
          
        });
    for (ISupplier supplier : targetList.getList()) {
      targetModelsList.add(new SupplierModel(supplier));
    }
    IListModel<ISupplierModel> returnModel = new ListModel<>();
    returnModel.setList(targetModelsList);
    
    return returnModel;
  }
}
