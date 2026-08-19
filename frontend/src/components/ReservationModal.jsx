import { useState } from "react";
import { reservationApi } from "../api/client";
import { useAuth } from "../context/AuthContext";
import ErrorBanner from "./ErrorBanner.jsx";

// datetime-local input'u backend'in beklediği LocalDateTime formatına ("2026-07-22T14:00:00") çeviriyor
function toLocalDateTime(value) {
    return value ? `${value}:00` : "";
}

export default function ReservationModal({ table, onClose, onSuccess }) {
    const [form, setForm] = useState({ startTime: "", endTime: "", guestCount: 2, notes: "" });
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const { token } = useAuth();

    function handleChange(e) {
        const { name, value } = e.target;
        setForm({ ...form, [name]: name === "guestCount" ? Number(value) : value });
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");

        if (form.guestCount > table.capacity) {
            setError(`Bu masa en fazla ${table.capacity} kişi kabul ediyor.`);
            return;
        }

        setLoading(true);
        try {
            await reservationApi.create(
                {
                    tableId: table.id,
                    startTime: toLocalDateTime(form.startTime),
                    endTime: toLocalDateTime(form.endTime),
                    guestCount: form.guestCount,
                    notes: form.notes,
                },
                token
            );
            onSuccess();
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <form className="modal-card" onClick={(e) => e.stopPropagation()} onSubmit={handleSubmit}>
                <div className="modal-card__header">
                    <h2>Masa {table.tableNumber}</h2>
                    <button type="button" className="modal-card__close" onClick={onClose} aria-label="Kapat">
                        ×
                    </button>
                </div>

                <p className="modal-card__meta">
                    Kapasite: {table.capacity} kişi {table.location && `· ${table.location}`}
                </p>

                <ErrorBanner message={error} />

                <div className="form-row">
                    <label htmlFor="startTime">Başlangıç</label>
                    <input
                        id="startTime"
                        name="startTime"
                        type="datetime-local"
                        value={form.startTime}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="endTime">Bitiş</label>
                    <input
                        id="endTime"
                        name="endTime"
                        type="datetime-local"
                        value={form.endTime}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="guestCount">Kişi Sayısı</label>
                    <input
                        id="guestCount"
                        name="guestCount"
                        type="number"
                        min={1}
                        max={table.capacity}
                        value={form.guestCount}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="notes">Not (opsiyonel)</label>
                    <textarea id="notes" name="notes" rows={2} value={form.notes} onChange={handleChange} />
                </div>

                <button className="button button--primary" type="submit" disabled={loading}>
                    {loading ? "Oluşturuluyor..." : "Rezervasyonu Onayla"}
                </button>
            </form>
        </div>
    );
}
