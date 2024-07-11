// document.addEventListener("DOMContentLoaded", function() {
//     var headerHtml = `
//         <div class="header">
//             <div class="logo">YLOG</div>
//             <div class="nav">`;
//
//     if (isLoggedIn) {
//         headerHtml += `
//                 <button onclick="location.href='/new-write'">new write</button>
//                 <button onclick="location.href='/profile'">profile</button>`;
//     } else {
//         headerHtml += `
//                 <button onclick="location.href='/login'">login</button>`;
//     }
//
//     headerHtml += `
//             </div>
//         </div>`;
//
//     document.getElementById('header').innerHTML = headerHtml;
// });

// JavaScript for dropdown
document.addEventListener("DOMContentLoaded", function() {
    const userSettings = document.querySelector('.user-settings');
    const dropdownContent = userSettings.querySelector('.dropdown-content');

    userSettings.addEventListener('click', function() {
        dropdownContent.classList.toggle('show');
    });

    // Close the dropdown if the user clicks outside of it
    window.onclick = function(event) {
        if (!event.target.closest('.user-settings')) {
            if (dropdownContent.classList.contains('show')) {
                dropdownContent.classList.remove('show');
            }
        }
    }
});

