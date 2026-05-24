document.addEventListener("DOMContentLoaded", function () {

    const uploadArea = document.querySelector('.upload-area');
    const fotoInput = document.getElementById('fotoKerusakan');

    if (uploadArea && fotoInput) {
        uploadArea.addEventListener('click', function () {
            fotoInput.click();
        })
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
});