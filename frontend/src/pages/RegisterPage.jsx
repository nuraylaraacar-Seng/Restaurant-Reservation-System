import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { authApi } from "../api/client";
import { useAuth } from "../context/AuthContext";
import ErrorBanner from "../components/ErrorBanner";

export default function RegisterPage() {
    const [form, setForm] = useState({ firstName: "", lastName: "", email: "", password: "" });
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
            const response = await authApi.register(form);
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
                <h1>Hesap Oluştur</h1>
                <ErrorBanner message={error} />

                <div className="form-row">
                    <label htmlFor="firstName">Ad</label>
                    <input id="firstName" name="firstName" value={form.firstName} onChange={handleChange} required />
                </div>

                <div className="form-row">
                    <label htmlFor="lastName">Soyad</label>
                    <input id="lastName" name="lastName" value={form.lastName} onChange={handleChange} required />
                </div>

                <div className="form-row">
                    <label htmlFor="email">E-posta</label>
                    <input id="email" name="email" type="email" value={form.email} onChange={handleChange} required />
                </div>

                <div className="form-row">
                    <label htmlFor="password">Şifre</label>
                    <input
                        id="password"
                        name="password"
                        type="password"
                        minLength={6}
                        value={form.password}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button className="button button--primary" type="submit" disabled={loading}>
                    {loading ? "Kaydediliyor..." : "Kayıt Ol"}
                </button>

                <p className="auth-card__footer">
                    Zaten hesabın var mı? <Link to="/login">Giriş yap</Link>
                </p>
            </form>
        </div>
    );
}
