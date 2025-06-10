import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function FleetApp() {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [authMode, setAuthMode] = useState('login');
  const [form, setForm] = useState({ username: '', password: '', role: 'admin' });
  const [vehicles, setVehicles] = useState([]);
  const [newVehicle, setNewVehicle] = useState({ make: '', model: '', year: '', vin: '' });

  const handleAuth = async () => {
    const url = `http://localhost:3000/${authMode}`;
    const res = await axios.post(url, form);
    if (authMode === 'login') {
      localStorage.setItem('token', res.data.token);
      setToken(res.data.token);
    }
  };

  const fetchVehicles = async () => {
    const res = await axios.get('http://localhost:3000/vehicles', {
      headers: { Authorization: `Bearer ${token}` },
    });
    setVehicles(res.data);
  };

  const addVehicle = async () => {
    const fakeIOT = () => (Math.random() * 10).toFixed(2);
    const data = {
      ...newVehicle,
      front_iot: fakeIOT(),
      back_iot: fakeIOT(),
    };
    await axios.post('http://localhost:3000/vehicles', data, {
      headers: { Authorization: `Bearer ${token}` },
    });
    fetchVehicles();
  };

  const deleteVehicle = async (id) => {
    if (window.confirm('Ви впевнені, що хочете видалити транспортний засіб?')) {
      await axios.delete(`http://localhost:3000/vehicles/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchVehicles();
    }
  };

  const logout = () => {
    if (window.confirm('Вийти з облікового запису?')) {
      localStorage.removeItem('token');
      setToken(null);
    }
  };

  useEffect(() => {
    if (token) fetchVehicles();
  }, [token]);

  if (!token) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-100 to-gray-300 text-zinc-800">
        <div className="w-full max-w-sm p-10 bg-white rounded-xl shadow-2xl border-2 border-gray-300">
          <h1 className="text-3xl font-bold text-center mb-8 tracking-tight">{authMode === 'login' ? 'Вхід' : 'Реєстрація'}</h1>
          <input type="text" placeholder="Логін" className="w-full px-4 py-3 mb-4 rounded border border-zinc-300 focus:outline-none focus:ring-2 focus:ring-blue-300" onChange={(e) => setForm({ ...form, username: e.target.value })} />
          <input type="password" placeholder="Пароль" className="w-full px-4 py-3 mb-4 rounded border border-zinc-300 focus:outline-none focus:ring-2 focus:ring-blue-300" onChange={(e) => setForm({ ...form, password: e.target.value })} />
          {authMode === 'register' && (
            <input type="text" placeholder="Роль (admin або driver)" className="w-full px-4 py-3 mb-4 rounded border border-zinc-300 focus:outline-none focus:ring-2 focus:ring-blue-300" onChange={(e) => setForm({ ...form, role: e.target.value })} />
          )}
          <button onClick={handleAuth} className="w-full py-3 bg-blue-700 hover:bg-blue-800 rounded text-white font-bold transition">Підтвердити</button>
          <button onClick={() => setAuthMode(authMode === 'login' ? 'register' : 'login')} className="mt-4 w-full text-sm text-blue-700 hover:underline">
            {authMode === 'login' ? 'Ще не маєте акаунту?' : 'Вже зареєстровані?'}
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex flex-col bg-gradient-to-br from-gray-100 to-gray-200 text-zinc-900">
      <header className="p-8 bg-white border-b-2 border-gray-300 shadow">
        <h1 className="text-5xl font-extrabold text-center tracking-tight text-zinc-800">TrackWay</h1>
      </header>

      <main className="flex-1 p-10">
        <div className="flex justify-end mb-8">
          <button
            onClick={logout}
            className="px-6 py-3 rounded-full bg-red-600 text-white font-semibold shadow-md hover:shadow-lg hover:bg-red-700 transition-all duration-300 transform hover:scale-105"
          >
            Вийти 🚪
          </button>
        </div>

        <div className="max-w-3xl mx-auto bg-white p-8 rounded-lg border border-zinc-300 shadow-lg mb-12">
          <h2 className="text-3xl font-bold mb-6">Додати транспорт</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <input placeholder="Марка" className="px-4 py-3 rounded border border-zinc-300 focus:outline-none focus:ring-2 focus:ring-blue-300" onChange={(e) => setNewVehicle({ ...newVehicle, make: e.target.value })} />
            <input placeholder="Модель" className="px-4 py-3 rounded border border-zinc-300 focus:outline-none focus:ring-2 focus:ring-blue-300" onChange={(e) => setNewVehicle({ ...newVehicle, model: e.target.value })} />
            <input placeholder="Рік" type="number" className="px-4 py-3 rounded border border-zinc-300 focus:outline-none focus:ring-2 focus:ring-blue-300" onChange={(e) => setNewVehicle({ ...newVehicle, year: e.target.value })} />
            <input placeholder="VIN" className="px-4 py-3 rounded border border-zinc-300 focus:outline-none focus:ring-2 focus:ring-blue-300" onChange={(e) => setNewVehicle({ ...newVehicle, vin: e.target.value })} />
            <div className="col-span-full">
              <button onClick={addVehicle} className="mt-2 w-full py-3 bg-green-600 hover:bg-green-700 rounded text-white font-bold transition">Додати</button>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
          {vehicles.map((v) => (
            <div key={v._id} className="bg-white p-6 rounded-lg border border-zinc-300 shadow-lg hover:shadow-xl transition">
              <h2 className="text-2xl font-bold mb-2">{v.make} {v.model}</h2>
              <p className="text-sm text-zinc-600">Рік: {v.year}</p>
              <p className="text-sm text-zinc-600">VIN: {v.vin}</p>
              <p className="text-green-700 mt-2 font-medium">📡 Передній датчик: {v.front_iot} м</p>
              <p className="text-green-700 font-medium">📡 Задній датчик: {v.back_iot} м</p>
              <button onClick={() => deleteVehicle(v._id)} className="mt-4 text-sm text-red-600 font-medium hover:underline">Видалити</button>
            </div>
          ))}
        </div>
      </main>

      <footer className="p-6 text-center text-sm text-zinc-600 bg-white border-t-2 border-gray-300">
        &copy; 2025 TrackWay. Всі права захищені.
      </footer>
    </div>
  );
}