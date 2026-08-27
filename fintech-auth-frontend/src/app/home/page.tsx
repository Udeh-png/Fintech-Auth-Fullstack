"use client";

/* eslint-disable @next/next/no-img-element */
import { useState } from "react";
import { FaRegTrashCan } from "react-icons/fa6";
import { LuLogOut, LuShieldCheck } from "react-icons/lu";
import { LogoutPopup } from "@/components/shared/LogoutModal";
import { DeleteAccountPopup } from "@/components/shared/DeleteAccountModal";

export default function HomePage() {
  const [openLogoutModal, setOpenLogoutModal] = useState(false);
  const [openDeleteModal, setOpenDeleteModal] = useState(false);
  return (
    <div className="">
      <header className="flex flex-col items-start gap-10 px-0 py-5 sm:px-6 lg:flex-row lg:items-center lg:justify-between lg:px-15 lg:py-0 md:border-2 rounded-2xl border-white/10">
        <div className="w-full lg:w-auto">
          <div className="mb-8 space-y-4 sm:mb-10 sm:space-y-5">
            <div className="">
              <h5 className="mb-2 text-lg text-primary sm:text-xl">
                Welcome to my app,
              </h5>
              <div className="flex items-center gap-3 text-4xl sm:text-5xl lg:text-6xl">
                <h1 className="font-semibold leading-tight">Chisom Udeh</h1>
              </div>
            </div>
            <p className="max-w-xl text-base leading-7 text-white/80 sm:text-xl sm:leading-normal">
              You have successfully signed in to the app.{" "}
              <br className="hidden sm:block" /> Try signing in on other
              devices.
            </p>
          </div>

          <div className="flex w-fit items-center gap-x-3 rounded-xl bg-primary/10 px-4 py-3 text-base text-primary sm:px-5 sm:text-lg">
            <LuShieldCheck className="text-2xl sm:text-3xl" />
            <p>Premium Account</p>
          </div>
        </div>

        <div className="relative hidden shrink-0 py-4 after:absolute after:left-1/2 after:top-1/2 after:-z-5 after:size-[110%] after:-translate-1/2 after:rounded-full after:bg-primary after:opacity-20 after:blur-2xl lg:block">
          <img src="/images/logo.png" alt="" className="md:max-w-80 max-w-20" />
        </div>
      </header>

      <div className="md:mt-10 mt-5 px-0 sm:px-6 lg:px-10">
        <h4 className="text-2xl font-semibold sm:text-3xl">
          Account & Security
        </h4>
        <p className="mt-1 text-sm text-white/65 sm:text-base">
          Manage your account settings and security preferences
        </p>

        <div className="mt-5 md:space-y-10 space-y-5">
          <div className="flex flex-col items-start gap-5 rounded-xl border border-white/10 bg-gray-800/10 p-4 sm:flex-row sm:items-center sm:gap-x-7 sm:p-5">
            <div className="w-fit rounded-xl bg-primary/10 p-3 text-3xl text-primary sm:p-4 sm:text-4xl">
              <LuLogOut />
            </div>

            <div className="flex w-full flex-1 flex-col gap-4 sm:flex-row sm:items-center sm:justify-between sm:gap-6">
              <div className="min-w-0">
                <p className="text-lg sm:text-xl">Logout</p>
                <p className="text-sm leading-6 text-white/65 sm:text-base sm:leading-normal">
                  Sign out of your application account on this device
                </p>
              </div>

              <button
                className="flex w-full shrink-0 items-center justify-center gap-3 rounded-xl bg-primary px-5 py-3 text-lg sm:w-auto sm:px-7 sm:text-xl"
                onClick={() => setOpenLogoutModal(true)}
              >
                <span>Logout</span>
                <LuLogOut />
              </button>
            </div>
          </div>

          <div className="flex flex-col items-start gap-5 rounded-xl border border-white/10 bg-gray-800/10 p-4 sm:flex-row sm:items-center sm:gap-x-7 sm:p-5">
            <div className="w-fit rounded-xl bg-red-500/10 p-3 text-3xl text-red-500 sm:p-4 sm:text-4xl">
              <FaRegTrashCan />
            </div>

            <div className="flex w-full flex-1 flex-col gap-4 sm:flex-row sm:items-center sm:justify-between sm:gap-6">
              <div className="min-w-0">
                <p className="text-lg sm:text-xl">Delete Account</p>
                <p className="text-sm leading-6 text-white/65 sm:text-base sm:leading-normal">
                  Permanently delete your account and all your stored data
                </p>
              </div>

              <button
                className="flex w-full shrink-0 items-center justify-center gap-3 rounded-xl border border-red-500/50 text-red-500 lpx-5 py-3 text-lg sm:w-auto sm:px-7 sm:text-xl"
                onClick={() => setOpenDeleteModal(true)}
              >
                <span>Delete Account</span>
                <FaRegTrashCan />
              </button>
            </div>
          </div>
        </div>

        <LogoutPopup
          isOpen={openLogoutModal}
          onClose={() => setOpenLogoutModal(false)}
        />

        <DeleteAccountPopup
          isOpen={openDeleteModal}
          onClose={() => setOpenDeleteModal(false)}
        />
      </div>
    </div>
  );
}
