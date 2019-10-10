$(document).ready(function() {
  $( "#readMoreButton" ).click(function AboutUs() {
  $("#helloHeading").hide();
   $("#HomePageParagraph").hide();
    $("#HomePageParagraph2").hide();
    $("#readMoreButton").hide();
 $('#aboutUsHeading').attr('hidden', false);
  $('#AboutUsParagraph').attr('hidden', false);
   $('#backButton').attr('hidden', false);



});

 $( "#backButton" ).click(function Back() {
  $("#helloHeading").show();
   $("#HomePageParagraph").show();
    $("#HomePageParagraph2").show();
    $("#readMoreButton").show();
 $('#aboutUsHeading').attr('hidden', true);
  $('#AboutUsParagraph').attr('hidden', true);
   $('#backButton').attr('hidden', true);



});


});