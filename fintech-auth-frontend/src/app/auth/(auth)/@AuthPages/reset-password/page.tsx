"use client";

import { FormWrapper } from "@/components/auth/FormWrapper";
import { PasswordInput } from "@/components/auth/PasswordInput";
import { PasswordFieldWithChecks } from "@/components/auth/PasswordFieldWIthChecks";

import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { motion, AnimatePresence } from "framer-motion";
import { MaterialSpinner } from "@/components/shared/MaterialSpinner";
import z from "zod";
import { useRouter } from "next/navigation";

const resetPasswordSchema = z
  .object({
    password: z
      .string()
      .min(1, { message: "REQUIRED" })
      .min(8, "MIN_LENGTH")
      .regex(/[A-Z]/, "UPPERCASE")
      .regex(/[a-z]/, "LOWERCASE")
      .regex(/[0-9]/, "NUMBER")
      .regex(/[^A-Za-z0-9]/, "SPECIAL_CHARACTER"),
    confirmPassword: z
      .string()
      .min(1, { message: "Confirm password is required" }),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords do not match",
    path: ["confirmPassword"],
  });

type ResetPasswordType = z.infer<typeof resetPasswordSchema>;

export default function ResetPassword() {
  const router = useRouter();
  const backendHostname = process.env.NEXT_PUBLIC_BACKEND_HOSTNAME;

  const {
    handleSubmit,
    register,
    trigger,
    formState: { errors, isSubmitted, isSubmitting },
  } = useForm<ResetPasswordType>({
    resolver: zodResolver(resetPasswordSchema),
    criteriaMode: "all",
  });

  const handleFormSubmit = async (data: ResetPasswordType) => {
    console.log("submitted");
    const request = await fetch(`${backendHostname}/api/auth/reset-password`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify({ password: data.password }),
    });

    if (request.status == 200) {
      router.push("/dashboard");
      window.sessionStorage.setItem("passwordResetSuccess", "1");
    }
  };

  return (
    <FormWrapper>
      <h1 className="md:text-3xl text-2xl font-semibold mb-5 mt-2">
        Reset your password
      </h1>
      <form
        className="md:px-0 px-1 pb-5"
        onSubmit={handleSubmit(handleFormSubmit)}
      >
        <div className="space-y-5">
          <PasswordFieldWithChecks
            passwordErrors={errors.password}
            isSubmitted={isSubmitted}
            register={register}
            trigger={trigger}
          />

          <div className="input-container">
            <label htmlFor="confirmPassword" className="input-label">
              Confirm Password
            </label>

            <PasswordInput
              id="confirmPassword"
              elementId="confirmPassword"
              error={Boolean(errors.confirmPassword)}
              {...register("confirmPassword")}
            />

            {errors.confirmPassword && (
              <p className="input-error-text">
                {errors.confirmPassword.message}
              </p>
            )}
          </div>

          <div className="mt-10">
            <button
              type="submit"
              className="button-primary flex items-center justify-center gap-x-2 disabled:brightness-75 disabled:cursor-default!"
              disabled={isSubmitting}
            >
              <p className="relative">
                Reset Password
                <AnimatePresence>
                  {isSubmitting && (
                    <motion.span
                      initial={{ opacity: 0 }}
                      animate={{ opacity: 1 }}
                      exit={{ opacity: 0 }}
                      transition={{ duration: 0.1 }}
                      className="size-3.75 absolute left-[120%] top-1/2 -translate-y-1/2"
                    >
                      <MaterialSpinner sizeInPx={15} />
                    </motion.span>
                  )}
                </AnimatePresence>
              </p>
            </button>
          </div>
        </div>
      </form>
    </FormWrapper>
  );
}
