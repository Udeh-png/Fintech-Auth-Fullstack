"use client";

import { useState, useEffect } from "react";
import { LuCopy, LuCopyCheck } from "react-icons/lu";

export const CopyButton = () => {
  const [copied, setCopied] = useState(false);

  useEffect(() => {
    if (copied) {
      const timeout = setTimeout(() => {
        setCopied(false);
      }, 3000);

      return () => clearTimeout(timeout);
    }
  }, [copied]);

  return (
    <div>
      {copied ? (
        <LuCopyCheck className="text-primary cursor-pointer" />
      ) : (
        <LuCopy
          className="text-primary cursor-pointer"
          onClick={async () => {
            await navigator.clipboard.writeText("0838459856");

            setCopied(true);
          }}
        />
      )}
    </div>
  );
};
