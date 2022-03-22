import { ContextList } from '../contexts/context-creator';
import React, {Component} from 'react';

export default function (View)  {

  if(!View) {
    throw new Error("UNDEFINED_VIEW_PASSED");
  }

  return class ZoomContext extends Component {
    render () {
      return <ContextList.Consumer>
        {({currentZoomValue}) => (<View {...this.props} currentZoomValue = {currentZoomValue}/>
        )}
      </ContextList.Consumer>;
    }
  }
}
