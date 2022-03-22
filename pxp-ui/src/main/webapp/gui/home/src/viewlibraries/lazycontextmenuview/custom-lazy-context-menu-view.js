import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import CoreLazyContextMenuView from './core-lazy-context-menu-view';

const oPropTypes = CoreLazyContextMenuView.propTypes;
oPropTypes.customObject = ReactPropTypes.object;

const oEvents = CoreLazyContextMenuView.events;

class CustomLazyContextMenuView extends CoreLazyContextMenuView.view {

  constructor (props) {
    super(props);
  }

  getItemLabel = (oItem) => {

    let oCustomObj = this.props.customObject;
    if(oCustomObj) {
      let oCustomItem = oCustomObj[oItem.id];
      if(oCustomItem) {
        return CS.getLabelOrCode(oCustomItem);
      }
    }
    return CS.getLabelOrCode(oItem);
  };

}
CustomLazyContextMenuView.propTypes = oPropTypes;

export const view = CustomLazyContextMenuView;
export const events = oEvents;