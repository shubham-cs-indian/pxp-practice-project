import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';

const oEvents = {
  SELECTION_TOGGLE_BUTTON_CLICKED: 'selection_toggle_button_clicked'
};
const oPropTypes = {
  disabledItems: ReactPropTypes.array,
  items: ReactPropTypes.array,
  selectedItems: ReactPropTypes.array,
  context: ReactPropTypes.string,
  contextKey: ReactPropTypes.string,
  singleSelect: ReactPropTypes.bool,
};
/**
 * @class SelectionToggleView - use for select toggle view.
 * @memberOf Views
 * @property {array} [disabledItems] -  array for selection toggle disabledItems.
 * @property {array} [items] -  array for selection toggle item.
 * @property {array} [selectedItems] -  array for selected item.
 * @property {string} [context] -  string of context.
 * @property {string} [contextKey] -  string of contextKey.
 * @property {bool} [singleSelect] -  boolean which is used for singleSelect or multi.
 */

// @CS.SafeComponent
class SelectionToggleView extends React.Component{

  constructor(props) {
    super(props);
  }

  componentDidMount =()=> {
  }

  componentDidUpdate =()=> {
  }

  handleSelectableElementClicked =(sId)=> {
    if (this.props.onSelectHandler) {
      this.props.onSelectHandler(sId);
      return;
    }
    EventBus.dispatch(oEvents.SELECTION_TOGGLE_BUTTON_CLICKED, this.props.context, this.props.contextKey, sId, this.props.singleSelect);
  }

  render() {
    var _this = this;
    var __props = _this.props;
    var aItems = __props.items;
    var aSelectedIds = __props.selectedItems;
    var aDisabledIds = __props.disabledItems;
    var aSelectableElements = [];

    CS.forEach(aItems, function (oElement) {
      var sClassName = "tagElement ";

      if (CS.includes(aSelectedIds, oElement.id)) {
        sClassName += "tagElementSelected ";
      }
      var sId = oElement.id;

      var fOnClick = _this.handleSelectableElementClicked.bind(_this, sId);
      if(CS.includes(aDisabledIds, sId)) {
        fOnClick = CS.noop;
      }

      aSelectableElements.push(
          <div className={sClassName} title={CS.getLabelOrCode(oElement)} key={sId}
               onClick={fOnClick}>
            {CS.getLabelOrCode(oElement)}
          </div>);
    });

    return (
        <div className="selectionToggleView">
          <div className="tagListBody">
            {aSelectableElements}
          </div>
        </div>
    );
  }

}

SelectionToggleView.propTypes = oPropTypes;

export const view = SelectionToggleView;
export const events = oEvents;
