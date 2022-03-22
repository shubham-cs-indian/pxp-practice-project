/*package com.cs.postgre.article.delete;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.config.interactor.model.klass.DeleteKlassInstanceModel;
import com.cs.core.config.interactor.model.klass.DeleteKlassInstanceRequestModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.exception.configuration.DeleteInstanceFailedException;
import com.cs.core.runtime.interactor.model.klassinstance.DeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.strategy.usecase.klassinstance.IDeleteKlassInstanceStrategy;
import com.cs.pxpui.unittest.base.configuration.BaseConfigurationTest;

import junit.framework.Assert;

public class DeleteKlassInstancePostGreStrategyTest extends BaseConfigurationTest {

  private static final String  TEST_1_FIRST_ARTICLE_ID  = "467a5921-f2c0-48ac-8da7-db80520dfceb";
  private static final String  TEST_2_FIRST_ARTICLE_ID  = "c0ebe181-029f-4248-9979-7df0ad716578";
  private static final String  TEST_2_SECOND_ARTICLE_ID = "7c9b7e4c-b5ae-4620-ad52-b6afe9a68a1d";
  private static final String  TEST_3_FIRST_ARTICLE_ID  = "1a122d0f-56a6-4050-93dd-5d1572728577";
  private static final String  TEST_3_SECOND_ARTICLE_ID = "dcd7024c-76c9-4b28-8013-deb6711002fe";
  private static final String  TEST_4_FIRST_ARTICLE_ID  = "e59e6205-eff0-46f3-8a54-20325b0f718d";
  private static final String  TEST_4_SECOND_ARTICLE_ID = "a2effd82-3dda-4300-9458-c0b554c9a9ec";
  private static final String  TEST_4_THIRD_ARTICLE_ID = "f358f43f-e8cb-40de-b8a0-db3b42081b33";
  private static final String  TEST_4_FOURTH_ARTICLE_ID = "7884424f-1f21-4079-b380-1f904d93f7e1";

  @Autowired
  IDeleteKlassInstanceStrategy deleteKlassInstancePostGreStrategy;

  @BeforeClass
  public static void creatArticleInstanceForDelete()
  {

  }


   * Delete Single Instance Only in single request
   * TEST_1
   *
  //@Test
  @Test(expected = DeleteInstanceFailedException.class)
  public void deleteArticleInstancebyIdTest() throws Exception
  {
    DeleteKlassInstanceRequestModel requestModel = new DeleteKlassInstanceRequestModel();
    List<IDeleteKlassInstanceModel> deleteRequestList = new ArrayList<>();
    requestModel.setDeleteRequest(deleteRequestList);

    IDeleteKlassInstanceModel firstRequestInstanceModel = new DeleteKlassInstanceModel();
    firstRequestInstanceModel.setBaseType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
    List<String> ids = new ArrayList<>();
    ids.add(TEST_1_FIRST_ARTICLE_ID);
    firstRequestInstanceModel.setIds(ids);
    deleteRequestList.add(firstRequestInstanceModel);

    IDeleteInstancesResponseModel responseModel = deleteKlassInstancePostGreStrategy.execute(requestModel);
    List<String> success = ((DeleteInstancesResponseModel)responseModel).getSuccess();
    IExceptionModel failure = ((DeleteInstancesResponseModel)responseModel).getFailure();

    Assert.assertNotNull(requestModel);

    Assert.assertEquals(0, failure.getExceptionDetails().size());
    Assert.assertEquals(0, failure.getDevExceptionDetails().size());
    Assert.assertEquals(1, success.size());
  }


   * Delete Multiple Instance Only in single request
   * TEST_2
   *
  //@Test
  @Test(expected = DeleteInstanceFailedException.class)
  public void deleteArticleInstancebyIdsTest() throws Exception
  {
    DeleteKlassInstanceRequestModel requestModel = new DeleteKlassInstanceRequestModel();
    List<IDeleteKlassInstanceModel> deleteRequestList = new ArrayList<>();
    requestModel.setDeleteRequest(deleteRequestList);

    IDeleteKlassInstanceModel firstRequestInstanceModel = new DeleteKlassInstanceModel();
    firstRequestInstanceModel.setBaseType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
    List<String> ids = new ArrayList<>();
    ids.add(TEST_2_FIRST_ARTICLE_ID);
    ids.add(TEST_2_SECOND_ARTICLE_ID);
    firstRequestInstanceModel.setIds(ids);
    deleteRequestList.add(firstRequestInstanceModel);

    IDeleteInstancesResponseModel responseModel = deleteKlassInstancePostGreStrategy.execute(requestModel);
    List<String> success = ((DeleteInstancesResponseModel)responseModel).getSuccess();
    IExceptionModel failure = ((DeleteInstancesResponseModel)responseModel).getFailure();

    Assert.assertNotNull(requestModel);

    Assert.assertEquals(0, failure.getExceptionDetails().size());
    Assert.assertEquals(0, failure.getDevExceptionDetails().size());
    Assert.assertEquals(2, success.size());
  }


   * Delete Multiple Instance Only in single request
   * TEST_3
   *
  //@Test
  @Test(expected = DeleteInstanceFailedException.class)
  public void deleteArticleInstancebyIdAndMultipleRequestTest() throws Exception
  {
    DeleteKlassInstanceRequestModel requestModel = new DeleteKlassInstanceRequestModel();
    List<IDeleteKlassInstanceModel> deleteRequestList = new ArrayList<>();
    requestModel.setDeleteRequest(deleteRequestList);

    IDeleteKlassInstanceModel firstRequestInstanceModel = new DeleteKlassInstanceModel();
    firstRequestInstanceModel.setBaseType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
    List<String> ids = new ArrayList<>();
    ids.add(TEST_3_FIRST_ARTICLE_ID);
    firstRequestInstanceModel.setIds(ids);
    deleteRequestList.add(firstRequestInstanceModel);

    IDeleteKlassInstanceModel secondRequestInstanceModel = new DeleteKlassInstanceModel();
    secondRequestInstanceModel.setBaseType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
    List<String> idsOfArticle = new ArrayList<>();
    idsOfArticle.add(TEST_3_SECOND_ARTICLE_ID);
    secondRequestInstanceModel.setIds(idsOfArticle);
    deleteRequestList.add(secondRequestInstanceModel);

    IDeleteInstancesResponseModel responseModel = deleteKlassInstancePostGreStrategy.execute(requestModel);
    List<String> success = ((DeleteInstancesResponseModel)responseModel).getSuccess();
    IExceptionModel failure = ((DeleteInstancesResponseModel)responseModel).getFailure();

    Assert.assertNotNull(requestModel);

    Assert.assertEquals(0, failure.getExceptionDetails().size());
    Assert.assertEquals(0, failure.getDevExceptionDetails().size());
    Assert.assertEquals(2, success.size());
  }



   * Delete Multiple Instance Only in multiple request
   * TEST_4
   *
  //@Test
  @Test(expected = DeleteInstanceFailedException.class)
  public void deleteArticleInstancebyIdsAndMultipleRequestTest() throws Exception
  {
    DeleteKlassInstanceRequestModel requestModel = new DeleteKlassInstanceRequestModel();
    List<IDeleteKlassInstanceModel> deleteRequestList = new ArrayList<>();
    requestModel.setDeleteRequest(deleteRequestList);

    IDeleteKlassInstanceModel firstRequestInstanceModel = new DeleteKlassInstanceModel();
    firstRequestInstanceModel.setBaseType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
    List<String> ids = new ArrayList<>();
    ids.add(TEST_4_FIRST_ARTICLE_ID);
    ids.add(TEST_4_SECOND_ARTICLE_ID);
    firstRequestInstanceModel.setIds(ids);
    deleteRequestList.add(firstRequestInstanceModel);

    IDeleteKlassInstanceModel secondRequestInstanceModel = new DeleteKlassInstanceModel();
    secondRequestInstanceModel.setBaseType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
    List<String> idsOfArticle = new ArrayList<>();
    idsOfArticle.add(TEST_4_THIRD_ARTICLE_ID);
    idsOfArticle.add(TEST_4_FOURTH_ARTICLE_ID);
    secondRequestInstanceModel.setIds(idsOfArticle);
    deleteRequestList.add(secondRequestInstanceModel);

    IDeleteInstancesResponseModel responseModel = deleteKlassInstancePostGreStrategy.execute(requestModel);
    List<String> success = ((DeleteInstancesResponseModel)responseModel).getSuccess();
    IExceptionModel failure = ((DeleteInstancesResponseModel)responseModel).getFailure();

    Assert.assertNotNull(requestModel);

    Assert.assertEquals(0, failure.getExceptionDetails().size());
    Assert.assertEquals(0, failure.getDevExceptionDetails().size());
    Assert.assertEquals(4, success.size());
  }


   * Delete Request Only with empty request
   * TEST_4
   *
  @Test
  public void deleteArticleInstanceRequestEmptyRequestTest() throws Exception
  {
    DeleteKlassInstanceRequestModel requestModel = new DeleteKlassInstanceRequestModel();
    List<IDeleteKlassInstanceModel> deleteRequestList = new ArrayList<>();
    requestModel.setDeleteRequest(deleteRequestList);

    IDeleteKlassInstanceModel firstRequestInstanceModel = new DeleteKlassInstanceModel();
    firstRequestInstanceModel.setBaseType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
    deleteRequestList.add(firstRequestInstanceModel);

    IDeleteInstancesResponseModel responseModel = deleteKlassInstancePostGreStrategy.execute(requestModel);
    List<String> success = ((DeleteInstancesResponseModel)responseModel).getSuccess();
    IExceptionModel failure = ((DeleteInstancesResponseModel)responseModel).getFailure();

    Assert.assertNotNull(requestModel);

    Assert.assertEquals(0, failure.getExceptionDetails().size());
    Assert.assertEquals(0, failure.getDevExceptionDetails().size());
    Assert.assertEquals(0, success.size());
  }

}*/
