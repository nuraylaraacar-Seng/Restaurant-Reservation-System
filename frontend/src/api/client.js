const BASE_URL = "http://localhost:8080/api";

// Backend'in ErrorResponse formatı: { timestamp, status, error, message }
// Bu fonksiyon her istekte token'ı otomatik ekliyor ve hata mesajını
// backend'in gönderdiği gerçek message alanından çekiyor.
async function request(path, { method = "GET", body, token } = {}) {
    const headers = { "Content-Type": "application/json" };
    if (token) headers.Authorization = `Bearer ${token}`;

    const response = await fetch(`${BASE_URL}${path}`, {
        method,
        headers,
        body: body ? JSON.stringify(body) : undefined,
    });

    // 204 No Content dönen endpointler var (deleteTable) - body parse etmeye çalışmıyoruz
    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (!response.ok) {
        const message = data?.message || "Bir şeyler ters gitti.";
        throw new Error(message);
    }

    return data;
}

export const authApi = {
    register: (payload) => request("/auth/register", { method: "POST", body: payload }),
    login: (payload) => request("/auth/login", { method: "POST", body: payload }),
};

export const tableApi = {
    getAll: () => request("/tables"),
    create: (payload, token) => request("/tables", { method: "POST", body: payload, token }),
    update: (id, payload, token) => request(`/tables/${id}`, { method: "PUT", body: payload, token }),
    remove: (id, token) => request(`/tables/${id}`, { method: "DELETE", token }),
};

export const reservationApi = {
    create: (payload, token) => request("/reservations", { method: "POST", body: payload, token }),
    getMine: (token) => request("/reservations/me", { token }),
    cancel: (id, token) => request(`/reservations/${id}/cancel`, { method: "PATCH", token }),
};
