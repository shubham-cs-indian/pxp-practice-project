package com.cs.runtime.strategy.plugin.usecase.datatransfer;

import com.cs.runtime.strategy.plugin.usecase.base.datatransfer.AbstractGetConfigDetailsForPrepareDataForRelationshipDataTransfer;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetConfigDetailsForPrepareDataForRelationshipDataTransfer
    extends AbstractGetConfigDetailsForPrepareDataForRelationshipDataTransfer {
  
  public GetConfigDetailsForPrepareDataForRelationshipDataTransfer(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForPrepareDataForRelationshipDataTransfer/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }
}
