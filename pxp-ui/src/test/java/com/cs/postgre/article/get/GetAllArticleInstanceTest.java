/*package com.cs.postgre.article.get;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.GetAllArticleInstance;
import com.cs.pxpui.unittest.base.configuration.BaseConfigurationTest;

public class GetAllArticleInstanceTest extends BaseConfigurationTest {


  @Test
  public void testGetAll()
  {
    boolean failed = false ;

    GetAllArticleInstance strategy = new GetAllArticleInstance();
    IGetKlassInstanceTreeStrategyModel model = new  GetKlassInstanceTreeStrategyModel();

    model.setFrom(0);
    int fetchSize = 5 ;
    model.setSize(fetchSize);
    try {
      IGetKlassInstanceTreeModel result = strategy.execute(model);
       List<IKlassInstanceInformationModel> children = result.getChildren();
       for (IKlassInstanceInformationModel article : children) {
        assertEquals(article.getAttributes().get(0).getAttributeId(), "nameattribute");
      }
      assertEquals(children.size() , fetchSize);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    failed = true;
    }
    assertFalse(failed);
  }
}
*/
