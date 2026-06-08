document.addEventListener("DOMContentLoaded", function () {

    const uploadArea = document.querySelector('.upload-area');
    const fileInput = document.getElementById('reportImages');
    if (uploadArea && fileInput) {
        uploadArea.addEventListener('click', function () {
            fileInput.click();
        });

        fileInput.addEventListener('change', function () {
            const h5Text = uploadArea.querySelector('h5');
            const pText = uploadArea.querySelector('p');
            const icon = uploadArea.querySelector('i')

            if (this.files && this.files.length > 0) {
                if (this.files.length > 3) {
                    h5Text.innerHTML = `<span class="text-danger fw-bold">Waduh, Kebanyakan!</span>`;
                    pText.innerHTML = `<span class="text-danger fw-bold">Maksimal 3 foto! Kamu memilih ${this.files.length} foto</span>`;
                    this.value = '';
                } else {
                    let fileNames = Array.from(this.files).map(f => f.name).join(', ');
                    h5Text.innerHTML = `<span class="text-success fw-bold"><i class="bi bi-check-circle"></i> File Terpilih!</span>`;
                    pText.innerHTML = `<span class="text-success fw-bold">${this.files.length} foto siap dikirim:</span><br><small>${fileNames}</small>`;
                }
            } else {
                h5Text.textContent = 'Klik untuk unggah atau seret file di sini';
                h5Text.className = 'h6 fw-bold mb-1';
                pText.textContent = 'PNG, JPG, dan WEBP. Maks 3 foto (Max 5MB/file)';
            }
        });
    }

    const triggerTabList = document.querySelectorAll('#masterDataTab button');
    if (triggerTabList.length > 0) {
        triggerTabList.forEach(triggerEl => {
            triggerEl.addEventListener('click', event => {
                triggerTabList.forEach(el => {
                    el.classList.remove('border-bottom', 'border-3', 'border-primary', 'text-dark');
                    el.classList.add('text-secondary');
                });

                event.target.classList.add('border-bottom', 'border-3', 'border-primary', 'text-dark');
                event.target.classList.remove('text-secondary');
            });
        });
    }

    let page = 0;
    let isLoading = false;
    let hasMore = true;

    const observer = new IntersectionObserver(async (entries) => {
        if (entries[0].isIntersecting && !isLoading && hasMore) {
            isLoading = true;
            page++;
            document.getElementById('loading-spinner').classList.remove('d-none');

            const urlParams = new URLSearchParams(window.location.search);
            urlParams.set('page', page);

            try {
                const res = await fetch(`/feed/fragments?${urlParams.toString()}`);
                const html = await res.text();

                if (!html.trim()) {
                    hasMore = false;
                } else {
                    document.getElementById('report-feed-container').insertAdjacentHTML('beforeend', html);
                }
            } catch (e) {
                console.error("Scroll load failed", e);
            } finally {
                isLoading = false;
                document.getElementById('loading-spinner').classList.add('d-none');
            }
        }
    }, { threshold: 1.0 });

    const trigger = document.getElementById('scroll-trigger');
    if (trigger) observer.observe(trigger);
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
        const statusRes = await fetch(`/api/v1/reports/${reportId}/upvotes`);
        const statusData = await statusRes.json();

        if (statusData.success) {
            const isUpvoted = statusData.data.upvotedByMe;

            if (isUpvoted) {
                const delRes = await fetch(`/api/v1/reports/${reportId}/upvotes`, { method: 'DELETE' });
                if (delRes.ok) {
                    countSpan.textContent = Math.max(0, parseInt(countSpan.textContent) - 1);
                    button.classList.remove('text-primary');
                }
            } else {
                const addRes = await fetch(`/api/v1/reports/${reportId}/upvotes`, { method: 'POST' });
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