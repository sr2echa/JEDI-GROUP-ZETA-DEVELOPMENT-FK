const API_BASE = '/api';

// State
let state = {
    token: localStorage.getItem('auth_token'),
    user: JSON.parse(localStorage.getItem('user_info') || '{}'),
    regPhase: 1,
    regData: {},
    selectedRole: null
};

// --- INIT ---
document.addEventListener('DOMContentLoaded', () => {
    if (state.token) {
        showDashboard();
    } else {
        showLogin();
    }

    if (state.token) setInterval(fetchNotifications, 10000);
});

// --- NAVIGATION & VIEWS ---

const navConfig = {
    'CUSTOMER': [
        { label: 'My Fitness Plan', id: 'bookings', view: renderCustomerBookings },
        { label: 'Browse Gyms', id: 'gyms', view: renderBrowseGyms },
        { label: 'My Waitlist', id: 'waitlist', view: renderCustomerWaitlist },
        { label: 'My Profile', id: 'profile', view: renderCustomerProfile }
    ],
    'GYM_OWNER': [
        { label: 'My Centers', id: 'centers', view: renderOwnerCenters },
        { label: 'Add Center', id: 'add-center', view: renderAddCenter },
        { label: 'Revenue', id: 'revenue', view: renderOwnerRevenue },
        { label: 'My Profile', id: 'profile', view: renderCustomerProfile }
    ],
    'ADMIN': [
        { label: 'Pending Owners', id: 'pending-owners', view: renderPendingOwners },
        { label: 'Pending Centers', id: 'pending-centers', view: renderPendingCenters },
        { label: 'System Logs', id: 'logs', view: renderAdminLogs },
        { label: 'My Profile', id: 'profile', view: renderCustomerProfile }
    ]
};

function showDashboard() {
    document.getElementById('auth-view').classList.add('hidden');
    document.getElementById('dashboard-view').classList.remove('hidden');

    document.getElementById('user-display').innerText = state.user.username;
    document.getElementById('role-badge').innerText = state.user.role;

    const navList = document.getElementById('nav-links');
    navList.innerHTML = '';
    const links = navConfig[state.user.role] || [];
    links.forEach((link, idx) => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.href = '#';
        a.innerText = link.label;
        a.onclick = (e) => {
            e.preventDefault();
            document.querySelectorAll('.sidebar li a').forEach(el => el.classList.remove('active'));
            a.classList.add('active');
            loadView(link);
        };
        li.appendChild(a);
        navList.appendChild(li);
        if (idx === 0) a.click();
    });
    fetchNotifications();
}

function loadView(viewConfig) {
    const titleEl = document.getElementById('page-title');
    const descEl = document.getElementById('page-desc');
    const bodyEl = document.getElementById('content-body');
    titleEl.innerText = viewConfig.label;
    descEl.innerText = `Manage your ${viewConfig.label.toLowerCase()}`;
    bodyEl.innerHTML = '<div class="empty-state">Loading...</div>';
    viewConfig.view(bodyEl);
}

// --- AUTH UI FLOWS ---

function showLogin() {
    state.regData = {};
    state.selectedRole = null;
    hideAllStages();
    document.title = "FlipFit | Login";
    document.getElementById('auth-title').innerText = "FlipFit";
    document.getElementById('auth-subtitle').innerText = "Enter your credentials to access the portal";
    document.getElementById('login-stage').classList.add('active');
    document.getElementById('register-progress').classList.add('hidden');
    document.getElementById('auth-error').classList.add('hidden');
}

function showRegister() {
    hideAllStages();
    document.title = "FlipFit | Register";
    document.getElementById('auth-title').innerText = "Register";
    document.getElementById('auth-subtitle').innerText = "Join the FlipFit community in 3 simple steps";
    document.getElementById('reg-stage-1').classList.add('active');
    document.getElementById('register-progress').classList.remove('hidden');
    updateProgress(33);
}

function showForgotPassword() {
    hideAllStages();
    document.getElementById('auth-title').innerText = "Reset Access";
    document.getElementById('auth-subtitle').innerText = "Update your password to regain access";
    document.getElementById('forgot-password-stage').classList.add('active');
    document.getElementById('register-progress').classList.add('hidden');
}

function hideAllStages() {
    document.querySelectorAll('.stage').forEach(s => s.classList.remove('active'));
    document.getElementById('auth-error').classList.add('hidden');
}

function updateProgress(percent) {
    document.getElementById('reg-progress-bar').style.width = percent + '%';
}

function nextStage(stage) {
    hideAllStages();
    document.getElementById(`reg-stage-${stage}`).classList.add('active');
    updateProgress(stage === 1 ? 33 : stage === 2 ? 66 : 100);
}

function selectRole(role) {
    state.selectedRole = role;
    document.querySelectorAll('.role-card').forEach(c => c.classList.remove('selected'));
    document.getElementById(`role-${role.toLowerCase().replace('_', '-')}`).classList.add('selected');
    document.getElementById('role-next-btn').disabled = false;
}

function validateBasicAndNext() {
    const form = document.getElementById('reg-basic-form');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }
    const data = Object.fromEntries(new FormData(form));
    state.regData = { ...state.regData, ...data };

    if (state.selectedRole === 'GYM_OWNER') {
        nextStage(3);
    } else {
        submitRegister();
    }
}

// --- AUTH ACTIONS ---

document.getElementById('login-form').onsubmit = async (e) => {
    e.preventDefault();
    const btn = e.target.querySelector('button');
    const data = Object.fromEntries(new FormData(e.target));
    btn.disabled = true;
    btn.innerText = 'Authenticating...';

    try {
        const res = await fetch(`${API_BASE}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const json = await res.json();
        if (json.success) {
            state.token = `${data.username}+admin`;
            state.user = { username: data.username, userId: json.userId, role: json.role };
            localStorage.setItem('auth_token', state.token);
            localStorage.setItem('user_info', JSON.stringify(state.user));
            showDashboard();
        } else {
            throw new Error(json.error || 'Login failed');
        }
    } catch (err) {
        showAuthError(err.message);
    } finally {
        btn.disabled = false;
        btn.innerText = 'Login';
    }
};

async function submitRegister() {
    const extraForm = document.getElementById('reg-extra-form');
    const extraData = Object.fromEntries(new FormData(extraForm));
    const finalData = { ...state.regData, ...extraData };

    try {
        let endpoint = '/users/register';
        let body = {
            username: finalData.username,
            password: finalData.password,
            email: finalData.email,
            roleChoice: state.selectedRole === 'ADMIN' ? 1 : (state.selectedRole === 'CUSTOMER' ? 2 : 3)
        };

        if (state.selectedRole === 'GYM_OWNER') {
            endpoint = '/owners/onboard';
            body = {
                username: finalData.username,
                password: finalData.password,
                pan: finalData.pan,
                gst: finalData.gst,
                aadhar: finalData.aadhar,
                location: finalData.location
            };
        }

        const res = await fetch(API_BASE + endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        const json = await res.json();
        if (json.success) {
            alert('Registration Successful! Please login.');
            showLogin();
        } else { throw new Error(json.error || 'Registration failed'); }
    } catch (err) { showAuthError(err.message); }
}

document.getElementById('forgot-password-form').onsubmit = async (e) => {
    e.preventDefault();
    const data = Object.fromEntries(new FormData(e.target));
    try {
        const res = await fetch(`${API_BASE}/users/password`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const json = await res.json();
        if (json.success) {
            alert('Password updated successfully!');
            showLogin();
        } else { throw new Error(json.error || 'Update failed'); }
    } catch (err) { showAuthError(err.message); }
};

document.getElementById('logout-btn').onclick = () => {
    localStorage.clear();
    window.location.reload();
};

function showAuthError(msg) {
    const errorEl = document.getElementById('auth-error');
    errorEl.innerText = msg;
    errorEl.classList.remove('hidden');
}

// --- DASHBOARD HELPERS ---

async function apiCall(endpoint, method = 'GET', body = null) {
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${state.token}`
    };
    const opts = { method, headers };
    if (body) opts.body = JSON.stringify(body);
    const res = await fetch(API_BASE + endpoint, opts);
    return res.json();
}

async function fetchNotifications() {
    const list = document.getElementById('notif-list');
    try {
        const res = await apiCall(`/notifications/${state.user.userId}`);
        const notifs = res.notifications || [];
        if (notifs.length === 0) { list.innerHTML = '<div class="notif-item">No new notifications</div>'; return; }
        list.innerHTML = notifs.map(n => `<div class="notif-item"><time style="font-size:0.75rem; display:block; margin-bottom:0.25rem;">${n.timestamp}</time>${n.message}</div>`).join('');
    } catch (e) { console.error("Notif error", e); }
}

document.getElementById('refresh-notifs').addEventListener('click', fetchNotifications);

// --- VIEW RENDERERS ---

function renderCustomerProfile(container) {
    container.innerHTML = `
        <div class="fade-in" style="max-width: 600px">
            <h2 style="margin-bottom: 2rem">Profile Settings</h2>
            <div class="card" style="border: 4px solid black; padding: 2rem">
                <p><strong>Username:</strong> ${state.user.username}</p>
                <p><strong>Role:</strong> ${state.user.role}</p>
                <p><strong>User ID:</strong> ${state.user.userId}</p>
            </div>
        </div>
    `;
}

async function renderCustomerBookings(container) {
    container.innerHTML = 'Loading your workouts...';
    try {
        const res = await apiCall(`/customers/${state.user.userId}/plan`);
        if (!res || res.length === 0) { container.innerHTML = '<p>No bookings found.</p>'; return; }
        let html = `<table><thead><tr><th>Booking ID</th><th>Slot</th><th>Status</th><th>Actions</th></tr></thead><tbody>`;
        res.forEach(b => {
            html += `<tr><td>${b.bookingId}</td><td>${b.scheduleId}</td><td>${b.status}</td><td>${b.status !== 'CANCELLED' ? `<button class="btn btn-sm btn-outline" style="color:red; border-color:red" onclick="cancelBooking('${b.bookingId}')">Cancel</button>` : '-'}</td></tr>`;
        });
        html += '</tbody></table>';
        container.innerHTML = html;
        window.cancelBooking = async (id) => { if (confirm('Cancel?')) { await apiCall(`/customers/bookings/${id}`, 'DELETE'); renderCustomerBookings(container); } };
    } catch (e) { container.innerHTML = e.message; }
}

async function renderBrowseGyms(container) {
    container.innerHTML = 'Fetching centers...';
    try {
        const res = await apiCall('/centers');
        const gyms = res.centers || [];
        let html = `<div style="display:grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 2rem;">`;
        gyms.forEach(g => {
            html += `<div class="role-card"><h3>${g.name}</h3><p>${g.city}</p><br><button class="btn btn-primary full-width" onclick="viewSlots('${g.centerId}')">Select Center</button></div>`;
        });
        html += '</div><div id="slots-area" style="margin-top: 4rem"></div>';
        container.innerHTML = html;
        window.viewSlots = async (cid) => {
            const list = await apiCall(`/slots/center/${cid}`);
            const slotsArea = document.getElementById('slots-area');
            let t = `<h3>Slots at Center ${cid}</h3><table><thead><tr><th>Time</th><th>Price</th><th>Seats</th><th>Action</th></tr></thead><tbody>`;
            (list.slots || []).forEach(s => {
                const isFull = s.availableSeats <= 0;
                t += `<tr><td>${s.startTime} - ${s.endTime}</td><td>$${s.price}</td><td>${s.availableSeats}/${s.capacity}</td><td>${!isFull ? `<button class="btn btn-sm btn-primary" onclick="bookSlot('${s.slotId}')">Book</button>` : `<button class="btn btn-sm btn-outline" onclick="joinWaitlist('${s.slotId}')">Waitlist</button>`}</td></tr>`;
            });
            t += '</tbody></table>';
            slotsArea.innerHTML = t;
        };
        window.bookSlot = async (id) => { const r = await apiCall(`/customers/${state.user.userId}/bookings`, 'POST', { slotId: id }); alert(r.success ? 'Success!' : r.error); };
        window.joinWaitlist = async (id) => { const r = await apiCall('/bookings/waitlist', 'POST', { userId: state.user.userId, slotId: id }); alert(r.message); };
    } catch (e) { container.innerHTML = e.message; }
}

async function renderCustomerWaitlist(container) {
    try {
        const res = await apiCall(`/bookings/waitlist/${state.user.userId}`);
        const list = res.waitlist || [];
        if (list.length === 0) { container.innerHTML = '<p>No active waitlists.</p>'; return; }
        container.innerHTML = `<h3>My Active Waitlists</h3><ul>` + list.map(i => `<li style="margin-bottom:1rem; border-bottom:1px solid #eee; padding-bottom:0.5rem">${i}</li>`).join('') + `</ul>`;
    } catch (e) { container.innerHTML = e.message; }
}

// --- ADMIN / OWNER VIEWS (Simplified) ---
async function renderPendingOwners(container) {
    const res = await apiCall('/admin/owners/pending');
    const owners = res.pendingOwners || [];
    if (owners.length === 0) { container.innerHTML = 'All clear!'; return; }
    let html = `<table><thead><tr><th>ID</th><th>PAN</th><th>Action</th></tr></thead><tbody>`;
    owners.forEach(o => html += `<tr><td>${o.userId}</td><td>${o.panNumber}</td><td><button class="btn btn-primary btn-sm" onclick="approveOwner('${o.userId}')">Approve</button></td></tr>`);
    container.innerHTML = html + `</tbody></table>`;
    window.approveOwner = async (id) => { await apiCall(`/admin/owners/${id}/approve`, 'PUT'); renderPendingOwners(container); };
}

async function renderPendingCenters(container) {
    const res = await apiCall('/admin/centers/pending');
    const centers = res.pendingCenters || [];
    if (centers.length === 0) { container.innerHTML = 'None pending.'; return; }
    let html = `<table><thead><tr><th>Name</th><th>City</th><th>Action</th></tr></thead><tbody>`;
    centers.forEach(c => html += `<tr><td>${c.name}</td><td>${c.city}</td><td><button class="btn btn-primary btn-sm" onclick="approveCenter('${c.centerId}')">Approve</button></td></tr>`);
    container.innerHTML = html + `</tbody></table>`;
    window.approveCenter = async (id) => { await apiCall(`/admin/centers/${id}/approve`, 'PUT'); renderPendingCenters(container); };
}

function renderOwnerCenters(container) { container.innerHTML = '<p>Your centers management dashboard.</p>'; }
function renderAddCenter(container) { container.innerHTML = '<p>Form to add new center business info.</p>'; }
function renderOwnerRevenue(container) { container.innerHTML = '<p>Revenue visualization coming soon.</p>'; }
function renderAdminLogs(container) { container.innerHTML = '<p>System operations log.</p>'; }
