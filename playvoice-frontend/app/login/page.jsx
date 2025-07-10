'use client';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { LoginButton } from '../../components/LoginButton';

export default function LoginPage() {
  const [id, setId] = useState('');
  const [pw, setPw] = useState('');
  const [idError, setIdError] = useState('');
  const [pwError, setPwError] = useState('');
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  const validate = () => {
    setIdError(id ? '' : 'ID를 입력하세요');
    setPwError(pw ? '' : '비밀번호를 입력하세요');
    return id && pw;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;
    setLoading(true);
    try {
      const res = await fetch('http://localhost:8000/api/v1/user-service/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: id, password: pw })
      });
      if (!res.ok) {
        const data = await res.json().catch(() => ({}));
        if (data.message?.includes('User')) setIdError('존재하지 않는 ID');
        else if (data.message?.includes('Password')) setPwError('비밀번호 오류');
        else setPwError('로그인 실패: 서버 오류');
        return;
      }
      const data = await res.json();
      localStorage.setItem('token', data.token);
      router.push('/');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      minHeight: '100vh', width: '100vw',
      background: 'url("/bg-stars.jpg") center/cover no-repeat',
      display: 'flex', flexDirection: 'column', justifyContent: 'space-between',
      fontFamily: 'Roboto, Helvetica, Arial, sans-serif',
    }}>
      {/* 상단 로고 */}
      <div style={{ padding: 32 }}>
        <img src="/playdata-logo.svg" alt="PLAYDATA" style={{ height: 32 }} />
      </div>
      {/* 메인 컨텐츠 */}
      <div style={{
        flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center',
        gap: 60, flexWrap: 'wrap',
        maxWidth: 1440, margin: '0 auto', width: '100%'
      }}>
        {/* 좌측 안내 */}
        <div style={{ maxWidth: 448, minWidth: 280, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 24 }}>
          <img src="/camp-banner.png" alt="캠프 안내" style={{ width: 330, height: 330, borderRadius: 24, objectFit: 'cover', marginBottom: 16 }} />
          <div style={{ fontWeight: 700, color: '#fff', fontSize: 28, marginBottom: 8, textAlign: 'left', width: '100%' }}>
            Welcome to PLAYDATA<br />Community Forum
          </div>
          <p style={{ color: '#fff', fontSize: 15, lineHeight: 1.6, textAlign: 'left', width: '100%' }}>
            A place where students and instructors come together to ask questions, share knowledge, and grow as developers. Whether you’re just starting out or guiding others, this is your space to connect and collaborate.
          </p>
        </div>
        {/* 우측 로그인 폼 */}
        <form onSubmit={handleSubmit} style={{
          minWidth: 330, maxWidth: 360, background: 'rgba(30,32,60,0.85)', borderRadius: 20, padding: 36,
          boxShadow: '0 10px 24px 0 rgba(0,0,0,0.22)', display: 'flex', flexDirection: 'column', gap: 18,
          alignItems: 'stretch', justifyContent: 'center',
        }}>
          <div style={{ fontWeight: 700, color: '#fff', fontSize: 24, marginBottom: 12 }}>Login</div>
          <div style={{ marginBottom: 8 }}>
            <div style={{ color: '#8F8F8F', fontSize: 13, marginBottom: 4 }}>ID</div>
            <input
              style={{
                width: '100%', padding: '14px 12px', borderRadius: 10,
                border: idError ? '2px solid #EF3434' : '2px solid #8A38F5',
                background: 'rgba(255,255,255,0.10)', color: '#222', fontSize: 16,
                outline: 'none', marginBottom: 2,
                boxShadow: idError ? '0 0 0 4px rgba(255,107,107,0.15)' : '0 0 0 4px rgba(138,56,245,0.10)',
                transition: 'border 0.2s, box-shadow 0.2s',
              }}
              placeholder="ID"
              value={id}
              onChange={e => setId(e.target.value)}
              autoComplete="username"
              aria-invalid={!!idError}
            />
            {idError && <div style={{ color: '#EF3434', fontSize: 12, marginTop: 2 }}>{idError}</div>}
          </div>
          <div style={{ marginBottom: 8 }}>
            <div style={{ color: '#8F8F8F', fontSize: 13, marginBottom: 4 }}>Password</div>
            <input
              style={{
                width: '100%', padding: '14px 12px', borderRadius: 10,
                border: pwError ? '2px solid #EF3434' : '2px solid #8A38F5',
                background: 'rgba(255,255,255,0.10)', color: '#222', fontSize: 16,
                outline: 'none', marginBottom: 2,
                boxShadow: pwError ? '0 0 0 4px rgba(255,107,107,0.15)' : '0 0 0 4px rgba(138,56,245,0.10)',
                transition: 'border 0.2s, box-shadow 0.2s',
              }}
              placeholder="Password"
              type="password"
              value={pw}
              onChange={e => setPw(e.target.value)}
              autoComplete="current-password"
              aria-invalid={!!pwError}
            />
            {pwError && <div style={{ color: '#EF3434', fontSize: 12, marginTop: 2 }}>{pwError}</div>}
          </div>
          <LoginButton type="submit" disabled={loading}>
            {loading ? '로그인 중...' : 'Login'}
          </LoginButton>
          <div style={{ textAlign: 'right', marginTop: 4 }}>
            <a href="#" style={{ color: '#8876D9', fontSize: 13, textDecoration: 'underline', cursor: 'pointer' }}>Find ID/PW</a>
          </div>
        </form>
      </div>
      {/* 하단 카피라이트 */}
      <div style={{
        textAlign: 'center', color: '#fff', padding: 16, fontSize: 13,
        background: 'rgba(0,0,0,0.10)', letterSpacing: 0.2
      }}>
        Copyright © 2025 플레이데이터 All rights reserved.
      </div>
    </div>
  );
} 