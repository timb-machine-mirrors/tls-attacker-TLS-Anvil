"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[321],{3905:function(e,t,r){r.d(t,{Zo:function(){return c},kt:function(){return m}});var n=r(7294);function o(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function a(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function i(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?a(Object(r),!0).forEach((function(t){o(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):a(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function l(e,t){if(null==e)return{};var r,n,o=function(e,t){if(null==e)return{};var r,n,o={},a=Object.keys(e);for(n=0;n<a.length;n++)r=a[n],t.indexOf(r)>=0||(o[r]=e[r]);return o}(e,t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(n=0;n<a.length;n++)r=a[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(o[r]=e[r])}return o}var u=n.createContext({}),s=function(e){var t=n.useContext(u),r=t;return e&&(r="function"==typeof e?e(t):i(i({},t),e)),r},c=function(e){var t=s(e.components);return n.createElement(u.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},d=n.forwardRef((function(e,t){var r=e.components,o=e.mdxType,a=e.originalType,u=e.parentName,c=l(e,["components","mdxType","originalType","parentName"]),d=s(r),m=o,f=d["".concat(u,".").concat(m)]||d[m]||p[m]||a;return r?n.createElement(f,i(i({ref:t},c),{},{components:r})):n.createElement(f,i({ref:t},c))}));function m(e,t){var r=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var a=r.length,i=new Array(a);i[0]=d;var l={};for(var u in t)hasOwnProperty.call(t,u)&&(l[u]=t[u]);l.originalType=e,l.mdxType="string"==typeof e?e:o,i[1]=l;for(var s=2;s<a;s++)i[s]=r[s];return n.createElement.apply(null,i)}return n.createElement.apply(null,r)}d.displayName="MDXCreateElement"},874:function(e,t,r){r.r(t),r.d(t,{assets:function(){return c},contentTitle:function(){return u},default:function(){return m},frontMatter:function(){return l},metadata:function(){return s},toc:function(){return p}});var n=r(7462),o=r(3366),a=(r(7294),r(3905)),i=["components"],l={slug:"hello-world",title:"Hello World \ud83d\ude80 \ud83c\udf89",authors:["mmaehren","pnieting"]},u=void 0,s={permalink:"/blog/hello-world",editUrl:"https://github.com/tls-attacker/TLS-Anvil/tree/main/Docs/blog/22-06-2022-hello-world.md",source:"@site/blog/22-06-2022-hello-world.md",title:"Hello World \ud83d\ude80 \ud83c\udf89",description:"Finally we are able to share our latest work with you. TLS-Anvil, an automated test suite for TLS 1.2/1.3 servers and clients. Our paper describing TLS-Anvil and discussing results we could obtain by testing open source TLS implementations is published as part of USENIX 2022.",date:"2022-07-20T15:35:56.000Z",formattedDate:"July 20, 2022",tags:[],readingTime:.54,truncated:!1,authors:[{name:"Marcel Maehren",title:"TLS-Anvil Author",url:"https://github.com/mmaehren",imageURL:"https://github.com/mmaehren.png",key:"mmaehren"},{name:"Philipp Nieting",title:"TLS-Anvil Co-Author",url:"https://github.com/kavakuo",imageURL:"https://github.com/kavakuo.png",key:"pnieting"}],frontMatter:{slug:"hello-world",title:"Hello World \ud83d\ude80 \ud83c\udf89",authors:["mmaehren","pnieting"]}},c={authorsImageUrls:[void 0,void 0]},p=[],d={toc:p};function m(e){var t=e.components,r=(0,o.Z)(e,i);return(0,a.kt)("wrapper",(0,n.Z)({},d,r,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("p",null,"Finally we are able to share our latest work with you. TLS-Anvil, an automated test suite for TLS 1.2/1.3 servers and clients. Our paper describing TLS-Anvil and discussing results we could obtain by testing open source TLS implementations is published as part of USENIX 2022."),(0,a.kt)("p",null,"TLS-Anvil was designed with the goal in mind to provide an easy to use and extendible testing solution for TLS. We use combinatorial testing to be able discover hard observable RFC compliance and security issues, that only occur when certain parameters are negotiated."),(0,a.kt)("p",null,"Have a look at our ",(0,a.kt)("a",{parentName:"p",href:"/publications"},"publications")," and ",(0,a.kt)("a",{parentName:"p",href:"/docs/Quick-Start/index"},"docs")," if you are interested to learn how TLS-Anvil works under the hood."))}m.isMDXComponent=!0}}]);