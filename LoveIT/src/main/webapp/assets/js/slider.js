/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


var slider = document.getElementById("age");
var output = document.getElementById("age-value");

output.innerHTML = slider.value;
slider.oninput = function() {
    output.innerHTML = this.value;
}