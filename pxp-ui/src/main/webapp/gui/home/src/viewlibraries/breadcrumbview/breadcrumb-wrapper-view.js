import CS from '../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import BreadcrumbView from './breadcrumb-view';

const oEvents = {
  BREADCRUMB_ITEM_CLICKED: "breadcrumb_item_clicked"
};

const oPropTypes = {
  breadcrumbPath: ReactPropTypes.array,
  context: ReactPropTypes.string,
  clickHandler: ReactPropTypes.func,
  isDisabled: ReactPropTypes.bool,
  toolbarView: ReactPropTypes.array,
};
/**
 * @class BreadcrumbWrapperView - use to display breadcrumb for application.
 * @memberOf Views
 * @property {array} [breadcrumbPath] - this props n array which contain helpScreenId, Id, label and type of screen.
 * @property {string} [context] -  a context.
 * @property {func} [clickHandler] -  as a function which used when click on breadcrumb item click.
 * @property {bool} [isDisabled] -  boolean value for isDisabled or not.
 * @property {object} [toolbarView] -  a toolbarView.
 */

// @CS.SafeComponent
class BreadcrumbWrapperView extends React.Component {
  constructor(props) {
    super(props);
  }

  handleBreadcrumbItemClicked = (oItem, bIsLast) => {
    let fFunctionToExecute = this.props.clickHandler;
    let sContext = (this.props.context) ? (this.props.context) : "";

    if (!bIsLast && !this.props.isDisabled && !oItem.isDisabled) {
      if(CS.isFunction(fFunctionToExecute)){
        fFunctionToExecute(oItem);
      } else {
        EventBus.dispatch(oEvents.BREADCRUMB_ITEM_CLICKED, this, oItem, sContext);
      }
    }
  };

  getBreadcrumbItems = () => {
    let _this = this;
    let __props = _this.props;

    return <BreadcrumbView items={__props.breadcrumbPath} onClick={_this.handleBreadcrumbItemClicked}/>
  };

  render () {
    let oToolbarDOM = this.props.toolbarView ? (
        <div className="upperSectionToolbarContainer">
          {this.props.toolbarView}
        </div>) : null;
    return (
        <div className="breadcrumbViewContainer">
          <div className="breadCrumbView">
            {this.getBreadcrumbItems()}
          </div>
          {oToolbarDOM}
        </div>
    );
  }
}

BreadcrumbWrapperView.propTypes = oPropTypes;

export const view = BreadcrumbWrapperView;
export const events = oEvents;
