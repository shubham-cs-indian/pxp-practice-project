import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import TooltipView from './../../viewlibraries/tooltipview/tooltip-view';
import { view as ContextMenuViewNew } from './../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import CONSTANTS from '../../commonmodule/tack/constants';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oEvents = {
  BUTTON_VIEW_BUTTON_CLICKED: "button_view_button_clicked",
};

const oPropTypes = {

  className: ReactPropTypes.string,
  id: ReactPropTypes.string,
  label: ReactPropTypes.string,
  tooltip: ReactPropTypes.string,
  onClickHandler: ReactPropTypes.func,
  filterContext: ReactPropTypes.object,

  //---------- State ----------
  isDisabled: ReactPropTypes.bool,
  isActive: ReactPropTypes.bool,

  //---------- Support --------
  theme: ReactPropTypes.string,
  type: ReactPropTypes.string,

  //---------- Functions ------
  clickHandler: ReactPropTypes.func,
  applyHandler: ReactPropTypes.func,
  onChange: ReactPropTypes.func
};
/**
 * @class ButtonView - use for display button view.
 * @memberOf Views
 * @property {string} [className] -  className of buttons.
 * @property {string} [id] -  id of buttons.
 * @property {string} [label] -  label of buttons.
 * @property {string} [tooltip] - this tooltip for buttons.
 * @property {func} [onClickHandler] -  function which is used when onClickHandler event use.
 * @property {bool} [isDisabled] -  boolean value for button enable or disable.
 * @property {bool} [isActive] -  boolean value for button active or inactive.
 * @property {bool} [isHidden] -  boolean value for button hidden or visible.
 * @property {string} [theme] - this string for theme for button like light and dark.
 * @property {string} [type] - this string for type.
 * @property {number} [selectedCount] -  number
 * @property {func} [clickHandler] -  function which is used when clickHandler.
 * @property {func} [applyHandler] -  function which is used when applyHandler.
 * @property {func} [onChange] -  function which is used when button changed.
 */

// @CS.SafeComponent
class ButtonView extends React.Component {

  constructor (props) {
    super(props);
  };

  handleToolbarButtonClicked = (sButtonId, sButtonType, bIsActive, oViewContext, oFilterContext) => {
    let fOnChange = this.props.onChange;
    if (CS.isFunction(fOnChange)) {
      fOnChange(sButtonId);
    } else {
      EventBus.dispatch(oEvents.BUTTON_VIEW_BUTTON_CLICKED, sButtonId, sButtonType, bIsActive, oViewContext, oFilterContext);
    }
  };

  getSimpleButton = (oProps, sClassesTextSpan) => {
    let oSimpleButton = <span className={sClassesTextSpan}>
      {oProps.label}
    </span>;

    return oSimpleButton;
  };

  getMenuButton = (oProps, sClassesTextSpan) => {
    let oMenuButton = <div className={sClassesTextSpan + " newToolbarMenuButtonContainer"}>
      <ContextMenuViewNew
          contextMenuViewModel={oProps.contextMenuViewModel}
          isMultiselect={oProps.isMultiSelect}
          onClickHandler={oProps.clickHandler}
          onApplyHandler={oProps.applyHandler}
          loadMoreHandler={oProps.loadMoreHandler}

          searchHandler={oProps.searchHandler}
          searchText={oProps.searchText}
          style={oProps.style}
          showColor={oProps.showColor}
          showCustomIcon={oProps.showCustomIcon}
          isMovable={oProps.isMovable}
          menuListHeight={oProps.menuListHeight}
          showSelectedItems={oProps.showSelectedItems}
          selectedItems={oProps.selectedItems}
          onItemsChecked={oProps.onItemsChecked}
          showSearch={oProps.showSearch}
      >
        {oProps.label}
        <div className="dropDownTriggerButton"></div>
      </ContextMenuViewNew>
    </div>;

    return oMenuButton;
  };

  getButtonType = (oProps) =>{
    let oTextSpan;
    let sClassesTextSpan = (oProps.className) ? ("containsIcon textContent " + oProps.theme) : "textContent " + oProps.theme;

    switch (oProps.type) {
      default:
      case CONSTANTS.BUTTON_TYPE_SIMPLE:
        oTextSpan = this.getSimpleButton(oProps, sClassesTextSpan);
        break;

      case CONSTANTS.BUTTON_TYPE_MENU:
        oTextSpan = this.getMenuButton(oProps, sClassesTextSpan);
        break;
    };

    return oTextSpan;
  };

  renderToolbarButton = () =>{
    let oProps = this.props;
    let oButtonSpan = null;
    let oTextSpan = null;
    let oSelectedItemsCount = null;
    let sClassName = "toolbarItemNew ";
    if (oProps.theme) {
      sClassName += oProps.theme + " ";
    }

    if (oProps.className) {
      sClassName += oProps.className;
    }

    if (oProps.isViewSelected) {
      sClassName += " selected ";
    }


    if (oProps.className) {
      oButtonSpan = <div className={sClassName}>
      </div>
    }

    if(oProps.label){
      if(!oProps.className || (oProps.className && oProps.showLabel === true)){
        oTextSpan = this.getButtonType(oProps);                     // if no class name, means no icon is to be displayed.
                                                                    // In this case show the label
      }else if(oProps.className && oProps.showLabel === true){
        /* if class name is there means icon is to be displayed, then check the conditions from props, if the label is to be
           shown or not.
        */
        oTextSpan = this.getButtonType(oProps);
      }else{
        oTextSpan = null;
      }
    }

    if (oProps.selectedItemsCount) {
      oSelectedItemsCount = <div className="selectedItemCount">
        {oProps.selectedItemsCount}
      </div>;
    }

    let oView = null;

    let fOnClickHandler = this.handleToolbarButtonClicked.bind(this, oProps.id, oProps.type, oProps.isActive, oProps.viewContext, oProps.filterContext);
    if(oProps.view) {
      oView = oProps.view;
    } else {
      let sClassName = "customButton ";
      if (oProps.isActive) {
        sClassName += "active "
      }
      if (oProps.isDisabled) {
        sClassName += "disabled "
        fOnClickHandler = CS.noop;
      }

      oView = (
          <TooltipView placement={oProps.placement || "top"} label={oProps.tooltip || getTranslation()[oProps.label]}>
            <div className={sClassName}
                 onClick={fOnClickHandler}>
              <div className={"iconTextWrapper"}>
                {oButtonSpan} {/* button may contain - icon - for displaying the icon*/}
                {oTextSpan} {/* text caption for the button*/}
              </div>
              {oSelectedItemsCount} {/* notifications count, as in how many items are selected*/}
            </div>
          </TooltipView>
      );
    }

    let oButton =
      <div className={'customButtonContainer'}>
        {oView}
      </div>;
    return oButton;
  };

  render () {
    return this.renderToolbarButton();
  }
}

ButtonView.defaultProps = {
  className: '',
  label: '',
  placement:'',
  showLabel: false,
  tooltip: '',
  theme: 'light',
  type: 'simple',
  isMultiSelect: false,
  selectedItemsCount: 0,
  style: '',
  isViewSelected: '',
  isDisabled: false
};

ButtonView.propTypes = oPropTypes;

export const view = ButtonView;
export const events = oEvents;
