import { cookies } from "next/headers";
import { CopyButton } from "./CopyButton";
import { Config } from "@/config";

export const WalletInfoDisplay = async () => {
  const cookieStore = await cookies();

  const backendHostname = Config.API_URL;

  const res = await fetch(`${backendHostname}/api/account/wallet-info`, {
    headers: {
      Cookies: cookieStore.toString(),
    },
    cache: "no-store",
  });

  console.log(cookieStore.toString());

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
