/*package com.cs.postgre.article.create;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.pim.runtime.interactor.model.articleinstance.CreateTagInstanceStrategyModel;
import com.cs.pim.runtime.interactor.model.articleinstance.ICreateTagInstanceStrategyModel;
import com.cs.pim.runtime.strategy.usecase.articleinstance.ICreateArticleTagInstanceStrategy;
import com.cs.pxpui.unittest.base.configuration.BaseConfigurationTest;
import com.fasterxml.jackson.core.type.TypeReference;

public class CreateTagInstanceStrategyTest extends BaseConfigurationTest {

  @Autowired
  protected ICreateArticleTagInstanceStrategy createTagInstanceStrategyPostgre;

  @Test
  public void createArticleInstanceTest() throws Exception
  {
    ICreateTagInstanceStrategyModel model = new CreateTagInstanceStrategyModel();
    // setting model values

    TypeReference<IKlassInstance> typeReference = new TypeReference<IKlassInstance>(){};
    IKlassInstance articleInstance = ObjectMapperUtil.readValue(this.getClass()
        .getClassLoader().getResourceAsStream("com/cs/resources/klassInstance.json"), typeReference);
    model.setTags(articleInstance.getTags());
    IVoidModel responceModel = new VoidModel();
    responceModel = createTagInstanceStrategyPostgre.execute(model);
    assertNull(responceModel);
  }
}*/
