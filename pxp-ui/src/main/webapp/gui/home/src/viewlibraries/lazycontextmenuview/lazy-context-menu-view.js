import CoreLazyContextMenuView from './core-lazy-context-menu-view'
const oPropTypes = CoreLazyContextMenuView.propTypes; // eslint-disable-line
const oEvents = CoreLazyContextMenuView.events;// eslint-disable-line

/**
 * @class LazyContextMenuView - Use for Display Add section view in data model in setting screen.
 */
class LazyContextMenuView extends CoreLazyContextMenuView.view {

  constructor (props) {
    super(props);
  }

}

LazyContextMenuView.propTypes = oPropTypes;

export const view = LazyContextMenuView;
export const events = oEvents;
export const propTypes = oPropTypes;
