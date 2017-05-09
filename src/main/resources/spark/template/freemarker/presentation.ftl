<#assign content>
<#assign log=logged/>

<input type="radio" id="pulse" name="vistype" value="pulse" checked style="display:none">
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

<div id="work" onresize="resize_canvas()">
    <div id="visualizer_play" class="dropper">
        <div id="visf" class="tab">Display Options<span class="glyphicon glyphicon-triangle-bottom" style="float: right;"></span></div>
        <div id="opts">
            <input type="radio" id="setRainbow" name="viscolor" class="viscolor" value="rainbow" checked><span class="white">Rainbow</span>
            <input type="radio" id="setRgb" name="viscolor" class="viscolor" value="rgb"><span class="white">RGB</span>
            <div id="rgb">
                <label style="color:red; font-size:15pt"><div>R</div><input type="range" id="red" min="0" max="1" step="0.1" class="one_range"/><input type="number" min="0" max="1" step="0.1" class="range_compatible"/></label>
                <label style="color:green; font-size:15pt"><div>G</div><input type="range" id="green" min="0" max="1" step="0.1" class="one_range"/><input type="number" min="0" max="1" step="0.1" class="range_compatible"/></label>
                <label style="color:blue; font-size:15pt"><div>B</div><input type="range" id="blue" min="0" max="1" step="0.1" class="one_range"/><input type="number" min="0" max="1" step="0.1" class="range_compatible"/></label>
            </div>
            <label for="transparency" class="white" id="t_id"><div>Transparency</div><input type="range" id="transparency" class="one_range" min="0" max="1" step="0.1"/><input type="number" min="0" max="1" step="0.1" id="transparency_num" class="range_compatible" style="color:#2B2B2B"/></label>
        </div>
    </div>
</div>

<script src="js/three/three.js"></script>
<script src="js/three/EffectComposer.js"></script>
<script src="js/three/RenderPass.js"></script>
<script src="js/three/ShaderPass.js"></script>
<script src="js/three/CopyShader.js"></script>
<script src="js/three/DigitalGlitch.js"></script>
<script src="js/three/GlitchPass.js"></script>
<script src="js/three/BloomPass.js"></script>
<script src="js/three/ConvolutionShader.js"></script>
<script src="js/visualizer.js"></script>

<audio id="myAudio" src="${vidaud[1]}"></audio>
<#--<span class="go_back" id="go_back_3">&larr;</span>-->
<canvas id="canvas" style="display: block">
</canvas>
<div id="frame_play">
  <video id="preview">
      <source src="${vidaud[0]}" type="video/mp4">
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
    $(document).ready(function() {
        $(".range_compatible").val(0.5);
        $("#transparency_num").val(0.5);
        initVisualizerAudio();
        initVisualizer();
        startVisualizer();
        document.getElementById("myAudio").play();
        $('#preview')[0].play();
    })

    $("#visf").click(function(e) {
        e.preventDefault();
        let $tab = $(this).children().first();
        $("#opts").slideToggle("slow", function() {
            // Animation complete.
            if($tab.hasClass("glyphicon-triangle-right")) {
                $tab.removeClass("glyphicon-triangle-right");
                $tab.addClass("glyphicon-triangle-bottom");
            } else {
                $tab.removeClass("glyphicon-triangle-bottom");
                $tab.addClass("glyphicon-triangle-right");
            }
        });
    })
    $("#setRgb").change(function(e) {
        e.preventDefault();
        $("#rgb").slideDown("slow", function() {
            // Animation complete.
        });
    })

    $("#setRainbow").change(function(e) {
        e.preventDefault();
        $("#rgb").slideUp("slow", function() {
            // Animation complete.
        });
    });
    $(".one_range").change(function(e) {
        let num = $(this).val();
        $(this).parent().children().last().val(num);
    });
    $(".range_compatible").on("change click", function(e) {
        let num = $(this).val();
        if(num > 1) {
            $(this).val(1);
            num = 1;
        } else if(num < 0) {
            $(this).val(0);
            num = 0;
        }
        $(this).parent().children().eq(1).val(num);
    });
    $('#preview').on('ended',function(){
        $('#myAudio')[0].pause;
        $('#myAudio')[0].currentTime = 0;
        $(this)[0].currentTime = 0;
        resetSoundCounter();
        $('#myAudio')[0].play();
        $(this)[0].play();
    });
    $("#transparency").change(function() {
        $("#frame_play").css("opacity", $(this).val());
    })
    $("#transparency_num").on("click change", function() {
        $("#frame_play").css("opacity", $(this).val());
    })
</script>

</#assign>
<#include "main.ftl">
