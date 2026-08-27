"use client";

import { LuLogOut } from "react-icons/lu";
import { Modal } from "./Modal";
import { useMediaQuery } from "@/hooks/UseMediaQuery";
import { BottomSheet } from "./BottomSheet";
import { useRouter } from "next/navigation";
import { LoadingScreen } from "./LoadingScreen";
import { useState } from "react";

const LogoutComp = ({ onClose }: { onClose: () => void }) => {
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();

  const backendHostname = process.env.NEXT_PUBLIC_BACKEND_HOSTNAME;

  const handleLogout = async () => {
    setIsLoading(true);
    const logoutReq = await fetch(`${backendHostname}/api/account/logout`, {
      method: "POST",
      credentials: "include",
    });
    setIsLoading(false);
    if (String(logoutReq.status).startsWith("2")) router.push("/auth/login");
  };
  return (
    <div className="space-y-5 w-fit bg-[#161224] md:p-7 px-3 py-7 rounded-2xl text-white">
      <LoadingScreen isLoading={isLoading} />
      <div className="text-2xl p-3 bg-primary/10 rounded-full text-primary size-fit">
        <LuLogOut />
      </div>

      <div>
        <p className="text-2xl font-semibold mb-1">Log out?</p>
        <p className="text-sm text-white/60">
          You&apos;ll need to sign in again to access your account.
        </p>
      </div>

      <div className="flex md:flex-row flex-col-reverse justify-between gap-3">
        <button
          className="flex-1 md:py-2 py-3 border border-white/10 rounded-xl cursor-pointer"
          onClick={onClose}
        >
          Cancel
        </button>
        <button
          className="flex-1 md:py-2 py-3 bg-primary rounded-xl cursor-pointer"
          onClick={handleLogout}
        >
          Log out
        </button>
      </div>
    </div>
  );
};

export const LogoutPopup = ({
  isOpen,
  onClose,
}: {
  isOpen: boolean;
  onClose: () => void;
}) => {
  const isDesktop = useMediaQuery("(min-width: 768px)");
  if (isDesktop)
    return (
      <Modal isOpen={isOpen} onClose={onClose}>
        <LogoutComp onClose={onClose} />
      </Modal>
    );

  return (
    <BottomSheet isOpen={isOpen} onClose={onClose}>
      <LogoutComp onClose={onClose} />
    </BottomSheet>
  );
};
