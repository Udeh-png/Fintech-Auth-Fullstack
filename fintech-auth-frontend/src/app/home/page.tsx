/* eslint-disable @next/next/no-img-element */
import { FaRegTrashCan } from "react-icons/fa6";
import { LuCopy, LuLogOut, LuShieldCheck } from "react-icons/lu";
import { LogoutButton } from "@/components/home/LogoutButton";
import { DeleteAccountButton } from "@/components/home/DeleteAccountButton";
import { UserInfoDisplay } from "@/components/home/HomeUserInfoDisplay";
import { WalletInfoDisplay } from "@/components/home/WalletInfoDisplay";
import { Suspense } from "react";
import { MaterialSpinner } from "@/components/shared/MaterialSpinner";

export default function HomePage() {
  return (
    <>
      <div className="">
        <header className="flex flex-col items-start gap-10 px-0 py-5 sm:px-6 lg:flex-row lg:items-center lg:justify-between lg:px-15 lg:py-5 md:border-2 rounded-2xl border-white/10">
          <div className="w-full lg:w-auto">
            <div className="mb-8 space-y-4 sm:mb-10 sm:space-y-5">
              <div className="">
                <h5 className="mb-2 text-lg text-primary sm:text-xl">
                  Welcome to my app,
                </h5>
                <div className="md:h-25 h-15 flex items-center">
                  <Suspense
                    fallback={<MaterialSpinner sizeInPx={30} color="primary" />}
                  >
                    <UserInfoDisplay />
                  </Suspense>
                </div>
              </div>

              <div className="md:h-12 h-10 flex items-center">
                <Suspense
                  fallback={<MaterialSpinner sizeInPx={15} color="primary" />}
                >
                  <WalletInfoDisplay />
                </Suspense>
              </div>
            </div>

            <div className="flex w-fit items-center gap-x-3 rounded-xl bg-primary/10 px-4 py-3 text-base text-primary sm:px-5 sm:text-lg">
              <LuShieldCheck className="text-2xl sm:text-3xl" />
              <p>Premium Account</p>
            </div>
          </div>

          <div className="relative hidden shrink-0 py-4 after:absolute after:left-1/2 after:top-1/2 after:-z-5 after:size-[110%] after:-translate-1/2 after:rounded-full after:bg-primary after:opacity-20 after:blur-2xl lg:block">
            <img
              src="/images/logo.png"
              alt=""
              className="md:max-w-80 max-w-20"
            />
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

                <LogoutButton />
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

                <DeleteAccountButton />
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
