/*package com.cs.postgre.article.save;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.klassinstance.AbstractKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.pim.runtime.strategy.usecase.articleinstance.ISaveArticleAttributeInstanceStrategy;
import com.cs.pxpui.unittest.base.configuration.BaseConfigurationTest;

public class SaveAttributeInstanceStrategyTest extends BaseConfigurationTest {

  @Autowired
  protected ISaveArticleAttributeInstanceStrategy saveArticleAttributeInstanceStrategy;

  @Test
  public void saveArticleAttributeInstanceTest() throws Exception
  {
    IKlassInstanceSaveModel saveArticleInstance = ObjectMapperUtil.readValue(this.getClass()
        .getClassLoader().getResourceAsStream("com/cs/resources/saveKlassInstance.json"), AbstractKlassInstanceSaveModel.class);

    IModel responceModel = new VoidModel();
    responceModel = saveArticleAttributeInstanceStrategy.execute(saveArticleInstance);
    assertNull(responceModel);
  }
}*/
