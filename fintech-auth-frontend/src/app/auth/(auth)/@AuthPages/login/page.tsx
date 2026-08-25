"use client";

import { useForm } from "react-hook-form";
import Link from "next/link";
import { loginSchema, LogInType } from "@/types";
import { zodResolver } from "@hookform/resolvers/zod";
import { ErrorMessage } from "@/components/auth/ErrorMessage";
import { FormWrapper } from "@/components/auth/FormWrapper";
import { MaterialSpinner } from "@/components/shared/MaterialSpinner";
import { AnimatePresence, motion } from "framer-motion";
import { PasswordInput } from "@/components/auth/PasswordInput";
import { useRouter } from "next/navigation";

export default function Login() {
  const backendHostname = process.env.NEXT_PUBLIC_BACKEND_HOSTNAME;
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors, isSubmitting },
  } = useForm<LogInType>({ resolver: zodResolver(loginSchema) });
  const router = useRouter();

  const handleOnSubmit = async (data: LogInType) => {
    const response = await fetch(`${backendHostname}/api/auth/login`, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (response.ok) {
      router.push("/dashboard");
    }

    const errorData = await response.json();
    if (errorData?.message) {
      setError("root", {
        message: errorData.message,
      });
    }
  }; // displaying error message
  return (
    <FormWrapper>
      <form
        onSubmit={handleSubmit(handleOnSubmit)}
        className="px-1 md:px-0 md:space-y-7 space-y-5"
      >
        <div>
          <p className="text-3xl font-bold">Welcome back!</p>
          <p className="text-gray-400">
            Enter your details to get back to your finances.
          </p>
        </div>

        <div className="flex flex-col md:gap-7 gap-5">
          <div className="input-container w-full">
            <label htmlFor="email" className="input-label">
              Email Address
            </label>
            <label
              className={`form-input-wrapper ${errors.email ? "ring-red-500! ring-2!" : ""}`}
            >
              <input
                autoComplete="email"
                type="email"
                id="email"
                placeholder="name@example.com"
                {...register("email")}
              />
            </label>

            {errors.email && (
              <p className="input-error-text">{errors.email.message}</p>
            )}
          </div>

          <div className="input-container">
            <label htmlFor="password" className="input-label">
              Password
            </label>
            <PasswordInput
              elementId="password"
              error={Boolean(errors.password)}
              id="password"
              autoComplete="new-password"
              {...register("password")}
            />
            <p className="input-error-text">{errors.password?.message}</p>

            <Link
              href="/auth/forgot-password"
              className="text-primary font-small text-sm underline mt-3 block"
            >
              Forgotten Password?
            </Link>
          </div>
        </div>

        <div>
          <button
            type="submit"
            className="button-primary flex items-center justify-center gap-x-2 disabled:brightness-75 disabled:cursor-default!"
            disabled={isSubmitting}
          >
            <p className="relative">
              Log In
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

        <ErrorMessage
          condition={Boolean(errors.root)}
          message={errors.root?.message || ""}
        />

        <p className="mt-5 text-center text-sm text-gray-500 border-t border-border pt-5">
          Don&apos;t have an account?{" "}
          <Link
            href="/auth/register"
            className="text-primary font-medium underline"
          >
            Signup
          </Link>
        </p>
      </form>
    </FormWrapper>
  );
}
