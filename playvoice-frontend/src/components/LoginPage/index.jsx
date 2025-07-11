import React from "react";
import { LoginButtons } from "./LoginButtons";
import Image from "next/image";
import playdata_logo from "@/assets/images/playdata-logo.svg";
import advertisementImage from "@/assets/images/login-ad.png";
import image from "@/assets/images/delete-icon.png";
import multiply from "@/assets/images/delete-icon.png";
import styles from "./style.module.css";

export const LoginPage = () => {
    return (
        <div className={styles["login-page"]}>
            <div className={styles["overlap-group"]}>
                <div className={styles.frame}>
                    <header className={styles.header}>
                        <img
                            src="/assets/images/playdata-logo.svg"
                            alt="Playdata LOGO"
                            width={120}
                            height={30}
                        />
                    </header>

                    <div className={styles.div001}>
                        <div className={styles["frame001"]}>
                            <Image
                                className={styles["advertisement-image"]}
                                alt="Advertisement image"
                                src={advertisementImage}
                                style={{ objectFit: "cover",
                                         objectPosition: "top"
                                }}
                            />

                            <div className={styles["welcome-message"]}>
                                <div className={styles["welcome-title"]}>
                                    Welcome to PLAYDATA
                                    <br />
                                    Community Forum
                                </div>

                                <p className={styles["welcome-description"]}>
                                    A place where students and instructors come together to ask
                                    questions, share knowledge, and grow as developers. Whether
                                    you&#39;re just starting out or guiding others, this is your
                                    space to connect and collaborate.
                                </p>
                            </div>

                            <div className={styles["frame002"]}>
                                <div className={styles["login-title"]}>Login</div>

                                <div className={styles["frame003"]}>
                                    {/* 👤 ID 입력 */}
                                    <div className={styles["login-input"]}>
                                        <div className={styles["input-wrapper"]}>
                                            <input
                                                type="text"
                                                id="username"
                                                required
                                                className={styles["input"]}
                                            />
                                            <label htmlFor="username" className={styles["label"]}>
                                                ID
                                            </label>
                                            <img
                                                className={styles["multiply"]}
                                                alt="Multiply"
                                                src={multiply}
                                            />
                                        </div>
                                        <div className={styles["invalid-username-wrapper"]}>
                                            <div className={styles["invalid-username"]}>Invalid ID</div>
                                        </div>
                                    </div>

                                    {/* 🔒 Password 입력 */}
                                    <div className={styles["login-input"]}>
                                        <div className={styles["input-wrapper"]}>
                                            <input
                                                type="password"
                                                id="password"
                                                required
                                                className={styles["input"]}
                                            />
                                            <label htmlFor="password" className={styles["label"]}>
                                                Password
                                            </label>
                                            <img
                                                className={styles["img"]}
                                                alt="Multiply"
                                                src={image}
                                            />
                                        </div>
                                        <div className={styles["invalid-username-wrapper"]}>
                                            <div className={styles["invalid-username"]}>Invalid Password</div>
                                        </div>
                                    </div>
                                </div>

                                <div className={styles["login-button"]}>
                                    <LoginButtons className={styles["login-buttons-instance"]} />
                                    <div className={styles["invalid-password-wrapper"]}>
                                        <div className={styles["invalid-password"]}>Find ID/PW</div>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <p className={styles["text-wrapper"]}>
                            Copyright ⓒ 2025 플레이데이터 All rights reserved.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};
