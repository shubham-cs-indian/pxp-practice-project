import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import TooltipView from './../../viewlibraries/tooltipview/tooltip-view';


const oEvents = {
  HANDLE_HEADER_MENU_CLICKED: "handle_header_menu_clicked"
};

const oPropTypes = {
  oMenuItem: ReactPropTypes.object,
};

/** Need to make it as a component. Probably need to shift it to a library **/
function ItemCount ({count = 0, className = ""}) {
  return count ? <div className={`selectCount ${className}`}>{count}</div> : null;
}


/** Functional Component **/
/**
 * @class HeaderMenuItemView - Use to Display Menus in Header of Application.
 * @memberOf Views
 * @property {object} [oMenuItem] - Pass Object contain containerClassName, id, label, menuClassName.
 */


function HeaderMenuItemView ({oMenuItem}) {

  const {id: sMenuId, menuClassName, containerClassName: sContainerClassName, count} = oMenuItem;
  const sLabel = CS.getLabelOrCode(oMenuItem);
  const sClassName = `menuItem  ${menuClassName}`;

  const handleMenuItemClicked = function () {
    EventBus.dispatch(oEvents.HANDLE_HEADER_MENU_CLICKED, sLabel, sMenuId);
  };

  return (
      <div className={sContainerClassName}>
        <TooltipView label={sLabel}>
          <div className="tooltipWrapper">
            <div className={sClassName} onClick={handleMenuItemClicked}/>
            <ItemCount count={+count}/>
          </div>
        </TooltipView>
      </div>
  );
}

HeaderMenuItemView.propTypes = oPropTypes;

export const view = CS.SafeComponent(HeaderMenuItemView);
export const events = oEvents;
