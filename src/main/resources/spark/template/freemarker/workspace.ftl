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
  <!-- <input id="audio" type="file" name="audio" accept =".mp3, .wav, .midi, .mid"/>
  <label for="audio" class="white">Audio</label>
  <input id="video" type="file" name="video" accept =".mp4, .mov"/>
  <label for="video" class="white">Video</label> -->
  <div id="vf" class="tab">Video Filters</div>
  <div id="moveable_vf">
    <div class="filter_pair" id="filter_selector">
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
      <span id="new_filter" style="font-size:15pt; cursor: pointer">+</span>
    </div>
    <ul id="filters">
  	</ul>
  </div>
  <div style="text-align: right" id="public_wrap">
    <input type="checkbox" id="public" name="public" value="true">Public<br>
  </div>
  <button class="my-button red-button" id="render">Render</button>
  <div id="visualizer">
    <div id="visf" class="tab">Display Options</div>
    <div id="opts">
      <input type="radio" id="setRainbow" name="viscolor" class="viscolor" value="rainbow" checked><span class ="white">Rainbow</span>
      <input type="radio" id="setRgb" name="viscolor" class="viscolor" value="rgb"><span class="white">RGB</span>
      <div id="rgb">
        <label class="white">R<input type="range" id="red" min="0" max="1" step="0.1"/></label>
        <label class="white">G<input type="range" id="green" min="0" max="1" step="0.1"/></label>
        <label class="white">B<input type="range" id="blue" min="0" max="1" step="0.1"/></label>
      </div>
    </div>
  </div>
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
<script src="js/visualizer.js"></script>

<audio id="myAudio" src="01 Ultralight Beam.mp3"></audio>
<canvas id="canvas" style="display:none">
</canvas>
<div id="empty_black">
  <div id="video_drop_area">
    <p id="video_drop_text">To begin, drag a video file onto the canvas.</p>>
    <p id="video_drop_error" style="display:none">File type not accepted (.mp4 and .mov are accepted).</p>
  </div>

  <div id="video_audio_prompt">
    <p id="prompt_text">Would you like to use the audio from this video or provide a new audio file?</p>
    <button id="prompt_use"></button>
    <button id="prompt_dont_use"></button>
  </div>

  <div id="audio_drop_area" style="display:none">
    <p id="audio_drop_text">Drag an audio file onto the canvas.</p>
    <p id="audio_drop_error" style="display:none">File type not accepted (.wav, .mp3, and .mid are accepted).</p>
  </div>

  <p id="choose_filters" style="display:none">Select your audio-visual filter specifications.</p>
</div>

<div id="frame">
  <video id="preview" autoplay>
      <source src="giphy.mp4" type="video/mp4">
  </video>
</div>

<script type="text/javascript">
  let audioFile;
  let videoFile;
  function resize_canvas() {
    canvas = document.getElementById("canvas");
    canvas.width = "87%";
    canvas.height = "80%";
  };
  $(document).ready(function() {
    let fd = new FormData();  //to send to backend upon render
    let usingAudioFromVideo = false;

    let max_fields = 5; //maximum input boxes allowed
    let wrapper = $("#filters"); //Fields wrapper
    let x = 1;
    $("#new_filter").click(function(e) {
      e.preventDefault();
      if(x < max_fields) { //max input box allowed
        x++; //text box increment
        $(wrapper).append('<li class="filter_pair"><span>'+ $("#filter_selector").children().first().val() +'</span>-----------<span>'+ $("#filter_selector").children().eq(1).val() +'</span><a href="#" class="remove">x</a></li>'); //add new filter space
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
          $("#empty_black").hide();
          $("#canvas").show();
          $("#frame").show();
          $("#visualizer").show();
          $("#transparency").show();
          $("#t_id").show();
          $("#done").show();
          $("#render").prop("disabled",false);
          startVisualizer();
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
      //let audioInput = $("#audio");
      //let videoInput = $("#video");
      // so that you can get the file you wanted to upload
      //let audioFile = audioInput[0].files[0];
      //let videoFile = videoInput[0].files[0];
      let public;
      if($('#checkArray:checkbox:checked').length > 0) {
        public = "true";
      } else {
        public = "false";
      }
      // create the container for our file data
      //let fd = new FormData();
      console.log(audioFile);
      console.log(videoFile);
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
    $("#opts").slideToggle("slow", function() {
    // Animation complete.
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
  })

  $("#video_drop_area").hover(function(e) {
    $("#video_drop_area").css("border-color", "white");
    $("#video_drop_text").css("color", "white");
    $("#video_drop_error").css("color", "white");
  }, function(e) {
    $("#video_drop_area").css("border-color", "#878787");
    $("#video_drop_text").css("color", "#878787");
    $("#video_drop_error").css("color", "#878787");
  })

  $("#video_drop_area").on("dragover", function(e) {
    e.preventDefault();
    e.stopPropagation();
  })

  $("#video_drop_area").on("dragenter", function(e) {
    e.preventDefault();
    e.stopPropagation();
  })

  $("#video_drop_area").on("drop", function(e) {
    e.preventDefault();
    e.stopPropagation();

    let dropped = e.originalEvent.dataTransfer.files[0];

    console.log(dropped);

    if (dropped.type === "video/mp4" || dropped.type === "video/mov") {
      //console.log(videoFile);

      videoFile = dropped;

      $("#video_drop_area").fadeOut("slow", function() {
        // Animation complete.

        $("#audio_drop_area").fadeIn("slow", function() {
          // Animation complete.
        })
      })
    } else {
      console.log("didn't work");
      $("#video_drop_error").show();
    }
  })

  $("#audio_drop_area").hover(function(e) {
    $("#audio_drop_area").css("border-color", "white");
    $("#audio_drop_text").css("color", "white");
    $("#audio_drop_error").css("color", "white");
  }, function(e) {
    $("#audio_drop_area").css("border-color", "#878787");
    $("#audio_drop_text").css("color", "#878787");
    $("#audio_drop_error").css("color", "#878787");
  })

  $("#audio_drop_area").on("dragover", function(e) {
    e.preventDefault();
    e.stopPropagation();
  })

  $("#audio_drop_area").on("dragenter", function(e) {
    e.preventDefault();
    e.stopPropagation();
  })

  $("#audio_drop_area").on("drop", function(e) {
    e.preventDefault();
    e.stopPropagation();

    dropped = e.originalEvent.dataTransfer.files[0];

    console.log(dropped);

    if (dropped.type === "audio/wav" || dropped.type === "audio/mp3"
      || dropped.type === "audio/mid") {
      audioFile = dropped;

      $("#audio_drop_area").fadeOut("slow", function() {
        // Animation complete.
        $("#choose_filters").fadeIn("slow", function() {
          // Animation complete.
        })
      })
    } else {
      console.log("didn't work");
      $("#audio_drop_error").show();
    }
  })

  $("#transparency").change(function() {
    $("#frame").css("opacity", $(this).val() / 100);
  })
</script>
</#assign>
<#include "main.ftl">
