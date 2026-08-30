"use client";

import { AnimatePresence, motion } from "framer-motion";
import { MaterialSpinner } from "./MaterialSpinner";

export const LoadingScreen = ({ isLoading }: { isLoading: boolean }) => {
  return (
    <AnimatePresence>
      {isLoading && (
        <motion.div
          initial={{
            opacity: 0,
          }}
          animate={{
            opacity: 1,
          }}
          exit={{
            opacity: 0,
          }}
          transition={{
            duration: 0.15,
          }}
          className="fixed inset-0 w-full h-full bg-black/50 flex items-center justify-center z-100000000"
        >
          <MaterialSpinner sizeInPx={50} color="primary" />
        </motion.div>
      )}
    </AnimatePresence>
  );
};
