"use client";

import { useEffect, useRef } from "react";
import { FaExclamationCircle } from "react-icons/fa";

export const ErrorMessage = ({
  condition,
  message,
}: {
  condition: boolean;
  message: string;
}) => {
  const errMsgRef = useRef<HTMLDivElement | null>(null);
  useEffect(() => {
    if (errMsgRef.current && condition) {
      errMsgRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [condition]);

  if (condition)
    return (
      <div className="bg-red-800 p-3 rounded" ref={errMsgRef}>
        <FaExclamationCircle className="inline align-middle mr-1" />
        <span className="text-sm">{message}</span>
      </div>
    );

  return null;
};
