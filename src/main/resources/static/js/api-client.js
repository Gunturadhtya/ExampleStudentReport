const apiClient = {
    async request(endpoint, options = {}) {
        const headers = { ...options.headers };
        const method = (options.method || 'GET').toUpperCase();

        if (['POST', 'PUT', 'PATCH'].includes(method) && !headers['Idempotency-Key']) {
            headers['Idempotency-Key'] = crypto.randomUUID();
        }

        const isFormData = options.body instanceof FormData;
        if (!isFormData && !headers['Content-Type'] && method !== 'GET') {
            headers['Content-Type'] = 'application/json';
        }

        let body = options.body;
        if (body && !isFormData && typeof body === 'object') {
            body = JSON.stringify(body);
        }

        const fetchOptions = { ...options, method, headers, body };

        try {
            return await fetch(endpoint, fetchOptions);
        } catch (error) {
            console.error(`[API Client] ${method} ${endpoint} Failed:`, error);
            throw error;
        }
    },

    get(endpoint, options = {}) {
        return this.request(endpoint, { method: 'GET', ...options });
    },
    post(endpoint, body, options = {}) {
        return this.request(endpoint, { method: 'POST', body, ...options });
    },
    put(endpoint, body, options = {}) {
        return this.request(endpoint, { method: 'PUT', body, ...options });
    },
    patch(endpoint, body, options = {}) {
        return this.request(endpoint, { method: 'PATCH', body, ...options });
    },
    delete(endpoint, options = {}) {
        return this.request(endpoint, { method: 'DELETE', ...options });
    }
};