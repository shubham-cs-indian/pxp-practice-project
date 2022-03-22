import CS from '../../libraries/cs';

var ColorUtils = (function () {

  var _hexToRGBConversion = function (sHexValue) {
    const sShorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;
    sHexValue = sHexValue.replace(sShorthandRegex, (m, r, g, b) => {
      return r + r + g + g + b + b;
    });

    const sResult = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(sHexValue);
    return CS.isNotEmpty(sResult) ? [
          parseInt(sResult[1], 16),
          parseInt(sResult[2], 16),
          parseInt(sResult[3], 16),
        ] : {};
  };

  var _shadeBlendConvert = function (iPercentage, sFromColor, sToColor, bUseLinear) {
    let r, g, b, P, f, t, h, i = parseInt, m = Math.round, a = typeof(sToColor) == "string";
    if (typeof(iPercentage) != "number" || iPercentage < -1 || iPercentage > 1 || typeof(sFromColor) != "string" || (sFromColor[0] != 'r' && sFromColor[0] != '#') || (sToColor && !a))return null;
    function pSBCr (d) {
      let n = d.length, x = {};
      if (n > 9) {
        [r, g, b, a] = d = d.split(","), n = d.length; // eslint-disable-line
        if (n < 3 || n > 4)return null;
        x.r = i(r[3] == "a" ? r.slice(5) : r.slice(4)), x.g = i(g), x.b = i(b), x.a = a ? parseFloat(a) : -1 // eslint-disable-line
      } else {
        if (n == 8 || n == 6 || n < 4)return null;
        if (n < 6) d = "#" + d[1] + d[1] + d[2] + d[2] + d[3] + d[3] + (n > 4 ? d[4] + d[4] : "");
        d = i(d.slice(1), 16);
        if (n == 9 || n == 5) x.r = d >> 24 & 255, x.g = d >> 16 & 255, x.b = d >> 8 & 255, x.a = m((d & 255) / 0.255) / 1000; // eslint-disable-line
        else x.r = d >> 16, x.g = d >> 8 & 255, x.b = d & 255, x.a = -1 // eslint-disable-line
      }
      return x
    };
    let oTrueObj = {r: 0, g: 0, b: 0, a: -1};
    let oFalseObj = {r: 255, g: 255, b: 255, a: -1};
    h = sFromColor.length > 9, h = a ? sToColor.length > 9 ? true : sToColor == "c" ? !h : false : h, f = pSBCr(sFromColor), P = iPercentage < 0, t = sToColor && sToColor != "c" ? pSBCr(sToColor) : P ? oTrueObj : oFalseObj, iPercentage = P ? iPercentage * -1 : iPercentage, P = 1 - iPercentage; // eslint-disable-line
    if (!f || !t)return null;
    if (bUseLinear) r = m(P * f.r + iPercentage * t.r), g = m(P * f.g + iPercentage * t.g), b = m(P * f.b + iPercentage * t.b); // eslint-disable-line
    else r = m((P * f.r ** 2 + iPercentage * t.r ** 2) ** 0.5), g = m((P * f.g ** 2 + iPercentage * t.g ** 2) ** 0.5), b = m((P * f.b ** 2 + iPercentage * t.b ** 2) ** 0.5); // eslint-disable-line
    a = f.a, t = t.a, f = a >= 0 || t >= 0, a = f ? a < 0 ? t : t < 0 ? a : a * P + t * iPercentage : 0; // eslint-disable-line
    if (h)return "rgb" + (f ? "a(" : "(") + r + "," + g + "," + b + (f ? "," + m(a * 1000) / 1000 : "") + ")";
    else return "#" + (4294967296 + r * 16777216 + g * 65536 + b * 256 + (f ? m(a * 255) : 0)).toString(16).slice(1, f ? undefined : -2)
  };

  /************************************* Public API's **********************************************/
  return {

    hexToRGBConversion: function (sHexValue) {
      return _hexToRGBConversion(sHexValue);
    },

    shadeBlendConvert: function (iPercentage, sFromColor, sToColor, bUseLinear) {
      return _shadeBlendConvert(iPercentage, sFromColor, sToColor, bUseLinear);
    },
  };

})();

export default ColorUtils;
