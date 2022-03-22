import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import PdfReactorServerProps from './../model/pdf-reactor-server-config-view-props';
import SettingUtils from './../helper/setting-utils';
import {getTranslations as getTranslation} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import CS from "../../../../../../libraries/cs";
import {PdfReactorServerRequestMapping as oRequestMapping} from '../../tack/setting-screen-request-mapping';
import RequestMapping from "../../../../../../libraries/requestmappingparser/request-mapping-parser";

let PdfReactorServerStore = (function () {

      let _triggerChange = function () {
        PdfReactorServerStore.trigger('pdf-reactor-server-changed');
      };

      let _makeActivePdfReactorServerDirty = function () {
        let oPdfReactorServerModel = PdfReactorServerProps.getPdfReactorServerData();
        SettingUtils.makeObjectDirty(oPdfReactorServerModel);
        return oPdfReactorServerModel.clonedObject ? oPdfReactorServerModel.clonedObject : oPdfReactorServerModel;
      };

      let _handleCommonConfigSingleValueChanged = function (sKey, sVal) {
        let oPdfReactorServerData = _makeActivePdfReactorServerDirty();
        if (oPdfReactorServerData) {
          oPdfReactorServerData[sKey] = CS.trim(sVal);
        }
        _triggerChange();
      };

      let _testServerConfiguration = function (oCallbackData = {}) {
        let oPdfReactorServerData = PdfReactorServerProps.getPdfReactorServerData();
        oPdfReactorServerData = oPdfReactorServerData.clonedObject;

        SettingUtils.csPostRequest(oRequestMapping.TestServerConfiguration, {}, oPdfReactorServerData, successSavePdfReactorServerConfiguration.bind(this, oCallbackData), failureTestPdfReactorServerConfiguration);
      };

      let failureTestPdfReactorServerConfiguration = function (oResponse) {
        SettingUtils.failureCallback(oResponse, "failureTestPdfReactorServerConfiguration", getTranslation());
      };

      let _handleDiscardAction = function (oCallbackData = {}) {
        let oPdfReactorServerModel = PdfReactorServerProps.getPdfReactorServerData();
        delete  oPdfReactorServerModel.clonedObject;
        delete oPdfReactorServerModel.isDirty;
        alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        if (CS.isFunction(oCallbackData.functionToExecute)) {
          oCallbackData.functionToExecute();
        }
        _triggerChange();
      };

      let _fetchPdfReactorServerDetails = function () {
        let oPutData = {
          id: "smartDocument"
        };
        let sURL = oRequestMapping.GetServerConfiguration;
        SettingUtils.csCustomGetRequest(RequestMapping.getRequestUrl(sURL, oPutData), {},
            successFetchPdfReactorServer, failureFetchPdfReactorServer);
      };

      let successFetchPdfReactorServer = function (oResponse) {
        let oSuccessResponse = oResponse.success;
        oSuccessResponse.rendererPort == null ? oSuccessResponse.rendererPort = "" : oSuccessResponse.rendererPort; // eslint-disable-line
        oSuccessResponse.rendererHostIp == null ? oSuccessResponse.rendererHostIp = "" : oSuccessResponse.rendererHostIp; // eslint-disable-line
        PdfReactorServerProps.setPdfReactorServerData(oSuccessResponse);
        _triggerChange();
      };

      let failureFetchPdfReactorServer = function (oResponse) {
        SettingUtils.failureCallback(oResponse, "failureFetchPdfReactorServer", getTranslation())
        _triggerChange();
      };

      let successSavePdfReactorServerConfiguration = function (oCallbackData, oResponse) {
        successFetchPdfReactorServer(oResponse);
        alertify.success(getTranslation().SUCCESSFULLY_SAVED);
        if (CS.isFunction(oCallbackData.functionToExecute)) {
          oCallbackData.functionToExecute();
        }
      };

      return {
        handleCommonConfigSingleValueChanged: function (sKey, sVal) {
          _handleCommonConfigSingleValueChanged(sKey, sVal);
        },

        handleDiscardAction: function (oCallbackData) {
          _handleDiscardAction(oCallbackData);
        },

        handleSaveAction: function (oCallbackData) {
          _testServerConfiguration(oCallbackData);
        },

        fetchPdfReactorServerDetails: function () {
          _fetchPdfReactorServerDetails();
        }
      }
    }
)();


MicroEvent.mixin(PdfReactorServerStore);
export default PdfReactorServerStore;
