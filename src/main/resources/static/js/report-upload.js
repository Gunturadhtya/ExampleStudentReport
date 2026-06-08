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
            const icon = uploadArea.querySelector('i');

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
});