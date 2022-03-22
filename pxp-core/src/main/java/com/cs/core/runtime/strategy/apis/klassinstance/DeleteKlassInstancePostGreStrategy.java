/*package com.cs.core.runtime.strategy.apis.klassinstance;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.exception.RDBMSException;
import com.cs.core.runtime.interactor.model.klassinstance.DeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.usecase.klassinstance.IDeleteKlassInstanceStrategy;

@Component
public class DeleteKlassInstancePostGreStrategy implements IDeleteKlassInstanceStrategy {


  @Override
  public IDeleteInstancesResponseModel execute(IDeleteKlassInstanceRequestModel model)
      throws Exception
  {
    List<IDeleteKlassInstanceModel> deleteRequest = model.getDeleteRequest();
    List<String> successfulIds = new ArrayList<>();


    for (IDeleteKlassInstanceModel iDeleteKlassInstanceModel : deleteRequest) {

      List<String> ids = iDeleteKlassInstanceModel.getIds();

      try{

        for (String id : ids) {
          //FIXME add comment from UI if provided
          baseEntityDAO.delete("delete comment");
          successfulIds.addAll(ids);
        }
      }
      catch (RDBMSException cause) {
        //FIXME handle cause and throw application level exception
      }
    }
    DeleteInstancesResponseModel deleteInstancesResponseModel = new DeleteInstancesResponseModel();
    deleteInstancesResponseModel.setSuccess(successfulIds);
    return deleteInstancesResponseModel;
  }
}*/
