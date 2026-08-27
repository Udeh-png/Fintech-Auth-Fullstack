/* eslint-disable react-hooks/set-state-in-effect */
"use client";

import { otpInputSchema, OtpInputType } from "@/types";
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import { IoIosMailUnread } from "react-icons/io";
import { motion } from "framer-motion";
import { LoadingScreen } from "@/components/shared/LoadingScreen";
import { redirect, useSearchParams } from "next/navigation";
import { ErrorMessage } from "@/components/auth/ErrorMessage";

const fields = [
  "otpInput1",
  "otpInput2",
  "otpInput3",
  "otpInput4",
  "otpInput5",
  "otpInput6",
] as const; // with "as const", the type is created behind the scenes, as those exact string values,
//  rather than just string. This is important so it matches the type of the form data.

export default function EmailVerificationPage() {
  const backendHostname = process.env.NEXT_PUBLIC_BACKEND_HOSTNAME;

  const inputContainerRef = useRef<HTMLDivElement>(null);
  const formRef = useRef<HTMLFormElement>(null);
  const [pasted, setPasted] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [otpRequestTimestamp, setOtpTimestamp] = useState<number>();
  const [userEmail, setUserEmail] = useState<string | null>(null);

  const expiryDateObj = useMemo(() => {
    const expiresIn = 60000; // 5 mins in ms
    return new Date((otpRequestTimestamp || 0) + expiresIn);
  }, [otpRequestTimestamp]);

  const [pageHasMounted, setPageHasMounted] = useState(false);
  const [mins, setMins] = useState("00");
  const [secs, setSecs] = useState("00");
  const [expired, setExpired] = useState(false);

  const param = useSearchParams();

  const {
    register,
    handleSubmit,
    trigger,
    setError,
    setValue,
    setFocus,
    formState: { errors, isSubmitted, isValid },
  } = useForm<OtpInputType>({
    resolver: zodResolver(otpInputSchema),
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const val = e.target.value.replace(/\D/g, "");
    const nextElem = e.target.nextElementSibling as HTMLElement;
    e.target.value = val;
    if (val && e.target.nextElementSibling) {
      nextElem.focus();
    }
    if (isSubmitted) {
      trigger();
    }
  };

  const handleKeyPress = async (e: React.KeyboardEvent) => {
    const inputElem = e.target as HTMLInputElement;
    const prevElem = inputElem.previousElementSibling as HTMLElement;
    const nextElem = inputElem.nextElementSibling as HTMLElement;

    if ((e.key === "Backspace" || e.key === "ArrowLeft") && prevElem) {
      await new Promise(() => {
        setTimeout(() => {
          prevElem.focus();
        }, 10);
      });
    } else if (e.key === "ArrowRight" && nextElem) {
      await new Promise(() => {
        setTimeout(() => {
          nextElem.focus();
        }, 10);
      });
    }
  };

  const verifyOtp = async (value: string) => {
    const apiEndpoint =
      param.get("context") == "register" ? "registration/verify" : "verify-otp";
    return await fetch(`${backendHostname}/api/auth/${apiEndpoint}`, {
      method: "POST",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        otp: value,
      }),
    });
  };

  const resendOtp = async () => {
    const apiEndpoint =
      param.get("context") == "register"
        ? "registration/resend-otp"
        : "resend-otp";
    return await fetch(`${backendHostname}/api/auth/${apiEndpoint}`, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
    });
  };

  const handleOnSubmit = async () => {
    const inputContainer = inputContainerRef.current;
    if (inputContainer) {
      const children = Array.from(inputContainer.children);

      const values = children
        .map((child) => (child as HTMLInputElement).value)
        .join("");

      setIsLoading(true);
      const response = await verifyOtp(values);
      setIsLoading(false);

      console.log(response);
      if (response.status === 200) {
        localStorage.removeItem("userEmail");

        console.log("otp is valid");
        if (param.get("context") == "register") redirect("/dashboard");

        redirect("/auth/reset-password");
      }

      const error = await response.json();

      setError("root", {
        message: error.message,
      });
    }
  };

  const handleResendOtp = async () => {
    // TODO:Make resend reset form state
    setIsLoading(true);
    const response = await resendOtp();

    setOtpTimestamp(new Date().getTime());

    setIsLoading(false);

    if (response.status !== 200) {
      const error = await response.json();

      setError("root", {
        message: error.message,
      });
      return;
    }
    setExpired(false);
  };

  useEffect(() => {
    const change = () => {
      setPageHasMounted(true);
    };
    change();
  }, []); // set page mounted

  useEffect(() => {
    const otpRequestTimestamp = localStorage.getItem("otpRequestTimestamp");
    if (otpRequestTimestamp) {
      setOtpTimestamp(Number(otpRequestTimestamp));
    }
    const userEmail =
      localStorage.getItem("userEmail") || "You shouldn't be here";
    if (userEmail) {
      setUserEmail(userEmail);
    }
  }, []); // get otpRequestTimestamp and userEmail from localStorage

  useEffect(() => {
    const interval = setInterval(() => {
      const gap = (expiryDateObj.getTime() - new Date().getTime()) / 1000;

      if (gap <= 1) {
        setMins("00");
        setSecs("00");
        clearInterval(interval);
        setExpired(true);
      }

      setMins(() =>
        Math.max(Math.floor(gap / 60))
          .toString()
          .padStart(2, "0"),
      );
      setSecs(() =>
        Math.max(Math.floor(gap % 60))
          .toString()
          .padStart(2, "0"),
      );
    }, 100);

    return () => {
      clearInterval(interval);
    };
  }, [expiryDateObj]); // timer interval

  useEffect(() => {
    const container = inputContainerRef.current;
    (container?.firstElementChild as HTMLElement)?.focus();
  }, []); // focus on first input

  useEffect(() => {
    const container = inputContainerRef.current;
    const handlePaste = (e: ClipboardEvent) => {
      e.preventDefault();
      const data = e.clipboardData?.getData("text");
      const containerChildren: Element[] = [
        ...(e.currentTarget as HTMLElement)?.children,
      ];
      if (!data) return;

      if (!/^\d+$/.test(data)) return;

      const splitData = data?.split("");

      containerChildren.forEach((_, idx) => {
        setValue(`otpInput${idx + 1}` as keyof OtpInputType, splitData[idx]);
      });

      setPasted(true);
      setFocus("otpInput6");
    };

    container?.addEventListener("paste", handlePaste);

    return () => {
      container?.removeEventListener("paste", handlePaste);
    };
  }, [inputContainerRef, setValue, setFocus]); // past event listener

  useEffect(() => {
    const form = formRef.current;
    if (pasted || isValid) {
      form?.requestSubmit();
    }
  }, [isValid, pasted]); // auto-submit

  return (
    <div className="flex items-center justify-center md:h-dvh md:-my-10">
      <div className="form-wrapper2 md:p-10 md:w-fit w-full">
        <LoadingScreen isLoading={isLoading} />
        <form
          action="javascript:void(0)"
          className="space-y-7"
          ref={formRef}
          onSubmit={(e) => handleSubmit(handleOnSubmit)(e)}
        >
          <div className="flex flex-col items-center gap-5">
            <div className="p-2 bg-primary rounded-[0.6rem] w-fit text-3xl shadow-[0_5px_20px_color-mix(in_srgb,var(--primary-color)_40%,transparent)]">
              <IoIosMailUnread />
            </div>

            <p className="text-3xl font-bold">Verify Account</p>
            <div className="text-center">
              <p className="text-white/70 md:font-light font-normal">
                We&apos;ve sent a 6-digit verification code to
              </p>
              <span className="font-medium block h-6.5">{userEmail}</span>
            </div>
          </div>

          <div className="w-full">
            <div
              className="flex md:gap-5 justify-between"
              ref={inputContainerRef}
            >
              {fields.map((field, i) => {
                const { onChange, ...rest } = register(field);
                return (
                  <motion.input
                    animate={errors[field] ? { translateX: [0, 5, -5, 0] } : {}}
                    transition={{
                      type: "tween",
                    }}
                    key={i}
                    autoComplete={i === 0 ? "one-time-code" : "off"}
                    inputMode="numeric"
                    type="text"
                    maxLength={1}
                    placeholder="•"
                    className={`otp-input ${errors[field] ? "border-red-500!" : ""}`}
                    onFocus={(e) => {
                      const inputElem = e.target as HTMLInputElement;
                      const end = inputElem.value.length;
                      inputElem.setSelectionRange(end, end);
                    }}
                    onKeyDown={handleKeyPress}
                    {...rest}
                    onChange={(e) => {
                      onChange(e);
                      handleChange(e);
                    }}
                  />
                );
              })}
            </div>
          </div>

          <div className="">
            <input
              type="submit"
              className="button-primary"
              value={"Verify"}
              disabled={!pageHasMounted}
            />
          </div>

          <ErrorMessage
            condition={!!errors.root}
            message={errors.root?.message || ""}
          />
        </form>

        <p className="text-center text-white/70 md:font-light font-normal mt-5">
          Didn&apos;t recieve the code?{" "}
          <button
            className="text-primary font-semibold disabled:cursor-not-allowed disabled:text-gray-600 transition-all cursor-pointer text-sm"
            disabled={!expired}
            onClick={handleResendOtp}
          >
            Resend Code {!expired && `${mins}:${secs}`}
          </button>
        </p>
      </div>
    </div>
  );
}
