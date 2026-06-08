document.addEventListener("DOMContentLoaded", function () {

    const modalEditRoom = document.getElementById('modalEditRoom');
    if (modalEditRoom) {
        modalEditRoom.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const building = button.getAttribute('data-building');
            const name = button.getAttribute('data-name');
            const floor = button.getAttribute('data-floor');
            const code = button.getAttribute('data-code');

            modalEditRoom.querySelector('#editRoomId').value = id;
            modalEditRoom.querySelector('#editRoomBuilding').value = building;
            modalEditRoom.querySelector('#editRoomName').value = name;
            modalEditRoom.querySelector('#editRoomFloor').value = floor;
            modalEditRoom.querySelector('#editRoomCode').value = code;
        })
    }

    const modalDeleteRoom = document.getElementById('modalDeleteRoom');
    if (modalDeleteRoom) {
        modalDeleteRoom.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const name = button.getAttribute('data-name');

            modalDeleteRoom.querySelector('#deleteRoomId').value = id;
            modalDeleteRoom.querySelector('#deleteRoomNameDisplay').textContent = name;
        });
    }

    const formAddRoom = document.getElementById('formAddRoom');
    if (formAddRoom) {
        formAddRoom.addEventListener('submit', async function (e) {
            e.preventDefault();

            const buildingId = this.querySelector('[name="buildingId"]').value;
            const name = this.querySelector('[name="name"]').value;
            const code = this.querySelector('[name="code"]').value;
            const floor = parseInt(this.querySelector('[name="floor"]').value);
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Menyimpan...`
            btnSubmit.disabled = true;

            try {
                const response = await apiClient.post('/api/v1/rooms', { buildingId, name, code, floor });
                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    alert('Gagal menambah ruangan: ' + (result.message || 'Unknown error'));
                    btnSubmit.innerHTML = 'Simpan Ruangan';
                    btnSubmit.disabled = false;
                }

            } catch (error) {
                alert('Terjadi kesalahan jaringan');
                btnSubmit.innerHTML = 'Simpan Ruangan';
                btnSubmit.disabled = false
            }
        })
    }

    const formEditRoom = document.getElementById('formEditRoom');
    if (formEditRoom) {
        formEditRoom.addEventListener('submit', async function (e) {
            e.preventDefault();

            const id = document.getElementById('editRoomId').value;
            const buildingId = document.getElementById('editRoomBuilding').value;
            const name = document.getElementById('editRoomName').value;
            const code = document.getElementById('editRoomCode').value;
            const floor = parseInt(document.getElementById('editRoomFloor').value);
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Menyimpan...`
            btnSubmit.disabled = true;

            try {
                const response = await apiClient.put(`/api/v1/rooms/${id}`, { buildingId, name, code, floor });
                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    alert('Gagal mengedit ruangan: ' + result.message || 'Unknown error');
                    btnSubmit.innerHTML = 'Simpan Perubahan';
                    btnSubmit.disabled = false;
                }
            } catch (error) {
                console.error(error);
                alert('Terjadi kesalahan jaringan');
                btnSubmit.innerHTML = 'Simpan Perubahan';
                btnSubmit.disabled = false;
            }
        })
    }

    const formDeleteRoom = document.getElementById('formDeleteRoom');
    if (formDeleteRoom) {
        formDeleteRoom.addEventListener('submit', async function (e) {
            e.preventDefault();

            const id = document.getElementById('deleteRoomId').value;
            const btnSubmit = this.querySelector('button[type="submit"]');

            btnSubmit.innerHTML = `<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Menghapus...`;
            btnSubmit.disabled = true;

            try {
                const response = await apiClient.delete(`/api/v1/rooms/${id}`);
                const result = await response.json();

                if (response.ok && result.success) {
                    window.location.reload();
                } else {
                    alert('Gagal menghapus ruangan: ' + (result.message || 'Unknown error'));
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
})