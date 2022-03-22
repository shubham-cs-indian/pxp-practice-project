package com.cs.runtime.strategy.plugin.usecase.file;

import com.cs.runtime.strategy.plugin.usecase.file.base.AbstractSaveFileSupplierKlassMapping;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;

public class SaveArticleFileSupplierKlassMapping extends AbstractSaveFileSupplierKlassMapping {
  
  public SaveArticleFileSupplierKlassMapping(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @Override
  public boolean execute(OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    return super.execute(iRequest, iResponse);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveArticleFileSupplierKlassMapping/*" };
  }
}
