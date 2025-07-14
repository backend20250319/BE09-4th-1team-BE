'use client';
import React, { useState } from "react";
import styles from "./style.module.css";
import Image from "next/image";
import { LoginButtons } from "./LoginButtons";

const FindIdPwButton = ({ onClick }) => (
  <button className={styles["find-idpw-button"]} type="button" onClick={onClick}>
    Find ID/PW
  </button>
);

export const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [usernameError, setUsernameError] = useState('');
  const [passwordError, setPasswordError] = useState('');

  const handleClearUsername = () => setUsername('');
  const handleTogglePassword = () => setShowPassword((prev) => !prev);

  return (
    <div className={styles["login-page"]}>
      <header className={styles.header}>
        <Image
          src="/assets/images/playdata-logo.svg"
          alt="Playdata LOGO"
          width={133}
          height={23}
        />
      </header>

      <div className={styles["overlap-group"]}>
        <div className={styles.div001}>
          <div className={styles.frame001}>
            <img
              className={styles["advertisement-image"]}
              src="/assets/images/login-ad.png"
              alt="Ad"
            />
            <div className={styles["welcome-message"]}>
              <div className={styles["welcome-title"]}>
                Welcome to PLAYDATA<br />Community Forum
              </div>
              <p className={styles["welcome-description"]}>
                A place where students and instructors come together to ask questions, share knowledge, and grow as developers. Whether you're just starting out or guiding others, this is your space to connect and collaborate
              </p>
            </div>
          </div>

          <div className={styles["frame002"]}>
            <div className={styles["login-title"]}>Login</div>
            <div className={styles["frame003"]}>
              <div className={styles["login-input"]}>
                {username && (
                  <label className={styles["input-label"]} htmlFor="username">ID</label>
                )}
                <div className={styles["input-wrapper"]}>
                  <input
                    id="username"
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    className={usernameError ? `${styles.input} ${styles["input-error"]}` : styles.input}
                    placeholder="ID"
                  />
                  {username && (
                    <button
                      type="button"
                      onClick={handleClearUsername}
                      className={`${styles.icon} ${styles.clear}`}
                    >
                      <Image
                        src="/assets/images/clear.png"
                        alt="Clear"
                        width={16}
                        height={16}
                      />
                    </button>
                  )}
                </div>
                {usernameError && (
                  <div className={styles["input-label-error"]}>{usernameError}</div>
                )}
              </div>

              <div className={styles["login-input"]}>
                {password && (
                  <label className={styles["input-label"]} htmlFor="password">Password</label>
                )}
                <div className={styles["input-wrapper"]}>
                  <input
                    id="password"
                    type={showPassword ? "text" : "password"}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className={passwordError ? `${styles.input} ${styles["input-error"]}` : styles.input}
                    placeholder="Password"
                  />
                  {password && (
                    <button
                      type="button"
                      onClick={handleTogglePassword}
                      className={`${styles.icon} ${styles.toggle}`}
                    >
                      <img
                        src="/assets/images/eye-open.png"
                        alt="Toggle password"
                        width={16}
                        height={16}
                      />
                    </button>
                  )}
                </div>
                {passwordError && (
                  <div className={styles["input-label-error"]}>{passwordError}</div>
                )}
              </div>
            </div>

            <div className={styles["login-button"]}>
              <LoginButtons username={username} password={password} />
              <div className={styles["invalid-password-wrapper"]}>
                <FindIdPwButton />
              </div>
            </div>
          </div>
        </div>
      </div>

      <p className={styles["text-wrapper"]}>
        Copyright ⓒ 2025 플레이데이터 All rights reserved.
      </p>
    </div>
  );
};
