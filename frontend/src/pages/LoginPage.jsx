import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { authApi } from "../api/client";
import { useAuth } from "../context/AuthContext";
import ErrorBanner from "../components/ErrorBanner";

export default function LoginPage() {
    const [form, setForm] = useState({ email: "", password: "" });
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const { login } = useAuth();
    const navigate = useNavigate();

    function handleChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            const response = await authApi.login(form);
            login(response);
            navigate("/");
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="auth-page">
            <form className="auth-card" onSubmit={handleSubmit}>
                <h1>Giriş Yap</h1>
                <ErrorBanner message={error} />

                <div className="form-row">
                    <label htmlFor="email">E-posta</label>
                    <input id="email" name="email" type="email" value={form.email} onChange={handleChange} required />
                </div>

                <div className="form-row">
                    <label htmlFor="password">Şifre</label>
                    <input id="password" name="password" type="password" value={form.password} onChange={handleChange} required />
                </div>

                <button className="button button--primary" type="submit" disabled={loading}>
                    {loading ? "Giriş yapılıyor..." : "Giriş Yap"}
                </button>

                <p className="auth-card__footer">
                    Hesabın yok mu? <Link to="/register">Kayıt ol</Link>
                </p>
            </form>
        </div>
    );
}
