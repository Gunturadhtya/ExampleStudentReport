document.addEventListener("DOMContentLoaded", function () {
    const uploadArea = document.querySelector('.upload-area');
    const fileInput = document.getElementById('reportImages');
    if (uploadArea && fileInput) {
        uploadArea.addEventListener('click', function () {
            fileInput.click();
        });

        fileInput.addEventListener('change', function () {
            const h5Text = uploadArea.querySelector('h5');
            const pText = uploadArea.querySelector('p')
            const icon = uploadArea.querySelector('i')

            if (this.files && this.files.length > 0) {
                if (this.files.length > 3) {
                    icon.classList.remove('d-none');
                    h5Text.innerHTML = `<span class="text-danger fw-bold">Waduh, Kebanyakan!</span>`;
                    pText.innerHTML = `<span class="text-danger fw-bold">Maksimal 3 foto! Kamu memilih ${this.files.length} foto</span>`;
                    this.value = '';
                } else {
                    icon.classList.add('d-none');
                    let fileNames = Array.from(this.files).map(f => f.name).join(', ');
                    h5Text.innerHTML = `<span class="text-success fw-bold"><i class="bi bi-check-circle"></i> File Terpilih!</span>`;
                    pText.innerHTML = `<span class="text-success fw-bold">${this.files.length} foto siap dikirim:</span><br><small>${fileNames}</small>`;
                }
            } else {
                icon.classList.remove('d-none');
                h5Text.textContent = 'Klik untuk unggah file';
                h5Text.className = 'h6 fw-bold mb-1';
                pText.textContent = 'PNG, JPG, dan WEBP. Maks 3 foto (Max 5MB/file)';
            }
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

    const formSubmitReport = document.getElementById('formSubmitReport');
    if (formSubmitReport) {
        formSubmitReport.addEventListener('submit', async function (e) {
            e.preventDefault();

            const submitBtn = this.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Memproses...`;

            try {
                const reportData = {
                    title: document.getElementById('title').value,
                    description: document.getElementById('description').value,
                    categoryId: document.getElementById('categoryId').value,
                    roomId: this.querySelector('select[name="roomId"]').value
                };

                const reportRes = await fetch('/api/v1/reports', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(reportData)
                });

                const result = await reportRes.json();

                if (!reportRes.ok || !result.success) {
                    throw new Error(result.message || "Unknown error");
                }

                if (fileInput && fileInput.files.length > 0) {
                    const imgFormData = new FormData();
                    Array.from(fileInput.files).forEach(file => {
                        imgFormData.append('images', file);
                    });

                    await fetch(`/api/v1/reports/${result.data.id}/images`, {
                        method: 'POST',
                        body: imgFormData
                    });
                }

                window.location.replace('/feed');

            } catch(error) {
                console.error("API Error:", error);
                alert('Gagal mengirim laporan: ' + error.message);

                submitBtn.disabled = false;
                submitBtn.innerHTML = originalBtnHtml;
            }
        });
    }

    const updateStatusForm = document.getElementById('updateStatusForm');
    if (updateStatusForm) {
        updateStatusForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const reportId = this.getAttribute('data-report-id');
            const status = document.getElementById('reportStatus').value;
            const submitBtn = this.querySelector('button[type="submit"]');
            const originalBtnHtml = submitBtn.innerHTML;

            submitBtn.disabled = true;
            submitBtn.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span>`;

            try {
                const response = await fetch(`/api/v1/reports/${reportId}/status`, {
                    method: 'PATCH',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ status: status, notes: null })
                });

                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    throw new Error(result.message || 'Unknown error');
                }
            } catch (error) {
                console.error("API Error:", error);
                alert("Gagal memperbarui status: " + error.message);

                submitBtn.disabled = false;
                submitBtn.innerHTML = originalBtnHtml;
            }
        });
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