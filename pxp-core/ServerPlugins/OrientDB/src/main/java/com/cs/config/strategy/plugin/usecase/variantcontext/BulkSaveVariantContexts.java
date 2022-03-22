package com.cs.config.strategy.plugin.usecase.variantcontext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.validationontype.InvalidContextTypeException;
import com.cs.core.config.interactor.exception.variantcontext.BulkSaveVariantContextsFailedException;
import com.cs.core.config.interactor.model.variantcontext.IBulkSaveVariantContextsResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGetAllVariantContextsResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGridEditVariantContextInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class BulkSaveVariantContexts extends AbstractOrientPlugin {
  
  public BulkSaveVariantContexts(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveVariantContexts/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> variantContextsSaveList = (List<Map<String, Object>>) requestMap
        .get("list");
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> listOfSuccessSaveVariantContexts = new ArrayList<>();
    List<Map<String,Object>> auditInfoList = new ArrayList<>();
    
    for (Map<String, Object> variantContext : variantContextsSaveList) {
      Vertex contextNode = null;
      try {
        contextNode = UtilClass.getVertexById(
            (String) variantContext.get(IGridEditVariantContextInformationModel.ID),
            VertexLabelConstants.VARIANT_CONTEXT);
        if (!variantContext.get(IGridEditVariantContextInformationModel.TYPE)
            .equals(contextNode.getProperty(IGridEditVariantContextInformationModel.TYPE))) {
          throw new InvalidContextTypeException();
        }
        UtilClass.saveNode(variantContext, contextNode, new ArrayList<>());
        Map<String, Object> contextMap = UtilClass.getMapFromVertex(Arrays.asList(
            CommonConstants.CODE_PROPERTY, IGridEditVariantContextInformationModel.LABEL,
            IGridEditVariantContextInformationModel.ICON,
            IGridEditVariantContextInformationModel.TYPE,
            IGridEditVariantContextInformationModel.CODE,
            IGridEditVariantContextInformationModel.IS_STANDARD), contextNode);
        listOfSuccessSaveVariantContexts.add(contextMap);
        AuditLogUtils.fillAuditLoginfo(auditInfoList, contextNode, Entities.CONTEXT, Elements.UNDEFINED);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    UtilClass.getGraph()
        .commit();
    if (listOfSuccessSaveVariantContexts.isEmpty()) {
      throw new BulkSaveVariantContextsFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    Map<String, Object> responseMap = new HashMap<>();
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IGetAllVariantContextsResponseModel.LIST, listOfSuccessSaveVariantContexts);
    responseMap.put(IBulkSaveVariantContextsResponseModel.SUCCESS, successMap);
    responseMap.put(IBulkSaveVariantContextsResponseModel.FAILURE, failure);
    responseMap.put(IBulkSaveVariantContextsResponseModel.AUDIT_LOG_INFO, auditInfoList);
    
    return responseMap;
  }
}
