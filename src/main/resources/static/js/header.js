document.addEventListener("DOMContentLoaded", function() {
    var headerHtml = `
        <div class="header">
            <div class="logo">YLOG</div>
            <div class="nav">`;

    if (isLoggedIn) {
        headerHtml += `
                <button onclick="location.href='/new-write'">new write</button>
                <button onclick="location.href='/profile'">profile</button>`;
    } else {
        headerHtml += `
                <button onclick="location.href='/login'">login</button>`;
    }

    headerHtml += `
            </div>
        </div>`;

    document.getElementById('header').innerHTML = headerHtml;
});