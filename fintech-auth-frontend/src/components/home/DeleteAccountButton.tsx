"use client";

import { FaRegTrashCan } from "react-icons/fa6";
import { DeleteAccountPopup } from "../shared/DeleteAccountModal";
import { useState } from "react";

export const DeleteAccountButton = () => {
  const [openDeleteModal, setOpenDeleteModal] = useState(false);

  return (
    <div>
      <button
        className="flex w-full shrink-0 items-center justify-center gap-3 rounded-xl border border-red-500/50 text-red-500 lpx-5 py-3 text-lg sm:w-auto sm:px-7 sm:text-xl"
        onClick={() => setOpenDeleteModal(true)}
      >
        <span>Delete Account</span>
        <FaRegTrashCan />
      </button>

      <DeleteAccountPopup
        isOpen={openDeleteModal}
        onClose={() => setOpenDeleteModal(false)}
      />
    </div>
  );
};
