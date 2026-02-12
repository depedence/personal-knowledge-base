const STORAGE_KEY = 'pkb-theme';

function getStoredTheme() {
    try {
        return localStorage.getItem(STORAGE_KEY);
    } catch (error) {
        return null;
    }
}

function storeTheme(theme) {
    try {
        localStorage.setItem(STORAGE_KEY, theme);
    } catch (error) {
        // ignore storage errors
    }
}

function updateToggleButtons(theme) {
    const buttons = document.querySelectorAll('[data-theme-toggle]');
    const isDark = theme === 'dark';
    const icon = isDark ? '☀' : '☾';
    const label = isDark ? 'Переключить на светлую тему' : 'Переключить на тёмную тему';

    buttons.forEach((button) => {
        const iconEl = button.querySelector('.theme-toggle__icon');
        if (iconEl) {
            iconEl.textContent = icon;
        } else {
            button.textContent = icon;
        }
        button.setAttribute('aria-label', label);
    });
}

function applyTheme(theme) {
    const normalized = theme === 'light' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', normalized);
    updateToggleButtons(normalized);
}

function initThemeToggle() {
    const saved = getStoredTheme();
    const initialTheme = saved || 'dark';
    applyTheme(initialTheme);

    document.addEventListener('click', (event) => {
        const button = event.target.closest('[data-theme-toggle]');
        if (!button) {
            return;
        }
        const currentTheme = document.documentElement.getAttribute('data-theme') || 'dark';
        const nextTheme = currentTheme === 'dark' ? 'light' : 'dark';
        applyTheme(nextTheme);
        storeTheme(nextTheme);
    });
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initThemeToggle);
} else {
    initThemeToggle();
}
