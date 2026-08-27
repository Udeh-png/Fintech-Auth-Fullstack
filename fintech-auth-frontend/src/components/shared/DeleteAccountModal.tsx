import { Modal } from "./Modal";
import { useMediaQuery } from "@/hooks/UseMediaQuery";
import { BottomSheet } from "./BottomSheet";
import { FaRegTrashCan } from "react-icons/fa6";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { LoadingScreen } from "./LoadingScreen";

const DeleteAccountComp = ({ onClose }: { onClose: () => void }) => {
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();

  const backendHostname = process.env.NEXT_PUBLIC_BACKEND_HOSTNAME;

  const handleDeleteAccount = async () => {
    setIsLoading(true);
    const delReq = await fetch(
      `${backendHostname}/api/account/delete-account`,
      {
        method: "DELETE",
        credentials: "include",
      },
    );
    setIsLoading(false);

    if (String(delReq.status).startsWith("2")) router.push("/auth/register");
  };
  return (
    <div className="space-y-5 w-fit bg-[#161224] md:p-7 px-3 py-7 rounded-2xl text-white max-w-100">
      <LoadingScreen isLoading={isLoading} />
      <div className="text-2xl p-3 bg-red-500/10 rounded-full text-red-500 size-fit">
        <FaRegTrashCan />
      </div>

      <div>
        <p className="text-2xl font-semibold mb-1">Delete Account?</p>
        <p className="text-sm text-white/60">
          Are you sure you want to delete your account? This action is permanent
          and can not be undone
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
          className="flex-1 md:py-2 py-3 bg-red-800 rounded-xl cursor-pointer"
          onClick={handleDeleteAccount}
        >
          Delete Account
        </button>
      </div>
    </div>
  );
};

export const DeleteAccountPopup = ({
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
        <DeleteAccountComp onClose={onClose} />
      </Modal>
    );

  return (
    <BottomSheet isOpen={isOpen} onClose={onClose}>
      <DeleteAccountComp onClose={onClose} />
    </BottomSheet>
  );
};
