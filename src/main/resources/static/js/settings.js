document.addEventListener("DOMContentLoaded", function () {

    async function submitSettingsForm(formId, url, method, extractPayload) {
        const form = document.getElementById(formId);
        if (!form) return;

        form.addEventListener('submit', async function (e) {
            e.preventDefault();

            const btn = this.querySelector('button[type="submit"]');
            const originalText = btn.innerHTML;
            btn.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Menyimpan...`;
            btn.disabled = true;

            try {
                const response = await fetch(url, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Idempotency-Key': crypto.randomUUID()
                    },
                    body: JSON.stringify(extractPayload(this))
                });

                const result = await response.json();

                if (response.ok && result.success) {
                    alert(result.message || 'Perubahan berhasil disimpan!');
                    if (method === 'PATCH' && url.includes('password')) {
                        this.reset();
                    } else {
                        window.location.reload();
                    }
                } else {
                    alert('Gagal: ' + (result.message || 'Unknown error'));
                }
            } catch (error) {
                console.error('API Error:', error);
                alert('Terjadi kesalahan jaringan');
            } finally {
                btn.innerHTML = originalText;
                btn.disabled = false;
            }
        });
    }

    submitSettingsForm('formUpdateProfile', '/api/v1/users/me', 'PUT', (form) => ({
        name: form.querySelector('[name="name"]').value,
        email: form.querySelector('[name="email"]').value
    }));

    submitSettingsForm('formChangePassword', '/api/v1/users/me/password', 'PATCH', (form) => ({
        oldPassword: form.querySelector('[name="oldPassword"]').value,
        newPassword: form.querySelector('[name="newPassword"]').value
    }));

    submitSettingsForm('formUpdateStudentData', '/api/v1/users/me/student-data', 'PATCH', (form) => ({
        nim: form.querySelector('[name="nim"]').value,
        faculty: form.querySelector('[name="faculty"]').value,
        major: form.querySelector('[name="major"]').value,
        year: parseInt(form.querySelector('[name="year"]').value, 10)
    }));
});