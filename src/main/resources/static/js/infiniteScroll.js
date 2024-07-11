document.addEventListener('DOMContentLoaded', function () {
    let page = 1;

    function loadMorePosts() {
        const postsBox = document.getElementById('posts-box');
        if (postsBox.scrollTop + postsBox.clientHeight >= postsBox.scrollHeight) {
            page++;
            fetch(`/Ylog/${username}/post?page=${page}`)
                .then(response => response.text())
                .then(data => {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(data, 'text/html');
                    const newPosts = doc.querySelector('.posts-box ul').innerHTML;
                    postsBox.querySelector('ul').insertAdjacentHTML('beforeend', newPosts);
                })
                .catch(error => console.error('Error loading more posts:', error));
        }
    }

    const postsBox = document.getElementById('posts-box');
    postsBox.addEventListener('scroll', loadMorePosts);
});
