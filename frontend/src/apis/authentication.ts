export async function register(email: string, password: string): Promise<void> {
    const res = await fetch("http://localhost:8080/auth/register", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ email, password }),
    });

    if (!res.ok) {
        throw new Error("Registration failed");
    }

    const data = await res.json();
    localStorage.setItem("user_id", data.userId);
}

export async function login(email: string, password: string): Promise<void> {
    const res = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ email, password }),
    });

    if (!res.ok) {
        throw new Error("Login failed");
    }

    const data = await res.json();
    localStorage.setItem("user_id", data.userId);
}

export async function logout(): Promise<void> {
    const res = await fetch("http://localhost:8080/auth/logout", {
        method: "POST",
        headers: {"Content-Type": "application/json"}
    })

    if (!res.ok) {
        throw new Error("Logout failed");
    }

    localStorage.removeItem("user_id");
}
