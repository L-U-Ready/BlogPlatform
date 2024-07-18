function deletePost(username, postId) {
    if (confirm('Are you sure you want to delete this post?')) {
        fetch(`/Ylog/${username}/post/${postId}/delete`, {
            method: 'DELETE',
            credentials: 'include'
        })
            .then(response => {
                if (response.ok) {
                    document.getElementById(`post-${postId}`).remove();
                } else {
                    alert('Failed to delete post');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred while deleting the post');
            });
    }
}