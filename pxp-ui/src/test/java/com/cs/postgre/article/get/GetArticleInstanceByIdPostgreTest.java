/*package com.cs.postgre.article.get;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.strategy.usecase.templating.IGetArticleInstanceForCustomTabStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.pxpui.unittest.base.configuration.BaseConfigurationTest;
import com.fasterxml.jackson.core.type.TypeReference;


public class GetArticleInstanceByIdPostgreTest extends BaseConfigurationTest {

  @Autowired
  IGetArticleInstanceForCustomTabStrategy getArticleInstanceByIdPostgre;

  @Test
  public void getArticleInstancebyIdTest() throws Exception
  {
    GetInstanceRequestStrategyModelForCustomTab model = new GetInstanceRequestStrategyModelForCustomTab();

    TypeReference<IKlassInstance> typeReference = new TypeReference<IKlassInstance>(){};
    IKlassInstance articleInstance = ObjectMapperUtil.readValue(this.getClass().getClassLoader().getResourceAsStream("com/cs/resources/klassInstance.json"), typeReference);
    model.setId(articleInstance.getId());
    IGetKlassInstanceCustomTabModel responseModel = getArticleInstanceByIdPostgre.execute(model);

    Assert.assertNotNull(responseModel);
    Assert.assertEquals(articleInstance.getId(), responseModel.getKlassInstance().getId());
  }
}*/
