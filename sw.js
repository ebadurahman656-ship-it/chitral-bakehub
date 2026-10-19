/**
 * Chitral BakeHub — Progressive Web App Service Worker
 * Brand Tagline: Freshly prepared. Locally delivered.
 * 
 * Security Guarantee:
 * Never caches sensitive user authentication, payment processing,
 * checkout requests, or private personal telemetry.
 */

const CACHE_NAME = 'bakehub-v1.0.0';
const STATIC_ASSETS = [
  './',
  './index.html',
  './offline.html',
  './manifest.json',
  './manifest.webmanifest',
  './favicon.ico',
  './icons/icon-192.png',
  './icons/icon-512.png',
  './icons/icon-maskable-192.png',
  './icons/icon-maskable-512.png',
  './images/ic_bakehub_icon.jpg',
  './images/img_hero_banner.jpg',
  './images/img_bakery_hindukush.jpg',
  './images/img_cake_pastry.jpg'
];

// Sensitive endpoints that MUST NEVER be cached
const SENSITIVE_PATTERNS = [
  /\/api\//i,
  /\/auth/i,
  /\/token/i,
  /\/checkout/i,
  /\/payment/i,
  /\/order\/submit/i,
  /\/admin\/secure/i,
  /\/account/i
];

// Install Event — Pre-cache static shell & offline fallback
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      console.log('[BakeHub SW] Pre-caching offline shell and branding assets');
      return cache.addAll(STATIC_ASSETS);
    }).then(() => self.skipWaiting())
  );
});

// Activate Event — Invalidate older caches
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((name) => {
          if (name !== CACHE_NAME) {
            console.log('[BakeHub SW] Purging deprecated cache:', name);
            return caches.delete(name);
          }
        })
      );
    }).then(() => self.clients.claim())
  );
});

// Fetch Event — Safe Caching with Offline Fallback
self.addEventListener('fetch', (event) => {
  const request = event.request;
  const url = new URL(request.url);

  // 1. Non-GET requests (POST, PUT, DELETE) are never cached
  if (request.method !== 'GET') {
    return;
  }

  // 2. Sensitive endpoints & APIs are strictly network-only
  const isSensitive = SENSITIVE_PATTERNS.some((pattern) => pattern.test(url.pathname));
  if (isSensitive) {
    event.respondWith(fetch(request));
    return;
  }

  // 3. HTML Navigations — Network first, then cache, fallback to offline.html
  if (request.mode === 'navigate') {
    event.respondWith(
      fetch(request)
        .then((response) => {
          // If valid network response, put a copy in cache
          if (response && response.status === 200) {
            const responseClone = response.clone();
            caches.open(CACHE_NAME).then((cache) => {
              cache.put(request, responseClone);
            });
          }
          return response;
        })
        .catch(() => {
          return caches.match(request).then((cachedResponse) => {
            if (cachedResponse) {
              return cachedResponse;
            }
            return caches.match('./offline.html');
          });
        })
    );
    return;
  }

  // 4. Static Assets (Images, Icons, Fonts) — Cache-first, then network
  event.respondWith(
    caches.match(request).then((cachedResponse) => {
      if (cachedResponse) {
        // Fetch in background to revalidate cache (Stale-While-Revalidate)
        fetch(request)
          .then((networkResponse) => {
            if (networkResponse && networkResponse.status === 200) {
              caches.open(CACHE_NAME).then((cache) => {
                cache.put(request, networkResponse.clone());
              });
            }
          })
          .catch(() => { /* Offline fallback already provided */ });

        return cachedResponse;
      }

      // If not in cache, fetch from network
      return fetch(request).then((networkResponse) => {
        if (!networkResponse || networkResponse.status !== 200 || networkResponse.type !== 'basic') {
          return networkResponse;
        }

        const responseToCache = networkResponse.clone();
        caches.open(CACHE_NAME).then((cache) => {
          cache.put(request, responseToCache);
        });

        return networkResponse;
      });
    })
  );
});
