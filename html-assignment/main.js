/* ============================================================
   main.js — JavaScript Exercises 1–14
   Project: CityConnect — Local Community Event Portal
   ============================================================ */


/* ============================================================
   EXERCISE 1: JavaScript Basics & Setup
   • console.log on script load
   • alert (once per session) when page fully loaded via window load
   ============================================================ */
console.log("Welcome to the Community Portal");

window.addEventListener('load', () => {
  /* Alert on first visit this session only — avoids repeating on reload */
  if (!sessionStorage.getItem('portalAlertShown')) {
    alert('Welcome! The CityConnect Community Portal has fully loaded.');
    sessionStorage.setItem('portalAlertShown', 'true');
  }
  debugLog('Ex1: window "load" fired — page fully loaded, alert shown once per session', 'ok');
});


/* ============================================================
   EXERCISE 2: Syntax, Data Types, and Operators
   • const for event name and date, let for seat count
   • Template literal to concatenate event info
   • ++ / -- to manage seat count on registration
   ============================================================ */
const eventName = "Summer Fest 2025";   // const — never reassigned
const eventDate = "12 Jul 2025";        // const — never reassigned
let availableSeats = 50;                // let   — changes on register/cancel

const eventInfo = `Event: ${eventName} | Date: ${eventDate} | Seats available: ${availableSeats}`;
console.log(eventInfo);

function updateSeatDisplay() {
  const el = document.getElementById('ex2-seats');
  if (el) el.textContent = `Seats available: ${availableSeats}`;
}

function incrementSeat() {
  availableSeats++;  // ++ operator
  updateSeatDisplay();
  debugLog(`Ex2: ++ → availableSeats = ${availableSeats}`, 'ok');
}

function decrementSeat() {
  if (availableSeats > 0) availableSeats--;  // -- operator
  updateSeatDisplay();
  debugLog(`Ex2: -- → availableSeats = ${availableSeats}`, availableSeats === 0 ? 'warn' : 'ok');
}


/* ============================================================
   EXERCISE 3: Conditionals, Loops, and Error Handling
   • if-else to hide past or full events
   • forEach() to loop and display each event
   • try-catch around registration logic
   ============================================================ */
const allEvents = [
  { name: 'Summer Fest 2025',       date: '2025-07-12', seats: 50,  category: 'music',  past: false },
  { name: 'Tech Talks @ Town Hall', date: '2025-07-19', seats: 30,  category: 'tech',   past: false },
  { name: 'Kids Art Workshop',      date: '2025-07-26', seats: 0,   category: 'art',    past: false },
  { name: 'Heritage Night Market',  date: '2025-06-01', seats: 100, category: 'food',   past: true  },
  { name: 'Community Sports Day',   date: '2025-08-05', seats: 20,  category: 'sports', past: false },
];

function renderEx3Events() {
  const container = document.getElementById('ex3-list');
  if (!container) return;
  container.innerHTML = '';

  /* forEach() loop — Exercise 3 */
  allEvents.forEach(ev => {
    const li = document.createElement('li');

    /* if-else conditional — Exercise 3 */
    if (ev.past) {
      li.className   = 'ex3-item ex3-past';
      li.textContent = `❌ ${ev.name} — Past event (hidden from public listing)`;
    } else if (ev.seats === 0) {
      li.className   = 'ex3-item ex3-full';
      li.textContent = `⚠️ ${ev.name} — Fully booked`;
    } else {
      li.className   = 'ex3-item ex3-active';
      li.textContent = `✅ ${ev.name} — ${ev.seats} seats available`;
    }

    container.appendChild(li);
  });
}

/* try-catch error handling — Exercise 3 */
function registerWithErrorHandling(ev, userName) {
  try {
    if (!userName || userName.trim() === '')
      throw new Error('Username cannot be empty');
    if (ev.past)
      throw new Error(`"${ev.name}" is a past event`);
    if (ev.seats === 0)
      throw new Error(`"${ev.name}" is fully booked`);

    ev.seats--;
    return { success: true, msg: `Registered ${userName} for "${ev.name}" (${ev.seats} seats left)` };
  } catch (err) {
    debugLog(`Ex3 catch: ${err.message}`, 'err');
    return { success: false, msg: `Error: ${err.message}` };
  }
}


/* ============================================================
   EXERCISE 4: Functions, Scope, Closures, Higher-Order Functions
   • addEvent(), registerUser(), filterEventsByCategory()
   • Closure: createCategoryTracker() keeps private counts object
   • Callbacks passed to filterEventsByCategory()
   ============================================================ */

/* Closure — counts is private, inaccessible from outside */
function createCategoryTracker() {
  const counts = {};  // private via closure scope
  return {
    register(category) { counts[category] = (counts[category] || 0) + 1; },
    getCount(category) { return counts[category] || 0; },
    getAll()           { return { ...counts }; }
  };
}
const categoryTracker = createCategoryTracker();

function addEvent(name, date, category, seats = 20) {
  const ev = { name, date, seats: Number(seats), category, past: false };
  allEvents.push(ev);
  debugLog(`Ex4: addEvent() → "${name}" [${category}] added to allEvents`, 'ok');
  /* Refresh dependent views */
  renderEx3Events();
  renderDynamicEvents();
  renderEx6();
}

function registerUser(userName, eventName) {
  const ev = allEvents.find(e => e.name === eventName);
  if (!ev) { debugLog(`Ex4: registerUser() — event not found: "${eventName}"`, 'err'); return; }
  const result = registerWithErrorHandling(ev, userName);
  if (result.success) {
    categoryTracker.register(ev.category);
    updateCategoryStats();
    renderEx3Events();
    renderDynamicEvents();
  }
  return result.msg;
}

/* Higher-order function — callback receives the filtered array */
function filterEventsByCategory(category, callback) {
  const filtered = allEvents.filter(ev => ev.category === category && !ev.past);
  return callback(filtered);
}

function updateCategoryStats() {
  const container = document.getElementById('ex4-stats');
  if (!container) return;
  const stats = categoryTracker.getAll();
  if (!Object.keys(stats).length) return;
  container.innerHTML = Object.entries(stats)
    .map(([cat, n]) => `<span class="stat-badge">${cat}: ${n} reg${n > 1 ? 's' : ''}</span>`)
    .join('');
}


/* ============================================================
   EXERCISE 5: Objects and Prototypes
   • Event constructor function
   • checkAvailability() added to prototype
   • Object.entries() to list all key-value pairs
   ============================================================ */
function EventObj(name, date, category, seats, location) {
  this.name     = name;
  this.date     = date;
  this.category = category;
  this.seats    = seats;
  this.location = location;
}

/* Added to prototype — shared across all instances */
EventObj.prototype.checkAvailability = function () {
  if (this.seats > 10) return `✅ Available — ${this.seats} seats left`;
  if (this.seats > 0)  return `⚠️ Almost full — only ${this.seats} left`;
  return '❌ Fully booked';
};

EventObj.prototype.toString = function () {
  return `${this.name} (${this.category}) on ${this.date} @ ${this.location}`;
};

const protoEvent = new EventObj('Summer Fest 2025', '12 Jul 2025', 'music', 50, 'City Park');

function renderEx5() {
  const container = document.getElementById('ex5-output');
  if (!container) return;

  /* Object.entries() — Exercise 5 */
  const entries = Object.entries(protoEvent);

  container.innerHTML = `
    <p><strong>Instance:</strong> <code>${protoEvent}</code></p>
    <p style="margin:8px 0"><strong>checkAvailability():</strong> ${protoEvent.checkAvailability()}</p>
    <p><strong>Object.entries():</strong></p>
    <ul class="code-list">
      ${entries.map(([k, v]) => `<li><code>${k}</code>: <em>${v}</em></li>`).join('')}
    </ul>`;
}


/* ============================================================
   EXERCISE 6: Arrays and Methods
   • .push() to add a new event
   • .filter() to show only music events
   • .map() to format display cards
   ============================================================ */
function renderEx6() {
  const container = document.getElementById('ex6-output');
  if (!container) return;

  /* Spread clone so original allEvents is never mutated here */
  const snapshot = [...allEvents];

  /* .push() */
  snapshot.push({ name: 'Workshop on Baking', date: '2025-08-10', seats: 15, category: 'food', past: false });

  /* .filter() — music only */
  const musicOnly = snapshot.filter(e => e.category === 'music');

  /* .map() — formatted display cards */
  const cards = snapshot.map(e => {
    const cap = e.category.charAt(0).toUpperCase() + e.category.slice(1);
    return `${cap} Event: ${e.name}`;
  });

  container.innerHTML = `
    <p><strong>.push():</strong> snapshot has ${snapshot.length} events (added "Workshop on Baking")</p>
    <p style="margin-top:6px"><strong>.filter() — music only:</strong> ${musicOnly.map(e => e.name).join(', ') || 'none'}</p>
    <p style="margin-top:6px"><strong>.map() — formatted cards (first 4):</strong></p>
    <ul class="code-list">${cards.slice(0, 4).map(c => `<li>${c}</li>`).join('')}</ul>`;
}


/* ============================================================
   EXERCISE 7: DOM Manipulation
   • querySelector() to access elements
   • createElement() + appendChild() for each card
   • Update UI when user registers or cancels
   ============================================================ */
function renderDynamicEvents(eventsToRender) {
  /* querySelector() — Exercise 7 */
  const container = document.querySelector('#dynamicEventsList');
  if (!container) return;

  const list = eventsToRender !== undefined ? eventsToRender : getCurrentFilteredEvents();
  container.innerHTML = '';

  if (!list.length) {
    container.innerHTML = '<p style="color:var(--muted); grid-column:1/-1; font-style:italic;">No events match your filter.</p>';
    return;
  }

  list.forEach(ev => {
    /* createElement() — Exercise 7 */
    const card = document.createElement('div');
    card.className = 'js-event-card';
    card.dataset.category  = ev.category;
    card.dataset.eventName = ev.name;

    card.innerHTML = `
      <span class="js-card-badge">${ev.category}</span>
      <div class="js-card-title">${ev.name}</div>
      <div class="js-card-meta">📅 ${ev.date} &nbsp;|&nbsp; 🪑 <span class="seat-count">${ev.seats}</span> seats</div>
      <div class="js-card-actions">
        <button class="btn btn-primary btn-sm js-reg-btn" data-name="${ev.name}">Register →</button>
        <button class="btn btn-sm js-cancel-btn" data-name="${ev.name}">Cancel</button>
      </div>
      <div class="js-card-status"></div>`;

    /* appendChild() — Exercise 7 */
    container.appendChild(card);
  });

  attachCardListeners();
}

function attachCardListeners() {
  /* onclick for Register buttons — Exercise 8 */
  document.querySelectorAll('.js-reg-btn').forEach(btn => {
    btn.onclick = function () {
      const name   = this.dataset.name;
      const ev     = allEvents.find(e => e.name === name);
      if (!ev) return;

      const result = registerWithErrorHandling(ev, 'Guest User');
      const card   = this.closest('.js-event-card');
      const status = card.querySelector('.js-card-status');

      /* Update UI — Exercise 7 */
      status.textContent = result.msg;
      status.style.color = result.success ? 'var(--success)' : 'var(--danger)';

      if (result.success) {
        card.querySelector('.seat-count').textContent = ev.seats;
        categoryTracker.register(ev.category);
        updateCategoryStats();
        renderEx3Events();
      }
      debugLog(`Ex7/8: onclick Register "${name}" — ${result.msg}`, result.success ? 'ok' : 'warn');
    };
  });

  /* Cancel button — removes card from DOM (Exercise 7 UI update) */
  document.querySelectorAll('.js-cancel-btn').forEach(btn => {
    btn.onclick = function () {
      const card = this.closest('.js-event-card');
      card.style.transition = 'opacity .25s, transform .25s';
      card.style.opacity    = '0';
      card.style.transform  = 'scale(0.95)';
      setTimeout(() => {
        card.remove();
        debugLog(`Ex7: Card removed for "${this.dataset.name}"`, 'warn');
      }, 250);
    };
  });
}


/* ============================================================
   EXERCISE 8: Event Handling
   • onclick — Register buttons (in attachCardListeners above)
   • onchange — filter events by category dropdown
   • keydown  — quick search by event name
   ============================================================ */
let currentSearch   = '';
let currentCategory = '';

function getCurrentFilteredEvents() {
  return allEvents.filter(ev => {
    if (ev.past) return false;
    const matchCat    = !currentCategory || ev.category === currentCategory;
    const matchSearch = !currentSearch
      || ev.name.toLowerCase().includes(currentSearch.toLowerCase());
    return matchCat && matchSearch;
  });
}

function setupEventHandlers() {
  /* onchange — category filter dropdown */
  const catFilter = document.getElementById('categoryFilter');
  if (catFilter) {
    catFilter.addEventListener('change', function () {
      currentCategory = this.value;
      renderDynamicEvents();
      debugLog(`Ex8: onchange category → "${this.value || 'all'}"`, 'ok');
    });
  }

  /* keydown — quick search by name */
  const searchInput = document.getElementById('searchEvents');
  if (searchInput) {
    searchInput.addEventListener('keydown', function (e) {
      /* setTimeout(0) reads the value after the keydown updates it */
      setTimeout(() => {
        currentSearch = this.value.trim();
        renderDynamicEvents();
      }, 0);
      if (e.key === 'Enter') {
        debugLog(`Ex8: keydown Enter — search: "${this.value}"`, 'ok');
      }
    });
  }
}


/* ============================================================
   EXERCISE 9: Async JS, Promises, Async/Await
   • fetch() from mock JSON endpoint (jsonplaceholder)
   • .then() / .catch() version
   • async/await version with loading spinner
   ============================================================ */
const MOCK_API = 'https://jsonplaceholder.typicode.com/posts?_limit=4';

/* .then() / .catch() — Exercise 9 */
function fetchWithPromise() {
  const spinner = document.getElementById('ex9-spinner');
  const output  = document.getElementById('ex9-output');
  if (!spinner || !output) return;

  spinner.style.display = 'flex';
  output.innerHTML = '';
  debugLog('Ex9 (.then): fetching mock events…', 'info');

  fetch(MOCK_API)
    .then(res => {
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      return res.json();
    })
    .then(data => {
      spinner.style.display = 'none';
      output.innerHTML = `
        <p><strong>.then()/.catch() — fetched ${data.length} items:</strong></p>
        <ul class="code-list">
          ${data.map(d => `<li>${d.title.slice(0, 65)}</li>`).join('')}
        </ul>`;
      debugLog(`Ex9 (.then): success — ${data.length} items`, 'ok');
    })
    .catch(err => {
      spinner.style.display = 'none';
      output.innerHTML = `<p style="color:var(--danger)">Fetch error: ${err.message}</p>`;
      debugLog(`Ex9 (.then) error: ${err.message}`, 'err');
    });
}

/* async/await — Exercise 9 */
async function fetchWithAsync() {
  const spinner = document.getElementById('ex9-spinner');
  const output  = document.getElementById('ex9-output');
  if (!spinner || !output) return;

  spinner.style.display = 'flex';
  output.innerHTML = '';
  debugLog('Ex9 (async/await): fetching mock events…', 'info');

  try {
    const res  = await fetch(MOCK_API);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const data = await res.json();
    spinner.style.display = 'none';
    output.innerHTML = `
      <p><strong>async/await — fetched ${data.length} items:</strong></p>
      <ul class="code-list">
        ${data.map(d => `<li>${d.title.slice(0, 65)}</li>`).join('')}
      </ul>`;
    debugLog(`Ex9 (async/await): success — ${data.length} items`, 'ok');
  } catch (err) {
    spinner.style.display = 'none';
    output.innerHTML = `<p style="color:var(--danger)">Error: ${err.message}</p>`;
    debugLog(`Ex9 (async/await) error: ${err.message}`, 'err');
  }
}


/* ============================================================
   EXERCISE 10: Modern JavaScript Features
   • let / const throughout this file
   • Default parameters in function signatures
   • Destructuring to extract event details
   • Spread operator to clone array before filtering
   ============================================================ */

/* Default parameters — Exercise 10 */
function formatEventLabel(name = 'Unnamed Event', category = 'general', seats = 0) {
  return `[${category.toUpperCase()}] ${name} — ${seats} seats`;
}

/* Destructuring — Exercise 10 */
function getEventSummary(eventObj) {
  const { name, date, category, seats, location } = eventObj;
  return `${name} | ${category} | ${date} | ${location} | ${seats} seats`;
}

/* Spread operator to clone before filtering — Exercise 10 */
function cloneAndFilter(category) {
  const cloned = [...allEvents];  // spread clone — original untouched
  return cloned.filter(e => e.category === category && !e.past);
}

function renderEx10() {
  const container = document.getElementById('ex10-output');
  if (!container) return;

  const label    = formatEventLabel('Summer Fest 2025', 'music', 50);
  const summary  = getEventSummary(protoEvent);
  const filtered = cloneAndFilter('music');

  container.innerHTML = `
    <p><strong>Default params:</strong>
      <code>formatEventLabel()</code> → <em>${label}</em></p>
    <p style="margin-top:8px"><strong>Destructuring:</strong>
      <code>const { name, date, … } = eventObj</code> → <em>${summary}</em></p>
    <p style="margin-top:8px"><strong>Spread + filter (music):</strong>
      <code>[...allEvents].filter(…)</code> → <em>${filtered.map(e => e.name).join(', ') || 'none'}</em></p>
    <p style="margin-top:8px; color:var(--muted); font-size:.88rem;">
      Original <code>allEvents</code> is unchanged — the spread created a safe copy before filtering.
    </p>`;
}


/* ============================================================
   EXERCISE 11: Working with Forms
   • form.elements to capture name, email, event
   • event.preventDefault() — handled in existing submitRegistration()
   • Validate inputs and show errors inline
   ============================================================ */
function readFormElements() {
  const form = document.getElementById('registrationForm');
  if (!form) return;

  /* form.elements — Exercise 11 */
  const name  = form.elements['name'].value.trim();
  const email = form.elements['email'].value.trim();
  const event = form.elements['event'].value;
  const date  = form.elements['date'].value;

  const errors = [];
  if (!name)                                                    errors.push('Name required');
  if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email))    errors.push('Valid email required');
  if (!event)                                                   errors.push('Event selection required');
  if (!date)                                                    errors.push('Date required');

  const out = document.getElementById('ex11-output');
  if (out) {
    out.innerHTML = errors.length
      ? `<span style="color:var(--danger)">Issues: ${errors.join(' &nbsp;|&nbsp; ')}</span>`
      : `<span style="color:var(--success)">
           ✅ name="<strong>${name}</strong>" &nbsp;|&nbsp;
           email="<strong>${email}</strong>" &nbsp;|&nbsp;
           event="<strong>${event}</strong>" &nbsp;|&nbsp;
           date="<strong>${date}</strong>"
         </span>`;
  }
  debugLog(`Ex11: form.elements — ${errors.length ? errors.join('; ') : 'all valid'}`,
    errors.length ? 'warn' : 'ok');
}


/* ============================================================
   EXERCISE 12: AJAX & Fetch API — POST
   • fetch() POST user data to mock API
   • setTimeout() simulates a delayed server response
   • Show success/failure message after submission
   ============================================================ */
async function postRegistrationAjax() {
  const btn    = document.getElementById('ex12-btn');
  const output = document.getElementById('ex12-output');
  if (!btn || !output) return;

  btn.disabled     = true;
  output.innerHTML = '<em style="color:var(--muted);">Submitting… (1.5 s simulated delay)</em>';
  debugLog('Ex12: POST registration payload to mock API…', 'info');

  /* setTimeout — simulates delayed server response (Exercise 12) */
  await new Promise(resolve => setTimeout(resolve, 1500));

  const payload = {
    name:  'Jane Doe',
    email: 'jane@example.com',
    event: 'Summer Fest 2025',
    date:  '2025-07-12'
  };

  try {
    const res = await fetch('https://jsonplaceholder.typicode.com/posts', {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      body:    JSON.stringify(payload)
    });
    if (!res.ok) throw new Error(`Server error ${res.status}`);
    const data = await res.json();
    output.innerHTML = `<span style="color:var(--success)">
      ✅ POST successful — mock server returned ID <strong>${data.id}</strong><br>
      <small>Payload sent: <code>${JSON.stringify(payload)}</code></small>
    </span>`;
    debugLog(`Ex12: POST success — returned ID ${data.id}`, 'ok');
  } catch (err) {
    output.innerHTML = `<span style="color:var(--danger)">❌ Failed: ${err.message}</span>`;
    debugLog(`Ex12: POST error — ${err.message}`, 'err');
  } finally {
    btn.disabled = false;
  }
}


/* ============================================================
   EXERCISE 13: Debugging and Testing
   • console.group / console.log / console.warn / console.error
   • Step-by-step trace of form submission pipeline
   • Commented debugger statement for breakpoint demo
   • Log fetch request payload
   ============================================================ */
function debugFormTrace() {
  const form   = document.getElementById('registrationForm');
  const output = document.getElementById('ex13-output');
  if (!form) return;

  const data = {
    name:  form.elements['name']?.value.trim()  || '',
    email: form.elements['email']?.value.trim() || '',
    event: form.elements['event']?.value        || '',
    date:  form.elements['date']?.value         || ''
  };

  /* Step-by-step console trace — open DevTools (F12) to see this */
  console.group('📋 Ex13 — Form Submission Debug Trace');
  console.log('Step 1: Form data captured via form.elements', data);
  console.log('Step 2: Validating fields…');

  const errors = [];
  if (!data.name)  errors.push('Name missing');
  if (!data.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email))
    errors.push('Email invalid');
  if (!data.event) errors.push('Event not selected');
  if (!data.date)  errors.push('Date not selected');

  if (errors.length) console.warn('Step 3: Validation FAILED →', errors);
  else               console.log('Step 3: Validation PASSED ✓');

  console.log('Step 4: Fetch payload →', JSON.stringify(data));
  // debugger;  // ← Uncomment this line, then click the button to pause here in DevTools
  console.groupEnd();

  if (output) {
    output.innerHTML = errors.length
      ? `<p style="color:var(--danger)">Validation issues: <strong>${errors.join(' | ')}</strong></p>
         <p style="font-size:.85rem; color:var(--muted);">Open DevTools Console (F12) for the full grouped trace.</p>`
      : `<p style="color:var(--success)">✅ Form valid — payload: <code>${JSON.stringify(data)}</code></p>
         <p style="font-size:.85rem; color:var(--muted);">Open DevTools Console (F12) for the full grouped trace.</p>`;
  }
  debugLog(`Ex13: trace logged to console — ${errors.length ? errors.join('; ') : 'form valid'}`,
    errors.length ? 'warn' : 'ok');
}


/* ============================================================
   EXERCISE 14: jQuery and JS Frameworks
   • $('#registerBtn').click() for click handling
   • .fadeOut() and .fadeIn() for event cards
   • Note: why React/Vue is better for larger apps
   ============================================================ */
function setupJQuery() {
  if (typeof $ === 'undefined') {
    debugLog('Ex14: jQuery not loaded — skipping', 'warn');
    return;
  }

  /* $('#registerBtn').click() — Exercise 14 */
  $('#registerBtn').on('click', function () {
    const $btn = $(this);
    debugLog('Ex14: jQuery #registerBtn click fired', 'ok');

    $btn.text('✓ Registered!').prop('disabled', true).css('background', 'var(--success)');

    /* .fadeOut() then .fadeIn() on each jQuery card — Exercise 14 */
    $('.js-event-card-jq').each(function (i) {
      const $card = $(this);
      $card.delay(i * 120).fadeOut(350, function () {
        $card.delay(200).fadeIn(500);
      });
    });

    setTimeout(() => {
      $btn.text('Register (jQuery)').prop('disabled', false).css('background', '');
    }, 2200);
  });

  /* Animate cards in on page load */
  $('.js-event-card-jq').hide().each(function (i) {
    $(this).delay(i * 180).fadeIn(500);
  });

  debugLog('Ex14: jQuery loaded — $().on("click"), .fadeIn(), .fadeOut() ready', 'ok');
}


/* ============================================================
   INIT — DOMContentLoaded
   Wire up all exercise buttons and render initial state
   ============================================================ */
document.addEventListener('DOMContentLoaded', () => {

  /* Ex 2 */
  const infoEl = document.getElementById('ex2-info');
  if (infoEl) infoEl.textContent = eventInfo;
  updateSeatDisplay();
  document.getElementById('ex2-increase')?.addEventListener('click', incrementSeat);
  document.getElementById('ex2-decrease')?.addEventListener('click', decrementSeat);

  /* Ex 3 */
  renderEx3Events();

  /* Ex 3 — register demo button (also demonstrates Ex 4 registerUser) */
  document.getElementById('ex3-reg-btn')?.addEventListener('click', () => {
    const result = registerUser('Demo User', 'Summer Fest 2025');
    const out = document.getElementById('ex3-result');
    if (out && result) out.textContent = result;
  });

  /* Ex 4 — add event form */
  document.getElementById('ex4-add-btn')?.addEventListener('click', () => {
    const name  = document.getElementById('ex4-name')?.value.trim();
    const date  = document.getElementById('ex4-date')?.value;
    const cat   = document.getElementById('ex4-cat')?.value;
    const seats = document.getElementById('ex4-seats')?.value || 20;
    if (!name || !date || !cat) { alert('Please fill in event name, date, and category.'); return; }
    addEvent(name, date, cat, seats);
    document.getElementById('ex4-name').value  = '';
    document.getElementById('ex4-date').value  = '';
    document.getElementById('ex4-seats').value = '20';
  });

  /* Ex 5, 6, 10 */
  renderEx5();
  renderEx6();
  renderEx10();

  /* Ex 7 + 8 */
  renderDynamicEvents();
  setupEventHandlers();

  /* Ex 9 */
  document.getElementById('ex9-promise-btn')?.addEventListener('click', fetchWithPromise);
  document.getElementById('ex9-async-btn')?.addEventListener('click', fetchWithAsync);

  /* Ex 11 */
  document.getElementById('ex11-btn')?.addEventListener('click', readFormElements);

  /* Ex 12 */
  document.getElementById('ex12-btn')?.addEventListener('click', postRegistrationAjax);

  /* Ex 13 */
  document.getElementById('ex13-btn')?.addEventListener('click', debugFormTrace);

  /* Ex 14 — jQuery may still be loading; fall back to window load */
  if (typeof $ !== 'undefined') {
    setupJQuery();
  } else {
    window.addEventListener('load', setupJQuery);
  }

  debugLog('main.js: DOMContentLoaded — Exercises 1–14 initialised ✓', 'ok');
});
