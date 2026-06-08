document.addEventListener("DOMContentLoaded", function() {

    const modalEditCategory = document.getElementById('modalEditCategory');
    if (modalEditCategory) {
        modalEditCategory.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const name = button.getAttribute('data-name');
            const desc = button.getAttribute('data-desc');

            modalEditCategory.querySelector('#editCategoryId').value = id;
            modalEditCategory.querySelector('#editCategoryName').value = name;
            modalEditCategory.querySelector('#editCategoryDesc').value = desc;
        })
    }

    const modalDeleteCategory = document.getElementById('modalDeleteCategory');
    if (modalDeleteCategory) {
        modalDeleteCategory.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const name = button.getAttribute('data-name');

            modalDeleteCategory.querySelector('#deleteCategoryId').value = id;
            modalDeleteCategory.querySelector('#deleteCategoryNameDisplay').textContent = name;
        });
    }

    const formAddCategory = document.getElementById('formAddCategory');
    if (formAddCategory) {
        formAddCategory.addEventListener('submit', async function(e) {
            e.preventDefault();

            const name = this.querySelector('[name="name"]').value;
            const description = this.querySelector('[name="description"]').value;
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class ="spinner-border spinner-border-sm" aria-hidden="true"></span> Menyimpan...`;
            btnSubmit.disabled = true;

            try {
                const response = await apiClient.post('/api/v1/categories', { name, description });
                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    alert('Gagal menambah kategori: ' + (result.message || 'Unknown error'));
                    btnSubmit.innerHTML = 'Simpan Kategori';
                    btnSubmit.disabled = false;
                }
            } catch (error) {
                console.error(error);
                alert('Terjadi kesalahan jaringan');
                btnSubmit.innerHTML = 'Simpan Kategori';
                btnSubmit.disabled = false;
            }
        });
    }

    const formEditCategory = document.getElementById('formEditCategory');
    if (formEditCategory) {
        formEditCategory.addEventListener('submit', async function(e) {
            e.preventDefault();

            const id = document.getElementById('editCategoryId').value;
            const name = document.getElementById('editCategoryName').value;
            const description = document.getElementById('editCategoryDesc').value;
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class ="spinner-border spinner-border-sm" aria-hidden="true"></span> Menyimpan...`;
            btnSubmit.disabled = true;

            try {
                const response = await apiClient.put(`/api/v1/categories/${id}`, { name, description });
                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    alert('Gagal mengedit kategori: ' + (result.message || 'Unknown error'));
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

    const formDeleteCategory = document.getElementById('formDeleteCategory');
    if (formDeleteCategory) {
        formDeleteCategory.addEventListener('submit', async function(e) {
            e.preventDefault();

            const id = document.getElementById('deleteCategoryId').value;
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Menghapus...`
            btnSubmit.disabled = true;

            try {
                const response = await apiClient.delete(`/api/v1/categories/${id}`);
                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    alert('Gagal menghapus kategori: ' + (result.message || 'Unknown error'));
                    btnSubmit.innerHTML = 'Ya, Hapus';
                    btnSubmit.disabled = false;
                }
            } catch (error) {
                console.error(error);
                alert('Terjadi kesalahan jaringan');
                btnSubmit.innerHTML = 'Ya, Hapus';
                btnSubmit.disabled = false;
            }
        })
    }
});