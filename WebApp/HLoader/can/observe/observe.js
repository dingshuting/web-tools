steal("can/util","can/util/bind","can/construct",function(d,y){var h=function(a){return a&&!d.isDeferred(a)&&(d.isArray(a)||d.isPlainObject(a)||a instanceof d.Observe)},l=function(a,b){return d.each(a,function(a){a&&a.unbind&&a.unbind("change"+b)})},p=function(a,b,c,e,g){e=e||f;g=g||f.List;a instanceof f?c._bindings&&l([a],c._cid):a=d.isArray(a)?new g(a):new e(a);c._bindings&&u(a,b,c);return a},u=function(a,b,c){a.bind("change"+c._cid,function(){var e=d.makeArray(arguments),g=e.shift();e[0]=("*"===
b?[c.indexOf(a),e[0]]:[b,e[0]]).join(".");g.triggeredNS=g.triggeredNS||{};g.triggeredNS[c._cid]||(g.triggeredNS[c._cid]=!0,d.trigger(c,g,e))})},m=function(a,b,c){a.each(function(a,g){c[g]=h(a)&&d.isFunction(a[b])?a[b]():a});return c},q=function(a,b){return b?[a]:d.isArray(a)?a:(""+a).split(".")},v=1,k=0,r=[],t=[],w=function(a){return function(){var b=this;this._each(function(c,e){c&&c.bind&&u(c,a||e,b)})}},f=d.Map=d.Observe=d.Construct({bind:d.bindAndSetup,unbind:d.unbindAndTeardown,id:"id",canMakeObserve:h,
startBatch:function(a){k++;a&&t.push(a)},stopBatch:function(a,b){a?k=0:k--;if(0==k){var c=r.slice(0),e=t.slice(0);r=[];t=[];v++;b&&this.startBatch();d.each(c,function(a){d.trigger.apply(d,a)});d.each(e,function(a){a()})}},triggerBatch:function(a,b,c){if(!a._init){if(0==k)return d.trigger(a,b,c);b="string"===typeof b?{type:b}:b;b.batchNum=v;r.push([a,b,c])}},keys:function(a){var b=[];f.__reading&&f.__reading(a,"__keys");for(var c in a._data)b.push(c);return b}},{setup:function(a){this._data={};d.cid(this,
".observe");this._init=1;this.attr(a);this.bind("change"+this._cid,d.proxy(this._changes,this));delete this._init},_bindsetup:w(),_bindteardown:function(){var a=this._cid;this._each(function(b){l([b],a)})},_changes:function(a,b,c,e,d){f.triggerBatch(this,{type:b,batchNum:a.batchNum},[e,d])},_triggerChange:function(a,b,c,e){f.triggerBatch(this,"change",d.makeArray(arguments))},_each:function(a){var b=this.__get(),c;for(c in b)b.hasOwnProperty(c)&&a(b[c],c)},attr:function(a,b){var c=typeof a;if("string"!==
c&&"number"!==c)return this._attrs(a,b);if(1===arguments.length)return f.__reading&&f.__reading(this,a),this._get(a);this._set(a,b);return this},each:function(){f.__reading&&f.__reading(this,"__keys");return d.each.apply(void 0,[this.__get()].concat(d.makeArray(arguments)))},removeAttr:function(a){var b=this instanceof d.Observe.List;a=q(a);var c=a.shift(),e=b?this[c]:this._data[c];if(a.length)return e.removeAttr(a);b?this.splice(c,1):c in this._data&&(delete this._data[c],c in this.constructor.prototype||
delete this[c],f.triggerBatch(this,"__keys"),this._triggerChange(c,"remove",void 0,e));return e},_get:function(a){var b="string"===typeof a&&!!~a.indexOf(".")&&this.__get(a);if(b)return b;a=q(a);b=this.__get(a.shift());return a.length?b?b._get(a):void 0:b},__get:function(a){return a?this._data[a]:this._data},_set:function(a,b,c){a=q(a,c);c=a.shift();var e=this.__get(c);if(h(e)&&a.length)e._set(a,b);else{if(a.length)throw"can.Observe: Object does not exist";this.__convert&&(b=this.__convert(c,b));
this.__set(c,b,e)}},__set:function(a,b,c){if(b!==c){var e=this.__get().hasOwnProperty(a)?"set":"add";this.___set(a,h(b)?p(b,a,this):b);"add"==e&&f.triggerBatch(this,"__keys",void 0);this._triggerChange(a,e,b,c);c&&l([c],this._cid)}},___set:function(a,b){this._data[a]=b;a in this.constructor.prototype||(this[a]=b)},bind:d.bindAndSetup,unbind:d.unbindAndTeardown,serialize:function(){return m(this,"serialize",{})},_attrs:function(a,b){if(void 0===a)return m(this,"attr",{});a=d.extend({},a);var c,e=this,
g;f.startBatch();this.each(function(c,f){g=a[f];void 0===g?b&&e.removeAttr(f):(e.__convert&&(g=e.__convert(f,g)),g instanceof d.Observe?e.__set(f,g,c):h(c)&&h(g)&&c.attr?c.attr(g,b):c!=g&&e.__set(f,g,c),delete a[f])});for(c in a)g=a[c],this._set(c,g,!0);f.stopBatch();return this},compute:function(a){return d.compute(this,a)}}),x=[].splice,n=f({setup:function(a,b){this.length=0;d.cid(this,".observe");this._init=1;d.isDeferred(a)?this.replace(a):this.push.apply(this,d.makeArray(a||[]));this.bind("change"+
this._cid,d.proxy(this._changes,this));d.extend(this,b);delete this._init},_triggerChange:function(a,b,c,e){f.prototype._triggerChange.apply(this,arguments);~a.indexOf(".")||("add"===b?(f.triggerBatch(this,b,[c,+a]),f.triggerBatch(this,"length",[this.length])):"remove"===b?(f.triggerBatch(this,b,[e,+a]),f.triggerBatch(this,"length",[this.length])):f.triggerBatch(this,b,[c,+a]))},__get:function(a){return a?this[a]:this},___set:function(a,b){this[a]=b;+a>=this.length&&(this.length=+a+1)},_each:function(a){for(var b=
this.__get(),c=0;c<b.length;c++)a(b[c],c)},_bindsetup:w("*"),serialize:function(){return m(this,"serialize",[])},splice:function(a,b){var c=d.makeArray(arguments),e;for(e=2;e<c.length;e++){var f=c[e];h(f)&&(c[e]=p(f,"*",this,this.constructor.Observe,this.constructor))}void 0===b&&(b=c[1]=this.length-a);e=x.apply(this,c);d.Observe.startBatch();0<b&&(this._triggerChange(""+a,"remove",void 0,e),l(e,this._cid));2<c.length&&this._triggerChange(""+a,"add",c.slice(2),e);d.Observe.stopBatch();return e},_attrs:function(a,
b){if(void 0===a)return m(this,"attr",[]);a=d.makeArray(a);f.startBatch();this._updateAttrs(a,b);f.stopBatch()},_updateAttrs:function(a,b){for(var c=Math.min(a.length,this.length),e=0;e<c;e++){var d=this[e],f=a[e];h(d)&&h(f)?d.attr(f,b):d!=f&&this._set(e,f)}a.length>this.length?this.push.apply(this,a.slice(this.length)):a.length<this.length&&b&&this.splice(a.length)}});d.each({push:"length",unshift:0},function(a,b){var c=[][b];n.prototype[b]=function(){for(var b=[],d=a?this.length:0,f=arguments.length,
k;f--;)k=arguments[f],b[f]=h(k)?p(k,"*",this,this.constructor.Observe,this.constructor):k;f=c.apply(this,b);this.comparator&&!b.length||this._triggerChange(""+d,"add",b,void 0);return f}});d.each({pop:"length",shift:0},function(a,b){n.prototype[b]=function(){var c,e=arguments;c=e[0]&&d.isArray(e[0])?e[0]:d.makeArray(e);e=a&&this.length?this.length-1:0;c=[][b].apply(this,c);this._triggerChange(""+e,"remove",void 0,[c]);c&&c.unbind&&c.unbind("change"+this._cid);return c}});d.extend(n.prototype,{indexOf:function(a){this.attr("length");
return d.inArray(a,this)},join:[].join,reverse:[].reverse,slice:function(){var a=Array.prototype.slice.apply(this,arguments);return new this.constructor(a)},concat:function(){var a=[];d.each(d.makeArray(arguments),function(b,c){a[c]=b instanceof d.Observe.List?b.serialize():b});return new this.constructor(Array.prototype.concat.apply(this.serialize(),a))},forEach:function(a,b){d.each(this,a,b||this)},replace:function(a){d.isDeferred(a)?a.then(d.proxy(this.replace,this)):this.splice.apply(this,[0,
this.length].concat(d.makeArray(a||[])));return this}});d.List=f.List=n;f.setup=function(){d.Construct.setup.apply(this,arguments);this.List=f.List({Observe:this},{})};return f});