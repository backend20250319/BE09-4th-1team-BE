"use client";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    // localStorage에서 토큰 확인
    const token = typeof window !== "undefined" ? localStorage.getItem("token") : null;
    if (!token) {
      router.replace("/login");
    }
  }, [router]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    router.replace("/login");
  };

  return (
    <main style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '100vh' }}>
      <h1>환영합니다! 🎉</h1>
      <p>로그인에 성공하셨습니다.</p>
      <button onClick={handleLogout} style={{ marginTop: 20, padding: '8px 20px', borderRadius: 6, background: '#0070f3', color: '#fff', border: 'none', cursor: 'pointer' }}>
        로그아웃
      </button>
    </main>
  );
} 