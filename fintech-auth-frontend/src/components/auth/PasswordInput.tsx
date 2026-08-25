"use client";

import { InputHTMLAttributes, useRef, useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa6";

export const PasswordInput = ({
  elementId,
  error,
  ...props
}: InputHTMLAttributes<HTMLInputElement> & {
  elementId: string;
  error: boolean;
}) => {
  const [showPassword, setShowPassword] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);
  return (
    <label
      className={`form-input-wrapper ${error ? "ring-red-500! ring-2!" : ""}`}
    >
      <input
        autoComplete="new-password"
        type={showPassword ? "text" : "password"}
        placeholder="•••••••••••"
        {...props}
      />

      <button
        onClick={() => {
          document.getElementById(elementId)?.focus();
          setShowPassword((prev) => !prev);
          inputRef.current?.focus();
        }}
        type="button"
        className="text-lg text-gray-400 cursor-pointer"
      >
        {showPassword ? <FaEyeSlash /> : <FaEye />}
      </button>
    </label>
  );
};
