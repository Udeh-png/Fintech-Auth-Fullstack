"use client";

import { FormWrapper } from "@/components/auth/FormWrapper";
import { MaterialSpinner } from "@/components/shared/MaterialSpinner";
import { Config } from "@/config";
import { zodResolver } from "@hookform/resolvers/zod";
import { motion, AnimatePresence } from "framer-motion";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { GoInfo } from "react-icons/go";
import z from "zod";

const zodSchema = z.object({
  email: z.email(),
});

type FormType = z.infer<typeof zodSchema>;

export default function ResetPassword() {
  const backendHostname = Config.API_URL;
  const router = useRouter();
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<FormType>({
    resolver: zodResolver(zodSchema),
  });

  return (
    <FormWrapper>
      <form
        className="px-1 md:px-0 md:space-y-7 space-y-5"
        onSubmit={handleSubmit(async (data) => {
          const req = await fetch(
            `${backendHostname}/api/auth/forgot-password`,
            {
              method: "POST",
              credentials: "include",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({
                email: data.email,
              }),
            },
          );

          if (req.status === 200) {
            localStorage.setItem("userEmail", data.email);
            router.push("/auth/email-verification?context=reset");
          }
        })}
      >
        <p className="text-3xl font-bold">Reset your password</p>

        <div className="input-container w-full">
          <label htmlFor="email" className="input-label">
            Email Address
          </label>
          <input
            {...register("email")}
            autoComplete="email"
            type="email"
            id="email"
            className={`form-input-wrapper ${errors.email ? "ring-red-500! ring-2!" : ""}`}
            placeholder="name@example.com"
          />

          {errors.email && (
            <p className="input-error-text">{errors.email.message}</p>
          )}
        </div>

        <div className="border-white/10 border-2 rounded-lg px-3 py-5 flex items-center gap-x-2">
          <div className="flex items-start gap-x-2">
            <GoInfo className="text-xl text-primary" />
            <p className="-mt-0.5">
              We will send a One Time Password (OTP) to your email address
            </p>
          </div>
        </div>

        <div>
          <button
            type="submit"
            className="button-primary flex items-center justify-center gap-x-2 disabled:brightness-75 disabled:cursor-default!"
            disabled={isSubmitting}
          >
            <p className="relative">
              Request Password Reset
              <AnimatePresence>
                {isSubmitting && (
                  <motion.span
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                    transition={{ duration: 0.1 }}
                    className="size-3.75 absolute left-[105%] top-1/2 -translate-y-1/2"
                  >
                    <MaterialSpinner sizeInPx={15} />
                  </motion.span>
                )}
              </AnimatePresence>
            </p>
          </button>
        </div>
      </form>
    </FormWrapper>
  );
}
