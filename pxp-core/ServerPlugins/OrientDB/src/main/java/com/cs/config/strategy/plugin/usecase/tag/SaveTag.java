package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.tag.BulkSaveTagFailedException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkSaveTagResponseModel;
import com.cs.core.config.interactor.model.tag.IGetTagGridResponseModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveTag extends AbstractUpdateTag {
  
  public SaveTag(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveTag/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfTags = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSaveTag = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();

    for (Map<String, Object> tagMap : listOfTags) {
      try {
        tagMap = updateTag(tagMap,auditInfoList);
        listOfSuccessSaveTag.add(tagMap);
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, null);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, null);
      }
    }
    if (listOfSuccessSaveTag.isEmpty()) {
      throw new BulkSaveTagFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> bulkSaveTagResponse = new HashMap<String, Object>();
    Map<String, Object> successMap = new HashMap<>();
    Map<String, Object> referencedTags = new HashMap<>();
    
    referencedTags = TagUtils.fillReferenceTags(listOfSuccessSaveTag);
    successMap.put(IGetTagGridResponseModel.TAGS_LIST, listOfSuccessSaveTag);
    successMap.put(IGetTagGridResponseModel.REFERENCED_TAGS, referencedTags);
    
    bulkSaveTagResponse.put(IBulkSaveTagResponseModel.SUCCESS, successMap);
    bulkSaveTagResponse.put(IBulkSaveTagResponseModel.FAILURE, failure);
    bulkSaveTagResponse.put(IBulkSaveTagResponseModel.AUDIT_LOG_INFO, auditInfoList); 
    
    return bulkSaveTagResponse;
  }
}
