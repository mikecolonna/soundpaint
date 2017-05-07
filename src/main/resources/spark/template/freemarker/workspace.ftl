<#assign content>
<nav class="navbar navbar-default">
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
        <img src="images/panda_before.jpg" style="height: 27px">
      </a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a href="/">Home</a></li>
        <li class="active"><a href="/workspace">Workspace</a></li>
        <li><a href="/projects">Projects</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a>${name}</a></li>
        <li><a href="/logout">Logout</a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<div class="error">
  ${error}
</div>

<h1><span>WorkSpace</span></h1>

<div id="work" onresize="resize_canvas()">
  <input id="audio" type="file" name="audio" accept =".mp3, .wav, .midi, .mid"/>
  <label for="audio" class="white">Audio</label>
  <input id="video" type="file" name="video" accept =".mp4, .mov"/>
  <label for="video" class="white">Video</label>
  <div id="vf" class="tab">Video Filters</div>
  <div id="moveable_vf">
  	<ul id="filters">
  		<li class="filter_pair">
  			<select>
          <option value="Amplitude">Amplitude</option>
          <option value="Tempo">Tempo</option>
          <option value="Frequency">Frequency</option>
        </select>
  			<select>
          <option value="Tint">Tint</option>
          <option value="Push">Push</option>
          <option value="Bulge">Bulge</option>
          <option value="Emboss">Emboss</option>
        </select>
      </li>
  	</ul>
    <div style="text-align: right">
      <button class="my-button" id="new_filter">Add Filter Pair</button>
    </div>
  </div>
  <div id="visf" class="tab">Visualizer Filters</div>
  <div id="rgb">
    <label>R<input type="range" id="red" min="0" max="1" step="0.1"/></label>
    <label>G<input type="range" id="green" min="0" max="1" step="0.1"/></label>
    <label>B<input type="range" id="blue" min="0" max="1" step="0.1"/></label>
  </div>
  <div style="text-align: right" id="public_wrap">
    <input type="checkbox" id="public" name="public" value="true">Public<br>
  </div>
  <button class="my-button red-button" id="render">Render</button>
  <input type="range" id="transparency" min="0" max="100" />
  <label for="transparency" class="white" id="t_id">Transparency</label>
  <button class="my-button red-button" onclick="location.href='http://google.com';" id="done">Done</button>
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
<script src="js/test2.js"></script>

<audio id="myAudio" src="01 Ultralight Beam.mp3"></audio>
<canvas id="canvas">
</canvas>
<!-- <div id="empty_black">
</div -->
<div id="frame">
  <video id="preview">
      <source src="giphy.mp4" type="video/mp4">
  </video>
</div>

<script type="text/javascript">
  function resize_canvas() {
    canvas = document.getElementById("canvas");
    canvas.width = "87%";
    canvas.height = "80%";
  };
  $(document).ready(function() {
    let max_fields = 5; //maximum input boxes allowed
    let wrapper = $("#filters"); //Fields wrapper
    let x = 1;
    $("#new_filter").on("click", function(e) {
      e.preventDefault();
      if(x < max_fields) { //max input box allowed
        x++; //text box increment
        $(wrapper).append('<li class="filter_pair"><select><option value="Amplitude">Amplitude</option><option value="Tempo">Tempo</option><option value="Frequency">Frequency</option></select><select><option value="Tint">Tint</option><option value="Push">Push</option><option value="Bulge">Bulge</option><option value="Emboss">Emboss</option></select><br><a href="#" class="remove">remove</a></li>'); //add new filter space
        $(".remove").on("click", function(e) { //user click on remove text
          e.preventDefault();
          $(this).parent('li').remove();
          x--;
        })
      } else {
        alert("Maximum 5 filters");
      }
    });

    // send file data using AJAX
    function sendFileWhenDone(fileData) {
      // you can access the file data from the file reader's event object as:

      console.log("File data we sent: ", fileData);

      // Send AJAX request with form data
      $.ajax({
        type: "POST",
        // specify the url we want to upload our file to
        url: '/render',
        // this is how we pass in the actual file data from the form
        data: fileData,
        processData: false,
        contentType: false,
        success: function(JSONsentFromServer) {
          // what do you do went it goes through
          let parsed = JSON.parse(JSONsentFromServer);
          $('#myAudio').attr('src', parsed.audiofp);
          document.getElementById("myAudio").play();
          $('#preview').attr('src', parsed.videofp);
          $("#transparency").show();
          $("#show").t_id();
          $("#done").show();
          $("#render").prop("disabled",false);
        },
        error: function(errorSentFromServer) {
          // what to do if error
          console.log("[Error]", errorSentFromServer);
        }
      })
    }

    $("#render").click(function(e) {
      e.preventDefault();
      //check if files have been uploaded or not!!!-------------------------------------------------------------------------------------------
      // if(document.getElementById("uploadBox").value != "") {
      //   // you have a file
      // }
      $("#render").prop("disabled",true);
      let filter_choices = [];
      for(let i=0; i<x; i++) {
        filter_choices.push($($(".filter_pair").toArray()[i]).children().first().val());
        filter_choices.push($($(".filter_pair").toArray()[i]).children().eq(1).val());
      }
      // get a reference to the fileInput
      let audioInput = $("#audio");
      let videoInput = $("#video");
      // so that you can get the file you wanted to upload
      let audioFile = audioInput[0].files[0];
      let videoFile = videoInput[0].files[0];
      let public;
      if($('#checkArray:checkbox:checked').length > 0) {
        public = "true";
      } else {
        public = "false";
      }
      // create the container for our file data
      let fd = new FormData();

      // encode the file
      fd.append('audioName', audioFile);
      fd.append('videoName', videoFile);
      fd.append('filters', JSON.stringify(filter_choices));
      fd.append('public', public);

      sendFileWhenDone(fd);
    })
    $('#frame').resizable({
      aspectRatio: true,
      resize: function(event, ui) {
        if(($(this).width() < ($("body").width()*.79) && $(this).height() < ($("body").height()*.86)) && ($(this).width() > ($("body").width()*.19) && $(this).height() > ($("body").height()*.22))) {

          console.log($(this).height());
          console.log("bodymin " + $("body").height()*.22);
          console.log($("body").height());
          console.log("bodymax " + $("body").height()*.86);
          $(this).css({
            'top': parseInt(ui.position.top, 10) + ((ui.originalSize.height - ui.size.height)) / 2,
            'left': parseInt(ui.position.left, 10) + ((ui.originalSize.width - ui.size.width)) / 2
          });
        }
      }
    });
  });

  $("#vf").click(function(e) {
    e.preventDefault();
    $("#moveable_vf").slideToggle("slow", function() {
    // Animation complete.
    });
  })
  $("#visf").click(function(e) {
    e.preventDefault();
    $("#rgb").slideToggle("slow", function() {
    // Animation complete.
    });
  })

  $("#transparency").change(function() {
    $("#frame").css("opacity", $(this).val() / 100);
  })
</script>
</#assign>
<#include "main.ftl">
