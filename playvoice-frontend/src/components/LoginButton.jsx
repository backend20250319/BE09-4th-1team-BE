import React, { useState } from 'react';

export function LoginButton({ type = 'button', disabled, children, className = '', ...props }) {
  const [hover, setHover] = useState(false);
  return (
    <button
      type={type}
      disabled={disabled}
      className={className}
      style={{
        width: '100%',
        padding: '14px 0',
        border: 'none',
        borderRadius: 30,
        fontWeight: 700,
        fontSize: 16,
        fontFamily: 'Roboto, Helvetica, Arial, sans-serif',
        background: disabled
          ? '#504197'
          : hover
            ? '#6e5cc2'
            : '#8876D9',
        color: '#fff',
        marginBottom: 8,
        cursor: disabled ? 'not-allowed' : 'pointer',
        boxShadow: '0 2px 8px 0 rgba(80,65,151,0.10)',
        transition: 'background 0.2s, color 0.2s, opacity 0.2s',
        opacity: disabled ? 0.7 : 1,
        outline: 'none',
      }}
      onMouseEnter={() => setHover(true)}
      onMouseLeave={() => setHover(false)}
      {...props}
    >
      {children}
    </button>
  );
} 