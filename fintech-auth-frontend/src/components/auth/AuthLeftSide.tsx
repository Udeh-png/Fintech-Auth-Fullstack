/* eslint-disable @next/next/no-img-element */
"use client";
import { motion } from "framer-motion";
import { usePathname } from "next/navigation";

export const AuthLeftSide = () => {
  const path = usePathname();
  return (
    <div className="flex items-start flex-col gap-6">
      <motion.div
        initial={{ translateY: 100, opacity: 0 }}
        animate={{ translateY: 0, opacity: 1 }}
        transition={{ type: "tween" }}
        className="relative"
      >
        <div className="absolute size-full rounded-full blur-2xl top-0 bg-primary/30 -z-10" />
        <img src="/images/logo.png" alt="" className="md:max-w-80 max-w-20" />
      </motion.div>
      <div className={`${path !== "/auth/register" ? "lg:block hidden" : ""}`}>
        <motion.p
          className="text-5xl font-bold"
          initial={{ translateY: 100, opacity: 0 }}
          animate={{ translateY: 0, opacity: 1 }}
          transition={{ type: "tween", delay: 0.2 }}
        >
          Take <span className="text-primary">Control</span> Of Your Finance
        </motion.p>
        <motion.p
          className="text-xl text-gray-500 mt-4 sm:flex hidden"
          initial={{ translateY: 100, opacity: 0 }}
          animate={{ translateY: 0, opacity: 1 }}
          transition={{ type: "tween", delay: 0.4 }}
        >
          The modern way to tract, manage, and grow your wealth with confidence.
          Join thousands today.
        </motion.p>
      </div>

      <div
        className={`flex items-center gap-5 ${path !== "/auth/register" ? "lg:flex hidden" : ""}`}
      >
        <motion.div
          className="flex"
          initial={{ translateX: -100, opacity: 0 }}
          animate={{ translateX: 0, opacity: 1 }}
          transition={{ type: "tween", delay: 0.5 }}
        >
          <div className="size-10 border-3 border-border-color rounded-full -mr-3 bg-red-500" />
          <div className="size-10 border-3 border-border-color rounded-full -mr-3 bg-green-500" />
          <div className="size-10 border-3 border-border-color rounded-full -mr-3 bg-blue-500" />
        </motion.div>

        <motion.p
          className={`text-sm font-semibold text-gray-500`}
          initial={{ translateX: 100, opacity: 0 }}
          animate={{ translateX: 0, opacity: 1 }}
          transition={{ type: "tween", delay: 0.5 }}
        >
          Trusted by 10k+ users
        </motion.p>
      </div>
    </div>
  );
};
