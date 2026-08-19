import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
    const { isAuthenticated, email, logout } = useAuth();
    const navigate = useNavigate();

    function handleLogout() {
        logout();
        navigate("/login");
    }

    return (
        <nav className="navbar">
            <Link to="/" className="navbar__brand">
                Masa<span>App</span>
            </Link>
            <div className="navbar__links">
                <Link to="/">Masalar</Link>
                {isAuthenticated ? (
                    <>
                        <Link to="/my-reservations">Rezervasyonlarım</Link>
                        <span className="navbar__email">{email}</span>
                        <button className="button button--ghost" onClick={handleLogout}>
                            Çıkış
                        </button>
                    </>
                ) : (
                    <>
                        <Link to="/login">Giriş</Link>
                        <Link to="/register" className="button button--primary button--small">
                            Kayıt Ol
                        </Link>
                    </>
                )}
            </div>
        </nav>
    );
}

