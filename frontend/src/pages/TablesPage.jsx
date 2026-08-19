import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { tableApi } from "../api/client";
import { useAuth } from "../context/AuthContext";
import TableCard from "../components/TableCard";
import ReservationModal from "../components/ReservationModal";
import ErrorBanner from "../components/ErrorBanner";

export default function TablesPage() {
    const [tables, setTables] = useState([]);
    const [selectedTable, setSelectedTable] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);
    const [successMessage, setSuccessMessage] = useState("");
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();

    async function loadTables() {
        try {
            const data = await tableApi.getAll();
            setTables(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadTables();
    }, []);

    function handleReserveClick(table) {
        if (!isAuthenticated) {
            navigate("/login");
            return;
        }
        setSelectedTable(table);
    }

    function handleReservationSuccess() {
        setSelectedTable(null);
        setSuccessMessage("Rezervasyon oluşturuldu!");
        setTimeout(() => setSuccessMessage(""), 4000);
    }

    return (
        <div className="page">
            <div className="page__header">
                <h1>Masalar</h1>
                {!isAuthenticated && <p className="page__hint">Rezervasyon yapmak için giriş yapmalısın.</p>}
            </div>

            <ErrorBanner message={error} />
            {successMessage && <div className="success-banner">{successMessage}</div>}

            {loading ? (
                <p className="page__loading">Yükleniyor...</p>
            ) : (
                <div className="table-grid">
                    {tables.map((table) => (
                        <TableCard key={table.id} table={table} onReserve={handleReserveClick} />
                    ))}
                </div>
            )}

            {selectedTable && (
                <ReservationModal
                    table={selectedTable}
                    onClose={() => setSelectedTable(null)}
                    onSuccess={handleReservationSuccess}
                />
            )}
        </div>
    );
}
