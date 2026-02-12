document.addEventListener('DOMContentLoaded', async () => {
    const passwordInput = document.getElementById('accountPassword');
    const toggleButton = document.getElementById('togglePassword');
    const usernameValue = document.getElementById('accountUsername');
    const userIdValue = document.getElementById('accountUserId');
    const headerUsername = document.querySelector('.header-container__controls-text');

    const changeUsernameButton = document.getElementById('changeUsernameButton');
    const changePasswordButton = document.getElementById('changePasswordButton');

    const modal = document.getElementById('accountEditModal');
    const modalOverlay = document.getElementById('accountModalOverlay');
    const modalClose = document.getElementById('accountModalClose');
    const modalCancel = document.getElementById('accountModalCancel');
    const modalTitle = document.getElementById('accountModalTitle');
    const modalLabel = document.getElementById('accountModalLabel');
    const modalInput = document.getElementById('accountModalInput');
    const modalError = document.getElementById('accountModalError');
    const modalForm = document.getElementById('accountEditForm');

    if (!passwordInput || !toggleButton || !usernameValue || !userIdValue || !modal || !modalForm) {
        return;
    }

    let currentUser = null;
    let currentField = null;

    async function loadCurrentUser() {
        const response = await fetch('/api/users/me');
        if (!response.ok) {
            throw new Error('Failed to load user');
        }

        const user = await response.json();
        currentUser = user;
        usernameValue.textContent = user.username || '—';
        userIdValue.textContent = `ID: ${user.id ?? '—'}`;
        passwordInput.value = user.password || '';

        if (headerUsername) {
            headerUsername.textContent = user.username || '';
        }
    }

    function openModal(field) {
        currentField = field;
        modalError.textContent = '';
        modalInput.value = '';

        if (field === 'username') {
            modalTitle.textContent = 'Change username';
            modalLabel.textContent = 'Username';
            modalInput.type = 'text';
            modalInput.placeholder = 'Enter new username';
            modalInput.minLength = 4;
            modalInput.maxLength = 16;
        } else {
            modalTitle.textContent = 'Change password';
            modalLabel.textContent = 'Password';
            modalInput.type = 'password';
            modalInput.placeholder = 'Enter new password';
            modalInput.minLength = 4;
            modalInput.maxLength = 255;
        }

        modal.classList.add('modal--visible');
        document.body.classList.add('modal-open');
        setTimeout(() => modalInput.focus(), 0);
    }

    function closeModal() {
        modal.classList.remove('modal--visible');
        document.body.classList.remove('modal-open');
        currentField = null;
    }

    function validateValue(value) {
        if (!value) {
            return 'Поле не должно быть пустым.';
        }

        if (currentField === 'username') {
            if (value.length < 4 || value.length > 16) {
                return 'Ник должен быть от 4 до 16 символов.';
            }
        }

        if (currentField === 'password') {
            if (value.length < 4) {
                return 'Пароль должен быть не короче 4 символов.';
            }
        }

        return '';
    }

    try {
        await loadCurrentUser();
    } catch (error) {
        console.error('Failed to load user data:', error);
        window.location.href = '/login';
    }

    toggleButton.addEventListener('click', () => {
        const isHidden = passwordInput.type === 'password';
        passwordInput.type = isHidden ? 'text' : 'password';
        toggleButton.classList.toggle('account-detail__toggle--active', isHidden);
        toggleButton.setAttribute('aria-label', isHidden ? 'Hide password' : 'Show password');
    });

    if (changeUsernameButton) {
        changeUsernameButton.addEventListener('click', () => openModal('username'));
    }

    if (changePasswordButton) {
        changePasswordButton.addEventListener('click', () => openModal('password'));
    }

    modalOverlay.addEventListener('click', closeModal);
    modalClose.addEventListener('click', closeModal);
    modalCancel.addEventListener('click', closeModal);

    modalForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const value = modalInput.value.trim();
        const error = validateValue(value);

        if (error) {
            modalError.textContent = error;
            return;
        }

        if (!currentUser || !currentField) {
            modalError.textContent = 'Данные пользователя ещё не загружены. Попробуйте снова.';
            return;
        }

        const payload = currentField === 'username'
            ? { username: value }
            : { password: value };

        try {
            const response = await fetch(`/api/users/${currentUser.id}`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const errorPayload = await response.json().catch(() => ({}));
                throw new Error(errorPayload.error || 'Не удалось обновить данные');
            }

            closeModal();
            await loadCurrentUser();
        } catch (error) {
            console.error('Update failed:', error);
            modalError.textContent = error.message || 'Не удалось обновить данные. Попробуйте снова.';
        }
    });
});
