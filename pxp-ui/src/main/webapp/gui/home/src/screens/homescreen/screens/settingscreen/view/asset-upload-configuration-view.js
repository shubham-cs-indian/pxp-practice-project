import CS from "../../../../../libraries/cs";
import React from 'react';
import ReactPropTypes from 'prop-types';
import TooltipView from "./../../../../../viewlibraries/tooltipview/tooltip-view";
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import MockDataForAssetConfiguration
  from '../../../../../commonmodule/tack/mock-data-for-asset-extension-configuration';
import {getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {view as GridYesNoPropertyView} from './../../../../../viewlibraries/gridview/grid-yes-no-property-view';
import {view as CustomTextFieldView} from '../../../../../viewlibraries/customtextfieldview/custom-text-field-view';
import {view as CustomDialogView} from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import {view as CommonConfigSectionView} from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view';

import ViewUtils from './utils/view-utils';

const oEvents = {
  ASSET_CONFIG_ADD_NEW_SECTION_CLICKED: "asset_config_add_new_section_clicked",
  ASSET_CONFIG_EDIT_SECTION_CLICKED: "asset_config_edit_section_clicked",
  ASSET_CONFIG_DIALOG_BUTTON_CLICKED: "asset_config_dialog_button_clicked",
  DELETE_ASSET_CONFIGURATION_ROW_CLICKED: "delete_asset_configuration_row_clicked",
  HANDLE_ASSET_CONFIG_ROW_DATA_CHANGE: "handle_asset_config_row_data_change"
};

const oPropTypes = {
  fields: ReactPropTypes.array,
  uploadConfigurationLabel: ReactPropTypes.string,
  currentDialogConfigurationModel: ReactPropTypes.object
};


// @CS.SafeComponent
class AssetUploadConfigSectionsView extends React.Component {
  static propTypes = oPropTypes;

  handleDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.ASSET_CONFIG_DIALOG_BUTTON_CLICKED, sButtonId);
  };

  getTextFieldView = (oField, sColumnId) => {

    let fOnBlurHandler = this.handleRowDataChange.bind(this, oField, sColumnId);
    let sHintText = ViewUtils.getDecodedTranslation(getTranslation().TYPE_ASSET_EXTENSION, {assetType: this.props.uploadConfigurationLabel});
    return (
        <CustomTextFieldView
            value={oField.extension}
            onBlur={fOnBlurHandler}
            label={"label"}
            hintText={sHintText}
            isDisabled={true}
            hideTooltip={true}
            isMultiLine={false}
        />)
  };

  getPropertyCellView = (oField, oColumn) => {
    let _this = this;
    let oInputView = null;
    let sType = oColumn.type;
    let sColumnId = oColumn.id;
    switch (sType) {

      case "yesno":
        oInputView = (<GridYesNoPropertyView propertyId={oField.id} value={oField[oColumn.id]}
                                             isDisabled={true}
                                             onChange={_this.handleRowDataChange.bind(_this, oField, sColumnId)}
                                             key={oField.id + "_" + oColumn.id}/>);
        break;

      case "text":
        oInputView = _this.getTextFieldView(oField, sColumnId);
        break;

    }
    return oInputView;
  };

  handleRowDataChange = (oField, sColumnId, oData) => {
    EventBus.dispatch(oEvents.HANDLE_ASSET_CONFIG_ROW_DATA_CHANGE, oField, sColumnId, oData)
  };

  getRowsForExtensionConfiguration = (oField, oColumn, oContextButton, aRowItems, iColumnIndex) => {
    let oInputView = this.getPropertyCellView(oField, oColumn);
    let sClassName = "extensionRowItem ";
    sClassName += oColumn.className || "";

    aRowItems.push(
        <div className={sClassName} key={iColumnIndex}>
          {oInputView}
        </div>);
    return aRowItems;
  };

  addNewExtensionConfigurationSection = () => {
    EventBus.dispatch(oEvents.ASSET_CONFIG_ADD_NEW_SECTION_CLICKED);
  };

  deleteAssetConfigurationRow = (oRow) => {
    EventBus.dispatch(oEvents.DELETE_ASSET_CONFIGURATION_ROW_CLICKED, oRow);
  };

  editAssetConfigurationRow = (sId) => {
    EventBus.dispatch(oEvents.ASSET_CONFIG_EDIT_SECTION_CLICKED, sId);
  };

  getAddConfigurationDialog () {
    if (CS.isEmpty(this.props.currentDialogConfigurationModel)) {
      return null;
    }
    let fButtonHandler = this.handleDialogButtonClicked;
    let {dialogViewModel: oDialogViewModel, currentDialogConfigurationModel: oSectionsData} = this.props.currentDialogConfigurationModel;
    return (
        <CustomDialogView {...oDialogViewModel}
                          onRequestClose={fButtonHandler.bind(this, oDialogViewModel.buttonData[0].id)}
                          buttonClickHandler={fButtonHandler}>
          <CommonConfigSectionView {...oSectionsData}/>
        </CustomDialogView>
    );
  }

  render () {
    let _this = this;
    let _props = _this.props;
    let oContextButton = {};
    let aColumns = MockDataForAssetConfiguration;
    let aHeaderFields = [];
    CS.forEach(aColumns, function (oColumn, iColumnIndex) {
      var sClassName = "extensionRowItem extensionColumnLabel ";
      sClassName += oColumn.className || "";
      aHeaderFields.push(<TooltipView placement="bottom" label={CS.getLabelOrCode(oColumn)} key={iColumnIndex}>
        <div className={sClassName}>{CS.getLabelOrCode(oColumn)}</div>
      </TooltipView>);
    });

    let oSectionHeaderViews = (<div className="extensionHeaderRow">{aHeaderFields}</div>);

    //sectionRows
    var aSectionRows = [];

    CS.forEach(_props.fields, function (oField, iFieldIndex) {
      let aRowItems = [];
      CS.forEach(aColumns, function (oColumn, iColumnIndex) {
        aRowItems = _this.getRowsForExtensionConfiguration(oField, oColumn, oContextButton, aRowItems, iColumnIndex);
      });

      aSectionRows.push(
          <div className="extensionRow" key={iFieldIndex}>
            {aRowItems}
            <div className={"iconsWrapper"}>
              <div className={"editIcon"} onClick={_this.editAssetConfigurationRow.bind(_this, oField.id)}></div>
              <div className={"removeIcon"} onClick={_this.deleteAssetConfigurationRow.bind(_this, oField)}></div>
            </div>
          </div>
      );
    });

    let oSectionsView = (<div className={"uploadExtensionSection"}>{aSectionRows}</div>);

    let oAddNewSectionView = (
        <div className={"addNewExtensionContainer"}>
          <TooltipView placement="bottom" label={getTranslation().ADD_NEW_ITEM}>
            <div className="addNewExtensionSection"
                 onClick={this.addNewExtensionConfigurationSection}/>
            <div className={"addNewSection"}>{"Add more"}</div>
          </TooltipView>
        </div>
    );


    return (
        <div className={"assetUploadConfigurationSection"}>
          {this.getAddConfigurationDialog()}
          <div className={"configurationSectionRow"}>{getTranslation().UPLOAD_CONFIGURATION}</div>
          <div className={"assetExtensionConfigurationSectionContainer"}>
            {oSectionHeaderViews}
            {oSectionsView}
            {oAddNewSectionView}
          </div>

        </div>

    );
  }
}


export {AssetUploadConfigSectionsView as view} ;
export {oEvents as events} ;

