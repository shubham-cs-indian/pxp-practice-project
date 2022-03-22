
import LocalStorageManager from '../../../../libraries/localstoragemanager/local-storage-manager';
import SessionStorageConstants from '../../../../commonmodule/tack/session-storage-constants';

(function () {
  var iUntitledNameCounter = LocalStorageManager.getPropertyFromLocalStorage('untitledNameCounter');

  LocalStorageManager.setPropertyInLocalStorage('untitledNameCounter', iUntitledNameCounter || 0);
  LocalStorageManager.setPropertyInLocalStorage('requestId', '');
  LocalStorageManager.setPropertyInLocalStorage(SessionStorageConstants.SESSION_ID, '');
  LocalStorageManager.setPropertyInLocalStorage('userScenario', 'loadContent');
})();
