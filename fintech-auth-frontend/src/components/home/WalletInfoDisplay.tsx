import { cookies } from "next/headers";
import { CopyButton } from "./CopyButton";
import { redirect } from "next/navigation";

export const WalletInfoDisplay = async () => {
  const cookieStore = await cookies();

  const backendHostname = process.env.NEXT_PUBLIC_BACKEND_HOSTNAME;

  const res = await fetch(`${backendHostname}/api/account/wallet-info`, {
    headers: {
      Cookies: cookieStore.toString(),
    },
    cache: "no-store",
  });

  if (!res.ok) {
    redirect("/auth/login");
  }

  const { accountNumber, bankName } = await res.json();
  const formattedAccNo = accountNumber.replace(
    /(\d{3})(\d{3})(\d{4})/,
    "$1 $2 $3",
  );
  return (
    <div className="text-white/70 w-fit md:-mt-2 gap-3">
      <div className="flex items-center gap-2">
        <p>{formattedAccNo}</p>
        <CopyButton text={accountNumber} />
      </div>
      <p className="text-sm">{bankName}</p>
    </div>
  );
};
