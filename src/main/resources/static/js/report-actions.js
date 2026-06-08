document.addEventListener("DOMContentLoaded", function () {

    const formSubmitReport = document.getElementById('formSubmitReport');
    if (formSubmitReport) {
        const fileInput = document.getElementById('reportImages');
        formSubmitReport.addEventListener('submit', async function (e) {
            e.preventDefault();

            const submitBtn = this.querySelector('button[type="submit"]');
            const originalBtnHtml = submitBtn.innerHTML;
            submitBtn.disabled = true;
            submitBtn.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Memproses...`;

            try {
                const reportData = {
                    title: document.getElementById('title').value,
                    description: document.getElementById('description').value,
                    categoryId: document.getElementById('categoryId').value,
                    roomId: this.querySelector('select[name="roomId"]').value
                };

                const reportRes = await apiClient.post('/api/v1/reports', reportData);
                const result = await reportRes.json();

                if (!reportRes.ok || !result.success) {
                    throw new Error(result.message || "Unknown error");
                }

                if (fileInput && fileInput.files.length > 0) {
                    const imgFormData = new FormData();
                    Array.from(fileInput.files).forEach(file => {
                        imgFormData.append('images', file);
                    });
                    await apiClient.post(`/api/v1/reports/${result.data.id}/images`, imgFormData);
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
                const response = await apiClient.patch(`/api/v1/reports/${reportId}/status`, { status: status, notes: null });
                const result = await response.json();
                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    throw new Error(result.message || 'Unknown error');
                }
            } catch (error) {
                alert("Gagal memperbarui status: " + error.message);
                submitBtn.disabled = false;
                submitBtn.innerHTML = originalBtnHtml;
            }
        });
    }

    const deleteImageBtns = document.querySelectorAll('.btn-delete-image');
    if (deleteImageBtns.length > 0) {
        deleteImageBtns.forEach(btn => {
            btn.addEventListener('click', async function() {
                if (!confirm("Apakah Anda yakin ingin menghapus foto ini?")) return;
                const reportId = this.getAttribute('data-report-id');
                const imageId = this.getAttribute('data-image-id');
                this.disabled = true;
                this.innerHTML = `<span class="spinner-border spinner-border-sm"></span>`;

                try {
                    const response = await apiClient.delete(`/api/v1/reports/${reportId}/images/${imageId}`);
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        const result = await response.json();
                        throw new Error(result.message);
                    }
                } catch (e) {
                    alert("Gagal menghapus gambar: " + e.message);
                    this.disabled = false;
                    this.innerHTML = `<i class="bi bi-trash"></i>`;
                }
            });
        });
    }

    const btnConfirmDeleteReport = document.getElementById('btnConfirmDeleteReport');
    if (btnConfirmDeleteReport) {
        btnConfirmDeleteReport.addEventListener('click', async function() {
            const reportId = this.getAttribute('data-report-id');
            this.disabled = true;
            this.innerHTML = `<span class="spinner-border spinner-border-sm"></span> Menghapus...`;

            try {
                const response = await apiClient.delete(`/api/v1/reports/${reportId}`);
                if (response.ok) {
                    window.location.replace('/feed');
                } else {
                    const result = await response.json();
                    throw new Error(result.message);
                }
            } catch (e) {
                alert("Gagal menghapus laporan: " + e.message);
                this.disabled = false;
                this.innerHTML = `Ya, Hapus`;
            }
        });
    }

    const formEditReport = document.getElementById('formEditReport');
    if (formEditReport) {
        formEditReport.addEventListener('submit', async function(e) {
            e.preventDefault();
            const reportId = this.getAttribute('data-report-id');
            const submitBtn = this.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerHTML;

            submitBtn.disabled = true;
            submitBtn.innerHTML = `<span class="spinner-border spinner-border-sm"></span> Menyimpan...`;

            try {
                const payload = {
                    title: document.getElementById('editReportTitle').value,
                    categoryId: document.getElementById('editReportCategory').value,
                    roomId: document.getElementById('editReportRoom').value,
                    description: document.getElementById('editReportDesc').value
                };

                const response = await apiClient.put(`/api/v1/reports/${reportId}`, payload);
                if (!response.ok) {
                    const result = await response.json();
                    throw new Error(result.message || "Gagal menyimpan teks laporan");
                }

                const fileInput = document.getElementById('editReportImages');
                if (fileInput && fileInput.files.length > 0) {
                    const imgFormData = new FormData();
                    Array.from(fileInput.files).forEach(file => {
                        imgFormData.append('images', file);
                    });

                    const imgResponse = await apiClient.post(`/api/v1/reports/${reportId}/images`, imgFormData);
                    if (!imgResponse.ok) {
                        const imgResult = await imgResponse.json();
                        throw new Error("Teks tersimpan, tapi gagal upload foto: " + (imgResult.message || "Unknown error"));
                    }
                }

                window.location.reload();

            } catch (e) {
                alert(e.message);
                submitBtn.disabled = false;
                submitBtn.innerHTML = originalText;
            }
        });
    }
});