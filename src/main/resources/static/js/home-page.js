let currentUser = null;
let currentNoteId = null;
let notes = [];
let selectedCategoryFilter = 'ALL';
const CATEGORY_LABELS = {
    PERSONAL: 'Personal',
    WORK: 'Work',
    NOTE: 'Note'
}

document.addEventListener('DOMContentLoaded', async () => {
    try {
        await loadCurrentUser();
        await loadNotes();
        setupEventListeners();
    } catch (error) {
        console.error('Initialization error: ', error);
        window.location.href = '/login';
    }
})

async function loadCurrentUser() {
    const response = await fetch('/api/users/me');
    if (!response.ok) throw new Error('Failed to load user');

    currentUser = await response.json();

    document.querySelector('.header-container__controls-text').textContent = currentUser.username;
}

async function loadNotes() {
    const response = await fetch('/api/notes');
    if (!response.ok) throw new Error('Failed to load notes');

    const data = await response.json();
    notes = data.notes;

    await applyFilterAndRefreshSelection();
}

async function applyFilterAndRefreshSelection() {
    const filteredNotes = getFilteredNotes();
    renderNotesList(filteredNotes);

    if (filteredNotes.length === 0) {
        showEmptyState();
        return;
    }

    const hasCurrentNoteInFiltered = filteredNotes.some(note => note.id === currentNoteId);
    const noteIdToShow = hasCurrentNoteInFiltered ? currentNoteId : filteredNotes[0].id;

    await showNote(noteIdToShow);
    setActiveNoteInSidebar(noteIdToShow);
}

function getFilteredNotes() {
    if (selectedCategoryFilter === 'ALL') {
        return notes;
    }

    return notes.filter(note => note.category === selectedCategoryFilter);
}

function renderNotesList(filteredNotes) {
    const notesList = document.getElementById('notesList');
    notesList.innerHTML = '';

    filteredNotes.forEach(note => {
        const noteItem = document.createElement('div');
        noteItem.className = 'notes-sidebar__item';
        if (note.id === currentNoteId) {
            noteItem.classList.add('notes-sidebar__item--active');
        }
        noteItem.dataset.noteId = note.id;

        noteItem.innerHTML = `
            <h4 class="notes-sidebar__item-title">${escapeHtml(note.title)}</h4>
            <span class="notes-sidebar__item-category ${getSidebarCategoryClass(note.category)}">${formatCategory(note.category)}</span>
            <span class="notes-sidebar__item-date">${formatDate(note.creationDate)}</span>
        `;

        noteItem.addEventListener('click', () => {
            setActiveNoteInSidebar(note.id);
            showNote(note.id);
        });

        notesList.appendChild(noteItem);
    });
}

function setActiveNoteInSidebar(noteId) {
    document.querySelectorAll('.notes-sidebar__item').forEach(item => {
        item.classList.toggle('notes-sidebar__item--active', item.dataset.noteId === String(noteId));
    });
}

async function showNote(noteId) {
    const response = await fetch(`/api/notes/${noteId}`);
    if (!response.ok) throw new Error('Failed to load note');

    const note = await response.json();
    currentNoteId = note.id;

    document.getElementById('noteEmpty').style.display = 'none';
    document.getElementById('noteEdit').style.display = 'none';
    document.getElementById('noteView').style.display = 'block';

    document.getElementById('noteTitle').textContent = note.title;
    const noteCategoryElement = document.getElementById('noteCategory');
    noteCategoryElement.textContent = formatCategory(note.category);
    noteCategoryElement.className = `notes-content__category ${getContentCategoryClass(note.category)}`;
    document.getElementById('noteBody').innerHTML = `<p>${escapeHtml(note.content).replace(/\n/g, '<br>')}</p>`;
    document.getElementById('noteCreated').textContent = `Created: ${formatDate(note.creationDate)}`;
    document.getElementById('noteUpdated').textContent = `Updated: ${formatDate(note.creationDate)}`;
}

function showEmptyState() {
    document.getElementById('noteView').style.display = 'none';
    document.getElementById('noteEdit').style.display = 'none';
    document.getElementById('noteEmpty').style.display = 'block';
    currentNoteId = null;
}

function setupEventListeners() {
    document.getElementById('newNoteButton').addEventListener('click', () => {
        openCreateModal();
    });

    document.getElementById('editNoteButton').addEventListener('click', () => {
        enterEditMode();
    });

    document.getElementById('deleteNoteButton').addEventListener('click', async () => {
        if (confirm('Are you sure you want to delete this note?')) {
            await deleteNote(currentNoteId);
        }
    });

    document.getElementById('cancelEditButton').addEventListener('click', () => {
        exitEditMode();
    });

    document.getElementById('noteForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        await saveEditedNote();
    });

    document.getElementById('closeModalButton').addEventListener('click', closeCreateModal);
    document.getElementById('modalOverlay').addEventListener('click', closeCreateModal);
    document.getElementById('cancelCreateButton').addEventListener('click', closeCreateModal);

    document.getElementById('createNoteForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        await createNote();
    });

    document.getElementById('notesCategoryFilter').addEventListener('change', async (e) => {
        selectedCategoryFilter = e.target.value;
        await applyFilterAndRefreshSelection();
    });
}

function openCreateModal() {
    const modal = document.getElementById('createNoteModal');
    modal.classList.add('modal--visible');
    document.body.classList.add('modal-open');

    document.getElementById('createNoteTitleInput').value = '';
    document.getElementById('createNoteContentInput').value = '';
    document.getElementById('createNoteCategoryInput').value = 'PERSONAL';

    setTimeout(() => {
        document.getElementById('createNoteTitleInput').focus();
    }, 100);
}

function closeCreateModal() {
    const modal = document.getElementById('createNoteModal');
    modal.classList.remove('modal--visible');
    document.body.classList.remove('modal-open');
}

async function createNote() {
    const title = document.getElementById('createNoteTitleInput').value;
    const content = document.getElementById('createNoteContentInput').value;
    const category = document.getElementById('createNoteCategoryInput').value;

    const response = await fetch('/api/notes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            title,
            content,
            category,
            userId: currentUser.id
        })
    });

    if (!response.ok) {
        alert('Failed to create note');
        return;
    }

    closeCreateModal();
    await loadNotes();
}

function enterEditMode() {
    const note = notes.find(n => n.id === currentNoteId);
    if (!note) return;

    document.getElementById('noteView').style.display = 'none';
    document.getElementById('noteEdit').style.display = 'block';

    document.getElementById('noteTitleInput').value = note.title;
    document.getElementById('noteContentInput').value = note.content;
    document.getElementById('noteCategoryInput').value = note.category;
}

function exitEditMode() {
    showNote(currentNoteId);
}

async function saveEditedNote() {
    const title = document.getElementById('noteTitleInput').value;
    const content = document.getElementById('noteContentInput').value;
    const category = document.getElementById('noteCategoryInput').value;

    const response = await fetch(`/api/notes/${currentNoteId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            title,
            content,
            category,
            userId: currentUser.id
        })
    });

    if (!response.ok) {
        alert('Failed to save note');
        return;
    }

    await loadNotes();
    await showNote(currentNoteId);
}

async function deleteNote(noteId) {
    const response = await fetch(`/api/notes/${noteId}`, {
        method: 'DELETE'
    });

    if (!response.ok) {
        alert('Failed to delete note');
        return;
    }

    await loadNotes();
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}.${month}.${year}`;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function formatCategory(category) {
    return CATEGORY_LABELS[category] || category;
}

function getSidebarCategoryClass(category) {
    return {
        WORK: 'notes-sidebar__item-category--work',
        PERSONAL: 'notes-sidebar__item-category--personal',
        NOTE: 'notes-sidebar__item-category--note'
    }[category] || '';
}

function getContentCategoryClass(category) {
    return {
        WORK: 'notes-content__category--work',
        PERSONAL: 'notes-content__category--personal',
        NOTE: 'notes-content__category--note'
    }[category] || '';
}
