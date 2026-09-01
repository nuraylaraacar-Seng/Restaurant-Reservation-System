import { useEffect, useState } from "react";
import { reservationApi } from "../api/client";
import { useAuth } from "../context/AuthContext";
import ErrorBanner from "../components/ErrorBanner";

const STATUS_LABELS = {
    PENDING: "Onay Bekliyor",
    CONFIRMED: "Onaylandı",
    CANCELLED: "İptal Edildi",
    COMPLETED: "Tamamlandı",
};

export default function MyReservationsPage() {
    const [reservations, setReservations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [cancellingId, setCancellingId] = useState(null);
    const { token } = useAuth();

    async function loadReservations() {
        try {
            const data = await reservationApi.getMine(token);
            setReservations(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadReservations();
    }, []);

    async function handleCancel(id) {
        setCancellingId(id);
        setError("");
        try {
            const updated = await reservationApi.cancel(id, token);
            setReservations((prev) => prev.map((r) => (r.id === id ? updated : r)));
        } catch (err) {
            setError(err.message);
        } finally {
            setCancellingId(null);
        }
    }

    return (
        <div className="page">
            <div className="page__header">
                <h1>Rezervasyonlarım</h1>
            </div>

            <ErrorBanner message={error} />

            {loading ? (
                <p className="page__loading">Yükleniyor...</p>
            ) : reservations.length === 0 ? (
                <p className="page__hint">Henüz bir rezervasyonun yok.</p>
            ) : (
                <div className="reservation-list">
                    {reservations.map((r) => (
                        <div key={r.id} className="reservation-row">
                            <div className="reservation-row__info">
                                <span className="reservation-row__table">Masa {r.tableNumber}</span>
                                <span className="reservation-row__time">
                  {new Date(r.startTime).toLocaleString("tr-TR")}
                </span>
                                <span className={`reservation-row__status status--${r.status.toLowerCase()}`}>
                  {STATUS_LABELS[r.status] ?? r.status}
                </span>
                            </div>

                            {(r.status === "PENDING" || r.status === "CONFIRMED") && (
                                <button
                                    className="button button--ghost button--small"
                                    disabled={cancellingId === r.id}
                                    onClick={() => handleCancel(r.id)}
                                >
                                    {cancellingId === r.id ? "İptal ediliyor..." : "İptal Et"}
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}
