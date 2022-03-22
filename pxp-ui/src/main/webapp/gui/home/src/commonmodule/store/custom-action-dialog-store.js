import CS from '../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import { view as CustomActionDialogView } from '../views/custom-action-dialog-view';
import MicroEvent from '../../libraries/microevent/MicroEvent.js';
import { getTranslations as getTranslation } from './helper/translation-manager.js';
import ActionDialogProps from './../props/action-dialog-props';
import UniqueIdentifierGenerator from '../../libraries/uniqueidentifiergenerator/unique-identifier-generator';

//TODO: #Clean Up:- Show alertify and error message with Translation
let CustomActionDialogStore = (function () {

  let _deleteDOMAfterButtonClick = () => {
    // ReactDOM.unmountComponentAtNode(document.getElementById("dialogContainer"));
    ActionDialogProps.setIsCustomDialogOpen(false);
  };

  let _showTriActionDialog = (sMessage, oConfirmHandler, oDiscardHandler, oCancelHandler, sHeader) => {
    sHeader = CS.isNotEmpty(sHeader) ? sHeader : getTranslation().WARNING;
    ActionDialogProps.setIsCustomDialogOpen(true);
    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true,
        handler: oCancelHandler
      },
      {
        id: "discard",
        label: getTranslation().DISCARD,
        isFlat: true,
        handler: oDiscardHandler
      },
      {
        id: "save",
        label: getTranslation().SAVE,
        isFlat: false,
        handler: oConfirmHandler
      }
    ];

    ReactDOM.render(<CustomActionDialogView
            header={sHeader}
            message={sMessage}
            buttonData={aButtonData}
            isTriActionDialog={true}
            deleteDOMCallback={_deleteDOMAfterButtonClick}
            key={UniqueIdentifierGenerator.generateUUID()}
        />,
        document.getElementById('dialogContainer'));
  };

  let _showConfirmDialog = (sMessage, sHeader, oConfirmHandler, oCancelHandler, oDialogTitleStyle, bIsManageConfig, aButtonData) => {
    sHeader = CS.isNotEmpty(sHeader) ? sHeader : getTranslation().WARNING;
    ActionDialogProps.setIsCustomDialogOpen(true);
    aButtonData = !CS.isEmpty(aButtonData)? aButtonData : [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true,
        handler: oCancelHandler
      },
      {
        id: "ok",
        label: getTranslation().OK,
        isFlat: false,
        handler: oConfirmHandler
      }
    ];
    let oMessageDom = <div style={{marginBottom: 24}}>{sMessage}</div>;
    ReactDOM.render(<CustomActionDialogView
            header={sHeader}
            message={oMessageDom}
            buttonData={aButtonData}
            titleStyle={oDialogTitleStyle}isManageConfig={bIsManageConfig}
            count={1}
            deleteDOMCallback={_deleteDOMAfterButtonClick}
            key={UniqueIdentifierGenerator.generateUUID()}
        />,
        document.getElementById('dialogContainer'));
  };

  let _showListModeConfirmDialog = (sHeader, aSelectedArticleNames, oConfirmHandler, oCancelHandler, bIsManageConfig) => {
    sHeader = CS.isNotEmpty(sHeader) ? sHeader : getTranslation().WARNING;
    ActionDialogProps.setIsCustomDialogOpen(true);
    if (aSelectedArticleNames) {
      aSelectedArticleNames.sort();
    }
    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true,
        handler: oCancelHandler
      },
      {
        id: "ok",
        label: getTranslation().OK,
        isFlat: false,
        handler: oConfirmHandler
      }
    ];

    let oArticleCountDom = (
        <div className="selectedArticleCountWrapper">
            {aSelectedArticleNames.length}
            {aSelectedArticleNames.length > 1 ? getTranslation().SELECTED_ITEMS : getTranslation().SELECTED_ITEM}
        </div>
    );

    let aListArray = [];
    CS.forEach(aSelectedArticleNames, function (oSelectedArticleName) {
      aListArray.push(<li className="listModeConfirmationItem">{oSelectedArticleName}</li>);
    });

    let oMessageDom = (
        <div className="listModeConfirmationContainer">
          {oArticleCountDom}
          <ol className="listModeConfirmationWrapper">
            {aListArray}
          </ol>
        </div>
    );

    ReactDOM.render(
        <CustomActionDialogView
            header={sHeader}
            message={oMessageDom}
            buttonData={aButtonData}
            deleteDOMCallback={_deleteDOMAfterButtonClick}
            isManageConfig={bIsManageConfig}
            count={aSelectedArticleNames.length}
            key={UniqueIdentifierGenerator.generateUUID()}
           />,
        document.getElementById('dialogContainer'));
  };

  let _showCustomConfirmDialog = (sHeader, sMessage, aCustomButtonData, oStyle) => {
    sHeader = CS.isNotEmpty(sHeader) ? sHeader : getTranslation().WARNING;
    let aButtonData = aCustomButtonData;
    ActionDialogProps.setIsCustomDialogOpen(true);

    ReactDOM.render(<CustomActionDialogView
            header={sHeader}
            message={sMessage}
            buttonData={aButtonData}
            style={oStyle}
            deleteDOMCallback={_deleteDOMAfterButtonClick}
            key={UniqueIdentifierGenerator.generateUUID()}/>,
        document.getElementById('dialogContainer'));
  };

  let _resetCustomDialog = () =>{
    _deleteDOMAfterButtonClick();
  };


  /********************************Public API**************************************/
  return {
    showTriActionDialog: function (sMessage, oConfirmHandler, oDiscardHandler, oCancelHandler, sHeader) {
      _showTriActionDialog(sMessage, oConfirmHandler, oDiscardHandler, oCancelHandler, sHeader);
    },

    showConfirmDialog: function (sMessage, sHeader, oConfirmHandler, oCancelHandler, oDialogTitleStyle, bIsManageConfig, aButtonData) {
      _showConfirmDialog(sMessage, sHeader, oConfirmHandler, oCancelHandler, oDialogTitleStyle, bIsManageConfig, aButtonData);
    },

    showListModeConfirmDialog: function (sHeader, aArticleNames, oConfirmHandler, oCancelHandler, bIsManageConfig) {
      _showListModeConfirmDialog(sHeader, aArticleNames, oConfirmHandler, oCancelHandler, bIsManageConfig);
    },

    showCustomConfirmDialog: function (sHeader, sMessage, aCustomButtonData, oStyle) {
      _showCustomConfirmDialog(sHeader, sMessage, aCustomButtonData, oStyle);
    },

    resetCustomDialog: function () {
      /** If we want to take any action on esc apart from closing the dialog then that we need to implement
       *  Modal is not supported yet i.e to prevent the dialog rom closing on pressing esc button also
       * */
      _resetCustomDialog();
    }
  };
})();

MicroEvent.mixin(CustomActionDialogStore);
export default CustomActionDialogStore;
