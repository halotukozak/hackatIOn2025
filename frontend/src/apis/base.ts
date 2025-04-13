export function base_url() {
    return import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";
}
