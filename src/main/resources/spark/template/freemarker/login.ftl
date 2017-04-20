<#assign content>

<div class="container" style="text-align: center">
  <img src="images/panda_before.jpg" style="width:30%">
  <form>
    <ul class="flex-outer" style="text-align: left">
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
      <li style="justify-content: center; margin-top: 3%">
        <button class="my-button activated-red" type="button" id="login">Login</button>
      </li>
      <li style="justify-content: center; margin-top: 3%">
        <a href="/register">Register</button>
      </li>
    </ul>
  </form>
</div>

<script type="text/javascript">
  $('.container').find('input, select, option').on('keyup blur focus click', function (e) {
  
  let $this = $(this)
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

$('#login').click(function(e) {
  e.preventDefault();
  let email = $('#email').val();
  let password = $('#password').val();
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
  if(!($("#email").hasClass("required-field") && $("#password").hasClass("required-field"))) {
    console.log("yay");
  }
});
</script>


</#assign>
<#include "main.ftl">