package com.cs.config.strategy.plugin.usecase.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

@SuppressWarnings("unchecked")
public class BulkCreateAttributes extends AbstractOrientPlugin {
  
  public BulkCreateAttributes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<Map<String, Object>> attributeList = new ArrayList<>();
    attributeList = (List<Map<String, Object>>) map.get("list");
    List<Map<String, Object>> createdAttributeList = new ArrayList<>();
    List<Map<String, Object>> failedAttributeList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();

    
    for (Map<String, Object> attribute : attributeList) {
      
      try {
        Map<String, Object> attributeMap = AttributeUtils.createAttributeData(attribute,auditInfoList);
        Map<String, Object> returnAttributeMap = new HashMap<>();
        returnAttributeMap.put(ISummaryInformationModel.ID, attributeMap.get(IAttributeModel.ID));
        returnAttributeMap.put(ISummaryInformationModel.LABEL,
            attributeMap.get(IAttributeModel.LABEL));
        createdAttributeList.add(returnAttributeMap);
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) attribute.get(IAttributeModel.LABEL));
        addToFailureIds(failedAttributeList, attribute);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) attribute.get(IAttributeModel.LABEL));
        addToFailureIds(failedAttributeList, attribute);
      }
    }
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, createdAttributeList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedAttributeList);
    result.put(IPluginSummaryModel.AUDIT_LOG_INFO, auditInfoList); 
    return result;
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedAttributeList,
      Map<String, Object> attribute)
  {
    Map<String, Object> failedAttributeMap = new HashMap<>();
    failedAttributeMap.put(ISummaryInformationModel.ID, attribute.get(IAttributeModel.ID));
    failedAttributeMap.put(ISummaryInformationModel.LABEL, attribute.get(IAttributeModel.LABEL));
    failedAttributeList.add(failedAttributeMap);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkCreateAttributes/*" };
  }
}
