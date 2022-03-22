import { ContextList } from '../contexts/ajax-method-context-creator';
import React, {Component} from 'react';

export default function (View)  {

  if(!View) {
    throw new Error("UNDEFINED_VIEW_PASSED");
  }

  return class PostMethodContext extends Component {
    render () {
      return <ContextList.Consumer>
        {
          (ajaxMethods) => (<View {...this.props} {...ajaxMethods}/>)
        }
      </ContextList.Consumer>;
    }
  }
}
