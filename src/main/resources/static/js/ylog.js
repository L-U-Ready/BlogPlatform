document.addEventListener("DOMContentLoaded", function () {
    const posts = JSON.parse(document.getElementById('posts-data').textContent);

    function renderPosts(posts) {
        const postsContainer = document.getElementById('posts-container');
        postsContainer.innerHTML = '';
        posts.forEach(post => {
            const postElement = document.createElement('div');
            postElement.className = 'post';
            postElement.innerHTML = `
                <h3><a href="/Ylog/${post.user.username}/post/${post.id}/${post.encodedTitle}">${post.title}</a></h3>
                <img src="/images/thumbnails/${post.thumbnailImage}" alt="Thumbnail Image" class="post-thumbnail">
                <p>${post.ment}</p>
                <p>Release Date: ${new Date(post.releaseDate).toLocaleDateString()}</p>
                <div class="post-author">
                    <img src="/images/profiles/${post.user.profileImage}" alt="Author Profile Image" class="author-profile">
                    <span>${post.user.username}</span>
                </div>
            `;
            postsContainer.appendChild(postElement);
        });
    }

    function sortPosts(criteria) {
        fetch(`/Ylog/sort?criteria=${criteria}`)
            .then(response => response.json())
            .then(data => {
                renderPosts(data);
            })
            .catch(error => console.error('Error:', error));
    }

    function sortPostsByDate() {
        posts.sort((a, b) => new Date(b.releaseDate) - new Date(a.releaseDate));
        renderPosts(posts);
    }

    function sortPostsByViews() {
        posts.sort((a, b) => b.views - a.views);
        renderPosts(posts);
    }

    function sortPostsByLikes() {
        posts.sort((a, b) => b.likes - a.likes);
        renderPosts(posts);
    }

    document.getElementById('sort-date').addEventListener('click', sortPostsByDate);
    document.getElementById('sort-views').addEventListener('click', sortPostsByViews);
    document.getElementById('sort-likes').addEventListener('click', sortPostsByLikes);

    // 초기 렌더링
    renderPosts(posts);
});
