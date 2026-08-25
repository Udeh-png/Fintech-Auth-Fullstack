"use client";

import { motion } from "framer-motion";

export const FormWrapper = ({ children }: { children: React.ReactNode }) => {
  return (
    <motion.div
      className="form-wrapper w-full"
      initial={{
        translateX: "var(--slide-in-offset)",
        opacity: 0,
      }}
      animate={{
        translateX: 0,
        opacity: 1,
      }}
      transition={{
        type: "tween",
      }}
    >
      {children}
    </motion.div>
  );
};
