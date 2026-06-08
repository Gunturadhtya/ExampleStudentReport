document.addEventListener("DOMContentLoaded", function() {

    const modalEditUser = document.getElementById('modalEditUser');
    if (modalEditUser) {
        modalEditUser.addEventListener('show.bs.modal', async function (event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const role = button.getAttribute('data-role');

            this.querySelector('#editUserId').value = id;
            this.querySelector('#editUserRole').value = role;
            this.querySelector('#editUserName').value = button.getAttribute('data-name');
            this.querySelector('#editUserEmail').value = button.getAttribute('data-email');

            const studentSection = this.querySelector('#studentDataSection');
            const loadingIndicator = this.querySelector('#studentDataLoading');

            if (role === 'USER' || role === 'STUDENT') {
                studentSection.classList.remove('d-none');
                loadingIndicator.classList.remove('d-none');

                ['Nim', 'Faculty', 'Major', 'Year'].forEach(f => this.querySelector(`#editUser${f}`).value = '');

                try {
                    const res = await apiClient.get(`/api/v1/users/${id}/student-data`);
                    const result = await res.json();

                    if (res.ok && result.success && result.data) {
                        this.querySelector('#editUserNim').value = result.data.nim || '';
                        this.querySelector('#editUserFaculty').value = result.data.faculty || '';
                        this.querySelector('#editUserMajor').value = result.data.major || '';
                        this.querySelector('#editUserYear').value = result.data.year || '';
                    }
                } catch (e) {
                    console.error("Gagal memuat data akademik", e);
                } finally {
                    loadingIndicator.classList.add('d-none');
                }
            } else {
                studentSection.classList.add('d-none');
            }
        });
    }

    const formEditUser = document.getElementById('formEditUser');
    if (formEditUser) {
        formEditUser.addEventListener('submit', async function(e) {
            e.preventDefault();

            const id = document.getElementById('editUserId').value;
            const role = document.getElementById('editUserRole').value;
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Menyimpan...`;
            btnSubmit.disabled = true;

            const userPayload = {
                name: document.getElementById('editUserName').value,
                email: document.getElementById('editUserEmail').value
            };

            try {
                const tasks = [apiClient.put(`/api/v1/users/${id}`, userPayload)];

                if (role === 'USER' || role === 'STUDENT') {
                    const studentPayload = {
                        nim: document.getElementById('editUserNim').value,
                        faculty: document.getElementById('editUserFaculty').value,
                        major: document.getElementById('editUserMajor').value,
                        year: parseInt(document.getElementById('editUserYear').value) || null
                    };
                    tasks.push(apiClient.patch(`/api/v1/users/${id}/student-data`, studentPayload));
                }

                const responses = await Promise.all(tasks);
                const isAllOk = responses.every(r => r.ok);

                if (isAllOk) {
                    window.location.reload();
                } else {
                    const errorBodies = await Promise.all(responses.map(r => r.json().catch(() => ({}))));
                    const errMsg = errorBodies.map(b => b.message).filter(Boolean).join(' | ');
                    throw new Error(errMsg || "Gagal mengupdate seluruh data");
                }
            } catch (error) {
                console.error(error);
                alert('Gagal: ' + error.message);
                btnSubmit.innerHTML = 'Simpan Perubahan';
                btnSubmit.disabled = false;
            }
        });
    }

    const modalUserStats = document.getElementById('modalUserStats');
    if (modalUserStats) {
        modalUserStats.addEventListener('show.bs.modal', async function(event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');

            this.querySelector('#statsUserName').textContent = button.getAttribute('data-name');
            this.querySelector('#statsContent').classList.add('d-none');
            this.querySelector('#statsLoading').classList.remove('d-none');

            try {
                const res = await apiClient.get(`/api/v1/users/${id}/stats`);
                const result = await res.json();

                if (res.ok && result.success) {
                    this.querySelector('#statTotal').textContent = result.data.reportCount;
                    this.querySelector('#statPending').textContent = result.data.pendingReport;
                    this.querySelector('#statInProgress').textContent = result.data.inProgressReport;
                    this.querySelector('#statCompleted').textContent = result.data.completedReport;
                    this.querySelector('#statRejected').textContent = result.data.rejectedReport;

                    this.querySelector('#statsContent').classList.remove('d-none');
                } else {
                    throw new Error(result.message);
                }
            } catch (e) {
                alert("Gagal memuat statistik pengguna: " + e.message);
                bootstrap.Modal.getInstance(this).hide();
            } finally {
                this.querySelector('#statsLoading').classList.add('d-none');
            }
        });
    }

    const modalDeleteUser = document.getElementById('modalDeleteUser');
    if (modalDeleteUser) {
        modalDeleteUser.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            this.querySelector('#deleteUserId').value = button.getAttribute('data-id');
            this.querySelector('#deleteUserNameDisplay').textContent = button.getAttribute('data-name');
        });
    }

    const formDeleteUser = document.getElementById('formDeleteUser');
    if (formDeleteUser) {
        formDeleteUser.addEventListener('submit', async function(e) {
            e.preventDefault();

            const id = document.getElementById('deleteUserId').value;
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Menghapus...`;
            btnSubmit.disabled = true;

            try {
                const response = await apiClient.delete(`/api/v1/users/${id}`);
                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    throw new Error(result.message || 'Data integrity terlanggar. Pengguna memiliki interaksi laporan aktif.');
                }
            } catch (error) {
                console.error(error);
                alert('Gagal: ' + error.message);
                btnSubmit.innerHTML = 'Ya, Hapus';
                btnSubmit.disabled = false;
            }
        });
    }
});