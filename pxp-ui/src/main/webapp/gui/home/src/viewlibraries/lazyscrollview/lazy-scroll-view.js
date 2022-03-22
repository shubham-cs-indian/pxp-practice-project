import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

const Events = {
};

const oPropTypes = {
  scrollBottomOffset: ReactPropTypes.number.isRequired
};
/**
 * @class LazyScrollView
 * @memberOf Views
 * @property {number} scrollBottomOffset - Deprecated.
 */

// @CS.SafeComponent
class LazyScrollView extends React.Component {

  constructor(props) {
    super(props);
  }

  /*handleOnScroll =()=>{
        var lazyScrollDom = this.refs.lazyScrollView;
        var scrollTop = lazyScrollDom.scrollTop;
        var scrollHeight = lazyScrollDom.scrollHeight;
        var lazyScrollDomHeight = lazyScrollDom.getBoundingClientRect().height;
        var currentBottomOffset = scrollHeight - scrollTop - lazyScrollDomHeight;
        if(currentBottomOffset < this.props.scrollBottomOffset){
            if(this.props.onInfiniteLoad) {
                this.props.onInfiniteLoad();
            }
        }
    }*/

  render() {
    return (
        <div className="lazyScrollView" ref="lazyScrollView">
          {/*<div className="lazyScrollView" ref="lazyScrollView" onScroll={CS.debounce(this.handleOnScroll, 200)}>*/}
          {this.props.children}
        </div>
    )

  }
}

LazyScrollView.propTypes = oPropTypes;

export const view = LazyScrollView;
export const events = Events;
