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
        <li><a href="/home">Home</a></li>
        <li><a href="/workspace">Workspace</a></li>
        <li><a href="/projects">Projects</a></li>
      </ul>
      <form class="navbar-form navbar-left">
        <div class="form-group">
          <input type="text" class="form-control" placeholder="Search">
        </div>
        <button type="submit" class="btn btn-default">Submit</button>
      </form>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="/login">Login</a></li>
        <li class="active"><a href="/register">Register</a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<div class="container">
    <form>
        <ul class="flex-outer">
          <li>
            <p style="margin-bottom=2%;"> Please fill in the fields with your information</p>
          </li>
          <li>
            <input type="text" id="first_name" name="first_name" placeholder="First Name*">
            <label for="first_name" class="my-label">First Name*</label>
            <label for="first_name" class="my-error">required</label>
          </li>
          <li>
            <input type="text" id="last_name" name="last_name" placeholder="Last Name*">
            <label for="last_name" class="my-label">Last Name*</label>
            <label for="last_name" class="my-error">required</label>
          </li>
          <li>
            <input type="email" id="email" name="email" placeholder="Email*">
            <label for="email" class="my-label">Email*</label>
            <label for="email" class="my-error">required</label>
          </li>
          <li>
            <input type="password" id="password" name="password" placeholder="Password*">
            <label for="password" class="my-label">Password*</label>
            <label for="password" class="my-error">required</label>
          </li>
          <li>
            <input type="password" id="password2" name="password2" placeholder="Retype Password*">
            <label for="password2" class="my-label">Retype Password*</label>
            <label for="password2" class="my-error">required</label>
          </li>
          <li style="justify-content: center; margin-top: 3%">
            <button class="my-button activated-red" type="button" id="register">Sign Up</button>
          </li>
        </ul>
    </form>
</div>
<script type="text/javascript">
  $('.container').find('input, select, option').on('keyup blur focus click', function (e) {
  
  var $this = $(this)
    if (e.type === 'keyup') {
      if ($this.val() === '') {
        $(this).parent().children().eq(1).animate({ opacity: 0 });
      } else {
        $(this).parent().children().eq(1).animate({ opacity: .9 });
      }
    } else if (e.type === 'blur') {
      if($this.val() === '' || $this.val() == null) {
        $(this).parent().children().eq(1).animate({ opacity: 0 }); 
      } else {
        $(this).parent().children().eq(1).animate({ opacity: .9 });   
      }   
    } else if (e.type === 'focus') {
      if($this.val() === '' || $this.val() == null) {
        $(this).parent().children().eq(1).animate({ opacity: 0 }); 
      } 
      else if($this.val() !== '') {
        $(this).parent().children().eq(1).animate({ opacity: .9 }); 
      }
    } else if (e.type === 'click') {
      if($this.val() === '' || $this.val() == null) {
        $(this).parent().children().eq(1).animate({ opacity: 0 }); 
      } 
      else if($this.val() !== '') {
        $(this).parent().children().eq(1).animate({ opacity: .9 }); 
      }
    }
});

$('#register').click(function(e) {
  e.preventDefault();
  var email = $('#email').val();
  var password = $('#password').val();
  if($("#first_name").val() === '') {
    $("#first_name").addClass("required-field");
    $("#first_name").parent().children().eq(2).animate({ opacity: 1 }); 
  } else {
    if($("#first_name").hasClass("required-field")) {
      $("#first_name").removeClass("required-field");
      $("#first_name").parent().children().eq(2).animate({ opacity: 0 }); 
    }
  }
  if($("#last_name").val() === '') {
    $("#last_name").addClass("required-field");
    $("#last_name").parent().children().eq(2).animate({ opacity: 1 }); 
  } else {
    if($("#last_name").hasClass("required-field")) {
      $("#last_name").removeClass("required-field");
      $("#last_name").parent().children().eq(2).animate({ opacity: 0 });
    }
  }
  if(email.indexOf('@') === -1) { //look for this character 
    $("#email").addClass("required-field");
    if(email === '') {
      $("#email").parent().children().eq(2).text("Email required");
    } else {
      $("#email").parent().children().eq(2).text("Please enter a proper email");
    }
    $("#email").parent().children().eq(2).animate({ opacity: 1 }); 
  } else {
    if($("#email").hasClass("required-field")) {
      $("#email").removeClass("required-field");
      $("#email").parent().children().eq(2).animate({ opacity: 0 });
    }
  }
  if(password.length < 6){
    $("#password").addClass("required-field");
    if(password === '') {
      $("#password").parent().children().eq(2).text("Password required");
    } else {
      $("#password").parent().children().eq(2).text("Password must be at least 6 characters long");
    }
    $("#password").parent().children().eq(2).animate({ opacity: 1 }); 
  } else {
    if($("#password").hasClass("required-field")) {
      $("#password").removeClass("required-field");
      $("#password").parent().children().eq(2).animate({ opacity: 0 });
    }
  }
  if($("#password2").val() === ''){
    $("#password2").parent().children().eq(2).text("This is required");
    $("#password2").addClass("required-field");
    $("#password2").parent().children().eq(2).animate({ opacity: 1 }); 
  } else if($("#password2").val() !== password && $("#password2").val() !== ''){
    $("#password2").parent().children().eq(2).text("Passwords don't match");
    $("#password2").addClass("required-field");
    $("#password2").parent().children().eq(2).animate({ opacity: 1 }); 
  } else {
    if($("#password2").hasClass("required-field")) {
      $("#password2").removeClass("required-field");
      $("#password2").parent().children().eq(2).animate({ opacity: 0 }); 
    }
  }
  if($("#first-name").hasClass("required-field") || $("#last-name").hasClass("required-field") || $("#email").hasClass("required-field") || $("#password").hasClass("required-field") || $("#password2").hasClass("required-field") || $("#student-mentor").hasClass("required-field")) {
  } else {
    $("#login").css("display", "none");
    $("#login-button").removeClass("active-color").addClass("activated");
    $("#profile-button").removeClass("not-activated").addClass("active-color");
    if($('#student-mentor').val() === 'student') {
      $("#mentee-profile").css("display", "block");
    } else if($('#student-mentor').val() === 'mentor'){
      $("#mentor-profile").css("display", "block");
    }
    $("#form-box").scrollTop(0);
    $(window).scrollTop(0);
  } 
});
</script>
</#assign>
<#include "main.ftl">