document.addEventListener('DOMContentLoaded', async () => {
    const passwordInput = document.getElementById('accountPassword');
    const toggleButton = document.getElementById('togglePassword');
    const usernameValue = document.getElementById('accountUsername');
    const userIdValue = document.getElementById('accountUserId');
    const headerUsername = document.querySelector('.header-container__controls-text');

    if (!passwordInput || !toggleButton || !usernameValue || !userIdValue) {
        return;
    }

    try {
        const response = await fetch('/api/users/me');
        if (!response.ok) {
            throw new Error('Failed to load user');
        }

        const user = await response.json();
        usernameValue.textContent = user.username || '—';
        userIdValue.textContent = `ID: ${user.id ?? '—'}`;
        passwordInput.value = user.password || '';

        if (headerUsername) {
            headerUsername.textContent = user.username || '';
        }
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
});
