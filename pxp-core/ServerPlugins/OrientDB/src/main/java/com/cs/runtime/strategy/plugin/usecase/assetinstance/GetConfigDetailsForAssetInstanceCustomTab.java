package com.cs.runtime.strategy.plugin.usecase.assetinstance;

import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.runtime.strategy.plugin.usecase.assetinstance.utils.AssetInstanceUtils;
import com.cs.runtime.strategy.plugin.usecase.klassinstance.GetConfigDetailsForCustomTab;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetConfigDetailsForAssetInstanceCustomTab extends GetConfigDetailsForCustomTab {
  
  public GetConfigDetailsForAssetInstanceCustomTab(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForAssetInstanceCustomTab/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }
  
  @Override
  protected List<String> getDefaultRuntimeTabs(IGetConfigDetailsHelperModel helperModel)
      throws Exception
  {
    List<String> tabIds = new ArrayList<>(super.getDefaultRuntimeTabs(helperModel));
    return AssetInstanceUtils.getRuntimeAssetInstanceTabs(helperModel, tabIds);
  }
}
