document.addEventListener("DOMContentLoaded", function () {
    const posts = JSON.parse(document.getElementById('posts-data').textContent);

    function renderPosts(posts) {
        const postsContainer = document.getElementById('posts-container');
        postsContainer.innerHTML = '';
        posts.forEach(post => {
            const postElement = document.createElement('div');
            postElement.className = 'post';
            postElement.innerHTML = `
                        <h3>${post.title}</h3>
                        <p>${post.content}</p>
                        <p>Likes: ${post.likes}</p>
                        <p>Release Date: ${new Date(post.releaseDate).toLocaleDateString()}</p>
                    `;
            postsContainer.appendChild(postElement);
        });
    }

    function sortPostsByDate() {
        posts.sort((a, b) => new Date(b.releaseDate) - new Date(a.releaseDate));
        renderPosts(posts);
    }

    function sortPostsByLikes() {
        posts.sort((a, b) => b.likes - a.likes);
        renderPosts(posts);
    }

    document.getElementById('sort-date').addEventListener('click', sortPostsByDate);
    document.getElementById('sort-likes').addEventListener('click', sortPostsByLikes);

    // 초기 렌더링
    renderPosts(posts);
});