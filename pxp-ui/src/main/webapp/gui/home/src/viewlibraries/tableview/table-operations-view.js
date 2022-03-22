import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as NewDateRangeSelectorView } from './../daterangeselectorview/new-date-range-selector-view';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';

const oEvents = {

};
const oPropTypes = {
  createButtonData: ReactPropTypes.shape({
    onClickHandler: ReactPropTypes.func
  }),
  // dateRangeSelectorData: ReactPropTypes.shape(oDateRangeSelectorPropTypes), //This view is already deprecate so
  // removing the usage of date-range-selector-view
/*
  columnOrganiserData: ReactPropTypes.shape(oContextMenuWithShufflePropTypes),
*/
  isDisableTableView: ReactPropTypes.bool
};

/**
 * @class TableOperationsView - Use for making and display context tab view.
 * @memberOf Views
 * @property {custom} [createButtonData] - Pass a function for create new context button click.
 * @property {custom} [saveButtonData] - Pass a function for save button click for save context.
 * @property {custom} [discardButtonData] - Pass a function for save button click for discard context.
 * @property {custom} [dateRangeSelectorData] - Pass a function for select date range.
 * @property {custom} [searchBarData] - Pass two function onBlur and onChange for change and blur effect.
 * @property {custom} [columnOrganiserData] - Pass data contain masterList, isMultiselect[boolean], oData, onApplyHandler, selectedList.
 * @property {bool} [isDisableTableView] - Pass boolean for true and false table view.
 */

// @CS.SafeComponent
class TableOperationsView extends React.Component {

  constructor(props) {
    super(props);
  }

  getCreateButtonView =(oCreateButtonData)=> {
    let fOnClickHandler = oCreateButtonData.onClickHandler || null;
    /*let bisDisabled = oCreateButtonData.isDisabled;
    var sClassName = bisDisabled ? "createButtonDisabled" : "createButton";*/
    // let oStyle = {
    //   float: "left",
    //   height: "30px",
    //   "margin-right": "10px",
    //   "border-radius": "3px",
    //   "lineHeight": "30px"
    // };
    let oCreateButtonLabelStyles = {
      "text-transform": "uppercase",
      "font-size": "11px",
    };
    let oStyle = {
      height: "28px",
      lineHeight: "28px",
      marginRight: "10px",
      padding: '0 10px',
      minWidth: '64px',
      minHeight: '28px',
      boxShadow: 'none'
    };

    return (
    <CustomMaterialButtonView
        label={getTranslation().CREATE}
        isRaisedButton={true}
        isDisabled={false}
        // style={oStyle}
        labelStyle={oCreateButtonLabelStyles}
        onButtonClick={fOnClickHandler}/>
    );
  }

 /* getSaveButtonView =()=> {
    let fOnClickHandler = this.props.saveButtonData.onClickHandler || null;
    // let oSaveButtonStyles = {
    //   float: "right",
    //   height: "30px",
    //   "margin-right": "10px",
    //   "border-radius": "3px",
    //   "lineHeight": "30px"
    // };
    let oSaveButtonLabelStyles = {
      "text-transform": "uppercase",
      "font-size": "11px",
    };

    return (
        <CustomMaterialButtonView
            label={getTranslation().SAVE}
            isRaisedButton={true}
            isDisabled={false}
            // style={oSaveButtonStyles}
            labelStyle={oSaveButtonLabelStyles}
            onButtonClick={fOnClickHandler}/>
    );
  }

  getDiscardButtonView =()=> {
    let fOnClickHandler = this.props.discardButtonData.onClickHandler || null;
    let oDiscardButtonStyles = {
      float: "right",
      height: "30px",
      "margin-right": "10px",
      "border-radius": "3px",
      "line-height":0

    };
    let oDiscardButtonLabelStyles = {
      "text-transform": "uppercase",
      "font-size": "11px",
    };

    return (
    <CustomMaterialButtonView
        label={getTranslation().DISCARD}
        isRaisedButton={false}
        isDisabled={false}
        style={oDiscardButtonStyles}
        labelStyle={oDiscardButtonLabelStyles}
        onButtonClick={fOnClickHandler}/>
    );
  }*/

  getDateRangeSelectorView =(oDateRangeSelectorData)=> {

    return (
        <NewDateRangeSelectorView
            {...oDateRangeSelectorData}
        />
    );
  }

  render() {

    let __props = this.props;
    let bDisplayCreateButton = !(CS.isEmpty(__props.createButtonData) || __props.createButtonData.isDisabled);
    let aTableViewOperation = [];

    bDisplayCreateButton ? aTableViewOperation.push(this.getCreateButtonView(__props.createButtonData)) : null;  // eslint-disable-line
    __props.dateRangeSelectorData ? aTableViewOperation.push(this.getDateRangeSelectorView(__props.dateRangeSelectorData)) : null; // eslint-disable-line

    return ((CS.isEmpty(aTableViewOperation) && __props.isDisableTableView) ? null :
            (<div className="tableOperationsView">
              {aTableViewOperation}
            </div>));
  }

}



TableOperationsView.propTypes = oPropTypes;

export const view = TableOperationsView;
export const events = oEvents;
