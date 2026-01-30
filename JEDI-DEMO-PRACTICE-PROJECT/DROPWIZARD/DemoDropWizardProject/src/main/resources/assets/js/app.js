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
    if (state.token) { showDashboard(); } else { showLogin(); }
    if (state.token) setInterval(fetchNotifications, 10000);
});

// --- NAVIGATION & VIEWS ---

const navConfig = {
    'CUSTOMER': [
        { label: 'My Fitness Plan', id: 'bookings', view: renderCustomerBookings },
        { label: 'Browse Gyms', id: 'gyms', view: renderBrowseGyms },
        { label: 'My Waitlist', id: 'waitlist', view: renderCustomerWaitlist },
        { label: 'Payment History', id: 'payments', view: renderPaymentHistory },
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
        { label: 'Approved Owners', id: 'approved-owners', view: renderApprovedOwners },
        { label: 'Pending Centers', id: 'pending-centers', view: renderPendingCenters },
        { label: 'Approved Centers', id: 'approved-centers', view: renderApprovedCenters },
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
    descEl.innerText = `FlipFit > ${viewConfig.label}`;
    bodyEl.innerHTML = '<div class="empty-state">Loading...</div>';
    viewConfig.view(bodyEl);
}

// --- AUTH ACTIONS SAME AS BEFORE BUT ENSURE CORRECT ROLE CHOICE ---
function showLogin() {
    state.regData = {};
    state.selectedRole = null;
    hideAllStages();
    document.getElementById('login-stage').classList.add('active');
    document.getElementById('register-progress').classList.add('hidden');
    document.getElementById('auth-error').classList.add('hidden');
}

function showRegister() {
    hideAllStages();
    document.getElementById('reg-stage-1').classList.add('active');
    document.getElementById('register-progress').classList.remove('hidden');
    updateProgress(33);
}

function showForgotPassword() {
    hideAllStages();
    document.getElementById('forgot-password-stage').classList.add('active');
}

function hideAllStages() {
    document.querySelectorAll('.stage').forEach(s => s.classList.remove('active'));
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
    if (!form.checkValidity()) { form.reportValidity(); return; }
    state.regData = { ...state.regData, ...Object.fromEntries(new FormData(form)) };
    if (state.selectedRole === 'GYM_OWNER') nextStage(3); else submitRegister();
}

document.getElementById('login-form').onsubmit = async (e) => {
    e.preventDefault();
    const btn = e.target.querySelector('button');
    const data = Object.fromEntries(new FormData(e.target));
    btn.disabled = true;
    try {
        const res = await fetch(`${API_BASE}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const json = await res.json();
        if (json.success) {
            state.token = `${data.username}+admin`;
            state.user = { userId: json.userId, username: json.name || data.username, role: json.role };
            localStorage.setItem('auth_token', state.token);
            localStorage.setItem('user_info', JSON.stringify(state.user));
            showDashboard();
        } else { throw new Error(json.error || 'Login failed'); }
    } catch (err) { showAuthError(err.message); } finally { btn.disabled = false; }
};

async function submitRegister() {
    const extraData = Object.fromEntries(new FormData(document.getElementById('reg-extra-form')));
    const finalData = { ...state.regData, ...extraData };
    try {
        let endpoint = '/users/register';
        let body = {
            username: finalData.username, password: finalData.password, email: finalData.email,
            roleChoice: state.selectedRole === 'ADMIN' ? 1 : (state.selectedRole === 'CUSTOMER' ? 2 : 3)
        };
        if (state.selectedRole === 'GYM_OWNER') {
            endpoint = '/owners/onboard';
            body = { ...body, pan: finalData.pan, gst: finalData.gst, aadhar: finalData.aadhar, location: finalData.location };
        }
        const res = await fetch(API_BASE + endpoint, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
        const json = await res.json();
        if (json.success) { alert('Success! Please login.'); showLogin(); } else { throw new Error(json.error || 'Registration failed'); }
    } catch (err) { showAuthError(err.message); }
}

function showAuthError(msg) { const e = document.getElementById('auth-error'); e.innerText = msg; e.classList.remove('hidden'); }

// --- DASHBOARD CORE ---

async function apiCall(endpoint, method = 'GET', body = null) {
    const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${state.token}` };
    const opts = { method, headers };
    if (body) opts.body = JSON.stringify(body);
    const res = await fetch(API_BASE + endpoint, opts);
    const json = await res.json();
    if (!res.ok) throw new Error(json.error || 'API Error');
    return json;
}

async function fetchNotifications() {
    try {
        const res = await apiCall(`/notifications/${state.user.userId}`);
        const list = document.getElementById('notif-list');
        const notifs = res.notifications || [];
        if (notifs.length === 0) { list.innerHTML = '<div class="notif-item">No new alerts</div>'; return; }
        list.innerHTML = notifs.map(n => `<div class="notif-item"><small>${n.timestamp}</small><p>${n.message}</p></div>`).join('');
    } catch (e) { console.error(e); }
}

document.getElementById('logout-btn').onclick = () => { localStorage.clear(); window.location.reload(); };

// --- CUSTOMER VIEWS ---

async function renderCustomerBookings(container) {
    try {
        const res = await apiCall(`/customers/${state.user.userId}/plan`);
        const bookings = res.bookings || [];
        if (bookings.length === 0) { container.innerHTML = '<p>No active bookings.</p>'; return; }
        let h = `<table><thead><tr><th>ID</th><th>Slot</th><th>Status</th><th>Action</th></tr></thead><tbody>`;
        bookings.forEach(b => {
            h += `<tr><td>${b.bookingId}</td><td>${b.scheduleId}</td><td><span class="badge">${b.status}</span></td><td>
                ${b.status !== 'CANCELLED' ? `<button class="btn btn-sm btn-outline" style="color:red" onclick="cancelBooking('${b.bookingId}')">Cancel</button>` : '-'}
            </td></tr>`;
        });
        container.innerHTML = h + `</tbody></table>`;
        window.cancelBooking = async (id) => { if (confirm('Cancel workout?')) { await apiCall(`/customers/bookings/${id}`, 'DELETE'); renderCustomerBookings(container); } };
    } catch (e) { container.innerHTML = e.message; }
}

async function renderBrowseGyms(container) {
    try {
        const res = await apiCall('/centers');
        const centers = res.centers || [];
        let h = `<div class="form-grid" style="grid-template-columns: repeat(auto-fill, minmax(280px, 1fr))">`;
        centers.forEach(c => {
            h += `<div class="role-card"><h3>${c.name}</h3><p>${c.city}</p><br><button class="btn btn-primary full-width" onclick="loadSlots('${c.centerId}')">View Slots</button></div>`;
        });
        container.innerHTML = h + `</div><div id="slot-list" style="margin-top:2rem"></div>`;
        window.loadSlots = async (cid) => {
            const sRes = await apiCall(`/slots/center/${cid}`);
            const slots = sRes.slots || [];
            let st = `<h3>Slots at Center ${cid}</h3><table><thead><tr><th>Time</th><th>Price</th><th>Seats</th><th>Action</th></tr></thead><tbody>`;
            slots.forEach(s => {
                const full = s.availableSeats <= 0;
                st += `<tr><td>${s.startTime} - ${s.endTime}</td><td>₹${s.price}</td><td>${s.availableSeats}/${s.capacity}</td><td>
                    ${!full ? `<button class="btn btn-sm btn-primary" onclick="bookNow('${s.slotId}')">Book</button>` : `<button class="btn btn-sm btn-outline" onclick="waitlistNow('${s.slotId}')">Waitlist</button>`}
                </td></tr>`;
            });
            document.getElementById('slot-list').innerHTML = st + `</tbody></table>`;
        };
        window.bookNow = async (sid) => { const r = await apiCall(`/customers/${state.user.userId}/bookings`, 'POST', { slotId: sid }); alert(r.message); renderPaymentHistory(container); };
        window.waitlistNow = async (sid) => { const r = await apiCall('/bookings/waitlist', 'POST', { userId: state.user.userId, slotId: sid }); alert(r.message); };
    } catch (e) { container.innerHTML = e.message; }
}

async function renderCustomerWaitlist(container) {
    try {
        const res = await apiCall(`/bookings/waitlist/${state.user.userId}`);
        const list = res.waitlist || [];
        if (list.length === 0) { container.innerHTML = '<p>You are not on any waitlists.</p>'; return; }
        container.innerHTML = `<h3>Positions</h3><ul>` + list.map(l => `<li style="padding:1rem; border-bottom:1px solid #eee">${l}</li>`).join('') + `</ul>`;
    } catch (e) { container.innerHTML = e.message; }
}

async function renderPaymentHistory(container) {
    try {
        const res = await apiCall(`/payments/history/${state.user.userId}`);
        const history = res.history || [];
        if (history.length === 0) { container.innerHTML = '<p>No transaction history.</p>'; return; }
        let h = `<table><thead><tr><th>Txn ID</th><th>Amount</th><th>Method</th><th>Status</th><th>Date</th></tr></thead><tbody>`;
        history.forEach(p => {
            h += `<tr><td style="font-family:var(--font-mono)">${p.transactionId}</td><td>₹${p.amount}</td><td>${p.method}</td><td>${p.status}</td><td>${p.timestamp}</td></tr>`;
        });
        container.innerHTML = h + `</tbody></table>`;
    } catch (e) { container.innerHTML = e.message; }
}

function renderCustomerProfile(container) {
    container.innerHTML = `<div class="role-card" style="max-width:400px"><h3>My Details</h3><p><strong>Name:</strong> ${state.user.username}</p><p><strong>UID:</strong> ${state.user.userId}</p><p><strong>Role:</strong> ${state.user.role}</p></div>`;
}

// --- GYM OWNER VIEWS ---

async function renderOwnerCenters(container) {
    try {
        const res = await apiCall(`/owners/${state.user.userId}/centers`);
        const centers = res.centers || [];
        if (centers.length === 0) { container.innerHTML = '<p>No centers added yet.</p>'; return; }
        let h = `<table><thead><tr><th>ID</th><th>Name</th><th>City</th><th>Status</th></tr></thead><tbody>`;
        centers.forEach(c => {
            h += `<tr><td>${c.centerId}</td><td>${c.name}</td><td>${c.city}</td><td>${c.approved ? 'APPROVED' : 'PENDING'}</td></tr>`;
        });
        container.innerHTML = h + `</tbody></table>`;
    } catch (e) { container.innerHTML = e.message; }
}

function renderAddCenter(container) {
    container.innerHTML = `<h3>Onboard New Gym</h3><form id="add-center-form"><div class="input-group"><label>Center Name</label><input type="text" name="name" required></div><div class="input-group"><label>Location/City</label><input type="text" name="location" required></div><button type="submit" class="btn btn-primary">Submit for Approval</button></form>`;
    document.getElementById('add-center-form').onsubmit = async (e) => {
        e.preventDefault();
        const data = Object.fromEntries(new FormData(e.target));
        try { const r = await apiCall(`/owners/${state.user.userId}/centers`, 'POST', data); alert(r.message); renderOwnerCenters(container); } catch (err) { alert(err.message); }
    };
}

async function renderOwnerRevenue(container) {
    try {
        const res = await apiCall(`/payments/revenue/${state.user.userId}`);
        container.innerHTML = `<div class="role-card" style="text-align:center"><h2>Total Revenue Generated</h2><h1 style="font-size:4rem; margin:2rem 0">₹${res.revenue || 0}</h1><p>Calculated from all confirmed bookings across your centers.</p></div>`;
    } catch (e) { container.innerHTML = e.message; }
}

// --- ADMIN VIEWS ---

async function renderPendingOwners(container) {
    try {
        const res = await apiCall('/admin/owners/pending');
        const list = res.pendingOwners || [];
        if (list.length === 0) { container.innerHTML = 'No pending owner approvals.'; return; }
        let h = `<table><thead><tr><th>ID</th><th>Email</th><th>PAN</th><th>Action</th></tr></thead><tbody>`;
        list.forEach(o => h += `<tr><td>${o.userId}</td><td>${o.email}</td><td>${o.panNumber}</td><td><button class="btn btn-sm btn-primary" onclick="admApproveOwner('${o.userId}')">Approve</button></td></tr>`);
        container.innerHTML = h + '</tbody></table>';
        window.admApproveOwner = async (id) => { await apiCall(`/admin/owners/${id}/approve`, 'PUT'); renderPendingOwners(container); };
    } catch (e) { container.innerHTML = e.message; }
}

async function renderApprovedOwners(container) {
    try {
        const res = await apiCall('/admin/owners?approved=true');
        const list = res.owners || [];
        container.innerHTML = `<table><thead><tr><th>ID</th><th>Name</th></tr></thead><tbody>` + list.map(o => `<tr><td>${o.userId}</td><td>${o.name}</td></tr>`).join('') + `</tbody></table>`;
    } catch (e) { container.innerHTML = e.message; }
}

async function renderPendingCenters(container) {
    try {
        const res = await apiCall('/admin/centers/pending');
        const list = res.pendingCenters || [];
        if (list.length === 0) { container.innerHTML = 'No pending center approvals.'; return; }
        let h = `<table><thead><tr><th>ID</th><th>Name</th><th>Owner</th><th>Action</th></tr></thead><tbody>`;
        list.forEach(c => h += `<tr><td>${c.centerId}</td><td>${c.name}</td><td>${c.ownerId}</td><td><button class="btn btn-sm btn-primary" onclick="admApproveCenter('${c.centerId}')">Approve</button></td></tr>`);
        container.innerHTML = h + '</tbody></table>';
        window.admApproveCenter = async (id) => { await apiCall(`/admin/centers/${id}/approve`, 'PUT'); renderPendingCenters(container); };
    } catch (e) { container.innerHTML = e.message; }
}

async function renderApprovedCenters(container) {
    try {
        const res = await apiCall('/admin/centers?approved=true');
        const list = res.centers || [];
        container.innerHTML = `<table><thead><tr><th>ID</th><th>Name</th><th>City</th></tr></thead><tbody>` + list.map(c => `<tr><td>${c.centerId}</td><td>${c.name}</td><td>${c.city}</td></tr>`).join('') + `</tbody></table>`;
    } catch (e) { container.innerHTML = e.message; }
}
