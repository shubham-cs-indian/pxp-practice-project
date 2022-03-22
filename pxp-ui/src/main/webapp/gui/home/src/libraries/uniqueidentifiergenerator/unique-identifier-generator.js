/**
 * Created by DEV on 16-06-2015.
 */

import '../../screens/homescreen/store/model/localstorage';
import LocalStorageManager from '../localstoragemanager/local-storage-manager';
import {getTranslations as oTranslations} from '../../commonmodule/store/helper/translation-manager.js'

var UniqueIdentifierGenerator = (function () {

  return {
    generateUUID: function () {
      var iCurrentTimeStamp = new Date().getTime();

      var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var iRandom = (iCurrentTimeStamp + Math.random() * 16) % 16 | 0;

        iCurrentTimeStamp = Math.floor(iCurrentTimeStamp / 16);

        return (c == 'x' ? iRandom : (iRandom & 0x3 | 0x8)).toString(16);
      });

      return uuid;
    },

    generateUntitledName: function(){
      var iUntitledNameCounter = LocalStorageManager.getPropertyFromLocalStorage('untitledNameCounter');
      LocalStorageManager.setPropertyInLocalStorage('untitledNameCounter', +iUntitledNameCounter + 1);
      return oTranslations().NEW + " " + iUntitledNameCounter;
    }
  }
})();

export default UniqueIdentifierGenerator;