steal("can/util","can/observe","can/util/string/deparam",function(b){var t=/\:([\w\.]+)/g,y=function(a){var c=[];b.each(a,function(a,e){c.push(("className"===e?"class":e)+'="'+("href"===e?a:b.esc(a))+'"')});return c.join(" ")},u=function(a,b){var d=0,e=0,f={},g;for(g in a.defaults)a.defaults[g]===b[g]&&(f[g]=1,d++);for(;e<a.names.length;e++){if(!b.hasOwnProperty(a.names[e]))return-1;f[a.names[e]]||d++}return d},h=!0,p=window.location,k=b.each,l=b.extend;b.route=function(a,c){c=c||{};var d=[],e=a.replace(t,
function(e,g,m){d.push(g);return"([^\\"+(a.substr(m+e.length,1)||b.route._querySeparator)+"]"+(c[g]?"*":"+")+")"});b.route.routes[a]={test:new RegExp("^"+e+"($|"+(b.route._querySeparator+"").replace(/([.?*+\^$\[\]\\(){}|\-])/g,"\\$1")+")"),route:a,names:d,defaults:c,length:a.split("/").length};return b.route};l(b.route,{_querySeparator:"&",_paramsMatcher:/^(?:&[^=]+=[^&]*)+/,param:function(a,c){var d,e=0,f,g=a.route,m=0;delete a.route;k(a,function(){m++});k(b.route.routes,function(b,c){f=u(b,a);f>
e&&(d=b,e=f);if(f>=m)return!1});b.route.routes[g]&&u(b.route.routes[g],a)===e&&(d=b.route.routes[g]);if(d){var n=l({},a),g=d.route.replace(t,function(b,c){delete n[c];return a[c]===d.defaults[c]?"":encodeURIComponent(a[c])}),h;k(d.defaults,function(b,a){n[a]===b&&delete n[a]});h=b.param(n);c&&b.route.attr("route",d.route);return g+(h?b.route._querySeparator+h:"")}return b.isEmptyObject(a)?"":b.route._querySeparator+b.param(a)},deparam:function(a){var c={length:-1};k(b.route.routes,function(b,d){b.test.test(a)&&
b.length>c.length&&(c=b)});if(-1<c.length){var d=a.match(c.test),e=d.shift(),f=(e=a.substr(e.length-(d[d.length-1]===b.route._querySeparator?1:0)))&&b.route._paramsMatcher.test(e)?b.deparam(e.slice(1)):{},f=l(!0,{},c.defaults,f);k(d,function(a,d){a&&a!==b.route._querySeparator&&(f[c.names[d]]=decodeURIComponent(a))});f.route=c.route;return f}a.charAt(0)!==b.route._querySeparator&&(a=b.route._querySeparator+a);return b.route._paramsMatcher.test(a)?b.deparam(a.slice(1)):{}},data:new b.Observe({}),routes:{},
ready:function(a){!1===a&&(h=a);if(!0===a||!0===h)b.route._setup(),v();return b.route},url:function(a,c){c&&(a=l({},q,a));return"#!"+b.route.param(a)},link:function(a,c,d,e){return"<a "+y(l({href:b.route.url(c,e)},d))+">"+a+"</a>"},current:function(a){return p.hash=="#!"+b.route.param(a)},_setup:function(){b.bind.call(window,"hashchange",v)},_getHash:function(){return p.href.split(/#!?/)[1]||""},_setHash:function(a){a=b.route.param(a,!0);p.hash="#!"+a;return a}});k("bind unbind delegate undelegate attr removeAttr".split(" "),
function(a){b.route[a]=function(){if(b.route.data[a])return b.route.data[a].apply(b.route.data,arguments)}});var w,q,v=b.route.setState=function(){var a=b.route._getHash();q=b.route.deparam(a);r&&a===x||b.route.attr(q,!0)},x,r;b.route.bind("change",function(a,c){r=1;clearTimeout(w);w=setTimeout(function(){r=0;var a=b.route.data.serialize();x=b.route._setHash(a)},1)});b.bind.call(document,"ready",b.route.ready);"complete"!==document.readyState&&"interactive"!==document.readyState||!h||b.route.ready();
b.route.constructor.canMakeObserve=b.Observe.canMakeObserve;return b.route});