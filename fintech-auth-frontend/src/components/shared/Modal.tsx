"use client";

import { useEffect } from "react";
import { AnimatePresence, motion } from "framer-motion";

export const Modal = ({
  isOpen,
  onClose,
  children,
}: {
  isOpen: boolean;
  onClose: () => void;
  children: React.ReactNode;
}) => {
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = "hidden";
      console.log("open");
    } else {
      document.body.style.overflow = "unset";
      console.log("close");
    }

    return () => {
      document.body.style.overflow = "unset";
    };
  }, [isOpen]);

  return (
    <AnimatePresence>
      {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center">
          {isOpen && (
            <motion.div
              className="fixed inset-0 bg-black/40"
              initial={{
                opacity: 0,
              }}
              animate={{
                opacity: 1,
              }}
              exit={{
                opacity: 0,
              }}
              onClick={onClose}
            />
          )}
          <div className="relative">{children}</div>
        </div>
      )}
    </AnimatePresence>
  );
};
