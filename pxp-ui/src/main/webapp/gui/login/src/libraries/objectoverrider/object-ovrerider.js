// TODO: #Refact20

/*
/!**
 * Created by CS56 on 9/29/2016.
 *!/
import CS from '../cs';

Object.defineProperty (Object.prototype, 'inheritsFrom', {
      value : function (oBaseClass) {
        var oConstructor = this.constructor;
        Object.setPrototypeOf (this, new oBaseClass.constructor());
        this.constructor = oConstructor;

        if(this._init && (typeof this._init === 'function')) {
          this._init.call(this);
        }
      },
      enumerable : false,
      writable : false
    }
);

Object.defineProperty (Object.prototype, 'resetDataProperties', {
      value : function (oObj) {
        var fConstructor = oObj.constructor;
        var oNewObj = new fConstructor();
        var oPrototype = Object.getPrototypeOf(oNewObj);
        CS.assign(oObj, oNewObj);
        Object.setPrototypeOf(oObj, oPrototype);
        oObj.constructor = fConstructor;
      },
      enumerable : false,
      writable : true
    }
);
*/
