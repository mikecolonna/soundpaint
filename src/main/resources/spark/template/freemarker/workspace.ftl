<#assign content>
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

<div id="work" onresize="resize_canvas()">
  <!-- <input id="audio" type="file" name="audio" accept =".mp3, .wav, .midi, .mid"/>
  <label for="audio" class="white">Audio</label>
  <input id="video" type="file" name="video" accept =".mp4, .mov"/>
  <label for="video" class="white">Video</label> -->
  <div class="dropper">
    <div id="vf" class="tab">Video Filters<span class="glyphicon glyphicon-triangle-right" style="float: right;"></span></div>
    <div id="moveable_vf">
      <div class="filter_pair" id="filter_selector">
    		<select>
          <option value="" disabled selected>Sound Param</option>
          <option value="Amplitude">Amplitude</option>
          <option value="Frequency">Frequency</option>
          <option value="Tempo">Tempo</option>
          <option value="Volume">Volume</option>
        </select>
    		<select>
          <option value="" disabled selected>Filter</option>
          <option value="Bulge">Bulge</option>
          <option value="Emboss">Emboss</option>
          <option value="Push">Push</option>
          <option value="Red">Red-Tint</option>
          <option value="Green">Green-Tint</option>
          <option value="Blue">Blue-Tint</option>
          <option value="Push">Push</option>
          <option value="Pinch">Pinch</option>
        </select>
        <span class="glyphicon glyphicon-plus" id ="new_filter"></span>
        <label><div>Sensitivity</div><input type="range" min="0" max="1" step="0.1" id="sensitivity" class="one_range"/><input type="number" min="0" max="1" step="0.1" class="range_compatible"/></label>
      </div>
      <ul id="filters">
    	</ul>
    </div>
  </div>
  <div style="text-align: right" id="public_wrap">
    <input type="checkbox" id="public" name="public" value="true" style="margin-right:5px">Make Public<br>
  </div>
  <button class="my-button red-button" id="render">Render</button>
  <div class="front_error">
  </div>
  <div id="visualizer" class="after_render dropper">
    <div id="visf" class="tab">Display Options<span class="glyphicon glyphicon-triangle-bottom" style="float: right;"></span></div>
    <div id="opts">
        <input type="radio" id="pulse" name="vistype" value="pulse" checked><span class="white">Pulse</span>
        <input type="radio" id="strobe" name="vistype" value="strobe"><span class="white">Strobe</span><br>
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
  <button class="my-button red-button after_render" onclick="location.href='/';" id="done">Done</button>
  <a id="restart" class="after_render gen_a" href="#">Upload Different Files</a>
</div>

<script src="js/three/three.js"></script>
<script src="js/three/EffectComposer.js"></script>
<script src="js/three/RenderPass.js"></script>
<script src="js/three/ShaderPass.js"></script>
<script src="js/three/CopyShader.js"></script>
<script src="js/three/BloomPass.js"></script>
<script src="js/three/ConvolutionShader.js"></script>
<script src="js/visualizer.js"></script>

<audio id="myAudio" src=""></audio>
<canvas id="canvas" class="after_render">
</canvas>
<div id="empty_black">

  <div id="video_drop_area">
    <p id="video_drop_text">To begin, drag a video file onto the canvas.</p>
    <p id="video_drop_error" style="display:none">File type not accepted (.mp4 and .mov are accepted).</p>
  </div>

  <div id="video_audio_prompt">
    <span class="go_back" id="go_back_1">&larr; Go Back</span>
    <p id="prompt_text">Would you like to use the audio from this video or provide a new audio file?</p>
    <br>
    <div id="buttons">
      <button id="prompt_use" class="button">Use Audio from Video</button>
      <button id="prompt_dont_use" class="button">Use Different Audio</button>
    </div>
  </div>

  <div id="audio_drop_area">
    <span class="go_back" id="go_back_2">&larr; Go Back</span>
    <p id="audio_drop_text">Drag an audio file onto the canvas.</p>
    <p id="audio_drop_error">File type not accepted (.wav, .mp3, and .mid are accepted).</p>
  </div>

  <div id="choose_filters_area">
    <span class="go_back" id="go_back_3">&larr; Go Back</span>
    <p id="choose_filters">Select your audio-visual filter specifications.</p>
  </div>

</div>

<div id="frame" class="after_render">
  <div id="resize_wrapper">
    <video id="preview" autoplay>
        <source src="" type="video/mp4">
    </video>
  </div>
</div>

<script type="text/javascript">
  let audioFile = null;
  let videoFile = null;
  let vid = "none";
  let usingAudioFromVideo = "false";

  function resize_canvas() {
    canvas = document.getElementById("canvas");
    canvas.width = "87%";
    canvas.height = "80%";
  };
  $(document).ready(function() {
    let fd = new FormData();  //to send to backend upon render

    let max_fields = 5; //maximum input boxes allowed
    let wrapper = $("#filters"); //Fields wrapper
    let x = 0;
    $("#new_filter").click(function(e) {
      e.preventDefault();
      if($("#filter_selector").children().first().val() == null) {
        $(".front_error").html("Please choose a sound parameter.");
        return;
      } else if($("#filter_selector").children().eq(1).val() == null) {
        $(".front_error").html("Please choose a video filter.");
        return;
      } else {
        $(".front_error").html("");
      }
      if(x < max_fields) { //max input box allowed
        x++; //text box increment
        $(wrapper).append('<li class="filter_pair"><span class="sound_param">'+ $("#filter_selector").children().first().val() +'</span>     --     <span class="filter_chosen">'+ $("#filter_selector").children().eq(1).val() +'</span> (<span class="sense">'+ $("#sensitivity").val() +'</span>)<span class="glyphicon glyphicon-remove remove"></span></li>'); //add new filter space
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
          vid = parsed.videoid;
          $("#done").attr("onclick","/video/" + vid);
          $('#myAudio').attr('src', parsed.audiofp);
          document.getElementById("myAudio").play();
          $('#preview').attr('src', parsed.videofp);
          $("#empty_black").hide();
          $(".after_render").slideDown();
          $("#render").prop("disabled",false);
          //console.log(parsed.animationdata);
          setAnimationData(parsed.animationdata);
          initVisualizerAudio();
          initVisualizer();
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
      if(videoFile == null) {
        $(".front_error").html("Please upload a video file.");
        return;
      } else if(usingAudioFromVideo == true) {
        if(audioFile == null) {
          $(".front_error").html("Please upload an audio file.");
          return;
        }
      } else {
        $(".front_error").html("");
      }
      $("#render").prop("disabled",true);
      let filter_choices = [];
      let $pairs = $(".filter_pair").toArray();
      console.log($pairs);
      for(let i=1; i<x+1; i++) {
        filter_choices.push($($pairs[i]).children().first().html());
        filter_choices.push($($pairs[i]).children().eq(1).html());
        filter_choices.push($($pairs[i]).children().eq(2).html());
      }
      // get a reference to the fileInput
      //let audioInput = $("#audio");
      //let videoInput = $("#video");
      // so that you can get the file you wanted to upload
      //let audioFile = audioInput[0].files[0];
      //let videoFile = videoInput[0].files[0];
      let pub;
      if($('#public:checked').length > 0) {
        pub = "true";
      } else {
        pub = "false";
      }
      // create the container for our file data
      //let fd = new FormData();
      console.log(audioFile);
      console.log(videoFile);
      // encode the file
      fd.append('audioName', audioFile);
      fd.append('videoName', videoFile);
      fd.append('usingAudioFromVideo', usingAudioFromVideo);
      fd.append('filters', JSON.stringify(filter_choices));
      fd.append('pub', pub);
      fd.append('vid', vid);

      sendFileWhenDone(fd);
    })
    $('#resize_wrapper').resizable({
      aspectRatio: true,
      resize: function(event, ui) {
          // console.log($(this).height());
          // console.log("bodymin " + $("body").height()*.22);
          // console.log($("body").height());
          // console.log("bodymax " + $("body").height()*.86);
        $(this).css({
          'top': parseInt(ui.position.top, 10) + ((ui.originalSize.height - ui.size.height)) / 2,
          'left': parseInt(ui.position.left, 10) + ((ui.originalSize.width - ui.size.width)) / 2
        });
      }
    });
    $(".range_compatible").val(.5);
    $('#preview').on('ended',function(){
      console.log("here");
      $('#myAudio')[0].pause;
      $('#myAudio')[0].currentTime = 0;
      $(this)[0].currentTime = 0;
      resetSoundCounter();
      $('#myAudio')[0].play();
      $(this)[0].play();


    });
  });

  $("#vf").click(function(e) {
    e.preventDefault();
    let $tab = $(this).children().first();
    $("#moveable_vf").slideToggle("slow", function() {
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
  })

  // range bars and respective input fields
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

    if (dropped.type === "video/mp4" || dropped.type === "video/mov") {
      videoFile = dropped;
      $("#video_drop_area").fadeOut("slow", function() {
        // Animation complete.
        $("#video_audio_prompt").fadeIn("slow", function() {
          // Animation complete.
        })
      })
    } else {
      $("#video_drop_error").show();
    }
  })

  $("#prompt_use").click(function(e) {
    usingAudioFromVideo = "true";
    $("#video_audio_prompt").fadeOut("slow", function() {
      $("#choose_filters_area").fadeIn("slow", function() {
        // Animation complete.
      })
    })
  })

  $("#prompt_dont_use").click(function(e) {
    $("#video_audio_prompt").fadeOut("slow", function() {
      $("#audio_drop_area").fadeIn("slow", function() {
        // Animation complete.
      })
    })
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

    if (dropped.type === "audio/wav" || dropped.type === "audio/mp3"
      || dropped.type === "audio/mid") {
      audioFile = dropped;

      $("#audio_drop_area").fadeOut("slow", function() {
        // Animation complete.
        $("#choose_filters_area").fadeIn("slow", function() {
          // Animation complete.
        })
      })
    } else {
      $("#audio_drop_error").show();
    }
  })

  $("#go_back_1").click(function(e) {
    $("#video_audio_prompt").fadeOut("slow", function(e) {
      $("#video_drop_area").fadeIn("slow", function(e) {
        // Animation complete.
      })
    })
  })

  $("#go_back_2").click(function(e) {
    $("#audio_drop_area").fadeOut("slow", function(e) {
      $("#video_audio_prompt").fadeIn("slow", function(e) {
        // Animation complete.
      })
    })
  })

  $("#go_back_3").click(function(e) {
    $("#choose_filters_area").fadeOut("slow", function(e) {
      $("#video_audio_prompt").fadeIn("slow", function(e) {
        // Animation complete.
      })
    })
  })

  $("#transparency").change(function() {
    $("#frame").css("opacity", $(this).val());
  })
  $("#transparency_num").on("click change", function() {
    $("#frame").css("opacity", $(this).val());
  })

  $("#restart").click(function(e) {
    e.preventDefault();
    audioFile = null;
    videoFile = null;
    $('#myAudio').attr('src', "");
    $('#preview').attr('src', "");
    $("#empty_black").show();
    $("#video_drop_area").fadeIn("slow", function(e) {});
    $(".after_render").slideUp();
  })
</script>
</#assign>
<#include "main.ftl">
