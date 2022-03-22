package com.cs.runtime.strategy.plugin.usecase.assetinstance;

import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.klass.GetConfigDetailsForTimelineTab;
import com.cs.runtime.strategy.plugin.usecase.assetinstance.utils.AssetInstanceUtils;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForAssetInstanceTimelineTab extends GetConfigDetailsForTimelineTab {
  
  public GetConfigDetailsForAssetInstanceTimelineTab(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForAssetInstanceTimelineTab/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    return (Map<String, Object>) super.execute(requestMap);
  }
  
  @Override
  protected List<String> getDefaultRuntimeTabs(IGetConfigDetailsHelperModel helperModel)
      throws Exception
  {
    List<String> tabIds = new ArrayList<>(super.getDefaultRuntimeTabs(helperModel));
    return AssetInstanceUtils.getRuntimeAssetInstanceTabs(helperModel, tabIds);
  }
}
