"use client";

/* eslint-disable @typescript-eslint/no-explicit-any */

import { PasswordListItem } from "@/components/auth/PasswordStrengthListItem";
import { passwordCriteria } from "@/data";
import { useState } from "react";
import { FieldError, UseFormRegister, UseFormTrigger } from "react-hook-form";
import { PasswordInput } from "./PasswordInput";

export const PasswordFieldWithChecks = ({
  passwordErrors,
  register,
  isSubmitted,
  trigger,
}: {
  passwordErrors: FieldError | undefined;
  register: UseFormRegister<any>;
  isSubmitted: boolean;
  trigger: UseFormTrigger<any>;
}) => {
  const [isTouched, setIsTouched] = useState(false);

  const passwordMeetsCriteria = (criteria: string) => {
    if (!isTouched) return false;

    const errorTypesArray = passwordErrors?.types;

    const errorType: string = passwordErrors?.type || "";
    return !(
      (Array.isArray(errorTypesArray?.invalid_format) &&
        errorTypesArray?.invalid_format?.includes(criteria)) ||
      errorTypesArray?.invalid_format === criteria ||
      errorTypesArray?.[errorType] === criteria ||
      (Array.isArray(errorTypesArray?.[errorType]) &&
        (errorTypesArray?.[errorType] as string[]).includes(criteria))
    );
  };
  return (
    <div>
      <div className={`input-container`}>
        <label htmlFor="password" className="input-label">
          Password
        </label>
        <PasswordInput
          autoComplete="new-password"
          id="password"
          elementId="password"
          error={!passwordMeetsCriteria("REQUIRED") && isSubmitted}
          {...register("password", {
            onChange: async () => {
              if (!isTouched) setIsTouched(true);
              trigger("password");
            },
          })}
        />

        {!passwordMeetsCriteria("REQUIRED") && isSubmitted && (
          <p className="input-error-text">Password is Required</p>
        )}
        <div className="grid md:grid-cols-2 md:gap-2.5 gap-2 mt-2 text-sm">
          {passwordCriteria.map((criteria) => (
            <PasswordListItem
              key={criteria.id}
              criteria={criteria}
              isValid={passwordMeetsCriteria(criteria.id)}
            />
          ))}
        </div>
      </div>
    </div>
  );
};
