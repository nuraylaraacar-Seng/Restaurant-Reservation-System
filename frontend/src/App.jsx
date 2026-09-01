import { Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import TablesPage from "./pages/TablesPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import MyReservationsPage from "./pages/MyReservationsPage";

export default function App() {
    return (
        <AuthProvider>
            <Navbar />
            <main>
                <Routes>
                    <Route path="/" element={<TablesPage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                    <Route
                        path="/my-reservations"
                        element={
                            <ProtectedRoute>
                                <MyReservationsPage />
                            </ProtectedRoute>
                        }
                    />
                </Routes>
            </main>
        </AuthProvider>
    );
}
