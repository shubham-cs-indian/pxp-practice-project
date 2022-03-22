import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as CustomTextFieldView } from '../customtextfieldview/custom-text-field-view';
import { view as NumberLocaleView } from '../numberlocaleview/number-locale-view';
import { view as MultiSelectSearchView } from '../multiselectview/multi-select-search-view';
import { view as GridYesNoPropertyView } from '../gridview/grid-yes-no-property-view';
import TooltipView from './../tooltipview/tooltip-view';
import EntityTasksFormTypeConstants from '../../commonmodule/tack/entity-tasks-form-view-types';
import {view as CustomDatePicker} from "../customdatepickerview/customdatepickerview";

var oEvents = {
  ENTITY_TASK_FORM_VALUE_CHANGED: "entity_task_form_value_changed"
};

const oPropTypes = {
  models: ReactPropTypes.arrayOf({
    id: ReactPropTypes.string,
    label: ReactPropTypes.string,
    type: ReactPropTypes.string,
    value: ReactPropTypes.oneOfType([ReactPropTypes.string, ReactPropTypes.array]),
    items: ReactPropTypes.array,
    viewProperties: ReactPropTypes.object
  }).isRequired,
  onChangeHandler: ReactPropTypes.func,
  context: ReactPropTypes.string
};
/**
 * @class ContentTasksFormView
 * @memberOf Views
 * @property {array} models - Model contains id, label, type, value, items, viewProperties etc.
 * @property {func} [onChangeHandler] -
 * @property {string} [context] - Used for differentiate to witch operation will be performed.
 */


// @CS.SafeComponent
class ContentTasksFormView extends React.Component {
  static propTypes = oPropTypes;


  handleEntityTasksFormValueChanged = (oModel, sChangedValue) => {
    let __props = this.props;
    if (CS.isFunction(__props.onChangeHandler)) {
      this.props.onChangeHandler(oModel, sChangedValue);
    } else {
      EventBus.dispatch(oEvents.ENTITY_TASK_FORM_VALUE_CHANGED, __props.context, oModel, sChangedValue);
    }
  };

  handleEntityTasksFormMSSValueChanged = (oModel, aSelectedItems) => {
    if (oModel.viewProperties.isMultiSelect) {
      aSelectedItems = CS.isEmpty(aSelectedItems) ? [] : aSelectedItems;
    } else {
      aSelectedItems = aSelectedItems[0];
      aSelectedItems = CS.isEmpty(aSelectedItems) ? "" : aSelectedItems;
    }
    this.handleEntityTasksFormValueChanged(oModel, aSelectedItems);
  };

  handleFormFieldDateChanged = (oModel, sContext, sNull ,sValue) => {
    let sDate = (sValue.getMonth() + 1) + "/" + sValue.getDate() + "/" + sValue.getFullYear();
    EventBus.dispatch(oEvents.ENTITY_TASK_FORM_VALUE_CHANGED, sDate, sContext, oModel);
  };

  getCustomTextFieldView = (oModel, oViewProperties) => {
    return (
        <CustomTextFieldView onBlur={this.handleEntityTasksFormValueChanged.bind(this, oModel)}
                             value={oModel.value} {...oViewProperties}/>
    )
  };

  getNumberFieldView = (oModel, oViewProperties) => {
    return (
        <NumberLocaleView onBlur={this.handleEntityTasksFormValueChanged.bind(this, oModel)}
                          value={oModel.value} {...oViewProperties}/>
    )
  };

  getMSSView = (oModel, oViewProperties) => {
    let sValue = oModel.value;
    let aSelectedItems = [];
    !CS.isEmpty(sValue) && (aSelectedItems = CS.isArray(oModel.value) ? oModel.value : [oModel.value]);

    return (
        <MultiSelectSearchView selectedItems={aSelectedItems}
                               items={oModel.items}
                               {...oViewProperties}
                               onApply={this.handleEntityTasksFormMSSValueChanged.bind(this, oModel)}/>
    )
  };

  getGridYesNoPropertyView = (oModel, oViewProperties) => {
    return (<GridYesNoPropertyView value={oModel.value} {...oViewProperties}
                                   onChange={this.handleEntityTasksFormValueChanged.bind(this, oModel)}/>)
  };

  getDateView = (oModel, oViewProperties) => {
    let sDate = "";
    if(CS.isNotEmpty(oModel.value)){
      let aDateSplit = oModel.value.split("/");
       sDate = aDateSplit[1] + "/" + aDateSplit[0] + "/" + aDateSplit[2];
    }
    let sValue = (sDate) ? new Date(sDate) : null;
    let oTextFieldStyle = {
      "width": "100%",
      "height": "30px"
    };

    let oUnderlineStyle = {
      "borderBottom": "none"
    };
    return (<div className="taskDatePickerDetail">
      <CustomDatePicker
          value={sValue}
          textFieldStyle={oTextFieldStyle}
          underlineStyle={oUnderlineStyle}
          disabled={false}
          endOfDay={false}
          hideRemoveButton={true}
          onChange={this.handleFormFieldDateChanged.bind(this, oModel, "formDate")}
      />
    </div>)
  };

  getValueViewByType = (oModel) => {
    let oViewToReturn = null;
    let sClassName = "entityTasksFormValue ";
    let oViewProperties = oModel.viewProperties;
    oViewProperties = CS.isObject(oViewProperties) ? oViewProperties : {};
    let sModelType = oModel.type;
    switch (oModel.type) {
      case EntityTasksFormTypeConstants.TEXT_FIELD:
        oViewToReturn = this.getCustomTextFieldView(oModel, oViewProperties);
        break;

      case EntityTasksFormTypeConstants.NUMBER_FIELD:
        oViewToReturn = this.getNumberFieldView(oModel, oViewProperties);
        break;

      case EntityTasksFormTypeConstants.MSS:
        oViewToReturn = this.getMSSView(oModel, oViewProperties);
        break;

      case EntityTasksFormTypeConstants.BOOLEAN:
        oViewToReturn = this.getGridYesNoPropertyView(oModel, oViewProperties);
        break;

      case EntityTasksFormTypeConstants.DATE_FIELD:
        oViewToReturn = this.getDateView(oModel, oViewProperties);
        break;
    }

    sModelType != EntityTasksFormTypeConstants.BOOLEAN && (sClassName += "showBorder");

    return (<div className={sClassName}>{oViewToReturn}</div>)
  };


  getView = () => {
    let aViews = [];
    let aModels = this.props.models;

    CS.forEach(aModels, (oModel) => {
      let oValueView = this.getValueViewByType(oModel);
      let oLabelView = (<TooltipView placement="bottom" label={CS.getLabelOrCode(oModel)}><div className={"entityTasksFormLabel"}>{CS.getLabelOrCode(oModel) + " : "}</div></TooltipView>);
      aViews.push(<div className={"entityTasksFormRow"}>{oLabelView}{oValueView}</div>);
    });

    return aViews;
  };


  render () {
    return (<div className={"entityTasksFormView"}>{this.getView()}</div>)
  }
}

export const view = ContentTasksFormView;
export const events = oEvents;
