import { ContextList } from '../contexts/context-creator';
import React, {Component} from 'react';

export default function (View)  {

  if(!View) {
    throw new Error("UNDEFINED_VIEW_PASSED");
  }

  return class UserMasterListContext extends Component {
    render () {
      return <ContextList.Consumer>
        {({masterUserList}) => (<View {...this.props} masterUserList = {masterUserList}/>
        )}
      </ContextList.Consumer>;
    }
  }
}
