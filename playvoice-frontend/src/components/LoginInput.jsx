import React from 'react';

export function LoginInput({ label, value, onChange, error, placeholder, type = 'text', autoComplete }) {
  return (
    <div style={{ marginBottom: 8 }}>
      <div style={{ color: '#8F8F8F', fontSize: 13, marginBottom: 4 }}>{label}</div>
      <input
        style={{
          width: '100%', padding: '14px 12px', borderRadius: 10,
          border: error ? '2px solid #EF3434' : '2px solid #8A38F5',
          background: 'rgba(255,255,255,0.10)', color: '#222', fontSize: 16,
          outline: 'none', marginBottom: 2,
          boxShadow: error ? '0 0 0 4px rgba(255,107,107,0.15)' : '0 0 0 4px rgba(138,56,245,0.10)',
          transition: 'border 0.2s, box-shadow 0.2s',
        }}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        type={type}
        autoComplete={autoComplete}
        aria-invalid={!!error}
      />
      {error && <div style={{ color: '#EF3434', fontSize: 12, marginTop: 2 }}>{error}</div>}
    </div>
  );
} 