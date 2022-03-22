package com.cs.config.strategy.plugin.usecase.klass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetKlassesTree;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetKlassesTree extends AbstractGetKlassesTree {
  
  public GetKlassesTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassesTree/*" };
  }
  
  @Override
  public String getTypeKlass()
  {
    return VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS;
  }
  
  @Override
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    try {
      return super.executeInternal(requestMap);
    }
    catch (KlassNotFoundException e) {
      throw new KlassNotFoundException(e);
    }
  }
  
  public List<String> getFieldsToFetch()
  {
    return Arrays.asList(
        IConfigEntityTreeInformationModel.LABEL, IConfigEntityTreeInformationModel.TYPE,
        IConfigEntityTreeInformationModel.CODE);
  }
}
