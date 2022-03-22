/**
 * Created by DEV on 08-07-2015.
 */
import UniqueIdentifierGenerator from '../uniqueidentifiergenerator/unique-identifier-generator.js';
import LocalStorageManager from '../localstoragemanager/local-storage-manager';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';

const actionInterceptorUtils = {

  setUseCase: function (_sUserScenario) {
    LocalStorageManager.setPropertyInLocalStorage('userScenario', _sUserScenario);
  },

  generateRequestId: function () {
    LocalStorageManager.setPropertyInLocalStorage('requestId', UniqueIdentifierGenerator.generateUUID());
  },

  invokeFunctionToBeCalled: function (oFunctionToBeCalled, aArgumentList) {
    return new Promise((resolve, reject) => {
      resolve(oFunctionToBeCalled(...aArgumentList));
    })
    .catch(
        function (oException) {
          const _sUserScenario = LocalStorageManager.getPropertyFromLocalStorage('userScenario');
          ExceptionLogger.error(_sUserScenario);
          ExceptionLogger.error(oException);
        }
    );
  }
};



const actionInterceptor = (sUseCase, oFunctionToExecute) => {

  return (...aArgumentList) => {
    actionInterceptorUtils.generateRequestId();
    actionInterceptorUtils.setUseCase(sUseCase);
    actionInterceptorUtils.invokeFunctionToBeCalled(oFunctionToExecute, aArgumentList);
  }

};

export default actionInterceptor;
