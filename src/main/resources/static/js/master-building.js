document.addEventListener("DOMContentLoaded", function() {

    const modalEditBuilding = document.getElementById('modalEditBuilding');
    if (modalEditBuilding) {
        modalEditBuilding.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const code = button.getAttribute('data-code');
            const name = button.getAttribute('data-name');

            modalEditBuilding.querySelector('#editBuildingId').value = id;
            modalEditBuilding.querySelector('#editBuildingCode').value = code;
            modalEditBuilding.querySelector('#editBuildingName').value = name;
        });
    }

    const modalDeleteBuilding = document.getElementById('modalDeleteBuilding');
    if (modalDeleteBuilding) {
        modalDeleteBuilding.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const name = button.getAttribute('data-name');

            modalDeleteBuilding.querySelector('#deleteBuildingId').value = id;
            modalDeleteBuilding.querySelector('#deleteBuildingNameDisplay').textContent = name;
        });
    }

    const formAddBuilding = document.getElementById('formAddBuilding');
    if (formAddBuilding) {
        formAddBuilding.addEventListener('submit', async function(e) {
            e.preventDefault();

            const code = this.querySelector('[name="code"]').value;
            const name = this.querySelector('[name="name"]').value;
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Menyimpan...`;
            btnSubmit.disabled = true;

            try {
                const response = await apiClient.post('/api/v1/buildings', { code, name });
                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    alert('Gagal menambah gedung: ' + (result.message || 'Unknown error'));
                    btnSubmit.innerHTML = 'Simpan Gedung';
                    btnSubmit.disabled = false;
                }
            } catch (error) {
                console.error(error);
                alert('Terjadi kesalahan jaringan');
                btnSubmit.innerHTML = 'Simpan Gedung';
                btnSubmit.disabled = false;
            }
        });
    }

    const formEditBuilding = document.getElementById('formEditBuilding');
    if (formEditBuilding) {
        formEditBuilding.addEventListener('submit', async function(e) {
            e.preventDefault();

            const id = document.getElementById('editBuildingId').value;
            const code = document.getElementById('editBuildingCode').value;
            const name = document.getElementById('editBuildingName').value;
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Menyimpan...`;
            btnSubmit.disabled = true;

            try {
                const response = await apiClient.put(`/api/v1/buildings/${id}`, { code, name });
                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    alert('Gagal mengedit gedung: ' + (result.message || 'Unknown error'));
                    btnSubmit.innerHTML = 'Simpan Perubahan';
                    btnSubmit.disabled = false;
                }
            } catch (error) {
                console.error(error);
                alert('Terjadi kesalahan jaringan');
                btnSubmit.innerHTML = 'Simpan Perubahan';
                btnSubmit.disabled = false;
            }
        });
    }

    const formDeleteBuilding = document.getElementById('formDeleteBuilding');
    if (formDeleteBuilding) {
        formDeleteBuilding.addEventListener('submit', async function(e) {
            e.preventDefault();

            const id = document.getElementById('deleteBuildingId').value;
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Menghapus...`;
            btnSubmit.disabled = true;

            try {
                const response = await apiClient.delete(`/api/v1/buildings/${id}`);
                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    alert('Gagal menghapus gedung: ' + (result.message || 'Unknown error'));
                    btnSubmit.innerHTML = 'Ya, Hapus';
                    btnSubmit.disabled = false;
                }
            } catch (error) {
                console.error(error);
                alert('Terjadi kesalahan jaringan');
                btnSubmit.innerHTML = 'Ya, Hapus';
                btnSubmit.disabled = false;
            }
        });
    }
});