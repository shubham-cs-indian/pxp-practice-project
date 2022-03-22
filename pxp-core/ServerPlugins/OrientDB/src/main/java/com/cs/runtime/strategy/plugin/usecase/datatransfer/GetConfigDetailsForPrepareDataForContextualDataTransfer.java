package com.cs.runtime.strategy.plugin.usecase.datatransfer;

import com.cs.runtime.strategy.plugin.usecase.base.datatransfer.AbstractGetConfigDetailsForPrepareDataForContexualDataTransfer;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetConfigDetailsForPrepareDataForContextualDataTransfer
    extends AbstractGetConfigDetailsForPrepareDataForContexualDataTransfer {
  
  public GetConfigDetailsForPrepareDataForContextualDataTransfer(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForPrepareDataForContextualDataTransfer/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }
}
