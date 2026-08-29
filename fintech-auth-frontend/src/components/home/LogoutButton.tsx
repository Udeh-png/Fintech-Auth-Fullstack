"use client";

import { useState } from "react";
import { LuLogOut } from "react-icons/lu";
import { LogoutPopup } from "../shared/LogoutModal";

export const LogoutButton = () => {
  const [openLogoutModal, setOpenLogoutModal] = useState(false);
  return (
    <div>
      <button
        className="flex w-full shrink-0 items-center justify-center gap-3 rounded-xl bg-primary px-5 py-3 text-lg sm:w-auto sm:px-7 sm:text-xl"
        onClick={() => setOpenLogoutModal(true)}
      >
        <span>Logout</span>
        <LuLogOut />
      </button>

      <LogoutPopup
        isOpen={openLogoutModal}
        onClose={() => setOpenLogoutModal(false)}
      />
    </div>
  );
};
