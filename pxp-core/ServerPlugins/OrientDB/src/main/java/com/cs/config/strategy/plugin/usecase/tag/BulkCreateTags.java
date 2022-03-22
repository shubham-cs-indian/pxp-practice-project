package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkCreateTags extends AbstractOrientPlugin {
  
  public BulkCreateTags(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> mapList = (List<Map<String, Object>>) requestMap.get("list");
    List<Map<String, Object>> returnMapList = new ArrayList<>();
    List<Map<String, Object>> failedTagList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();

    for (Map<String, Object> tagMap : mapList) {
      try {
        Map<String, Object> returnedTagMap = TagUtils.createTag(tagMap, auditLogInfoList);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put(ISummaryInformationModel.ID, returnedTagMap.get(ITagModel.ID));
        returnMap.put(ISummaryInformationModel.LABEL, returnedTagMap.get(ITagModel.LABEL));
        returnMapList.add(returnMap);
      }
      catch (TagNotFoundException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) tagMap.get(ITagModel.LABEL));
        addToFailureIds(failedTagList, tagMap);
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) tagMap.get(ITagModel.LABEL));
        addToFailureIds(failedTagList, tagMap);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) tagMap.get(ITagModel.LABEL));
        addToFailureIds(failedTagList, tagMap);
      }
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.SUCCESS, returnMapList);
    result.put(IPluginSummaryModel.FAILED_IDS, failedTagList);
    result.put(IPluginSummaryModel.AUDIT_LOG_INFO, auditLogInfoList); 

    return result;
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedTagList, Map<String, Object> tag)
  {
    Map<String, Object> failedTagMap = new HashMap<>();
    failedTagMap.put(ISummaryInformationModel.ID, tag.get(ITagModel.ID));
    failedTagMap.put(ISummaryInformationModel.LABEL, tag.get(ITagModel.LABEL));
    failedTagList.add(failedTagMap);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkCreateTags/*" };
  }
}
