package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetKlassTree;
import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetArticleTree extends AbstractGetKlassTree {
  
  public GetArticleTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetArticleTree/*" };
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
      throw new ArticleKlassNotFoundException(e);
    }
  }
}
