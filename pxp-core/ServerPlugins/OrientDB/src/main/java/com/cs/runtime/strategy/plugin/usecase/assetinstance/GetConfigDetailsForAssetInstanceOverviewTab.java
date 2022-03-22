package com.cs.runtime.strategy.plugin.usecase.assetinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.runtime.strategy.plugin.usecase.assetinstance.utils.AssetInstanceUtils;
import com.cs.runtime.strategy.plugin.usecase.klassinstance.GetConfigDetailsForOverviewTab;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForAssetInstanceOverviewTab extends GetConfigDetailsForOverviewTab {
  
  public GetConfigDetailsForAssetInstanceOverviewTab(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForAssetInstanceOverviewTab/*" };
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
  
  @Override
  protected List<String> getFieldsToFetch()
  {
    ArrayList<String> fieldsToFetch = new ArrayList<String>(super.getFieldsToFetch());
    fieldsToFetch.add(IAsset.TRACK_DOWNLOADS);
    
    return fieldsToFetch;
  }
}
