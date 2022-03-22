package com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.DeleteTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public abstract class AbstractDeleteTagTaxonomy extends AbstractOrientPlugin {
  
  public AbstractDeleteTagTaxonomy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getVertexType();
  
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<>();
    IExceptionModel failure = new ExceptionModel();
    List<String> idsToDelete = (List<String>) requestMap.get("ids");
    List<Map<String , Object>> auditInfoList = new ArrayList<>();
    for (String id : idsToDelete) {
      DeleteTaxonomyUtil.deleteKlassTaxonomies(id, getVertexType(), auditInfoList);
    }
    UtilClass.getGraph()
        .commit();
    responseMap.put(IBulkDeleteReturnModel.SUCCESS, idsToDelete);
    responseMap.put(IBulkDeleteReturnModel.FAILURE, failure);
    responseMap.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditInfoList);
    return responseMap;
  }
}
