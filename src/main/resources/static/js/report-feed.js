document.addEventListener("DOMContentLoaded", function () {
    const feedContainer = document.getElementById('report-feed-container');
    if (feedContainer) {
        let page = 0;
        let isLoading = false;
        let hasMore = true;

        const observer = new IntersectionObserver(async (entries) => {
            if (entries[0].isIntersecting && !isLoading && hasMore) {
                isLoading = true;
                page++;
                const spinner = document.getElementById('loading-spinner');
                if (spinner) spinner.classList.remove('d-none');

                const urlParams = new URLSearchParams(window.location.search);
                urlParams.set('page', page);

                try {
                    const res = await fetch(`/feed/fragments?${urlParams.toString()}`);
                    const html = await res.text();

                    if (!html.trim()) {
                        hasMore = false;
                    } else {
                        feedContainer.insertAdjacentHTML('beforeend', html);
                    }
                } catch (e) {
                    console.error("Scroll load failed", e);
                } finally {
                    isLoading = false;
                    if (spinner) spinner.classList.add('d-none');
                }
            }
        }, { threshold: 1.0 });

        const trigger = document.getElementById('scroll-trigger');
        if (trigger) observer.observe(trigger);
    }
});

document.addEventListener('click', async (event) => {
    const button = event.target.closest('.upvote-btn');
    if (!button) return;

    event.preventDefault();
    event.stopPropagation();

    const reportId = button.getAttribute('data-report-id');
    const countSpan = button.querySelector('.upvote-count');

    button.style.pointerEvents = 'none';
    button.style.opacity = '0.5';

    try {
        const statusRes = await apiClient.get(`/api/v1/reports/${reportId}/upvotes`);
        const statusData = await statusRes.json();

        if (statusData.success) {
            const isUpvoted = statusData.data.upvotedByMe;

            if (isUpvoted) {
                const delRes = await apiClient.delete(`/api/v1/reports/${reportId}/upvotes`);
                if (delRes.ok) {
                    countSpan.textContent = Math.max(0, parseInt(countSpan.textContent) - 1);
                    button.classList.remove('text-primary');
                }
            } else {
                const addRes = await apiClient.post(`/api/v1/reports/${reportId}/upvotes`);
                if (addRes.ok) {
                    countSpan.textContent = parseInt(countSpan.textContent) + 1;
                    button.classList.add('text-primary');
                }
            }
        }
    } catch (error) {
        console.error("Upvote failed:", error);
    } finally {
        button.style.pointerEvents = 'auto';
        button.style.opacity = '1';
    }
});