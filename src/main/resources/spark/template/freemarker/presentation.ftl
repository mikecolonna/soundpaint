<#assign content>
<#assign log=logged/>
<nav class="navbar navbar-default navbar-custom">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="/">
        <img src="images/panda_after.png" style="height: 27px">
      </a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a href="/">Home</a></li>
        <li><a href="/workspace">Workspace</a></li>
        <li><a href="/projects">Projects</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <#if log=="true">
          <li><a>${name}</a></li>
          <li><a href="/logout">Logout</a></li>
        <#else>
          <li><a href="/login">Login</a></li>
          <li><a href="/register">Register</a></li>
        </#if>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<script src="js/three/three.js"></script>
<script src="js/three/EffectComposer.js"></script>
<script src="js/three/RenderPass.js"></script>
<script src="js/three/ShaderPass.js"></script>
<script src="js/three/CopyShader.js"></script>
<script src="js/three/DigitalGlitch.js"></script>
<script src="js/three/GlitchPass.js"></script>
<script src="js/three/BloomPass.js"></script>
<script src="js/three/ConvolutionShader.js"></script>
<script src="js/test2.js"></script>

<audio id="myAudio" src="${audiofile}"></audio>
<span class="go_back" id="go_back_3">&larr; Go Back</span>
<canvas id="presentation_canvas">
</canvas>
<div id="frame">
  <video id="preview">
      <source src="${videofile}" type="video/mp4">
  </video>
</div>

<script type="text/javascript">
	$('#frame').resizable({
	  aspectRatio: true,
	  resize: function(event, ui) {
	    if($(this).width() < ($("body").width()*.79) && $(this).height() < ($("body").height()*.86)) {
	      $(this).css({
	        'top': parseInt(ui.position.top, 10) + ((ui.originalSize.height - ui.size.height)) / 2,
	        'left': parseInt(ui.position.left, 10) + ((ui.originalSize.width - ui.size.width)) / 2
	      });
	    }
	  }
	});
</script>

</#assign>
<#include "main.ftl">
