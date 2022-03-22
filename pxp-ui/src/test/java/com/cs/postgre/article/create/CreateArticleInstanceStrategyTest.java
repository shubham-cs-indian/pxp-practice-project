/*package com.cs.postgre.article.create;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.instance.CreateInstanceStrategyModel;
import com.cs.core.runtime.interactor.model.instance.ICreateInstanceStrategyModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.pim.runtime.strategy.usecase.articleinstance.ICreateArticleInstanceStrategy;
import com.cs.pxpui.unittest.base.configuration.BaseConfigurationTest;
import com.fasterxml.jackson.core.type.TypeReference;

public class CreateArticleInstanceStrategyTest extends BaseConfigurationTest {

  @Autowired
  protected ICreateArticleInstanceStrategy createArticleInstanceStrategyPostgre;

  @Test
  public void createArticleInstanceTest() throws Exception
  {
    ICreateInstanceStrategyModel model = new CreateInstanceStrategyModel();
    // setting model values

    TypeReference<IKlassInstance> typeReference = new TypeReference<IKlassInstance>(){};
    IKlassInstance articleInstance = ObjectMapperUtil.readValue(this.getClass()
        .getClassLoader().getResourceAsStream("com/cs/resources/klassInstance.json"), typeReference);
    model.setKlassInstance(articleInstance);

    IModel responceModel = new VoidModel();
    responceModel = createArticleInstanceStrategyPostgre.execute(model);
    assertNull(responceModel);
  }
}*/
