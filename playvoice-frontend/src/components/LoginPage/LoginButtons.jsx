import React from "react";

export const LoginButtons = ({ className, username, password }) => {
    const isDisabled = !(username && password);
    return (
        <button className={className} disabled={isDisabled}>
            Login
        </button>
    );
};
