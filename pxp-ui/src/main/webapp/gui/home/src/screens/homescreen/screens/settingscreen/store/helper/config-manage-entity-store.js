import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ManageEntityConfigProps from "../model/manage-entity-config-props";
import {getTranslations as getTranslation} from "../../../../../../commonmodule/store/helper/translation-manager";
import SettingUtils from "../../store/helper/setting-utils";
import alertify from "../../../../../../commonmodule/store/custom-alertify-store";
import CS from '../../../../../../libraries/cs';

const ManageEntityStore = (function () {

  let _triggerChange = function () {
    ManageEntityStore.trigger('manage-entity-config-changed');
  };

  let _handleManageEntityDialogOpenButtonClicked = function (aSelectedIds, sType, oCallback) {
    if (aSelectedIds.length == 0){
      alertify.error(getTranslation().NOTHING_SELECTED);
    }
    else{
      return CS.postRequest(`config/getentityconfig/${sType}`, {}, {ids: CS.concat(aSelectedIds)}, successGetMappingCallback.bind(this, oCallback), failureGetMappingCallback);
    }
  };

  var successGetMappingCallback = function (oCallback, oResponse) {
    let totalCount = oResponse.success.usageSummary.reduce((count, val) => count + val.totalCount, 0);

    if(totalCount > 0 || !(oCallback && oCallback.functionToExecute)) {
      ManageEntityConfigProps.setDataForDialog(oResponse.success);
      ManageEntityConfigProps.setIsEntityDialogOpen(true);
      ManageEntityConfigProps.setDataForDeleteEntity(true);
      oCallback && oCallback.functionToExecute && oCallback.functionToExecute();
    } else {
      if (oCallback && oCallback.functionToExecute) {
        ManageEntityConfigProps.setDataForDeleteEntity(false);
        oCallback.functionToExecute();
      }
    }
  };

  var failureGetMappingCallback = function (oCallback, oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureGetMappingCallback', getTranslation());
  };

  let _handleManageEntityDialogCloseButtonClicked = function () {
    ManageEntityConfigProps.setIsEntityDialogOpen(false);
    _triggerChange();
  }

  return {

    handleManageEntityDialogOpenButtonClicked: function (aSelectedIds,sType, oCallback) {
      _handleManageEntityDialogOpenButtonClicked(aSelectedIds,sType, oCallback).then(_triggerChange);
    },

    handleManageEntityDialogCloseButtonClicked: function () {
      _handleManageEntityDialogCloseButtonClicked();
    },
  };

})();

MicroEvent.mixin(ManageEntityStore);
export default ManageEntityStore;
