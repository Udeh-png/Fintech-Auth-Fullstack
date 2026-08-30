"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState, useEffect, useRef } from "react";
import { SignupFormType, signupSchema } from "@/types";
import { LoadingScreen } from "@/components/shared/LoadingScreen";
import Link from "next/link";
import { FormWrapper } from "@/components/auth/FormWrapper";
import { ErrorMessage } from "@/components/auth/ErrorMessage";
import { PasswordFieldWithChecks } from "@/components/auth/PasswordFieldWIthChecks";
import { PasswordInput } from "@/components/auth/PasswordInput";
import { useRouter } from "next/navigation";
import { Config } from "@/config";

export default function Signup() {
  const errMsgRef = useRef<HTMLDivElement | null>(null);
  const [, setTime] = useState<string | null>(null);
  const router = useRouter();
  const backendHostname = Config.API_URL;

  const {
    register,
    handleSubmit,
    trigger,
    setError,
    formState: { errors, isSubmitted, isSubmitting },
  } = useForm<SignupFormType>({
    resolver: zodResolver(signupSchema),
    criteriaMode: "all",
  });

  useEffect(() => {
    if (errMsgRef.current && errors.root) {
      errMsgRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [errors.root]);

  const onSubmit = async (data: SignupFormType) => {
    const response = await fetch(
      `${backendHostname}/api/auth/registration/initiate`,
      {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      },
    );

    if (response.status === 200) {
      localStorage.setItem("userEmail", data.email);
      setTime(() => {
        const time = new Date().getTime().toString();
        localStorage.setItem("otpRequestTimestamp", time || "0");
        return time;
      });
      router.push("/auth/email-verification?context=register");
      return;
    }

    const error = await response.json();
    if (error) {
      setError("root", {
        message: error.message,
      });
    }
  };

  return (
    <div className="lg:mt-0 mt-5">
      <FormWrapper>
        <LoadingScreen isLoading={isSubmitting} />
        <form
          action=""
          className="flex flex-col md:gap-7 gap-5 md:px-0 px-1"
          onSubmit={handleSubmit(onSubmit)}
        >
          <div className="mb-3">
            <p className="text-3xl font-bold">Create Your Account</p>
            <p className="text-gray-400">Sign up in seconds to get started</p>
          </div>

          <div className="flex md:flex-row justify-between gap-x-4 gap-y-8">
            <div className="input-container w-full">
              <label htmlFor="first name" className="input-label">
                First Name
              </label>
              <label
                className={`form-input-wrapper ${errors.firstName ? "ring-red-500! ring-2!" : ""}`}
              >
                <input
                  autoComplete=""
                  type="text"
                  id="first name"
                  placeholder="John"
                  {...register("firstName")}
                />
              </label>

              {errors.firstName && (
                <p className="input-error-text">{errors.firstName.message}</p>
              )}
            </div>

            <div className="input-container w-full">
              <label htmlFor="last name" className="input-label">
                Last Name
              </label>
              <label
                className={`form-input-wrapper ${errors.lastName ? "ring-red-500! ring-2!" : ""}`}
              >
                <input
                  autoComplete=""
                  type="text"
                  id="last name"
                  placeholder="Doe"
                  {...register("lastName")}
                />
              </label>

              {errors.lastName && (
                <p className="input-error-text">{errors.lastName.message}</p>
              )}
            </div>
          </div>

          <div className="input-container">
            <label htmlFor="email" className="input-label">
              Email
            </label>
            <label
              className={`form-input-wrapper ${errors.lastName ? "ring-red-500! ring-2!" : ""}`}
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

          <PasswordFieldWithChecks
            isSubmitted={isSubmitted}
            passwordErrors={errors.password}
            register={register}
            trigger={trigger}
          />

          <div className="input-container">
            <label htmlFor="confirm-password" className="input-label">
              Confirm Password
            </label>
            <PasswordInput
              elementId="confirm-password"
              error={Boolean(errors.confirmPassword)}
              autoComplete="password"
              id="confirm-password"
              {...register("confirmPassword")}
            />

            {errors.confirmPassword && (
              <p className="input-error-text">
                {errors.confirmPassword.message}
              </p>
            )}
          </div>

          <div>
            <button
              type="submit"
              className="button-primary"
              disabled={isSubmitting}
            >
              Sign Up
            </button>
          </div>

          <ErrorMessage
            condition={Boolean(errors.root)}
            message={errors.root?.message || ""}
          />

          <p className="mt-2 text-center text-sm border-t border-border-color pt-5 w-[80%] self-center">
            Already have an account?{" "}
            <Link
              href="/auth/login"
              className="text-primary font-medium underline"
            >
              Login
            </Link>
          </p>
        </form>
      </FormWrapper>
    </div>
  );
}
