$("gs").min=1;$("gs").max=100;try{document.querySelector("header p").textContent="Medal of Honor: Allied Assault · v1.3 b6"}catch(e){}
/* render buttons (edit or test) */
var pressed={},tog={};
function render(c,edit){c.innerHTML="";var h=c.clientHeight||150,g=$("gs").value/100,o=$("op").value/100;
B.forEach(function(b){var e=document.createElement("div"),d=h*(b.ty=="joy"?.5:.26)*Math.min(100,Math.max(1,b.s))/100*g;
e.className="b"+(b.ty=="joy"?" joy":"")+(b.sh=="sq"&&b.ty!="joy"?" sq":"")+(edit&&sel==b.id?" sel":"");
e.style.cssText="left:"+b.x+"%;top:"+b.y+"%;width:"+d+"px;height:"+d+"px;opacity:"+o+";font-size:"+Math.max(6,d/5)+"px";
if(b.ty=="joy"){e.innerHTML='<div class="kn"></div>'}else e.textContent=b.l||b.k;
e.dataset.id=b.id;c.appendChild(e);
if(edit)drag(e,b,c);else play(e,b,c)})}
function drag(e,b,c){e.onpointerdown=function(ev){ev.stopPropagation();e.setPointerCapture(ev.pointerId);sel=b.id;c.querySelectorAll(".b").forEach(function(x){x.classList.toggle("sel",x===e)});panel();
e.onpointermove=function(m){var r=c.getBoundingClientRect();b.x=Math.round(Math.max(4,Math.min(96,(m.clientX-r.left)/r.width*100)));b.y=Math.round(Math.max(6,Math.min(94,(m.clientY-r.top)/r.height*100)));e.style.left=b.x+"%";e.style.top=b.y+"%"}};
e.onpointerup=function(){e.onpointermove=null;save(1);sum()}}
function keysUpdate(){var a=Object.keys(pressed).filter(function(k){return pressed[k]});$("ti").textContent=a.length?a.join(" + "):t("tinfo")}
function play(e,b,c){if(b.ty=="joy"){var ids=null;
 e.onpointerdown=function(ev){ev.stopPropagation();e.setPointerCapture(ev.pointerId);ids=ev.pointerId;vib(15);mv(ev)};
 var mv=function(ev){var r=e.getBoundingClientRect(),cx=r.left+r.width/2,cy=r.top+r.height/2,dx=(ev.clientX-cx)/(r.width/2),dy=(ev.clientY-cy)/(r.height/2),m=Math.hypot(dx,dy);if(m>1){dx/=m;dy/=m}
  e.firstChild.style.transform="translate("+dx*50+"%,"+dy*50+"%)";pressed.W=dy<-.35;pressed.S=dy>.35;pressed.A=dx<-.35;pressed.D=dx>.35;keysUpdate()};
 e.onpointermove=function(ev){if(ev.pointerId==ids)mv(ev)};
 e.onpointerup=e.onpointercancel=function(){ids=null;e.firstChild.style.transform="";pressed.W=pressed.A=pressed.S=pressed.D=false;keysUpdate()};return}
 var on=function(v){pressed[b.k]=v;e.classList.toggle("act",v);keysUpdate()};
 e.onpointerdown=function(ev){ev.stopPropagation();e.setPointerCapture(ev.pointerId);vib(20);if(b.m=="tog"){tog[b.id]=!tog[b.id];on(tog[b.id])}else on(true)};
 e.onpointerup=e.onpointercancel=function(){if(b.m!="tog")on(false)}}
/* panel */
function panel(){var p=$("pnl"),b=B.filter(function(x){return x.id==sel})[0];if(b)b.s=Math.min(100,Math.max(1,b.s));if(!b){p.innerHTML='<span style="color:var(--mu)">'+t("pick")+'</span>';return}
var ko=KEYS.map(function(k){return'<option'+(k==b.k?" selected":"")+'>'+k+'</option>'}).join("");if(b.ty=="joy")ko="<option>WASD</option>";
p.innerHTML=(b.ty=="joy"?"":'<div class="stack"><div class="top">'+t("lb")+'</div><input type="text" id="pl" maxlength="8" value="'+b.l+'"></div><div class="stack"><div class="top">'+t("ky")+'</div><select id="pk">'+ko+'</select></div>')+
'<div class="stack"><div class="top"><span>'+t("sz")+'</span><span class="val" id="psv">'+b.s+'%</span></div><input type="range" id="ps" min="1" max="100" value="'+b.s+'"></div>'+
(b.ty=="joy"?"":'<div class="stack"><div class="top">'+t("sh")+'</div><div class="seg" id="psh"><button data-v="c" class="'+(b.sh=="c"?"on":"")+'">'+t("ci")+'</button><button data-v="sq" class="'+(b.sh=="sq"?"on":"")+'">'+t("sq")+'</button></div></div><div class="stack"><div class="top">'+t("bh")+'</div><div class="seg" id="pm"><button data-v="hold" class="'+(b.m=="hold"?"on":"")+'">'+t("hold")+'</button><button data-v="tog" class="'+(b.m=="tog"?"on":"")+'">'+t("tog")+'</button></div></div><button class="btn2 red" id="pd" style="width:100%">'+t("del")+'</button>');
var rr=function(){render($("pad"),true);save(1);sum()};
if(b.ty!="joy"){$("pl").oninput=function(){b.l=this.value;rr()};$("pk").onchange=function(){b.k=this.value;if(!$("pl").value){b.l=""}rr()};
 $("psh").querySelectorAll("button").forEach(function(x){x.onclick=function(){b.sh=x.dataset.v;panel();rr()}});$("pm").querySelectorAll("button").forEach(function(x){x.onclick=function(){b.m=x.dataset.v;panel();save(1)}});
 $("pd").onclick=function(){B=B.filter(function(z){return z!==b});sel=null;panel();rr()}}
$("ps").oninput=function(){b.s=+this.value;$("psv").textContent=b.s+"%";render($("pad"),true)};$("ps").onchange=function(){rr()}}
$("add").onclick=function(){var id="c"+Date.now(),b={id:id,ty:"b",l:"Custom",k:"F",x:50,y:50,s:90,sh:"c",m:"hold"};B.push(b);sel=id;render($("pad"),true);panel();save(1);sum()};
$("pre").onclick=function(){pi=(pi+1)%PS.length;B=PS[pi][1]();sel=null;$("pre").textContent=t("pre")+": "+t(PS[pi][0]);render($("pad"),true);panel();save(1);sum()};
/* test mode */
var raf=0,fc=0,lt=0,lf=0,fpsNow=0;
function openTest(){$("test").classList.add("on");pressed={};tog={};try{document.documentElement.requestFullscreen&&$("full").checked&&document.documentElement.requestFullscreen();screen.orientation.lock("landscape")}catch(e){}
 setTimeout(function(){render($("tc"),false);keysUpdate()},120);$("test").style.filter="brightness("+(0.5+$("br").value/100)+")";fc=0;lf=performance.now();lt=0;
 var cap=S.fps?1000/S.fps:0,fo=document.createElement("div");fo.id="fo";fo.style.cssText="position:absolute;left:10px;bottom:8px;color:var(--ac);font-size:13px;pointer-events:none";$("test").appendChild(fo);
 (function loop(n){raf=requestAnimationFrame(loop);if(cap&&n-lt<cap-2)return;lt=n;fc++;if(n-lf>=1000){fpsNow=Math.round(fc*1000/(n-lf));fc=0;lf=n;fo.textContent=$("fpsc").checked?fpsNow+" FPS":""}})(performance.now())}
function closeTest(){cancelAnimationFrame(raf);$("test").classList.remove("on");var f=$("fo");f&&f.remove();try{document.fullscreenElement&&document.exitFullscreen();screen.orientation.unlock()}catch(e){}}
$("tst").onclick=openTest;$("tx").onclick=closeTest;
var cl=null;$("tc").onpointerdown=function(e){cl={x:e.clientX,y:e.clientY}};$("tc").onpointermove=function(e){if(cl){var d=Math.round(Math.hypot(e.clientX-cl.x,e.clientY-cl.y)*$("se").value/50);$("ti").textContent=t("cam")+" Δ "+d}};$("tc").onpointerup=function(){cl=null;keysUpdate()};
T.id.apply="Terapkan pengaturan ke game";T.en.apply="Apply settings to game";
T.id.nopkg="Mesin OpenMoHAA belum terpasang. Pasang dulu APK mesinnya.";T.en.nopkg="OpenMoHAA engine not installed. Install the engine APK first.";
T.id.nofold="Pilih folder game dulu";T.en.nofold="Pick the game folder first";
T.id.cfgok="autoexec.cfg berhasil ditulis";T.en.cfgok="autoexec.cfg written";
T.id.err="Gagal menulis berkas";T.en.err="Write failed";T.id.launch="Membuka mesin game…";T.en.launch="Opening game engine…";
function A(){return window.Android}
function cfg(){var f=S.fps;if($("eco").checked&&(!f||f>30))f=30;var sc=$("res").value/100,d=window.devicePixelRatio||1,w=Math.round(screen.width*d*sc),h=Math.round(screen.height*d*sc);if(w<h){var x=w;w=h;h=x}
 var pm={L:2,M:1,H:0}[S.tex],g=(0.8+$("br").value/125).toFixed(2),fx=$("fx").checked?1:0;
 return["// MOHAA Launcher 1.3","seta com_maxfps "+(f||0),"seta cg_drawFPS "+($("fpsc").checked?1:0),"seta r_swapInterval "+($("vsync").checked?1:0),"seta r_mode -1","seta r_customwidth "+w,"seta r_customheight "+h,"seta r_fullscreen "+($("full").checked?1:0),"seta r_picmip "+pm,"seta r_gamma "+g,"seta r_dynamiclight "+fx,"seta r_shadows "+fx,"seta r_worldDlights 0","seta cg_fov "+$("fov").value,"seta sensitivity "+($("se").value/10).toFixed(1),""].join("\n")}
function onFolder(p,n){$("fpath").textContent=p+" · "+n+" pk3"}
$("pick").onclick=function(){A()?A().pickFolder():toast(t("pickf"))};
$("apply").onclick=function(){if(!A()){toast(cfg().split("\n").length-2+" cvar (preview)");return}var r=A().writeCfg(cfg());toast(t(r=="ok"?"cfgok":r))};
$("play").onclick=function(){if(!A()){toast(t("eng"));setTimeout(openTest,900);return}var r=A().launch(cfg());toast(t(r=="ok"?"launch":r))};
try{var ff=A()&&A().getFolder();if(ff){var q=ff.split("|");onFolder(q[0],q[1])}}catch(e){}
$("bk").onclick=function(){var s=JSON.stringify({S:S,B:B,v:vals()});try{navigator.clipboard.writeText(s);toast(t("copied"))}catch(e){toast(s.slice(0,40))}};
$("rs").onclick=function(){try{localStorage.removeItem("mohaa13")}catch(e){}location.reload()};
window.onresize=function(){if($("ctl").classList.contains("on"))render($("pad"),true)};
/* load */
try{var d=JSON.parse(localStorage.getItem("mohaa13")||"null");if(d){S=d.S||S;B=d.B||B;pi=d.pi||0;var v=d.v||{};for(var k in v){var e=$(k);if(!e)continue;if(e.type=="checkbox")e.checked=v[k];else{e.value=v[k];e.oninput&&e.oninput()}}
 setSeg("fps",S.fps);setSeg("tex",S.tex);setSeg("lang",S.lang);setSeg("mode",S.mode);LG=S.lang;$("fpsv").textContent=S.fps||t("free")}}catch(e){}
tr();render($("pad"),true);
